package store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Accounting {
    public static String getCustomerType(double totalCash, double totalCredit) {
        String response = "Corriente";
        if (totalCredit > totalCash) {
            response = "Crédito";
        }
        
        return response;
    }
    
    public static String getMoreItemsPurchaseCode(List<Bill> bills) {
        double biggest = 0;
        String code = null;
        for (Bill bill : bills) {
            if (bill.getTotalItems()> biggest) {
                biggest = bill.getTotalItems();
                code = bill.getCode();
            }
        }
        return code;
    }
    
    public static String getTop3Purchases(List<Bill> bills) {
        List<Double> prices = new ArrayList<>();
        
        for (Bill i : bills) {
            prices.add(i.getTotalPrice());
        }
        
        Collections.sort(prices);
        Collections.reverse(prices);
        
        String string = "";
        int count = 0;
        for (Double price : prices) {
            if (count < 3) {
                string = string + new StringBuilder()
                        .append(String.valueOf(price))
                        .append("\n")
                        .toString();
            }
            count ++;
        }
        
        return string;
    }
}
