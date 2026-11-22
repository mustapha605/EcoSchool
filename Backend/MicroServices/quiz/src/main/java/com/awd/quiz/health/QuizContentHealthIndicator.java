package com.awd.quiz.health;

import com.awd.quiz.repository.QuizRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * HealthIndicator personnalise (atelier Observabilite).
 * Apparait dans /actuator/health sous le composant "quizContent"
 * (nom du bean sans le suffixe HealthIndicator).
 * Si la lecture des quiz echoue, le composant passe DOWN, donc le service
 * entier passe DOWN, et Eureka (eureka.client.healthcheck.enabled=true) l'affiche DOWN.
 */
@Component
public class QuizContentHealthIndicator implements HealthIndicator {

    private final QuizRepository quizRepository;

    public QuizContentHealthIndicator(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public Health health() {
        try {
            long total = quizRepository.count();
            return Health.up()
                    .withDetail("quizzes", total)
                    .build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
