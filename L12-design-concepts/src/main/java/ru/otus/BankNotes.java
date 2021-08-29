package ru.otus;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BankNotes {

    private OneTypeBankNotes oneTypeBankNotes;

    @Setter
    private BankNotes otherBankNotes;

    public BankNotes(OneTypeBankNotes oneTypeBankNotes) {
        this.oneTypeBankNotes = oneTypeBankNotes;
    }

    public Optional<List<OneTypeBankNotes>> get(long moneyAmount) {
        int requiredMaxOneTypeBankNoteCount = (int) (moneyAmount / oneTypeBankNotes.getNominal());
        if (otherBankNotes == null) {
            if (oneTypeBankNotes.getNumber() >= requiredMaxOneTypeBankNoteCount &&
                    (long) oneTypeBankNotes.getNominal() * requiredMaxOneTypeBankNoteCount == moneyAmount) {
                List<OneTypeBankNotes> result = Collections.singletonList(
                        new OneTypeBankNotes(oneTypeBankNotes.getNominal(), requiredMaxOneTypeBankNoteCount));
                return Optional.of(result);
            } else return Optional.empty();
        } else {
            for (int i = requiredMaxOneTypeBankNoteCount; i >= 0; i--) {
                Optional<List<OneTypeBankNotes>> otherOneTypeBankNotesOpt = otherBankNotes.get(moneyAmount -
                        (long) i * oneTypeBankNotes.getNominal());
                if (otherOneTypeBankNotesOpt.isPresent()) {
                    List<OneTypeBankNotes> result = otherOneTypeBankNotesOpt.get();
                    result.add(new OneTypeBankNotes(oneTypeBankNotes.getNominal(), i));
                    return Optional.of(result);
                }
            }
            return Optional.empty();
        }
    }
}
