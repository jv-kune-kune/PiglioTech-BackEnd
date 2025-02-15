package org.kunekune.PiglioTech;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class PiglioTechApplicationTests {

  @Autowired
  private ApplicationContext context;

  @Test
  void contextLoads() {
    // Verify that the Spring Boot application context loads correctly
    assertNotNull(context, "The application context should not be null");
  }
}
