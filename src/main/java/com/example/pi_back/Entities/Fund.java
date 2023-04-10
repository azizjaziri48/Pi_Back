package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Fund implements Serializable {
    @Id
    @Column(name ="idFund")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFund ;
    private BigDecimal amountFund ;
    private BigDecimal tauxFund ;
    private BigDecimal tauxGain ;
   /* @OneToMany(mappedBy="funds")
    private Set<Credit> credits ;*/
    @JsonIgnore
    @OneToMany(mappedBy="fund",cascade=CascadeType.PERSIST)
    private Set<Project> Project;





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
