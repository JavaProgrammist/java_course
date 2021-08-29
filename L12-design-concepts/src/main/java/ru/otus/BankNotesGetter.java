package ru.otus;

import java.util.List;
import java.util.Optional;

public interface BankNotesGetter {

    Optional<List<OneTypeBankNotes>> getBankNotesWithTotalAmount(List<OneTypeBankNotes> availableBankNotes,
                                                                 long moneyAmount);
}
