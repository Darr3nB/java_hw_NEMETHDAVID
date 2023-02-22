package com.ndvr.challenge.service;

import com.ndvr.challenge.dataprovider.YahooFinanceClient;
import com.ndvr.challenge.model.Pricing;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static java.time.LocalDate.now;

@Service
@Slf4j
@AllArgsConstructor
public class ChallengeService {

    private final YahooFinanceClient dataProvider;

    public List<Pricing> getHistoricalAssetData(String symbol, LocalDate fromDate, LocalDate toDate) {
        log.info("Fetching historical price data for {}", symbol);
        return dataProvider.fetchPriceData(symbol, fromDate, toDate);
    }

    public List<BigDecimal> getProjectedAssetData(String symbol, Integer months) {
        log.info("Generating projected price data for {}", symbol);
        List<Pricing> pricingList = getHistoricalAssetData(symbol, now().minusYears(5), now());
        BigDecimal currentValue = pricingList.get(pricingList.size() - 1).getClosePrice();

        Map<String, List<BigDecimal>> monthlyPercentageDAta = getClosePricesByMonthAsPercentage(pricingList);

        List<BigDecimal> monthlyChanges = getMonthlyChanges(monthlyPercentageDAta);

        List<BigDecimal> result = new ArrayList<BigDecimal>();

        for (int i = 0; i < 1000; i++) {
            List<BigDecimal> currentScenario = scenario(monthlyChanges, currentValue, months);
            if (i == 0) {
                result = currentScenario;
            }
            if (currentScenario.get(currentScenario.size() - 1).compareTo(result.get(result.size() - 1)) > 0) {
                result = currentScenario;
            }
        }

        log.info("Lowest value: " + result.stream().sorted().findFirst());
        log.info("Median value: " + result.stream().sorted().skip(result.size() / 2).findFirst());

        return result;
    }

    private Map<String, List<BigDecimal>> getClosePricesByMonthAsPercentage(List<Pricing> pricingList) {
        Map<String, List<BigDecimal>> monthlyAverage = new LinkedHashMap<>();

        for (Pricing item : pricingList) {
            String monthYear = item.getTradeDate().toString().substring(0, 7);
            List<BigDecimal> closePrice = monthlyAverage.getOrDefault(monthYear, new ArrayList<>());
            closePrice.add(item.getClosePrice());
            monthlyAverage.put(monthYear, closePrice);
        }
        return monthlyAverage;
    }

    private List<BigDecimal> getMonthlyChanges(Map<String, List<BigDecimal>> monthlyPercentageData) {
        List<BigDecimal> monthlyChanges = new ArrayList<>();

        for (List<BigDecimal> closePrice : monthlyPercentageData.values()) {
            if (!closePrice.isEmpty()) {
                int numDays = closePrice.size();
                BigDecimal firstPrice = closePrice.get(0);
                BigDecimal lastPrice = closePrice.get(numDays - 1);
                BigDecimal resultForMonth = lastPrice.divide(firstPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

                monthlyChanges.add(resultForMonth);
            }
        }

        return monthlyChanges;
    }

    private List<BigDecimal> scenario(List<BigDecimal> monthlyChanges, BigDecimal currentValue, int monthsToCalculate) {
        LocalDate dateTillCalculate = LocalDate.now().plusMonths(monthsToCalculate);

        List<BigDecimal> closePrices = new ArrayList<BigDecimal>();

        for (LocalDate date = LocalDate.now(); date.isBefore(dateTillCalculate); date = date.plusDays(1)) {
            closePrices.add(getRandomValue(monthlyChanges).add(new BigDecimal(1)).multiply(currentValue));
        }

        return closePrices;
    }

    private BigDecimal getRandomValue(List<BigDecimal> monthlyChanges) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(monthlyChanges.size());

        return monthlyChanges.get(randomIndex);
    }
}
