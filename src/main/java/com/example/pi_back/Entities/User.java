package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private LocalDate BirthDate;
    private Long Phonenum;
    private String Email;
    private String adress;
    private String activitySector;
    @Enumerated(EnumType.STRING)
    private UserType usertype;
    @ManyToMany(mappedBy = "users")
    private Set<Offer> offers;
    @JsonIgnore
    @OneToMany(mappedBy="user")
    private Set<Account> accounts;

}
