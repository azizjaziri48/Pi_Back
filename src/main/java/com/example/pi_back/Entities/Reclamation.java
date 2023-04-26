package com.example.pi_back.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    private String description;
    private Boolean etat = false;
    private String reponse;
    @Enumerated(EnumType.STRING)
    private Subject subject;
    @ManyToOne
    private Transaction transaction;
}
