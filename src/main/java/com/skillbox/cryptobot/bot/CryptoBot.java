package com.skillbox.cryptobot.bot;

import com.skillbox.cryptobot.entity.Subscription;
import com.skillbox.cryptobot.repository.SubscriptionRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j

public class CryptoBot extends TelegramLongPollingCommandBot {

    private final String botUsername;
    private final CryptoCurrencyService service;
    private SubscriptionRepository repository;
    private static List<Subscription> subscriptions = new ArrayList<>();
    private static BigDecimal bitcoinPrice = null;
    @Autowired
    private String checkInterval;
    @Autowired
    private String notifyInterval;


    public CryptoBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            List<IBotCommand> commandList, CryptoCurrencyService service,
            SubscriptionRepository repository
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.service = service;
        this.repository = repository;

        commandList.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }


    @Override
    public void processNonCommandUpdate(Update update) {
        checkBitcoinPriceAndSendNotification();
    }


    @Scheduled(fixedDelayString = "#{@checkInterval}")
    private void checkBitcoinPriceAndSendNotification() {

        log.info("Проверка текущей стоимости биткойна - {} ", LocalDateTime.now());

        try {
            bitcoinPrice = new BigDecimal(service.getBitcoinPrice());
        } catch (IOException e) {
            log.error("Ошибка возникла /get_price методе", e);
        }
        subscriptions = repository.findBySubscriptionPriceGreaterThan(bitcoinPrice);

        sendNotifications();
    }

    @Scheduled(fixedDelayString = "#{@notifyInterval}")
    private synchronized void sendNotifications() {
        for (Subscription userSubscription : subscriptions) {
            sendNotification(userSubscription.getId());
        }
        subscriptions.clear();
    }

    private void sendNotification(Long userSubscription) {
        log.info("Рассылка уведомлений - {}", LocalDateTime.now());
        SendMessage answer = new SendMessage();
        answer.setChatId(userSubscription);
        try {
            answer.setText("Пора покупать, стоимость биткоина " + TextUtil.bigToString(bitcoinPrice) + " USD");
            execute(answer);

        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке уведомления", e);
        }
    }
}
