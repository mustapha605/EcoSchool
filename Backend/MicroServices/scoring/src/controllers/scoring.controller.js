// Mock in-memory scoring state for initial microservice stage
const scores = [
  { classId: 1, className: "6ème B - Éco-Gardiens", totalPoints: 450, rank: 1, badges: ["Zéro Déchet", "Éco-Lumière"] },
  { classId: 2, className: "5ème A - Vert Avenir", totalPoints: 320, rank: 2, badges: ["Anti-Gaspillage"] }
];

exports.hello = (req, res) => {
  res.json({ message: "hello I'm microservice scoring" });
};

exports.getLeaderboard = (req, res) => {
  res.json(scores);
};

exports.getClassScore = (req, res) => {
  const cId = parseInt(req.params.classId);
  const found = scores.find(s => s.classId === cId);
  if (!found) return res.status(404).json({ error: "Class score not found" });
  res.json(found);
};
