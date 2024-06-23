package fr.clementjaminion.macaronsbackend.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter salesCreationCounter;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.salesCreationCounter = meterRegistry.counter("custom.sales.creation");
    }

    public void incrementSalesCreationCounter() {
        this.salesCreationCounter.increment();
    }
}
