package com.example.pi_back.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private float amountinvestment;
    private int numberinv;
    private float tauxinvest;
    @Enumerated(EnumType.STRING)
    private IntevAge invetage;
    //   private String mailInvestesment;
    private int riskscore;
    private String town;
    private LocalDate startdate;
    private LocalDate enddate;
    private String category;

    @ManyToMany
    private Set<User> users;
    private float finalamount;

    @ManyToOne(cascade=CascadeType.PERSIST)
    private Fund fund;

    @ManyToMany
    private List<User> likes;
    @ManyToMany
    private List<User> dislikes;

    private String qrCode;

    public void setTaxCompliant(boolean isCompliant) {
    }
}
