package ru.otus;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Определяет номинал и количество банкнот, которые должны быть использованы для выдачи терминалом с таким учетом,
 * чтобы как можно меньше было самих банкнот.
 */
public class LargestBankNotesGetter implements BankNotesGetter {
    @Override
    public Optional<List<OneTypeBankNotes>> getBankNotesWithTotalAmount(List<OneTypeBankNotes> availableBankNotes,
                                                                        long moneyAmount) {
        if (availableBankNotes.size() == 0) return Optional.empty();

        AtomicReference<BankNotes> bankNotes = new AtomicReference<>();
        availableBankNotes.stream()
                .sorted(Comparator.comparingInt(OneTypeBankNotes::getDenomination))
                .forEach(item-> {
                    if (bankNotes.get() == null)
                        bankNotes.set(new BankNotes(item));
                    else {
                        bankNotes.set(new BankNotes(item, bankNotes.get()));
                    }
                });

        return bankNotes.get().get(moneyAmount);
    }
}
