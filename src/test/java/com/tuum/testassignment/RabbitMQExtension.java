package com.tuum.testassignment;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class RabbitMQExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer("rabbitmq");
        RABBIT_MQ_CONTAINER.start();
        System.setProperty("spring.rabbitmq.host", RABBIT_MQ_CONTAINER.getHost());
    }

    @Override
    public void afterAll(ExtensionContext context) {

    }
}
