package com.awd.scoring;
import org.springframework.amqp.core.*; import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter; import org.springframework.context.annotation.*;
@Configuration class MessagingConfig {
 @Bean TopicExchange ecoEvents() { return new TopicExchange("eco.events", true, false); }
 @Bean Queue scoringEvents() { return new Queue("scoring.events", true); }
 @Bean Binding scoringBinding(Queue scoringEvents, TopicExchange ecoEvents) { return BindingBuilder.bind(scoringEvents).to(ecoEvents).with("#"); }
 @Bean Jackson2JsonMessageConverter messageConverter() { return new Jackson2JsonMessageConverter(); }
}
