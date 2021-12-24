package com.ric.dev2.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ric.dev2.repository.MessageRepository;
import com.ric.dev2.repository.entity.RegistrationMessageEntity;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.Transaction;
import io.kubemq.sdk.queue.TransactionMessagesResponse;
import io.kubemq.sdk.tools.Converter;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class EventListener implements ApplicationEventListener<ServerStartupEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);
    private MessageRepository messageRepository;
    private Queue queue;

    public EventListener(MessageRepository messageRepository, Queue queue) {
        this.messageRepository = messageRepository;
        this.queue = queue;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        try {
            receive();
        } catch (ServerAddressNotSuppliedException e) {
            logger.error(e.getMessage());
        } catch (SSLException e) {
            logger.error(e.getMessage());
        }
    }

    private void receive() throws ServerAddressNotSuppliedException, SSLException {
        ExecutorService threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        threads.execute(() -> {
            while (true) {
                try {
                    Transaction tx = queue.CreateTransaction();
                    TransactionMessagesResponse resRec = tx.Receive(10, 10);
                    if (resRec.getMessage().getBody().length > 0) {
                        logger.info("MessageID: {}, Body: {}", resRec.getMessage().getMessageID(), Converter.FromByteArray(resRec.getMessage().getBody()));
                        ObjectMapper mapper = new ObjectMapper();
                        var messageJson = (String) Converter.FromByteArray(resRec.getMessage().getBody());
                        RegistrationMessageEntity messageEntity = mapper.readValue(messageJson, RegistrationMessageEntity.class);
                        messageEntity.setCreateDay(new Timestamp(System.currentTimeMillis()));
                        messageRepository.save(messageEntity);
                        TransactionMessagesResponse resAck = tx.AckMessage();
                        if (resAck.getIsError()) {
                            logger.error("Ack message error: {}", resAck.getError());
                        }
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        });

    }
}
