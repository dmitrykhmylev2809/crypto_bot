package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.subscriptionPrice > :value")
    List<Subscription> findBySubscriptionPriceGreaterThan(BigDecimal value);
}
