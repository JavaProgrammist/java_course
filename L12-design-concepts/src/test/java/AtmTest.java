import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AtmTest {

    private static Comparator<OneTypeBankNotes> getOneTypeBankNotesComparatorByNominalDesc() {
        return Comparator.comparingInt(OneTypeBankNotes::getDenominationVal).reversed();
    }

    @BeforeEach
    public void before1() {
        System.out.println("before1");
    }

    @BeforeEach
    public void before2() {
        System.out.println("before1");
    }

    @Test
    void putBankNotesTest() {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new AtmImpl(bankNotesGetter);
        atm.putBankNotes(buildBankNotes());
        assertThat(atm.getMoneyAmount()).isEqualTo(1158);
    }

    @Test
    void withdrawBankNotesTest() {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new AtmImpl(bankNotesGetter);
        atm.putBankNotes(buildBankNotes());
        List<OneTypeBankNotes> factWithdrawnBankNotes = atm.withdrawBankNotes(348)
                .stream().sorted(getOneTypeBankNotesComparatorByNominalDesc()).collect(Collectors.toList());
        List<OneTypeBankNotes> expectedWithdrawnBankNotes = Arrays.asList(
                new OneTypeBankNotes(Denomination.HUNDRED, 3),
                new OneTypeBankNotes(Denomination.TWENTY, 2),
                new OneTypeBankNotes(Denomination.FIVE, 1),
                new OneTypeBankNotes(Denomination.THREE, 1)
        );
        assertThat(factWithdrawnBankNotes).usingRecursiveComparison()
                .isEqualTo(expectedWithdrawnBankNotes);
    }

    @Test
    void withdrawBankNotesAndExceptionTest() {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new AtmImpl(bankNotesGetter);
        Throwable exception = assertThrows(RuntimeException.class,
                () -> atm.withdrawBankNotes(100));
        assertEquals(exception.getMessage(), "Not enough money or no suitable banknotes at the ATM");

        atm.putBankNotes(Collections.singletonList(new OneTypeBankNotes(Denomination.THREE, 5)));
        exception = assertThrows(RuntimeException.class,
                () -> atm.withdrawBankNotes(14));
        assertEquals(exception.getMessage(), "Not enough money or no suitable banknotes at the ATM");
    }

    private List<OneTypeBankNotes> buildBankNotes() {
        List<OneTypeBankNotes> bankNotes = new ArrayList<>();
        bankNotes.add(new OneTypeBankNotes(Denomination.ONE, 14));
        bankNotes.add(new OneTypeBankNotes(Denomination.THREE, 7));
        bankNotes.add(new OneTypeBankNotes(Denomination.FIVE, 8));
        bankNotes.add(new OneTypeBankNotes(Denomination.TEN, 4));
        bankNotes.add(new OneTypeBankNotes(Denomination.TWENTY, 12));
        bankNotes.add(new OneTypeBankNotes(Denomination.FIFTY, 6));
        bankNotes.add(new OneTypeBankNotes(Denomination.HUNDRED, 5));
        bankNotes.add(new OneTypeBankNotes(Denomination.THREE, 1));
        return bankNotes;
    }
}
