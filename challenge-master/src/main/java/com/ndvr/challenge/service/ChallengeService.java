package com.ndvr.challenge.service;

import com.ndvr.challenge.dataprovider.YahooFinanceClient;
import com.ndvr.challenge.model.Pricing;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ChallengeService {

    private final YahooFinanceClient dataProvider;

    public List<Pricing> getHistoricalAssetData(String symbol, LocalDate fromDate, LocalDate toDate) {
        log.info("Fetching historical price data for {}", symbol);
        return dataProvider.fetchPriceData(symbol, fromDate, toDate);
    }
    
    public List<BigDecimal> getProjectedAssetData(String symbol) {
        log.info("Generating projected price data for {}", symbol);
        // TODO Implement getProjectedAssetData()
        return List.of();
    }

}
