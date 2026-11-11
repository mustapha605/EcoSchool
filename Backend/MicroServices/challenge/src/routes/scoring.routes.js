const express = require('express');
const router = express.Router();
const controller = require('../controllers/scoring.controller');

router.get('/hello', controller.hello);
router.get('/leaderboard', controller.getLeaderboard);
router.get('/classes/:classId', controller.getClassScore);

module.exports = router;
