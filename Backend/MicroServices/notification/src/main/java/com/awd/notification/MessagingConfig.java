package com.awd.notification;
import org.springframework.amqp.core.*; import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter; import org.springframework.context.annotation.*;
@Configuration class MessagingConfig {
 @Bean TopicExchange ecoEvents() { return new TopicExchange("eco.events", true, false); }
 @Bean Queue notificationEvents() { return new Queue("notification.events", true); }
 @Bean Binding notificationBinding(Queue notificationEvents, TopicExchange ecoEvents) { return BindingBuilder.bind(notificationEvents).to(ecoEvents).with("#"); }
 @Bean Jackson2JsonMessageConverter messageConverter() { return new Jackson2JsonMessageConverter(); }
}
