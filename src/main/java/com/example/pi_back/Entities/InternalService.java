package com.example.pi_back.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.DateTime;
import lombok.*;
import net.bytebuddy.asm.Advice;

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
    private String Sector;
    @OneToMany(mappedBy="internalService",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Activity> activities;
    @ManyToMany(mappedBy="internalServices",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Account> accounts;
}
