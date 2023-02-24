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
public class Partner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String activity;
    private String name;
    private String sector;
    private String logo;
    @OneToOne(mappedBy="partner")
    private Offer offer;
    @OneToMany(mappedBy="partner")
    private Set<Service> services;

}