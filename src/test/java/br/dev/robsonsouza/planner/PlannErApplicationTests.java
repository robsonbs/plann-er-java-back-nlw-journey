package br.dev.robsonsouza.planner;

import br.dev.robsonsouza.planner.controller.TripController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlannErApplicationTests {
    
    @Autowired
    private TripController controller;
    
    @Test
    void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
    }
}
