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
public class InternalService implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private LocalDate Date;
    private int capacite;
    @OneToMany(mappedBy="internalService")
    private Set<Activity> activities;
}
