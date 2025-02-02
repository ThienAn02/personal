package com.annie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "find school by id",query="Select s from School s where s.id= :id")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String city;

    @OneToMany(targetEntity = Student.class)
    Set<Student> students =new HashSet<>();

    public School(String name, String city) {
        this.name=name;
        this.city=city;
    }

}