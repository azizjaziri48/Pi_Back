package com.example.pi_back.Services;

import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Statistic;

import java.util.List;

public interface StatisticService {
     Statistic CreateStatistic(List<Offer> products);

}
