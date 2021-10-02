package ru.otus;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Определяет номинал и количество банкнот, которые должны быть использованы для выдачи терминалом с таким учетом,
 * чтобы как можно меньше было самих банкнот.
 */
public class LargestBankNotesGetter implements BankNotesGetter {
    @Override
    public Optional<List<OneTypeBankNotes>> getBankNotesWithTotalAmount(Collection<OneTypeBankNotes> availableBankNotes,
                                                                        long moneyAmount) {
        if (availableBankNotes.size() == 0) {
            return Optional.empty();
        }
        BankNotes bankNotes = null;
        List<OneTypeBankNotes> sortedAvailableBankNotes = availableBankNotes.stream()
                .sorted(Comparator.comparingInt(OneTypeBankNotes::getDenominationVal))
                .collect(Collectors.toList());
        for (OneTypeBankNotes oneTypeBankNotes : sortedAvailableBankNotes) {
            if (bankNotes == null) {
                bankNotes = new BankNotes(oneTypeBankNotes);
            } else {
                bankNotes = new BankNotes(oneTypeBankNotes, bankNotes);
            }
        }

        assert bankNotes != null;
        return bankNotes.get(moneyAmount);
    }
}
