package ru.otus;

import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Банкноты.
 */
@Data
public class BankNotes {

    /**
     * Банкноты одного номинала.
     */
    private OneTypeBankNotes oneTypeBankNotes;

    /**
     * Другие банкноты любых номиналов.
     */
    @Setter
    private BankNotes otherBankNotes;

    public BankNotes(OneTypeBankNotes oneTypeBankNotes) {
        this.oneTypeBankNotes = oneTypeBankNotes;
    }

    public BankNotes(OneTypeBankNotes oneTypeBankNotes, BankNotes otherBankNotes) {
        this.oneTypeBankNotes = oneTypeBankNotes;
        this.otherBankNotes = otherBankNotes;
    }

    /**
     * Возвращает количество и номинал имеющихся банкнот, которые составляют сумму {@code moneyAmount},
     * если такие определены. В противном случае возвращает {@link Optional#empty}.
     *
     * @param moneyAmount сумма денег
     */
    public Optional<List<OneTypeBankNotes>> get(long moneyAmount) {
        int requiredMaxOneTypeBankNoteCount = (int) (moneyAmount / oneTypeBankNotes.getDenomination());
        if (oneTypeBankNotes.getNumber() >= requiredMaxOneTypeBankNoteCount &&
                (long) oneTypeBankNotes.getDenomination() * requiredMaxOneTypeBankNoteCount == moneyAmount) {
            List<OneTypeBankNotes> result = new ArrayList<>();
            result.add(new OneTypeBankNotes(oneTypeBankNotes.getDenomination(), requiredMaxOneTypeBankNoteCount));
            return Optional.of(result);
        }
        if (otherBankNotes != null) {
            for (int i = requiredMaxOneTypeBankNoteCount; i >= 0; i--) {
                Optional<List<OneTypeBankNotes>> otherOneTypeBankNotesOpt = otherBankNotes.get(moneyAmount -
                        (long) i * oneTypeBankNotes.getDenomination());
                if (otherOneTypeBankNotesOpt.isPresent()) {
                    List<OneTypeBankNotes> result = otherOneTypeBankNotesOpt.get();
                    if (i > 0)
                        result.add(new OneTypeBankNotes(oneTypeBankNotes.getDenomination(), i));
                    return Optional.of(result);
                }
            }
        }
        return Optional.empty();
    }
}
