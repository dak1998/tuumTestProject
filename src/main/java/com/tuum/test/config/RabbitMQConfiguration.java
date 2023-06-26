package com.tuum.test.config;

import com.tuum.test.util.RabbitMQConfigConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    Queue accountCreationQueue() {
        return new Queue(RabbitMQConfigConstants.ACCOUNT_CREATION_QNAME, false);
    }

    @Bean
    Queue accountBalanceUpdateQueue() {
        return new Queue(RabbitMQConfigConstants.ACCOUNT_BALANCE_UPDATE_QNAME, false);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(RabbitMQConfigConstants.EXCHANGE_NAME);
    }

    @Bean
    Binding bindingAccountCreation(Queue accountCreationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(accountCreationQueue)
                .to(exchange)
                .with(RabbitMQConfigConstants.ACCOUNT_CREATION_ROUTING_KEY);
    }

    @Bean
    Binding bindingAccountBalanceUpdate(Queue accountBalanceUpdateQueue, DirectExchange exchange) {
        return BindingBuilder.bind(accountBalanceUpdateQueue)
                .to(exchange)
                .with(RabbitMQConfigConstants.ACCOUNT_BALANCE_UPDATE_ROUTING_KEY);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate rabbitTemplate =  new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
