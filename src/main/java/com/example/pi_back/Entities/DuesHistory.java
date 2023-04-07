package com.example.pi_back.Entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class DuesHistory implements Serializable {

    @Id
    @Column(name ="idDues")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDues;

    @Temporal(TemporalType.DATE)
    @FutureOrPresent(message = "Date history shouldn't be before current date")
    private Date DateHistory;

    @Temporal(TemporalType.DATE)
    private Date SupposedDate;

    @ManyToOne
    @JsonIgnore
    private Credit credits;

    public DuesHistory() {
    }

    public DuesHistory(Date dateHistory, Date supposedDate, Credit credits) {
        this.DateHistory = dateHistory;
        this.SupposedDate = supposedDate;
        this.credits = credits;
    }

    @AssertTrue(message="Date history must be later supposed date")
    public boolean isDateHistoryAfterSupposedDate() {
        if(DateHistory == null || SupposedDate == null) {
            return true;
        }
        return SupposedDate.before(DateHistory);
    }

    public Long getIdDues() {
        return idDues;
    }

    public void setIdDues(Long idDues) {
        this.idDues = idDues;
    }

    public Date getDateHistory() {
        return DateHistory;
    }

    public void setDateHistory(Date dateHistory) {
        DateHistory = dateHistory;
    }

    public Date getSupposedDate() {
        return SupposedDate;
    }

    public void setSupposedDate(Date supposedDate) {
        SupposedDate = supposedDate;
    }

    public Credit getCredits() {
        return credits;
    }

    public void setCredits(Credit credits) {
        this.credits = credits;
    }
}
