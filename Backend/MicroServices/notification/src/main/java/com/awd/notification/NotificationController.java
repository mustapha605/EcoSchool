package com.awd.notification;
import java.time.Instant; import java.util.*; import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.amqp.rabbit.annotation.RabbitListener; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/notifications")
class NotificationController {
 private final List<Map<String,Object>> notifications = new CopyOnWriteArrayList<>();
 @GetMapping List<Map<String,Object>> all() { return notifications; }
 @RabbitListener(queues = "notification.events") void receive(Map<String,Object> event) { notifications.add(Map.of("receivedAt", Instant.now().toString(), "event", event)); }
}
