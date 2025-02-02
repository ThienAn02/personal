package com.annie.repository;



import com.annie.model.Tutor;

import javax.persistence.*;

public class TutorRepository {

    private EntityManager entityManager;
    private EntityManagerFactory emf;

    public TutorRepository() {
        this.emf = Persistence.createEntityManagerFactory("student_pu");
        this.entityManager = this.emf.createEntityManager();
    }
    public Tutor add(Tutor school) {
        entityManager.getTransaction().begin();
        entityManager.persist(school);
        entityManager.getTransaction().commit();
        return school;
    }

    public Tutor find(Long id) {
        return entityManager.find(Tutor.class, id);
    }

    public Tutor update(Tutor tutor) {
        Tutor tutorToUpdate = find(tutor.getId());
        entityManager.getTransaction().begin();
        tutorToUpdate.setFirstName(tutor.getFirstName());
        tutorToUpdate.setLastName(tutor.getLastName());
        entityManager.getTransaction().commit();
        return tutorToUpdate;
    }


    public void delete(Tutor tutor) {
        entityManager.getTransaction().begin();
        entityManager.remove(tutor);
        entityManager.getTransaction().commit();
    }


    public void close() {
        this.entityManager.close();
        this.emf.close();
    }
}