package sba.sms.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI{

    private final SessionFactory  sessionFactory;

    public CourseService() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void createCourse(Course course) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback(); // if the transaction fails rollback to original
            }
            e.printStackTrace();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        try(Session session = sessionFactory.openSession()){
            return session.get(Course.class, courseId);
        } catch (HibernateException e) {
            e.printStackTrace();
            return null; // return null if exception occurs
        }
    }

    @Override
    public List<Course> getAllCourses() {
        try(Session session = sessionFactory.openSession()){
            return session.createQuery("from Course").list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null; // return null if exception occurs
        }
    }
}
