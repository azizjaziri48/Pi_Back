package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Fund implements Serializable {
    @Id
    @Column(name ="idFund")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFund ;
    private float amountFund ;
    private float tauxFund ;
    private float tauxGain ;
   /* @OneToMany(mappedBy="funds")
    private Set<Credit> credits ;*/
    @JsonIgnore
    @OneToMany(mappedBy="fund")
    private Set<Investment> invest;


    public float getTauxGain() {
        return tauxGain;
    }
    public void setTauxGain(float tauxGain) {
        this.tauxGain = tauxGain;
    }

    public long getIdFund() {
        return idFund;
    }
    public void setIdFund(long idFund) {
        this.idFund = idFund;
    }
    public float getAmountFund() {
        return amountFund;
    }
    public void setAmountFund(float amountFund) {
        this.amountFund = amountFund;
    }

    public float getTauxFund() {
        return tauxFund;
    }
    public void setTauxFund(float tauxFund) {
        this.tauxFund = tauxFund;
    }
   /* public Set<Credit> getCredits() {
        return credits;
    }
    public void setCredits(Set<Credit> credits) {
        this.credits = credits;
    }*/
    public Set<Investment> getInvest() {
        return invest;
    }
    public void setInvest(Set<Investment> invest) {
        this.invest = invest;
    }

    @ManyToMany
    private Set<User> users;


}
/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFund ;
    private float amountFund ;
    private float tauxFund ;
    private float tauxGain ;
    @JsonIgnore
    @OneToMany(mappedBy = "fund")
    private Set<Investment> investesments;
}*/
