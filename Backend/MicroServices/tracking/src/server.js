const express = require('express');
const amqp = require('amqplib');
const mongoose = require('mongoose');

const app = express();
app.use(express.json());
const port = process.env.PORT || 8087;
const mongoUri = process.env.MONGO_URI || 'mongodb://localhost:27017/tracking_db';
const rabbitUrl = process.env.RABBITMQ_URL || 'amqp://guest:guest@localhost:5672';
let channel;

const Action = mongoose.model('Action', new mongoose.Schema({
  classId: { type: String, required: true }, metric: { type: String, enum: ['WASTE_KG', 'ENERGY_KWH', 'WATER_LITERS'], required: true },
  value: { type: Number, required: true, min: 0 }, challengeId: String, recordedAt: { type: Date, default: Date.now }
}, { timestamps: true }));

async function connectBroker() {
  const connection = await amqp.connect(rabbitUrl); channel = await connection.createChannel();
  await channel.assertExchange('eco.events', 'topic', { durable: true });
}
app.get('/api/tracking/health', (_, res) => res.json({ status: 'tracking-service running', broker: channel ? 'connected' : 'connecting' }));
app.post('/api/tracking/actions', async (req, res, next) => {
  try {
    const action = await Action.create(req.body);
    const factors = { WASTE_KG: 10, ENERGY_KWH: 5, WATER_LITERS: 0.1 };
    const event = { eventType: 'TrackingActionRecorded', actionId: action.id, classId: action.classId, metric: action.metric, value: action.value, points: Math.round(action.value * factors[action.metric]), occurredAt: action.recordedAt };
    channel.publish('eco.events', 'tracking.action.recorded', Buffer.from(JSON.stringify(event)), { persistent: true, contentType: 'application/json' });
    res.status(201).json(action);
  } catch (error) { next(error); }
});
app.get('/api/tracking/classes/:classId/summary', async (req, res, next) => {
  try { res.json(await Action.aggregate([{ $match: { classId: req.params.classId } }, { $group: { _id: '$metric', total: { $sum: '$value' } } }])); } catch (error) { next(error); }
});
app.use((error, _, res, __) => res.status(400).json({ error: error.message }));
Promise.all([mongoose.connect(mongoUri), connectBroker()]).then(() => app.listen(port, () => console.log(`Tracking listening on ${port}`))).catch(error => { console.error(error); process.exit(1); });
