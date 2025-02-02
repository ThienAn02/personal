package com.annie;

import com.annie.model.School;
import com.annie.model.Student;
import com.annie.model.Teacher;
import com.annie.model.Tutor;
import com.annie.repository.SchoolRepository;
import com.annie.repository.StudentRepository;
import com.annie.repository.TeacherRepository;
import com.annie.repository.TutorRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class App {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("student_pu");
    StudentRepository studentRepository = new StudentRepository();
    TutorRepository tutorRepository = new TutorRepository();
    SchoolRepository schoolRepository = new SchoolRepository();
    TeacherRepository teacherRepository = new TeacherRepository();

    Student student = new Student("Emi", "Wong");
    Student student1 = new Student("David", "Styles");
    Student student2 = new Student("Harry", "Potter");

    studentRepository.add(student);
    studentRepository.add(student1);
    studentRepository.add(student2);

    System.out.println("Added student: " + student);
    System.out.println("Added student: " + student1);
    System.out.println("Added student: " + student2);

    Tutor tutor = new Tutor("Jessica", "Watson");
    tutorRepository.add(tutor);
    System.out.println("Added tutor: " + tutor);

    student.setTutor(tutor);
    studentRepository.update(student);
    System.out.println("Updated student with tutor: " + student);

    School school = new School("Ly Tu Trong", "HCM");
    schoolRepository.add(school);
    System.out.println("Added school: " + school);

    schoolRepository.addStudent(student.getId(), student);
    schoolRepository.addStudent(student1.getId(), student1);

    school = schoolRepository.find(school.getId());
    System.out.println("School Students:");
    school.getStudents().forEach(System.out::println);

    schoolRepository.removeStudent(student.getId(), student);
    System.out.println("After removing student:");
    school.getStudents().forEach(System.out::println);

    student = studentRepository.find(student.getId());
    System.out.println("Found student: " + student);

    student.setLastName("John");
    studentRepository.update(student);
    System.out.println("Updated student: " + student);

    Teacher teacher = new Teacher("Mary", "Jane");
    teacher.addStudent(new Student("Suri","Wang"));
    teacher.addStudent(new Student("Jess","J"));
    teacherRepository.add(teacher);
    System.out.println("Add teacher: " + teacher.toString());

    Teacher teacher2 = new Teacher("Harry", "Styles");
    teacherRepository.add(teacher2);
    teacher.addStudent(new Student("Jerry","Jane"));
    teacher.addStudent(new Student("Jane","Jerry"));
    System.out.println("Add teacher: " + teacher2.toString());


    teacher.setSchool(school);
    teacherRepository.update(teacher);
    System.out.println("Updated teacher with school: " + teacher);

    teacher2.setSchool(school);
    teacherRepository.update(teacher2);
    System.out.println("Updated teacher with school: " + teacher2);

    studentRepository.findFirstNames().forEach(System.out::println);
    studentRepository.findLastNames().forEach(System.out::println);

    student = studentRepository.findById(student.getId());
    System.out.println("Found student: " + student);

    student.setLastName("Green");
    studentRepository.update(student);
    System.out.println("Updated student last name: " + student);

    student = studentRepository.updateFirstNameById("Audrey", student.getId());
    System.out.println("Updated first name: " + student);

    student = studentRepository.updateLastNameById("Charles", student.getId());
    System.out.println("Updated last name: " + student);

    List<Student> students = studentRepository.findByFirstNameStartWith("Au");
    students.forEach(System.out::println);

    students = studentRepository.findByLastNameEndWith("les");
    students.forEach(System.out::println);

    System.out.println("Number of students: " + studentRepository.count());

    students = studentRepository.findSortingByFirstName();
    students.forEach(System.out::println);

    students = studentRepository.findSortingById();
    students.forEach(System.out::println);


    studentRepository.delete(student);
    System.out.println("Deleted student: " + student);

    studentRepository.deleteById(student1.getId());
    System.out.println("Deleted student by id: " + student1);

    studentRepository.close();
    tutorRepository.close();
    schoolRepository.close();
    teacherRepository.close();

    emf.close();
  }
}
