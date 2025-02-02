package com.annie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student")
@ToString
@NamedQuery(name = "find student by id",query="Select s from Student s where s.id= :id")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 158)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 250)
    private String lastName;

    @OneToOne
    private Tutor tutor;

    @ManyToMany(mappedBy = "students")
    @ToString.Exclude
    private Set<Teacher> teachers = new HashSet<>();

    public Student(String firstName, String lastName) {
        this.setLastName(lastName);
        this.setFirstName(firstName);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.getStudents().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
        teacher.getStudents().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id != null && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
