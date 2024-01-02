package com.example.splitit.utils;

import com.example.splitit.Model.Group;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Objects;

public class Reckoning {
    public static HashMap<String, Double> calculate(Group group) {
        HashMap<String, Double> pairs = new HashMap<>();
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
