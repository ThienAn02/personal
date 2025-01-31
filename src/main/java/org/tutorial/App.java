package org.tutorial;


import org.tutorial.model.Student;
import org.tutorial.repository.StudentRepository;

import java.util.List;

public class App {
  public static void main(String[] args) {

    Student student = new Student("Emi", "Wong");

    StudentRepository studentRepository = new StudentRepository();
    studentRepository.add(student);
    System.out.println("Added student " + student.toString());
    studentRepository.findFirstNames().forEach(System.out::println);
    studentRepository.findLastNames().forEach(System.out::println);
    student =studentRepository.find(student.getId());
    System.out.println("found student "+student.toString());
    student.setLastName("John");
    studentRepository.update(student);
    System.out.println("update"+ student.toString());
    studentRepository.delete(student);
    System.out.println("delete"+ student.toString());

    //Persistence Operations and JPQL

    studentRepository.findFirstNames().forEach(System.out::println);

    studentRepository.findLastNames().forEach(System.out::println);

    student = studentRepository.find(student.getId());

    System.out.println("Found student " + student.toString());

    student = studentRepository.findById(student.getId());

    System.out.println("Found student (JPQL) " + student.toString());

    student.setLastName("Green");

    studentRepository.update(student);

    System.out.println("Updated student " + student.toString());

    student = studentRepository.updateFirstNameById("Fred", student.getId());

    System.out.println("Updated first name (JPQL)" + student.toString());

    student = studentRepository.updateLastNameById("Yellow", student.getId());

    System.out.println("Updated last name (JPQL)" + student.toString());

    List<Student> students = studentRepository.findByFirstNameStartWith("Fr");

    students.forEach(System.out::println);

    students = studentRepository.findByLastNameEndWith("ow");

    students.forEach(System.out::println);

    System.out.println("Number of student(s): "+  studentRepository.count());

    students = studentRepository.findSortingByFirstName();

    students.forEach(System.out::println);

    students = studentRepository.findSortingById();

    students.forEach(System.out::println);

    studentRepository.delete(student);

    System.out.println("Deleted student " + student.toString());

//
//    //CRITERIA BUILDER
//
//    List<Student> studentList = studentRepository.getStudentWithCriteriaBuilder();
//
//    System.out.println("Print Students (Criteria Builder): ");
//    studentList.forEach(System.out::println);
//
//    List<Student> studentListWhere = studentRepository.getStudentsWithWHEREFirstName();
//
//    System.out.println("Print Students (Criteria Builder with WHERE and GROUP BY): ");
//    studentListWhere.forEach(System.out::println);
//
//    studentRepository.close();

  }
}
