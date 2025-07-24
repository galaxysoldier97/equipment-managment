package mc.monacotelecom.tecrep.equipments.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class EsimNotificationConfiguration {

    @Value("${esim.notification.queue:tecrep_eqm_notification_smdp_esim}")
    public String exchangeName;

    @Value("${esim.notification.queue:tecrep_eqm_notification_smdp_esim-request}")
    public String queueName;

    @Value("${esim.notificationp.routing-key:#}")
    public String routingKey;

    @Bean
    Queue eSimNotificationQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "dead.letter." + queueName)
                .build();
    }

    @Bean
    Queue eSimNotificationDlq() {
        return QueueBuilder.durable("dead.letter." + queueName).build();
    }

    @Bean
    TopicExchange eSimNotificationExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    Binding eSimNotificationBinding(@Qualifier("eSimNotificationQueue") Queue queue, @Qualifier("eSimNotificationExchange") TopicExchange exchange) {
        log.info(String.format("Configuring exchange for eSim notification with: '%s':'%s':'%s'", exchangeName, queue.getName(), routingKey));
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        ObjectMapper objectMapper = (new ObjectMapper()).findAndRegisterModules();
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        DefaultClassMapper classMapper = new DefaultClassMapper();
        converter.setClassMapper(classMapper);
        return converter;
    }
}
