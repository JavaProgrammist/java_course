package ru.otus;

import lombok.Data;

/**
 * Банкноты одного номинала.
 */
@Data
public class OneTypeBankNotes {

    /**
     * Номинал.
     */
    private int denomination;

    /**
     * Количество банкнот.
     */
    private int number;

    public OneTypeBankNotes(int denomination, int number) {
        if (denomination <= 0)
            throw new RuntimeException("Denomination of bank note cannot be less than 0");

        this.denomination = denomination;
        this.number = number;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void addNumber(int number) {
        if (number < 0)
            throw new RuntimeException("Count of added money is less than 0");
        this.number += number;
    }

    public void subtractNumber(int number) {
        if (number < 0)
            throw new RuntimeException("Count of subtracted money is less than 0");
        if (this.number - number < 0)
            throw new RuntimeException("Remainder cannot be less than 0");
        this.number -= number;
    }

    /**
     * Возвращает общее количество денег, которые составляют банкноты.
     */
    public long getMoneyAmount() {
        return (long) denomination * number;
    }
}
