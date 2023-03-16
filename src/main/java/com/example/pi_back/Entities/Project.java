package com.example.pi_back.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String estimated_time;
    private float investable_amout;
    private int numberInv;
    private Double shareInv;
    private String Town;

    @Enumerated(EnumType.STRING)
    private IntevAge invetage;


    @ManyToMany
    private Set<User> users;


}
