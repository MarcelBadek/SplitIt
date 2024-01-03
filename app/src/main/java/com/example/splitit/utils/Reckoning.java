package com.example.splitit.utils;

import static java.lang.Math.abs;

import com.example.splitit.Model.Group;
import com.example.splitit.Model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Reckoning {
    public static HashMap<String, Double> calculateBalance(Group group) {
        HashMap<String, Double> pairs = new HashMap<>();

        if (group.getBills() == null) {
            return pairs;
        } else if (group.getBills().isEmpty()) {
            return pairs;
        }

        group.getMembers().forEach(member -> {
            pairs.put(member.getEmail(), 0d);
        });

        group.getBills().forEach(bill -> {
            double perPersonCost = bill.getPrice() / bill.getMembers().size();
            double whoPaiedCost = bill.getPrice() - perPersonCost;

            bill.getMembers().forEach(member -> {
                if (Objects.equals(member.getEmail(), bill.getWhoPaied().getEmail())) {
                    pairs.replace(member.getEmail(), pairs.get(member.getEmail()) + whoPaiedCost);
                } else {
                    pairs.replace(member.getEmail(), pairs.get(member.getEmail()) - perPersonCost);
                }
            });
        });

        pairs.forEach((key, value) -> {
            pairs.replace(key, round(pairs.get(key), 2));
        });

        return pairs;
    }

    public static List<Transaction> calculateTransactions(HashMap<String, Double> balance) {
        List<Transaction> transactions = new ArrayList<>();

        if (balance == null) {
            return transactions;
        } else if (balance.isEmpty()) {
            return transactions;
        }

        HashMap<String, Double> debtors = new HashMap<>();
        HashMap<String, Double> creditors = new HashMap<>();
        String creditorKey;
        Double creditorValue;
        String debtorKey;
        Double debtorValue;

        balance.forEach((key, value) -> {
            if (value < 0) {
                debtors.put(key, value);
            } else if (value > 0) {
                creditors.put(key, value);
            }
        });

        while (debtors.size() > 0 && creditors.size() > 0) {
            creditorKey = Collections.max(creditors.entrySet(), Map.Entry.comparingByValue()).getKey();
            creditorValue = creditors.get(creditorKey);

            debtorKey = Collections.max(debtors.entrySet(), Map.Entry.comparingByValue()).getKey();
            debtorValue = debtors.get(debtorKey);

            if (abs(debtorValue) > creditorValue) {
                transactions.add(new Transaction(debtorKey, creditorKey, creditorValue));
                debtors.replace(debtorKey, round(debtorValue + creditorValue, 2));
                creditors.remove(creditorKey);
            } else if (abs(debtorValue) < creditorValue){
                transactions.add(new Transaction(debtorKey, creditorKey, abs(debtorValue)));
                debtors.remove(debtorKey);
                creditors.replace(creditorKey, round(creditorValue + debtorValue, 2));
            } else {
                transactions.add(new Transaction(debtorKey, creditorKey, creditorValue));
                debtors.remove(debtorKey);
                creditors.remove(creditorKey);
            }
        }

        return transactions;
    }

    public static List<Transaction> calculateTransactions(Group group) {
        return calculateTransactions(calculateBalance(group));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
