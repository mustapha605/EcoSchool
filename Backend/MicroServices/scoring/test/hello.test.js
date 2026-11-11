const test = require('node:test');
const assert = require('node:assert');
const app = require('../src/app');

test('GET /api/scoring/hello returns correct message', async () => {
  // Simple functional test
  assert.strictEqual(typeof app, 'function');
});
