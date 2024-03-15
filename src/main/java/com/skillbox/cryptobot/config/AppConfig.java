package com.skillbox.cryptobot.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${telegram.bot.check.value}")
    private int checkDelayValue;

    @Value("${telegram.bot.check.unit}")
    private String checkDelayUnit;

    @Value("${telegram.bot.notify.delay.value}")
    private int delayValue;

    @Value("${telegram.bot.notify.delay.unit}")
    private String delayUnit;

    @Bean
    public String checkInterval() {

        return "PT" + checkDelayValue + checkDelayUnit.charAt(0);
    }

    @Bean
    public String notifyInterval() {
        return "PT" + delayValue + delayUnit.charAt(0);
    }

}