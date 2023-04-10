package com.example.pi_back.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Firstname;
    private String Secondname;
    private int age;

    private LocalDate BirthDate;
    private Long Phonenum;
    private String Email;
    private String adress;

    @Enumerated(EnumType.STRING)
    private UserType usertype;

    @ManyToMany
    private Set<Project> projects;

    @ManyToOne(cascade=CascadeType.PERSIST)
    private Account account;

}
