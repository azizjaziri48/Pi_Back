package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
    @OneToOne(mappedBy="partner",cascade = CascadeType.ALL)
    @JsonIgnore
    @Transient
    private Offer offer;
    @OneToMany(mappedBy="partner",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Service> services;


}