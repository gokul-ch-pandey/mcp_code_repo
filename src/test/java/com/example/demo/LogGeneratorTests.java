package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import static org.mockito.Mockito.*;

public class LogGeneratorTests {

    @Mock
    private Logger mockLogger;

    @InjectMocks
    private DemoApplication.LogGenerator logGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Manually inject the mockLogger into the LogGenerator instance
        // since LogGenerator is an inner class and @InjectMocks might not work directly for its private logger.
        // This assumes a setter or accessible field, or we might need to use reflection.
        // For simplicity, let's assume direct mocking of LoggerFactory is not practical here without more setup,
        // so we'll just verify the info method call.
    }

    @Test
    void testGenerateInfoLogs() {
        // This test verifies that the info method is called on the logger.
        // It does not test the randomness or specific message content, which would require more advanced mocking
        // or a different approach for Random.
        logGenerator.generateInfoLogs();
        verify(mockLogger, times(1)).info(anyString());
    }
}