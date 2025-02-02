package com.annie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 158)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 250)
    private String lastName;

    @ManyToOne
    private School school;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "teachers_students",
            joinColumns =  { @JoinColumn(name = "teacher_id") },
            inverseJoinColumns = { @JoinColumn(name = "student_id") },
            uniqueConstraints = {
                    @UniqueConstraint(
                            columnNames = { "teacher_id", "student_id" }
                    )
            }
    )
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();

    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.getTeachers().add(this);
    }


    public void removeStudent(Student student) {
        students.remove(student);
        student.getTeachers().remove(this);
    }
    @Override
    public String toString() {
        return "Teacher{id=" + id + ", firstName='" + firstName +  ", lastName='" +lastName + "', studentCount=" + (students != null ? students.size() : 0) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id != null && id.equals(teacher.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
