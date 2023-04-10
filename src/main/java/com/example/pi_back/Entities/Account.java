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
@Data
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String cin;
    private Long solde;
    private int age;

    private LocalDate opendate;
    private String RIB;
    private String state;
    @Enumerated(EnumType.STRING)
    private TypeAccount typeaccount;
    @JsonIgnore
    @OneToMany(mappedBy="account",cascade=CascadeType.PERSIST)
    private Set<User> users;


}
