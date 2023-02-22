package com.ndvr.challenge.controller;

import com.ndvr.challenge.model.Pricing;
import com.ndvr.challenge.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;

@RestController
@AllArgsConstructor
@Tag(name = "Challenge API")
@RequestMapping("market-data")
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(description = "Gets historical stock prices")
    @GetMapping("/{symbol}/historical")
    public List<Pricing> getHistoricalAssetData(@Parameter(description = "Stock symbol e.g.: TSLA")
                                                @PathVariable String symbol,
                                                @RequestParam(value = "startDate", required = false)
                                                @Parameter(description = "Date to fetch prices from e.g.: 2022-02-02")
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @Parameter(description = "Date to fetch prices until e.g.: 2022-02-02")
                                                @RequestParam(value = "endDate", required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return challengeService.getHistoricalAssetData(symbol,
                Optional.ofNullable(startDate).orElse(now().minusYears(5)),
                Optional.ofNullable(endDate).orElse(now()));
    }

    @Operation(description = "Gets projected stock prices")
    @GetMapping("/{symbol}/projected")
    public List<BigDecimal> getProjectedAssetData(@Parameter(description = "Stock symbol e.g.: TSLA")
                                                  @PathVariable String symbol,
                                                  @RequestParam(value = "months", required = false) Integer months) {
        months = months == null || months <= 0 ? 240 : months;

        return challengeService.getProjectedAssetData(symbol, months);
    }

}
