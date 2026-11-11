module.exports = {
  openapi: '3.0.3',
  info: {
    title: 'Scoring & Leaderboard Microservice API',
    version: '1.0.0',
    description: 'Node.js / Express service managing green points, badges, and school rankings'
  },
  paths: {
    '/api/scoring/hello': {
      get: {
        summary: 'Health check endpoint',
        responses: {
          '200': {
            description: 'Success',
            content: { 'application/json': { schema: { type: 'object' } } }
          }
        }
      }
    },
    '/api/scoring/leaderboard': {
      get: {
        summary: 'Get class leaderboard ranking',
        responses: {
          '200': { description: 'Leaderboard list' }
        }
      }
    }
  }
};
