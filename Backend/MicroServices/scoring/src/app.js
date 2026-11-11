const express = require('express');
const swaggerUi = require('swagger-ui-express');
const swaggerSpec = require('./config/swagger');
const scoringRoutes = require('./routes/scoring.routes');

const app = express();
app.use(express.json());

// Swagger Docs
app.use('/swagger-ui', swaggerUi.serve, swaggerUi.setup(swaggerSpec));
app.get('/v3/api-docs', (req, res) => res.json(swaggerSpec));

// Routes
app.use('/api/scoring', scoringRoutes);

// Error handlers
app.use((req, res) => res.status(404).json({ error: 'Endpoint Not Found' }));

module.exports = app;
