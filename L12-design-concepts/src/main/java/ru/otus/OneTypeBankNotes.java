package ru.otus;

public class OneTypeBankNotes {

    private int nominal;

    private int number;

    public OneTypeBankNotes(int nominal, int number) {
        if (nominal <= 0)
            throw new RuntimeException("Номинал не может быть меньше нуля");

        this.nominal = nominal;
        this.number = number;
    }

    public int getNominal() {
        return nominal;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void addNumber(int number) {
        this.number += number;
        if (number > 0)
            throw new RuntimeException("Номинал не может быть меньше нуля");
    }

    public long getMoneyAmount() {
        return (long) nominal * number;
    }
}
