const app = require('./app');

const PORT = process.env.PORT || 8084;
app.listen(PORT, () => {
    console.log(`[scoring-service] running on http://localhost:${PORT}`);
    console.log(`[scoring-service] Swagger UI: http://localhost:${PORT}/swagger-ui`);
});
