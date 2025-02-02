package com.annie.repository;

import com.annie.model.School;
import com.annie.model.Student;

import javax.persistence.*;
import java.util.Objects;

public class SchoolRepository {

    private EntityManager entityManager;
    private EntityManagerFactory emf;

    public SchoolRepository() {
        this.emf = Persistence.createEntityManagerFactory("student_pu");
        this.entityManager = this.emf.createEntityManager();
    }

    public School add(School school) {
        entityManager.getTransaction().begin();
        entityManager.persist(school);
        entityManager.getTransaction().commit();
        return school;
    }

    public void addStudent(Long id, Student student) {
        entityManager.getTransaction().begin();
        School school = find(id);

        if (school != null) {
            school.getStudents().add(student);
            entityManager.merge(school);
        } else {
            System.out.println("School not found, cannot add student.");
        }
        entityManager.getTransaction().commit();
    }

    public void removeStudent(Long id, Student student) {
        entityManager.getTransaction().begin();
        School school = find(id);

        if (school != null && school.getStudents() != null) {
            school.getStudents().removeIf(s -> Objects.equals(s.getId(), student.getId())); // Xóa student theo ID
            entityManager.merge(school); // Cập nhật entity trong DB
        } else {
            System.out.println("School or student list not found.");
        }
        entityManager.getTransaction().commit();
    }

    public School find(Long id) {
        return entityManager.find(School.class, id);
    }

    public School update(School school) {
        School schoolToUpdate = find(school.getId());

        if (schoolToUpdate != null) {
            entityManager.getTransaction().begin();
            schoolToUpdate.setName(school.getName());
            schoolToUpdate.setCity(school.getCity());
            entityManager.merge(schoolToUpdate); // Cập nhật DB
            entityManager.getTransaction().commit();
        } else {
            System.out.println("School not found, update failed.");
        }

        return schoolToUpdate;
    }

    public void delete(School school) {
        entityManager.getTransaction().begin();
        School schoolToDelete = find(school.getId());

        if (schoolToDelete != null) {
            entityManager.remove(schoolToDelete);
            System.out.println("Deleted school: " + schoolToDelete);
        } else {
            System.out.println("School not found, delete failed.");
        }
        entityManager.getTransaction().commit();
    }

    public void close() {
        this.entityManager.close();
        this.emf.close();
    }
}
