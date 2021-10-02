package ru.otus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StartPoint {

    public static void main(String[] args) {
        BankNotesGetter bankNotesGetter = new LargestBankNotesGetter();
        Atm atm = new AtmImpl(bankNotesGetter);

        List<OneTypeBankNotes> bankNotes = new ArrayList<>();
        bankNotes.add(new OneTypeBankNotes(Denomination.ONE, 14));
        bankNotes.add(new OneTypeBankNotes(Denomination.THREE, 7));
        bankNotes.add(new OneTypeBankNotes(Denomination.FIVE, 8));
        bankNotes.add(new OneTypeBankNotes(Denomination.TEN, 4));
        bankNotes.add(new OneTypeBankNotes(Denomination.TWENTY, 12));
        bankNotes.add(new OneTypeBankNotes(Denomination.FIFTY, 6));
        bankNotes.add(new OneTypeBankNotes(Denomination.HUNDRED, 5));
        bankNotes.add(new OneTypeBankNotes(Denomination.THREE, 1));

        atm.putBankNotes(bankNotes);
        List<OneTypeBankNotes> withdrawnBankNotes = atm.withdrawBankNotes(348)
                .stream().sorted(getOneTypeBankNotesComparatorByNominalDesc()).collect(Collectors.toList());
        for (OneTypeBankNotes item : withdrawnBankNotes) {
            System.out.println(String.format("Было снято %d банкнот номиналом %d", item.getNumber(),
                    item.getDenominationVal()));
        }
        System.out.println(atm.getMoneyAmount());

        bankNotes = new ArrayList<>();
        bankNotes.add(new OneTypeBankNotes(Denomination.THREE, 3));

        atm.putBankNotes(bankNotes);
        System.out.println(atm.getMoneyAmount());

        //atm.withdrawBankNotes(5000);
        //System.out.println(atm.getMoneyAmount());
    }

    private static Comparator<OneTypeBankNotes> getOneTypeBankNotesComparatorByNominalDesc() {
        return Comparator.comparingInt(OneTypeBankNotes::getDenominationVal).reversed();
    }
}
