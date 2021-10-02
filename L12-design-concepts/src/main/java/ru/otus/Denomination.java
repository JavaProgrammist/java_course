package ru.otus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Номинал банкнот.
 */
public enum Denomination {
    ONE(1),
    THREE(3),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100);

    Denomination(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }
}
