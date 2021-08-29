package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Atm {

    private List<OneTypeBankNotes> bankNotes = new ArrayList<>();
    private BankNotesGetter bankNotesGetter;

    public Atm(BankNotesGetter bankNotesGetter) {
        this.bankNotesGetter = bankNotesGetter;
    }


    public synchronized void putBankNotes(List<OneTypeBankNotes> bankNotes) {
        for (OneTypeBankNotes newBankNotes : bankNotes) {
            Optional<OneTypeBankNotes> existBankNotesOpt = bankNotes.stream()
                    .filter(item -> item.getNominal() == newBankNotes.getNominal())
                    .findFirst();
            if (existBankNotesOpt.isPresent())
                existBankNotesOpt.get().addNumber(newBankNotes.getNumber());
            else bankNotes.add(newBankNotes);
        }
    }

    public synchronized List<OneTypeBankNotes> withdrawBankNotes(int moneyAmount) {
        List<OneTypeBankNotes> withdrawnBankNotes = bankNotesGetter.getBankNotesWithTotalAmount(bankNotes, moneyAmount)
                .orElseThrow(() -> new RuntimeException("Недостаточно денег или нет подходящих банкнот в банкомате"));
        for (OneTypeBankNotes withdrawnOneTypeBankNotes : withdrawnBankNotes) {
            OneTypeBankNotes existOneTypeBankNotes = bankNotes.stream()
                    .filter(item -> item.getNominal() == withdrawnOneTypeBankNotes.getNominal())
                    .findFirst().orElseThrow(() -> new RuntimeException("Не найдена банкнота " +
                    "номиналом " + withdrawnOneTypeBankNotes.getNominal()));
            existOneTypeBankNotes.addNumber(Math.negateExact(withdrawnOneTypeBankNotes.getNumber()));
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
