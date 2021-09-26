package ru.otus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StartPoint {

    public static void main(String[] args) {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new Atm(bankNotesGetter);

        List<OneTypeBankNotes> bankNotes = new ArrayList<>();
        bankNotes.add(new OneTypeBankNotes(1, 14));
        bankNotes.add(new OneTypeBankNotes(3, 7));
        bankNotes.add(new OneTypeBankNotes(5, 8));
        bankNotes.add(new OneTypeBankNotes(10, 4));
        bankNotes.add(new OneTypeBankNotes(20, 12));
        bankNotes.add(new OneTypeBankNotes(50, 6));
        bankNotes.add(new OneTypeBankNotes(100, 5));
        bankNotes.add(new OneTypeBankNotes(3, 1));

        atm.putBankNotes(bankNotes);
        List<OneTypeBankNotes> withdrawnBankNotes = atm.withdrawBankNotes(348)
                .stream().sorted(getOneTypeBankNotesComparatorByNominalDesc()).collect(Collectors.toList());
        for (OneTypeBankNotes item : withdrawnBankNotes) {
            System.out.println(String.format("Было снято %d банкнот номиналом %d", item.getMoneyAmount(),
                    item.getDenomination()));
        }
        System.out.println(atm.getMoneyAmount());

        bankNotes = new ArrayList<>();
        bankNotes.add(new OneTypeBankNotes(3, 3));

        atm.putBankNotes(bankNotes);
        System.out.println(atm.getMoneyAmount());

        //atm.withdrawBankNotes(5000);
        //System.out.println(atm.getMoneyAmount());
    }

    private static Comparator<OneTypeBankNotes> getOneTypeBankNotesComparatorByNominalDesc() {
        return (item1, item2) -> item2.getDenomination() - item1.getDenomination();
    }
}
