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
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    @Autowired
    private SubscriptionRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        Long chatId = message.getChatId();

        repository.deleteById(chatId);
        String text = "Подписка отменена";

        answer.setChatId(message.getChatId());
        answer.setText(text);
        try {
            absSender.execute(answer);
        } catch (Exception e) {

        }
    }
}