package com.littleGame.dao;


import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.littleGame.model.Score;
import com.littleGame.utils.HibernateUtil;

public class ScoreDao {
	 /**
     * Save score
     * @param score
     */
    public void saveScore(Score score) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the student object
            session.save(score);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Get all score
     * @return
     */
    @SuppressWarnings("unchecked")
    public List < Score > getAllScore() {

        Transaction transaction = null;
        List < Score > listOfScore = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // get an user object
            //System.out.println('1s');
            listOfScore = session.createQuery("FROM Score").getResultList();
            //System.out.println('2s');
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        //System.out.println(Arrays.toString(listOfScore.toArray()));
        //listOfScore.forEach(System.out::println);
        
        return listOfScore;
    }
    
    
}