package com.ndvr.challenge.service;

import com.ndvr.challenge.dataprovider.YahooFinanceClient;
import com.ndvr.challenge.model.Pricing;
import com.ndvr.challenge.utility.Scenario;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.now;

@Service
@Slf4j
@AllArgsConstructor
public class ChallengeService {

    private final YahooFinanceClient dataProvider;
    @Autowired
    Scenario scenario;

    public List<Pricing> getHistoricalAssetData(String symbol, LocalDate fromDate, LocalDate toDate) {
        log.info("Fetching historical price data for {}", symbol);
        return dataProvider.fetchPriceData(symbol, fromDate, toDate);
    }

    public List<BigDecimal> getProjectedAssetData(String symbol) {
        log.info("Generating projected price data for {}", symbol);
        List<Pricing> pricingList = getHistoricalAssetData(symbol, now().minusYears(5), now());

        Map<String, List<BigDecimal>> monthlyAverage = calculateMonthlyAverages(pricingList);

        List<BigDecimal> monthlyChanges = getMonthlyChanges(monthlyAverage);

        scenario.scenario(monthlyChanges);

        return List.of();
    }

    private Map<String, List<BigDecimal>> calculateMonthlyAverages(List<Pricing> pricingList){
        Map<String, List<BigDecimal>> monthlyAverage = new LinkedHashMap<>();

        for (Pricing item : pricingList) {
            String monthYear = item.getTradeDate().toString().substring(0, 7);
            List<BigDecimal> closePrice = monthlyAverage.getOrDefault(monthYear, new ArrayList<>());
            closePrice.add(item.getClosePrice());
            monthlyAverage.put(monthYear, closePrice);
        }
        return monthlyAverage;
    }

    private List<BigDecimal> getMonthlyChanges(Map<String, List<BigDecimal>> monthlyAverage){
        List<BigDecimal> monthlyChanges = new ArrayList<>();

        for (List<BigDecimal> closePrice : monthlyAverage.values()){
            if (!closePrice.isEmpty()){
                int numDays = closePrice.size();
                double firstPrice = closePrice.get(0).doubleValue();
                double lastPrice = closePrice.get(numDays - 1).doubleValue();
                double resultForMonth = (lastPrice / firstPrice) - 1;

                monthlyChanges.add(new BigDecimal(resultForMonth));
            }
        }

        return monthlyChanges;
    }
}
