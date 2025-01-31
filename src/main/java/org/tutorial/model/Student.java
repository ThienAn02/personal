package org.tutorial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student")
@NamedQuery(name = "find student by id",query="Select s from Student s where s.id= :id")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name",nullable = false,length = 158)
    private String firstName;

    @Column(name = "last_name",nullable = false,length = 250)
    private String lastName;

    public Student(String firstName, String lastName) {
        this.setLastName(lastName);
        this.setFirstName(firstName);
    }
}
