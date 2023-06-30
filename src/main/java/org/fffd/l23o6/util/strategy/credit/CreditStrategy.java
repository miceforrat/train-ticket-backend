package org.fffd.l23o6.util.strategy.credit;

import java.util.HashMap;
import java.util.Map;

public class CreditStrategy {
    private final Integer[] key = {1000,3000,10000,50000};
    private Map<Integer, Double> discountTable;
    public CreditStrategy() {
        discountTable = new HashMap<Integer, Double>();

        // 添加折扣表中的规则
        discountTable.put(1000, 0.1);
        discountTable.put(3000, 0.15);
        discountTable.put(10000, 0.2);
        discountTable.put(50000, 0.3);
    }

    public double getDiscount(int mileagePoints) {
        double discount = 0.0;

        // 遍历折扣表中的规则，找到符合条件的最高折扣
        for (int i = 0 ; i < 4;i++) {
            if (mileagePoints >= key[i]) {
                discount = discountTable.getOrDefault(key[i],0.0);
            } else {
                break;
            }
        }

        return discount;
    }
}
