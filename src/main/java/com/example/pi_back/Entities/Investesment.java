package com.example.pi_back.Entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Investesment implements Serializable {

    @Id
    @Column(name = "idInvestesment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idInvestesment;

    private long cinInvestesment;
    private String nameInvestesment;
    private String secondnnameInvestesment;
    private String professionInvestesment;
    private float tauxInves;
    private boolean state;
    private float amoutInvestesment;
    private String mailInvestesment;
    private float finalAmount;

    @ManyToOne
    private Fund fund;

    public Investesment() {
    }

    public long getIdInvestesment() {
        return idInvestesment;
    }

    public void setIdInvestesment(long idInvestesment) {
        this.idInvestesment = idInvestesment;
    }

    public long getCinInvestesment() {
        return cinInvestesment;
    }

    public void setCinInvestesment(long cinInvestesment) {
        this.cinInvestesment = cinInvestesment;
    }

    public String getNameInvestesment() {
        return nameInvestesment;
    }

    public void setNameInvestesment(String nameInvestesment) {
        this.nameInvestesment = nameInvestesment;
    }

    public String getSecondnnameInvestesment() {
        return secondnnameInvestesment;
    }

    public void setSecondnnameInvestesment(String secondnnameInvestesment) {
        this.secondnnameInvestesment = secondnnameInvestesment;
    }

    public String getProfessionInvestesment() {
        return professionInvestesment;
    }

    public void setProfessionInvestesment(String professionInvestesment) {
        this.professionInvestesment = professionInvestesment;
    }

    public float getTauxInves() {
        return tauxInves;
    }

    public void setTauxInves(float tauxInves) {
        this.tauxInves = tauxInves;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public float getAmoutInvestesment() {
        return amoutInvestesment;
    }

    public void setAmoutInvestesment(float amoutInvestesment) {
        this.amoutInvestesment = amoutInvestesment;
    }

    public String getMailInvestesment() {
        return mailInvestesment;
    }

    public void setMailInvestesment(String mailInvestesment) {
        this.mailInvestesment = mailInvestesment;
    }

    public float getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(float finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

}
