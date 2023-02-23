package com.example.pi_back.Entities;

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
public class Transaction implements Serializable {
@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
private LocalDate date;
private long RIB_source;
private long RIB_recipient;
private int amount;
@ManyToOne
    private Account account;

    @OneToMany( mappedBy="transaction")
    private Set<Reclamation> reclamations;

}
