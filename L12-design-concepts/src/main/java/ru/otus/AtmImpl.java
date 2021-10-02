package ru.otus;

import java.util.*;
import java.util.stream.Stream;

/**
 * Банкомат.
 */
public class AtmImpl implements Atm {

    //Имеющиеся монеты разных номиналов.
    private final Map<Denomination, OneTypeBankNotes> bankNotesMap = new HashMap<>();

    private final BankNotesGetter bankNotesGetter;

    public AtmImpl(BankNotesGetter bankNotesGetter) {
        this.bankNotesGetter = bankNotesGetter;
    }

    @Override
    public synchronized void putBankNotes(List<OneTypeBankNotes> bankNotesList) {
        for (OneTypeBankNotes newBankNotes : bankNotesList) {
            OneTypeBankNotes existBankNotes = bankNotesMap.get(newBankNotes.getDenomination());
            if (existBankNotes == null) {
                bankNotesMap.put(newBankNotes.getDenomination(), newBankNotes);
            } else {
                existBankNotes.addNumber(newBankNotes.getNumber());
            }
        }
    }

    @Override
    public synchronized List<OneTypeBankNotes> withdrawBankNotes(int moneyAmount) {
        List<OneTypeBankNotes> withdrawnBankNotes = bankNotesGetter.getBankNotesWithTotalAmount(bankNotesMap.values(),
                moneyAmount)
                .orElseThrow(() -> new RuntimeException("Not enough money or no suitable banknotes at the ATM"));
        for (OneTypeBankNotes withdrawnOneTypeBankNotes : withdrawnBankNotes) {
            OneTypeBankNotes existOneTypeBankNotes = bankNotesMap.get(withdrawnOneTypeBankNotes.getDenomination());
            if (existOneTypeBankNotes == null) {
                throw new RuntimeException("No bank note of denomination " +
                        withdrawnOneTypeBankNotes.getDenomination() + " was found");
            }
            existOneTypeBankNotes.subtractNumber(withdrawnOneTypeBankNotes.getNumber());
            if (existOneTypeBankNotes.getNumber() == 0) {
                bankNotesMap.remove(existOneTypeBankNotes.getDenomination());
            }
        }
        return withdrawnBankNotes;
    }

    @Override
    public long getMoneyAmount() {
        return bankNotesMap.values().stream().map(OneTypeBankNotes::getMoneyAmount)
                .reduce(Long::sum).orElse(0L);
    }
}
