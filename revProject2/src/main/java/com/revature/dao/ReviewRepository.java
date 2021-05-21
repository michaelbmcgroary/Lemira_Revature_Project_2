package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.revature.exception.DatabaseException;
import com.revature.model.Review;

@Repository
@Transactional
public class ReviewRepository {

	@Autowired
	private SessionFactory sessionFactory;

	private UserRepository userRepository;
	
	public ReviewRepository(UserRepository userRepository) throws DatabaseException {
		this.userRepository = userRepository;
	}
	
	
	public Review newReview(Review review) throws DatabaseException {
		//may replace user with a DTO that doesn't include the id
		try {
			Session session = sessionFactory.getCurrentSession();
			review.setReviewID(0);
			session.persist(review);
			
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("from Review r WHERE r.user = :uid AND r.description = :desc AND r.gameID = :gid");
			query.setParameter("uid", review.getUser());
			query.setParameter("desc", review.getDescription());
			query.setParameter("gid", review.getGameID());
			Review retReview = (Review) query.getSingleResult();
			return retReview;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("Review could not be added. Exception message is: " + e.getMessage());
		} catch (javax.persistence.PersistenceException e) {
			throw new DatabaseException(e.getCause());
		}		
	}
	
	
	public Review getReviewByID(int reviewID) throws DatabaseException {
		Review retReview;
		try {
			Session session = sessionFactory.getCurrentSession();
			retReview = session.createQuery("from Review r WHERE r.reviewID = " + reviewID, Review.class).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException();
		}
		return retReview;
	}
	
	
	public List<Review> getAllReviews() throws DatabaseException{
		List<Review> reviewList = null;
		try {
			Session session = sessionFactory.getCurrentSession();
			reviewList = session.createQuery("from Review", Review.class).getResultList();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException();
		} catch (org.hibernate.ObjectNotFoundException e) {
			throw new DatabaseException();
		}
		
		return reviewList;
	}
	
	
	public List<Review> getReviewsByUser(String username) throws DatabaseException {
		List<Review> reviewList = null;
		try {
			int userID = userRepository.getUserIDByUsername(username);
			reviewList = new ArrayList<Review>();
			Session session = sessionFactory.getCurrentSession();
			reviewList = session.createQuery("from Review WHERE userID = " + userID, Review.class).getResultList();
		} catch (DatabaseException e) {
			throw new DatabaseException(e.getMessage());
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("No Reviews exist for this user");
		}
		
		return reviewList;
	}


	public List<Review> getReviewsByGame(int gameID) throws DatabaseException {
		List<Review> reviewList = null;
		try {
			reviewList = new ArrayList<Review>();
			Session session = sessionFactory.getCurrentSession();
			reviewList = session.createQuery("from Review WHERE gameID = " + gameID, Review.class).getResultList();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("No Reviews exist for this game");
		}
		
		return reviewList;
	}
	

}
