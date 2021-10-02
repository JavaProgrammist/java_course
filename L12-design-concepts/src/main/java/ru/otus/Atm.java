package ru.otus;

import java.util.List;

public interface Atm {

    void putBankNotes(List<OneTypeBankNotes> bankNotesList);

    List<OneTypeBankNotes> withdrawBankNotes(int moneyAmount);

    long getMoneyAmount();
}
