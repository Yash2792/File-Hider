package org.jsp.file_hider.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jsp.file_hider.model.User;

public class UserDAO {

	static EntityManager manager = Persistence.createEntityManagerFactory("dev").createEntityManager();

	public static Boolean saveUser(User user) {

		User userDb = isExist(user.getEmail());

		if (userDb == null) {
			EntityTransaction transaction = manager.getTransaction();
			manager.persist(user);
			transaction.begin();
			transaction.commit();
			return true;
		}
		return false;
	}

	public static User isExist(String email) {
		Query query = manager.createQuery("select user from User user where user.email=?1");
		query.setParameter(1, email);

		try {
			return (User) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
}
