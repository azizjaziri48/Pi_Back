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
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Firstname;
    private String Secondname;
    private LocalDate BirthDate;
    private Long Phonenum;
    private String Email;
    private String adress;

    @Enumerated(EnumType.STRING)
    private UserType usertype;
    @ManyToMany(mappedBy = "users")
    private Set<Offer> offers;
    @OneToOne
    private Account account;

}
