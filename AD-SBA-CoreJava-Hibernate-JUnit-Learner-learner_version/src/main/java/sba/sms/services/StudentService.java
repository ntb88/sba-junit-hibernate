package sba.sms.services;

import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI{

    private final SessionFactory sessionFactory;
    private final CourseService courseService;

    public StudentService() {
        sessionFactory = HibernateUtil.getSessionFactory();
        courseService = new CourseService();
    }



    @Override
    public List<Student> getAllStudents() {
        try(Session session = sessionFactory.openSession()){
            Query<Student> query = session.createQuery("from Student", Student.class);
            return query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null; //return null if error occurs
        }
    }

    @Override
    public void createStudent(Student student) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null)
                transaction.rollback(); //rollback if exception handling occurs
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        try(Session session = sessionFactory.openSession()){
            return session.get(Student.class, email);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null; //return null in case of exception
        }
    }

    @Override
    public boolean validateStudent(String email, String password) {
        try(Session session = sessionFactory.openSession()){
            Student student = getStudentByEmail(email);
            return student != null && student.getPassword().equals(password);
        }  catch (HibernateException e) {
            e.printStackTrace();
            return false; // if error occurs return false
        }
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            Student student = getStudentByEmail(email);
            Course course = courseService.getCourseById(courseId);

            if (student != null && course != null) {
                student.getCourses().add(course);
                course.getStudents().add(student);
                session.update(student);
                transaction.commit();
            }else {
                if (student == null) {
                    System.out.println("Student not found");
                }
                if (course == null) {
                    System.out.println("Course not found");
                }
            }
        } catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback(); // if error occurs rollback to the original state
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT c.* from course c JOIN student_courses sc ON c.id = sc.courses_id where sc.student_email = :email";
            return session.createNativeQuery(sql, Course.class)
                    .setParameter("email", email)
                    .list();
        } catch (HibernateException e) {
            e.printStackTrace(); // in case of exception
            return null; // return a null
        }
    }
}
