package com.example.pi_back.Entities;

import lombok.*;
import org.apache.tomcat.jni.Local;

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
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    private String description;
    private String subject;
    @ManyToOne
    private Transaction transaction;
}
