package com.tuum.testassignment.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class RabbitMQConfig {

    private final RabbitMQProperties rabbitMQProperties;

    private static final String DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    public RabbitMQConfig(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Bean
    public Queue accountQueue() {
        return QueueBuilder
                .durable(rabbitMQProperties.getAccountEventQueue())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getDeadEventExchange())
                .withArgument(DEAD_LETTER_ROUTING_KEY, rabbitMQProperties.getDeadAccountKey())
                .build();
    }

    @Bean
    public Queue balanceQueue() {
        return QueueBuilder
                .durable(rabbitMQProperties.getBalanceEventQueue())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getDeadEventExchange())
                .withArgument(DEAD_LETTER_ROUTING_KEY, rabbitMQProperties.getDeadBalanceKey())
                .build();
    }

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder
                .durable(rabbitMQProperties.getTransactionEventQueue())
                .withArgument(DEAD_LETTER_EXCHANGE, rabbitMQProperties.getDeadEventExchange())
                .withArgument(DEAD_LETTER_ROUTING_KEY, rabbitMQProperties.getDeadTransactionKey())
                .build();
    }

    @Bean
    public Queue deadAccountQueue() {
        return new Queue(rabbitMQProperties.getDeadAccountEventQueue());
    }

    @Bean
    public Queue deadBalanceQueue() {
        return new Queue(rabbitMQProperties.getDeadBalanceEventQueue());
    }

    @Bean
    public Queue deadTransactionQueue() {
        return new Queue(rabbitMQProperties.getDeadTransactionEventQueue());
    }

    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(rabbitMQProperties.getEventExchange());
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(rabbitMQProperties.getDeadEventExchange());
    }

    @Bean
    @DependsOn({"accountQueue", "eventExchange"})
    public Binding accountEventBinding(Queue accountQueue, TopicExchange eventExchange) {
        return BindingBuilder
                .bind(accountQueue)
                .to(eventExchange)
                .with("account.#");
    }

    @Bean
    @DependsOn({"balanceQueue", "eventExchange"})
    public Binding balanceEventBinding(Queue balanceQueue, TopicExchange eventExchange) {
        return BindingBuilder
                .bind(balanceQueue)
                .to(eventExchange)
                .with("balance.#");
    }

    @Bean
    @DependsOn({"transactionQueue", "eventExchange"})
    public Binding transactionEventBinding(Queue transactionQueue, TopicExchange eventExchange) {
        return BindingBuilder
                .bind(transactionQueue)
                .to(eventExchange)
                .with("transaction.#");
    }

    @Bean
    @DependsOn({"deadAccountQueue", "deadLetterExchange"})
    public Binding deadAccountEventBinding(Queue deadAccountQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadAccountQueue)
                .to(deadLetterExchange)
                .with(rabbitMQProperties.getDeadAccountKey());
    }

    @Bean
    @DependsOn({"deadBalanceQueue", "deadLetterExchange"})
    public Binding deadBalanceEventBinding(Queue deadBalanceQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadBalanceQueue)
                .to(deadLetterExchange)
                .with(rabbitMQProperties.getDeadBalanceKey());
    }

    @Bean
    @DependsOn({"deadTransactionQueue", "deadLetterExchange"})
    public Binding deadTransactionEventBinding(Queue deadTransactionQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(deadTransactionQueue)
                .to(deadLetterExchange)
                .with(rabbitMQProperties.getDeadTransactionKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
