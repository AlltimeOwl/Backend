package com.owl.payrit.domain.promise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromiseImageType {

    COIN("Coin.jpg"),
    HEART("Heart.jpg"),
    FOOD("Food.jpg"),
    MONEY("Money.jpg"),
    SHOPPING("Shopping.jpg"),
    COFFEE("Coffee.jpg"),
    PRESENT("Present.jpg"),
    BOOK("Book.jpg");

    private final String fileName;
}
