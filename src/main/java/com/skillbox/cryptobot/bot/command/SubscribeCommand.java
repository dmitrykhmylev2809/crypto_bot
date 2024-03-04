package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.Subscription;
import com.skillbox.cryptobot.repository.SubscriptionRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.math.BigDecimal;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class SubscribeCommand implements IBotCommand {

    private final CryptoCurrencyService service;

    @Autowired
    private SubscriptionRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();

        String subscribeValue = arguments[0];
        Long chatId = message.getChatId();

        Subscription subscription = repository.findById(chatId).orElse(null);
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setId(chatId);
        }
        subscription.setSubscriptionPrice(new BigDecimal(subscribeValue));
        repository.save(subscription);

        String text = "Новая подписка создана на стоимость " + subscribeValue + " USD";

        answer.setChatId(message.getChatId());
        try {
            String text1 = "Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD\n";
            text1 = text1.concat(text);
            answer.setText(text1);
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_price методе", e);
        }
    }
}