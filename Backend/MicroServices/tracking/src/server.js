const express = require('express');
const amqp = require('amqplib');
const mongoose = require('mongoose');
const os = require('os');
const { Eureka } = require('eureka-js-client');

const app = express();
app.use(express.json());
const port = process.env.PORT || 8087;
const mongoUri = process.env.MONGO_URI || 'mongodb://localhost:27017/tracking_db';
const rabbitUrl = process.env.RABBITMQ_URL || 'amqp://guest:guest@localhost:5672';
const eurekaHost = process.env.EUREKA_HOST || 'localhost';
const eurekaPort = Number(process.env.EUREKA_PORT || 8761);
const instanceHost = process.env.EUREKA_INSTANCE_HOSTNAME || 'localhost';
const instanceIp = process.env.EUREKA_INSTANCE_IP || Object.values(os.networkInterfaces())
  .flat()
  .find(address => address && address.family === 'IPv4' && !address.internal)?.address || '127.0.0.1';
let channel;

const Action = mongoose.model('Action', new mongoose.Schema({
  classId: { type: String, required: true }, metric: { type: String, enum: ['WASTE_KG', 'ENERGY_KWH', 'WATER_LITERS'], required: true },
  value: { type: Number, required: true, min: 0 }, challengeId: String, recordedAt: { type: Date, default: Date.now }
}, { timestamps: true }));

const eurekaClient = new Eureka({
  instance: {
    app: 'TRACKING-SERVICE',
    hostName: instanceHost,
    ipAddr: instanceIp,
    port: { '$': Number(port), '@enabled': 'true' },
    vipAddress: 'tracking-service',
    statusPageUrl: `http://${instanceHost}:${port}/info`,
    healthCheckUrl: `http://${instanceHost}:${port}/health`,
    homePageUrl: `http://${instanceHost}:${port}/`,
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn'
    }
  },
  eureka: { host: eurekaHost, port: eurekaPort, servicePath: '/eureka/apps/' }
});

async function connectBroker() {
  const connection = await amqp.connect(rabbitUrl); channel = await connection.createChannel();
  await channel.assertExchange('eco.events', 'topic', { durable: true });
}
app.get('/health', (_, res) => res.json({ status: 'UP' }));
app.get('/info', (_, res) => {
  const pkg = require('../package.json');
  res.json({ app: { name: pkg.name, version: pkg.version, description: 'Environmental tracking microservice' } });
});
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
Promise.all([mongoose.connect(mongoUri), connectBroker()])
  .then(() => app.listen(port, () => {
    console.log(`Tracking listening on ${port}`);
    eurekaClient.start();
  }))
  .catch(error => { console.error(error); process.exit(1); });

async function shutdown() {
  eurekaClient.stop();
  if (channel) await channel.close().catch(() => {});
  await mongoose.disconnect().catch(() => {});
  process.exit(0);
}
process.on('SIGINT', shutdown);
process.on('SIGTERM', shutdown);
