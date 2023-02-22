package com.ndvr.challenge.utility;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class Scenario {

    public void scenario(List<BigDecimal> monthlyChanges){
        scenario(monthlyChanges, 240);
    }

    public void scenario(List<BigDecimal> monthlyChanges, int monthsToCalculate) {
        LocalDate today = LocalDate.now();
        LocalDate dateTillCalculate = LocalDate.now().plusMonths(monthsToCalculate);
    }
}
