package com.example.pi_back.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@SuppressWarnings("serial")
@Entity
public class Investment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idInvestment ;
    private long cinInvestment ;
    private String nameInvestment ;
    private String secondnameInvestment ;
    private String professionInvestment ;
    private float tauxInves;
    private boolean state ;
    private float AmountInvestment;
    //j'ai ajouté un attribut
    private String mailInvestment;
    private float finalAmount;
    @ManyToOne
    private Fund fund;
    @Enumerated(EnumType.STRING)
    private IntevAge invetage;

    private String town;
    public String getTown() {
        return town;
    }
    public void setTown(String town) {
        this.town = town;
    }
    public float getFinalAmount() {
        return finalAmount;
    }
    public void setFinalAmount(float finalAmount) {
        this.finalAmount = finalAmount;
    }
    public float getTauxInves() {
        return tauxInves;
    }
    public void setTauxInves(float tauxInves) {
        this.tauxInves = tauxInves;
    }
    public long getCinInvestment() {
        return cinInvestment;
    }
    public void setCinInvestment(long cinInvestment) {
        this.cinInvestment = cinInvestment;
    }
    public String getNameInvestment() {
        return nameInvestment;
    }
    public void setNameInvestment(String nameInvestment) {
        this.nameInvestment = nameInvestment;
    }
    public String getSecondnameInvestment() {
        return secondnameInvestment;
    }
    public void setSecondnameInvestment(String secondnameInvestment) {
        this.secondnameInvestment = secondnameInvestment;
    }
    public String getProfessionInvestment() {
        return professionInvestment;
    }
    public void setProfessionInvestment(String professionInvestment) {
        this.professionInvestment = professionInvestment;
    }
    public float getAmountInvestment() {
        return AmountInvestment;
    }
    public void setAmountInvestment(float AmountInvestment) {
        this.AmountInvestment = AmountInvestment;
    }
    public Fund getFund() {
        return fund;
    }
    public void setFund(Fund fund) {
        this.fund = fund;
    }
    public long getIdInvestment() {
        return idInvestment;
    }
    public void setIdInvestment(long idInvestment) {
        this.idInvestment = idInvestment;
    }
    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public String getMailInvestment() {
        return mailInvestment;
    }
    public void setMailInvestment(String mailInvestment) {
        this.mailInvestment = mailInvestment;
    }
    public Investment(long idInvestment, long cinInvestment, String nameInvestment,
                      String secondnameInvestment, String professionInvestment, float tauxInves, boolean state,
                      float AmountInvestment, String mailInvestment, float finalAmount, Fund fund) {
        super();
        this.idInvestment = idInvestment;
        this.cinInvestment = cinInvestment;
        this.nameInvestment = nameInvestment;
        this.secondnameInvestment = secondnameInvestment;
        this.professionInvestment = professionInvestment;
        this.tauxInves = tauxInves;
        this.state = state;
        this.AmountInvestment = AmountInvestment;
        this.mailInvestment = mailInvestment;
        this.finalAmount = finalAmount;
        this.fund = fund;
    }
    public Investment() {
        super();
        // TODO Auto-generated constructor stub
    }

    @ManyToMany
    private Set<User> users;


}
