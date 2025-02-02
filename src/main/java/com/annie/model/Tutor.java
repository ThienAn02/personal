package com.annie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name",nullable = false,length = 158)
    private String firstName;

    @Column(name = "last_name",nullable = false,length = 250)
    private String lastName;

    public Tutor(String firstName, String lastName) {
        this.firstName=firstName;
        this.lastName=lastName;
    }

}