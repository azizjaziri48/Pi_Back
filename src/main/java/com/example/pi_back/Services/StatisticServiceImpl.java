package com.example.pi_back.Services;

import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Statistic;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService{
    @Override
    public Statistic CreateStatistic(List<Offer> offers) {
        Statistic statistics = new Statistic();
        ((Statistic) statistics).setCount(offers.stream().count());
        ((Statistic) statistics)
                .setAvg(BigDecimal.valueOf(offers.stream().mapToDouble(t -> t.getValeur()).average().orElse(0.0))
                        .setScale(2, RoundingMode.HALF_UP));
        ((Statistic) statistics)
                .setMin(BigDecimal.valueOf(offers.stream().mapToDouble(t -> t.getValeur()).min().orElse(0.0)).setScale(2,
                        RoundingMode.HALF_UP));
        ((Statistic) statistics)
                .setMax(BigDecimal.valueOf(offers.stream().mapToDouble(t -> t.getValeur()).max().orElse(0.0)).setScale(2,
                        RoundingMode.HALF_UP));
        ((Statistic) statistics).setSum(BigDecimal.valueOf(offers.stream().mapToDouble(t -> t.getValeur()).sum())
                .setScale(2, RoundingMode.HALF_UP));

        return (Statistic) statistics;
    }
}
