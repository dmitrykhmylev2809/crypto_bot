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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private SubscriptionRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        Long chatId = message.getChatId();
        String text = "";

        Subscription subscription = repository.findById(chatId).orElse(null);
        if (subscription != null) {
            BigDecimal subscriptionPrice = subscription.getSubscriptionPrice();
            text = "Вы подписаны на стоимость биткойна " + TextUtil.bigToString(subscriptionPrice) + " USD";
            } else {
            text = "Активные подписки отсутствуют";
        }
        answer.setChatId(message.getChatId());
        answer.setText(text);
        try {
            absSender.execute(answer);
        } catch (Exception e) {

        }
    }
}