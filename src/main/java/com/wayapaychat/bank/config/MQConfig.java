package com.wayapaychat.bank.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${spring.rabbitmq.queue}")
    private String QUEUE;

    @Value("${spring.rabbitmq.topic}")
    private String EXCHANGE;

    @Value("${spring.rabbitmq.routekey}")
    private String ROUTE_KEY;

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange mTopicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding mBinding(Queue queue, TopicExchange topic) {
        return BindingBuilder
                .bind(queue)
                .to(topic)
                .with(ROUTE_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate mAmqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());

        return template;
    }
}
