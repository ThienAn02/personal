package com.annie.repository;

import com.annie.model.Student;

import javax.persistence.*;
import java.util.List;

public class StudentRepository {

    private EntityManager entityManager;
    private EntityManagerFactory emf;

    public StudentRepository() {
        this.emf = Persistence.createEntityManagerFactory("student_pu");
        this.entityManager = this.emf.createEntityManager();
    }

    public StudentRepository(String pu) {
        this.emf = Persistence.createEntityManagerFactory(pu);
        this.entityManager = this.emf.createEntityManager();
    }

    public Student add(Student student) {
        entityManager.getTransaction().begin();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
        return student;
    }


    public Student find(Long id) {
        return entityManager.find(Student.class, id);
    }

    public Student findById(Long id)
    {
        Query query =entityManager.createNamedQuery("find student by id");
        query.setParameter("id",id);
        return (Student) query.getSingleResult();
    }
    public List<String> findFirstNames(){
        Query query=entityManager.createQuery("Select s.firstName from Student s");
        return query.getResultList();
    }
    public List<String> findLastNames(){
        Query query=entityManager.createQuery("Select s.lastName from Student s");
        return query.getResultList();
    }

    public void deleteById(Long id) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("Delete from Student where id =" + id);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    public List<Student> findByFirstNameStartWith(String keyword) {
        Query query = entityManager.createQuery("Select s from Student s where s.firstName like '" + keyword + "%'");
        return query.getResultList();
    }

    public List<Student> findByLastNameEndWith(String keyword) {
        Query query = entityManager.createQuery("Select s from Student s where s.lastName like '%" + keyword + "'");
        return query.getResultList();
    }

    public List<Student> findSortingByFirstName() {
        Query query = entityManager.createQuery("Select s from Student s order by s.firstName asc ");
        return query.getResultList();
    }

    public List<Student> findSortingById() {
        Query query = entityManager.createQuery("Select s from Student s order by s.id desc");
        return query.getResultList();
    }

    public Student update(Student student) {
        Student studentToUpdate = find(student.getId());
        entityManager.getTransaction().begin();
        studentToUpdate.setFirstName(student.getFirstName());
        studentToUpdate.setLastName(student.getLastName());
        entityManager.getTransaction().commit();
        return studentToUpdate;
    }
    public Student updateFirstNameById(String firstName, Long id){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("Update Student set firstName = '"+ firstName + "' where id = " + id );
        query.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.clear();
        return findById(id);
    }
    public Student updateLastNameById(String lastName, Long id){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("Update Student set lastName = '"+ lastName + "' where id = " + id );
        query.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.clear();
        return findById(id);
    }
    public int count(){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("Select count(s) from Student s");
        Long count = (Long) query.getSingleResult();
        entityManager.getTransaction().commit();
        return count.intValue();

    }

    public void delete(Student student) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(student) ? student : entityManager.merge(student));
        entityManager.getTransaction().commit();
    }



    public void close() {
        this.entityManager.close();
        this.emf.close();
    }
}