package com.example.pi_back.Entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private String description;
    private BigDecimal amountinvestment;
    private Currency currency;

    private BigDecimal tauxinvest;
    @Enumerated(EnumType.STRING)
    private IntevAge intevage;
 //   private String mailInvestesment;
private int riskscore;
    private String country;
    private LocalDate startdate;
    private LocalDate enddate;
    private String category;

    @ManyToMany
    private Set<User> users;
    private BigDecimal finalamount;

    @ManyToOne(cascade=CascadeType.PERSIST)
    private Fund fund;

    @ManyToMany
    private List<User> likes;
    @ManyToMany
    private List<User> dislikes;

    private String qrCode;



    @ManyToMany(mappedBy = "investedProjects")
    private Set<Investor> investors;

}


