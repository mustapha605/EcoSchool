package com.awd.scoring;
import java.util.*; import java.util.concurrent.ConcurrentHashMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/scoring")
class ScoringController {
 private final Map<String, Integer> scores = new ConcurrentHashMap<>();
 @GetMapping("/leaderboard") List<Map<String,Object>> leaderboard() { return scores.entrySet().stream().sorted(Map.Entry.<String,Integer>comparingByValue().reversed()).map(e -> Map.<String,Object>of("classId", e.getKey(), "totalPoints", e.getValue())).toList(); }
 @GetMapping("/{classId}") Map<String,Object> score(@PathVariable String classId) { return Map.of("classId", classId, "totalPoints", scores.getOrDefault(classId, 0)); }
 @RabbitListener(queues = "scoring.events") void onEvent(Map<String,Object> event) {
  if (!event.containsKey("classId") || !event.containsKey("points")) return;
  String classId = String.valueOf(event.get("classId")); int points = ((Number) event.get("points")).intValue(); scores.merge(classId, points, Integer::sum);
 }
}
