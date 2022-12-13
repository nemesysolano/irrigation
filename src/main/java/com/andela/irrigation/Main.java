package com.andela.irrigation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Entry point
 */

@SpringBootApplication
@EnableAsync
public class Main {

    /**
     *
     * @param args No command line args required
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
