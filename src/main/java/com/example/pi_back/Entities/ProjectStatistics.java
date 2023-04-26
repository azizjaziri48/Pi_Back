package com.example.pi_back.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatistics {

    private int totalProjects;
    private BigDecimal totalAmountInvestment;
    private BigDecimal averageTauxInvest;
    private int totalLikes;
    private int totalDislikes;
}