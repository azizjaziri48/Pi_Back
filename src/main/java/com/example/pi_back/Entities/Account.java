package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String cin;
    private Long solde;

    private LocalDate opendate;
    private String RIB;
    private String state;
    @Enumerated(EnumType.STRING)
    private TypeAccount typeaccount;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions;
    @ManyToMany
    @JsonIgnore
    private Set<InternalService> internalServices;

}
