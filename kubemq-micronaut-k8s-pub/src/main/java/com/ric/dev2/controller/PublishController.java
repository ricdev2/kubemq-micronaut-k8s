package com.ric.dev2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ric.dev2.repository.MessageRepository;
import com.ric.dev2.repository.entity.RegistrationMessageEntity;
import com.ric.dev2.dto.MessageDTO;
import com.ric.dev2.dto.PublisherResponseDTO;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Message;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.SendMessageResult;
import io.kubemq.sdk.tools.Converter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

@Path("/messages")
public class PublishController {

    private static final Logger logger = LoggerFactory.getLogger(PublishController.class);

    private MessageRepository messageRepository;
    private Queue queue;

    public PublishController(MessageRepository messageRepository, Queue queue) {
        this.messageRepository = messageRepository;
        this.queue = queue;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(rollbackOn = {RuntimeException.class, ServerAddressNotSuppliedException.class, IOException.class})
    public HttpResponse send(@Body MessageDTO messageDTO) throws ServerAddressNotSuppliedException, IOException {
        var entity = new RegistrationMessageEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setBody(messageDTO.getMessage());
        entity.setCreateDay(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(entity);

        var messageToDelivery = new Message();
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(entity);
        messageToDelivery.setBody(Converter.ToByteArray(body));
        SendMessageResult result = queue.SendQueueMessage(messageToDelivery);

        if (result.getIsError()) {
            logger.error(result.getError());
            throw new RuntimeException("Error detected when send message: " +  result.getError());
        }
        logger.info("Event ID: {} sent to queue - OK", result.getMessageID());
        return HttpResponse.status(HttpStatus.CREATED).body(new PublisherResponseDTO(entity.getId()));
    }

}