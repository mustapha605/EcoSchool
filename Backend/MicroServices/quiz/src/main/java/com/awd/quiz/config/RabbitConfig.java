package com.awd.quiz.config;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration public class RabbitConfig { @Bean TopicExchange ecoEvents() { return new TopicExchange("eco.events", true, false); } }
