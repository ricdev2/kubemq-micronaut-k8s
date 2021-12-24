package com.ric.dev2;

import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
        logger.info("Publisher up running!");
    }
}
