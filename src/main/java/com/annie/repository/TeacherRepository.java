package com.annie.repository;
import com.annie.model.Teacher;

import javax.persistence.*;

public class TeacherRepository {

    private EntityManager entityManager;
    private EntityManagerFactory emf;

    public TeacherRepository() {
        this.emf = Persistence.createEntityManagerFactory("student_pu");
        this.entityManager = this.emf.createEntityManager();
    }

    public Teacher add(Teacher teacher) {
        entityManager.getTransaction().begin();
        entityManager.persist(teacher);
        entityManager.getTransaction().commit();
        return teacher;
    }

    public Teacher find(Long id) {
        return entityManager.find(Teacher.class, id);
    }

    public Teacher update(Teacher teacher) {
        Teacher teacherToUpdate = find(teacher.getId());
        entityManager.getTransaction().begin();
        teacherToUpdate.setFirstName(teacher.getFirstName());
        teacherToUpdate.setLastName(teacher.getLastName());
        entityManager.getTransaction().commit();
        return teacherToUpdate;
    }


    public void delete(Teacher teacher) {
        entityManager.getTransaction().begin();
        entityManager.remove(teacher);
        entityManager.getTransaction().commit();
    }


    public void close() {
        this.entityManager.close();
        this.emf.close();
    }
}