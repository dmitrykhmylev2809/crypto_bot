package com.skillbox.cryptobot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "subscription_price")
    private BigDecimal subscriptionPrice;
}