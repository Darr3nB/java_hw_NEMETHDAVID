package com.ndvr.challenge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosePrice {

    BigDecimal closePrice;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate tradeDate;
}
