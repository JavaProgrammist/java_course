import org.junit.jupiter.api.Test;
import ru.otus.Atm;
import ru.otus.BankNotesGetter;
import ru.otus.LargestBankNotesGetter;
import ru.otus.OneTypeBankNotes;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtmTest {

    private static Comparator<OneTypeBankNotes> getOneTypeBankNotesComparatorByNominalDesc() {
        return (item1, item2) -> item2.getDenomination() - item1.getDenomination();
    }

    @Test
    void putBankNotesTest() {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new Atm(bankNotesGetter);
        atm.putBankNotes(buildBankNotes());
        assertThat(atm.getMoneyAmount()).isEqualTo(1158);
    }

    @Test
    void withdrawBankNotesTest() {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new Atm(bankNotesGetter);
        atm.putBankNotes(buildBankNotes());
        List<OneTypeBankNotes> factWithdrawnBankNotes = atm.withdrawBankNotes(348)
                .stream().sorted(getOneTypeBankNotesComparatorByNominalDesc()).collect(Collectors.toList());
        List<OneTypeBankNotes> expectedWithdrawnBankNotes = Arrays.asList(
                new OneTypeBankNotes(100, 3),
                new OneTypeBankNotes(20, 2),
                new OneTypeBankNotes(5, 1),
                new OneTypeBankNotes(3, 1)
        );
        assertThat(factWithdrawnBankNotes).usingRecursiveComparison()
                .isEqualTo(expectedWithdrawnBankNotes);
    }

    @Test
    void withdrawBankNotesAndExceptionTest() {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new Atm(bankNotesGetter);
        Throwable exception = assertThrows(RuntimeException.class,
                () -> atm.withdrawBankNotes(100));
        assertEquals(exception.getMessage(), "Not enough money or no suitable banknotes at the ATM");

        atm.putBankNotes(Collections.singletonList(new OneTypeBankNotes(3, 5)));
        exception = assertThrows(RuntimeException.class,
                () -> atm.withdrawBankNotes(14));
        assertEquals(exception.getMessage(), "Not enough money or no suitable banknotes at the ATM");
    }

    private List<OneTypeBankNotes> buildBankNotes() {
        List<OneTypeBankNotes> bankNotes = new ArrayList<>();
        bankNotes.add(new OneTypeBankNotes(1, 14));
        bankNotes.add(new OneTypeBankNotes(3, 7));
        bankNotes.add(new OneTypeBankNotes(5, 8));
        bankNotes.add(new OneTypeBankNotes(10, 4));
        bankNotes.add(new OneTypeBankNotes(20, 12));
        bankNotes.add(new OneTypeBankNotes(50, 6));
        bankNotes.add(new OneTypeBankNotes(100, 5));
        bankNotes.add(new OneTypeBankNotes(3, 1));
        return bankNotes;
    }
}
