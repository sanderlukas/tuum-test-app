package com.tuum.testassignment.rabbitmq;

import com.tuum.testassignment.account.Account;
import com.tuum.testassignment.common.event.BalanceUpdated;
import com.tuum.testassignment.common.event.TransactionCreated;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageProducer {

    private final AmqpTemplate amqpTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public RabbitMQMessageProducer(AmqpTemplate amqpTemplate, RabbitMQProperties rabbitMQProperties) {
        this.amqpTemplate = amqpTemplate;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    public void publish(Account accountCreated) {
        amqpTemplate.convertAndSend(rabbitMQProperties.getEventExchange(), rabbitMQProperties.getAccountKey(), accountCreated);
    }

    public void publish(BalanceUpdated balanceUpdated) {
        amqpTemplate.convertAndSend(rabbitMQProperties.getEventExchange(), rabbitMQProperties.getBalanceKey(), balanceUpdated);
    }

    public void publish(TransactionCreated transactionCreated) {
        amqpTemplate.convertAndSend(rabbitMQProperties.getEventExchange(),rabbitMQProperties.getTransactionKey(), transactionCreated);
    }
}
