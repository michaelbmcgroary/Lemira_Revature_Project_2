package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.revature.dto.LoginDTO;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.NotModeratorException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Review;
import com.revature.model.ReviewStatus;
import com.revature.model.User;


@Repository
@Transactional
public class ReviewRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Logger logger = LoggerFactory.getLogger(ReviewRepository.class);
	private UserRepository userRepository;
	
	public ReviewRepository(UserRepository userRepository) throws DatabaseException {
		this.userRepository = userRepository;
	}
	
	
	public Review newReview(Review review) throws DatabaseException {
		//may replace user with a DTO that doesn't include the id
		try {
			Session session = sessionFactory.getCurrentSession();
			//Time stamp may be set before but this is a more accurate time as it will be recorded at the time it enters the database
			review.setReviewID(0);
			session.persist(session);
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("from Review r WHERE r.userID = :uid AND r.description = :desc AND r.gameID = :gid ");
			query.setParameter("uid", review.getUser().getUserID());
			query.setParameter("desc", review.getDescription());
			query.setParameter("gid", review.getGameID());
			return review;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("Review could not be added. Exception message is: " + e.getMessage());
		} catch (javax.persistence.PersistenceException e) {
			throw new DatabaseException("Review could not be added because unique value already existed.");
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
	
	
	public List<Review> getAllReviews(LoginDTO user) throws NotModeratorException, DatabaseException{
		if(userRepository.isModerator(user) == false) {
			throw new NotModeratorException();
		}
		List<Review> reviewList = null;
		
		try {
			Session session = sessionFactory.getCurrentSession();
			reviewList = session.createQuery("from Review", Review.class).getResultList();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException();
		} catch (org.hibernate.ObjectNotFoundException e) {
			System.out.println(e.getEntityName());
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
		}
		
		return reviewList;
	}
	
	

	
	/*
	public Blob getRecieptByID(LoginDTO login, int reviewID) throws DatabaseException, NotModeratorException, NoRecieptException {
		try(Connection connection = ConnectionUtil.getConnection()){
			String query = "SELECT review_reciept FROM ers_review WHERE review_id = ?";
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, reviewID);
			ResultSet results = prepStatement.executeQuery();			
			if (results.next()) {
				Blob blob = results.getBlob("review_reciept");
				if(blob.length() == 0) {
					blob = null;
				}
				return blob;
			} else {
				throw new NoRecieptException("The requested ID has no reciept");
			}
		} catch (SQLException e) {
			throw new DatabaseException("No Database Connection");
		} 
	}
	*/

}
