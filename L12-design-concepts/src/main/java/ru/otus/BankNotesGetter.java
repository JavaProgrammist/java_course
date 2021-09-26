package ru.otus;

import java.util.List;
import java.util.Optional;

/**
 * Определяет номинал и количество банкнот, которые должны быть использованы для выдачи терминалом.
 */
public interface BankNotesGetter {

    /**
     * Возвращает номинал и количество банкнот, которые должны быть выданы.
     * @param availableBankNotes доступные банкноты
     * @param moneyAmount общее сумма денег, требуемая к выдаче
     */
    Optional<List<OneTypeBankNotes>> getBankNotesWithTotalAmount(List<OneTypeBankNotes> availableBankNotes,
                                                                 long moneyAmount);
}
