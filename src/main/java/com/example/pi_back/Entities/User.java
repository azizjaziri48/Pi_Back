package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstname;
    private String secondname;
    private LocalDate BirthDate;
    private Long phonenum;
    private String email;
    private String address;
    private Boolean locked = false;
    private Boolean enabled = false;
    private String activitySector;
    private String password;
    //null par defaut / true autorisé/false non autorisé
    private Boolean Credit_authorization;

    @Enumerated(EnumType.STRING)
    private UserType usertype;
    @ManyToMany(mappedBy = "users")
    private Set<Offer> offers;
    @JsonIgnore
    @OneToMany(mappedBy="user")
    private Set<Account> accounts;
    public User(String firstName,
                String lastName,
                String email,
                String password,
                UserType appUserRole) {
        this.firstname = firstName;
        this.secondname = lastName;
        this.email = email;
        this.password = password;
        this.usertype = appUserRole;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(usertype.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
