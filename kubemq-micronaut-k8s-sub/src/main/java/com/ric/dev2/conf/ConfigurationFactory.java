package com.ric.dev2.conf;

import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.queue.Queue;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.util.Random;

@Factory
public class ConfigurationFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFactory.class);

    @Property(name = "kubemq.channel.name")
    private String channelName;

    @Property(name = "kubemq.host")
    private String kubemqHost;

    @Property(name = "kubemq.port")
    private String kubemqPort;

    @Singleton
    public Queue channel() throws ServerAddressNotSuppliedException, SSLException {
        String clientIDRandom = "pub-app" + new Random().nextInt(1000);
        Queue queue = new Queue(channelName, clientIDRandom, kubemqHost + ":" + kubemqPort);
        logger.info("Channel Created OK");
        return queue;
    }

}
