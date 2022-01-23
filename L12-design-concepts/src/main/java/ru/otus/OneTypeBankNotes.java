package ru.otus;

/**
 * Банкноты одного номинала.
 */
public class OneTypeBankNotes {

    /**
     * Номинал.
     */
    private Denomination denomination;

    /**
     * Количество банкнот.
     */
    private int number;

    public OneTypeBankNotes(Denomination denomination, int number) {
        if (denomination.getValue() <= 0)
            throw new RuntimeException("Denomination of bank note cannot be less than 0");

        this.denomination = denomination;
        this.number = number;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int getDenominationVal(){
        return denomination.getValue();
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
        return (long) denomination.getValue() * number;
    }
}
