package com.ndvr.challenge.utility;

import com.ndvr.challenge.model.ClosePrice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Scenario {

    public void scenario(List<BigDecimal> monthlyChanges, BigDecimal currentValue) {
        scenario(monthlyChanges, currentValue, 240);
    }

    public List<ClosePrice> scenario(List<BigDecimal> monthlyChanges, BigDecimal currentValue, int monthsToCalculate) {
        LocalDate dateTillCalculate = LocalDate.now().plusMonths(monthsToCalculate);

        List<ClosePrice> closePrices = new ArrayList<ClosePrice>();

        for (LocalDate date = LocalDate.now(); date.isBefore(dateTillCalculate); date = date.plusDays(1)) {
            ClosePrice closePrice = new ClosePrice();

            closePrice.setTradeDate(date);
            closePrice.setClosePrice(getRandomValue(monthlyChanges).add(new BigDecimal(1)).multiply(currentValue));
            closePrices.add(closePrice);
        }

        return closePrices;
    }

    private BigDecimal getRandomValue(List<BigDecimal> monthlyChanges) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(monthlyChanges.size());

        return monthlyChanges.get(randomIndex);
    }
}
