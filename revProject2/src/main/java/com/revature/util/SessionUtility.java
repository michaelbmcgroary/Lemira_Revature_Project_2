package com.revature.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionUtility {
	
	private static SessionFactory sessionFactory;
	
	public synchronized static Session getSession() {
		if(sessionFactory == null) {
			sessionFactory = new Configuration()
					.setProperty("hibernate.connection.username", System.getenv("db_username_proj_2"))
					.setProperty("hibernate.connection.password", System.getenv("db_password_proj_2"))
					.configure("hibernate.cfg.xml")
					.buildSessionFactory();
		}
		return sessionFactory.openSession();
	}
}
