package com.bridgelaz.bridgelabzlms.util;

import com.bridgelaz.bridgelabzlms.dto.RabbitMqDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class RabbitMq {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;


    public void sendMessageToQueue(RabbitMqDTO message) {
        final String exchange = "QueueExchangeConn";
        final String routingKey = "RoutingKey";
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void send(MimeMessage message) {
        javaMailSender.send(message);
    }

    public void RabbitSend(RabbitMqDTO email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF_8");
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(email.getBody());
        send(message);
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receiveMessage(MimeMessage message) {
        send(message);
    }

}
