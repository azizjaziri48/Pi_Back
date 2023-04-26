package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
@Entity
public class Garantor implements Serializable {

    @Id
    @Column(name ="idGarantor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGarantor ;
    private String nameGarantor ;
    private String secondnameGarantor ;
    private float salaryGarantor ;
    private String workGarantor ;
    private String urlimage ;
    @JsonIgnore
    @OneToOne
    private Credit credit;

    // Getters and Setters
    public long getIdGarantor() {
        return idGarantor;
    }

    public void setIdGarantor(long idGarantor) {
        this.idGarantor = idGarantor;
    }

    public String getNameGarantor() {
        return nameGarantor;
    }

    public void setNameGarantor(String nameGarantor) {
        this.nameGarantor = nameGarantor;
    }

    public String getSecondnameGarantor() {
        return secondnameGarantor;
    }

    public void setSecondnameGarantor(String secondnameGarantor) {
        this.secondnameGarantor = secondnameGarantor;
    }

    public float getSalaryGarantor() {
        return salaryGarantor;
    }

    public void setSalaryGarantor(float salaryGarantor) {
        this.salaryGarantor = salaryGarantor;
    }

    public String getWorkGarantor() {
        return workGarantor;
    }

    public void setWorkGarantor(String workGarantor) {
        this.workGarantor = workGarantor;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }
}

