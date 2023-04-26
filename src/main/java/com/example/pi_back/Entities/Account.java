package com.example.pi_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String cin;
    private Long solde;

    private LocalDate opendate;
    private long RIB;
    private String state;
    private LocalDateTime lastUpdateDate;

    @Enumerated(EnumType.STRING)
    private TypeAccount typeaccount;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "account",cascade=CascadeType.ALL)
    private Set<Transaction> transactions;
    @ManyToMany
    @JsonIgnore
    private Set<InternalService> internalServices;

    public LocalDate getLastTransactionsDate() {
        if (this.transactions.isEmpty()) {
            return null;
        } else {
            // initialize transactions before accessing them
            Hibernate.initialize(this.transactions);
            return this.transactions.stream()
                    .map(Transaction::getDate)
                    .max(LocalDate::compareTo)
                    .get();
        }
    }
    @PrePersist
    public void setOpendate() {
        this.opendate = LocalDate.now();
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}
