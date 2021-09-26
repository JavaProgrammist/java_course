package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Банкомат.
 */
public class Atm {

    //Имеющиеся монеты разных номиналов.
    private List<OneTypeBankNotes> bankNotes = new ArrayList<>();

    private BankNotesGetter bankNotesGetter;

    public Atm(BankNotesGetter bankNotesGetter) {
        this.bankNotesGetter = bankNotesGetter;
    }

    public synchronized void putBankNotes(List<OneTypeBankNotes> bankNotesList) {
        for (OneTypeBankNotes newBankNotes : bankNotesList) {
            Optional<OneTypeBankNotes> existBankNotesOpt = this.bankNotes.stream()
                    .filter(item -> item.getDenomination() == newBankNotes.getDenomination())
                    .findFirst();
            if (existBankNotesOpt.isPresent())
                existBankNotesOpt.get().addNumber(newBankNotes.getNumber());
            else this.bankNotes.add(newBankNotes);
        }
    }

    public synchronized List<OneTypeBankNotes> withdrawBankNotes(int moneyAmount) {
        List<OneTypeBankNotes> withdrawnBankNotes = bankNotesGetter.getBankNotesWithTotalAmount(bankNotes, moneyAmount)
                .orElseThrow(() -> new RuntimeException("Not enough money or no suitable banknotes at the ATM"));
        for (OneTypeBankNotes withdrawnOneTypeBankNotes : withdrawnBankNotes) {
            OneTypeBankNotes existOneTypeBankNotes = bankNotes.stream()
                    .filter(item -> item.getDenomination() == withdrawnOneTypeBankNotes.getDenomination())
                    .findFirst().orElseThrow(() -> new RuntimeException("No bank note of denomination " +
                    withdrawnOneTypeBankNotes.getDenomination() + " was found"));
            existOneTypeBankNotes.subtractNumber(withdrawnOneTypeBankNotes.getNumber());
            if (existOneTypeBankNotes.getNumber() == 0)
                bankNotes.remove(existOneTypeBankNotes);
        }
        return withdrawnBankNotes;
    }

    public long getMoneyAmount() {
        return bankNotes.stream().map(OneTypeBankNotes::getMoneyAmount)
                .reduce(Long::sum).orElse(0L);
    }
}
