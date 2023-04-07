package com.example.pi_back.Entities;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.*;

@Entity
@Table(name="Credit")
public class Credit implements Serializable{

    @Id
    @Column(name = "idCredit")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCredit;

    @Min(value = 1000, message = "The amount must be greater than or equal to 1000")
    @Max(value = 35000, message = "The amount must be less than or equal to 35000")
    private float amount;

    @Temporal(TemporalType.DATE)
    private Date dateDemande;

    @Temporal(TemporalType.DATE)
    private Date obtainingDate;

    private Boolean state;

    //0 PAS DE DIFFERE 1 SI CREDIT A DIFFERE TOTAL
    private Boolean differe;

    // PERIODE DE DIFFERE en année
    private float diffPeriod;

    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "Monthly payment date shouldn't be before current date")
    private Date monthlyPaymentDate;

    private float monthlyPaymentAmount;

    //taux d'interet en année
    private float interestRate;

    //periode de credit en année
    @DecimalMin(value = "0.5", message = "Credit period must be more than or equal to 6 months")
    @DecimalMax(value = "5.0", message = "Credit period must be less than or equal to 5 years")
    private float creditPeriod;

    private float risk;

    private Boolean completed;

    private String reason;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "credit")
    private Set<Notification> notifications;

    @ManyToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    @ManyToOne
    private Fund funds;

    @JsonIgnore
    @OneToMany(mappedBy = "credits")
    private Set<DuesHistory> duesHistory;

    @OneToOne(mappedBy = "credit")
    private Garantor garantor;

    public Credit() {
        super();
    }

    public Credit(float amount, Date dateDemande, Date obtainingDate, Boolean state, Boolean differe, float diffPeriod,
                  Date monthlyPaymentDate, float monthlyPaymentAmount, float interestRate, float creditPeriod, float risk,
                  Boolean completed, String reason, Set<Notification> notifications, User user, Fund funds,
                  Set<DuesHistory> duesHistory, Garantor garantor) {
        super();
        this.amount = amount;
        this.dateDemande = dateDemande;
        this.obtainingDate = obtainingDate;
        this.state = state;
        this.differe = differe;
        this.diffPeriod = diffPeriod;
        this.monthlyPaymentDate = monthlyPaymentDate;
        this.monthlyPaymentAmount = monthlyPaymentAmount;
        this.interestRate = interestRate;
        this.creditPeriod = creditPeriod;
        this.risk = risk;
        this.completed = completed;
        this.reason = reason;
        this.notifications = notifications;
        this.user = user;
        this.funds = funds;
        this.duesHistory = duesHistory;
        this.garantor = garantor;
    }

    public Credit(float amount, float period, float interst) {
        this.amount=amount;
        this.creditPeriod=period;
        this.interestRate=interst;
    }

    // Getters and setters

    public Long getIdCredit() {
        return idCredit;
    }

    public void setIdCredit(Long idCredit) {
        this.idCredit = idCredit;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Date getObtainingDate() {
        return obtainingDate;
    }

    public void setObtainingDate(Date obtainingDate) {
        this.obtainingDate = obtainingDate;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Boolean getDiffere() {
        return differe;
    }

    public void setDiffere(Boolean differe) {
        this.differe
                = differe;
    }

    public float getDiffPeriod() {
        return diffPeriod;
    }

    public void setDiffPeriod(float diffPeriod) {
        this.diffPeriod = diffPeriod;
    }

    public Date getMonthlyPaymentDate() {
        return monthlyPaymentDate;
    }

    public void setMonthlyPaymentDate(Date monthlyPaymentDate) {
        this.monthlyPaymentDate = monthlyPaymentDate;
    }

    public float getMonthlyPaymentAmount() {
        return monthlyPaymentAmount;
    }

    public void setMonthlyPaymentAmount(float monthlyPaymentAmount) {
        this.monthlyPaymentAmount = monthlyPaymentAmount;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public float getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(float creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public float getRisk() {
        return risk;
    }

    public void setRisk(float risk) {
        this.risk = risk;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Fund getFunds() {
        return funds;
    }

    public void setFunds(Fund funds) {
        this.funds = funds;
    }

    public Set<DuesHistory> getDuesHistory() {
        return duesHistory;
    }

    public void setDuesHistory(Set<DuesHistory> duesHistory) {
        this.duesHistory = duesHistory;
    }

    public Garantor getGarantor() {
        return garantor;
    }

    public void setGarantor(Garantor garantor) {
        this.garantor = garantor;
    }
}