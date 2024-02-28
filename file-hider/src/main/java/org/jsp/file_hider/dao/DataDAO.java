package org.jsp.file_hider.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.Session;
import org.jsp.file_hider.model.Data;
import org.jsp.file_hider.model.User;

@SuppressWarnings("all")
public class DataDAO {

	static EntityManager manager = Persistence.createEntityManagerFactory("dev").createEntityManager();

	public List<Data> getAllFiles(Integer id) {

		Query query = manager.createQuery("select user.userData from User user where user.id=?1");
		query.setParameter(1, id);

		return query.getResultList();

	}

	public Boolean hide(String path, Integer user_id) throws IOException {

		EntityTransaction transaction = manager.getTransaction();

		User user = manager.find(User.class, user_id);

		if (user != null) {

			File file = new File(path);
			FileInputStream fileInputStream = new FileInputStream(file);
			Session session = manager.unwrap(Session.class);
			Blob blob = session.getLobHelper().createBlob(fileInputStream, file.length());
			Data data = new Data();

			data.setFileName(file.getName());
			data.setPath(file.getPath());
			data.setBin_data(blob);
			data.setUser(user);

			user.getUserData().add(data);

			manager.persist(user);
			transaction.begin();
			transaction.commit();

			fileInputStream.close();
			session.clear();
			file.delete();

			return true;
		}

		return false;
	}

	public Boolean restore(Integer file_id) throws SQLException, IOException {

		EntityTransaction transaction = manager.getTransaction();

		Data data = manager.find(Data.class, file_id);

		if (data != null) {
			String path = data.getPath();
			
			Session session = manager.unwrap(Session.class);
			InputStream inputStream = session.get(Data.class, file_id).getBin_data().getBinaryStream();
			File file = new File(path);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			
			byte[] buffer = new byte[16384];

			int bytesRead = -1;
			while((bytesRead = inputStream.read(buffer))!=-1) {
				fileOutputStream.write(buffer, 0, bytesRead);
			}
			
			manager.remove(data);
			transaction.begin();
			transaction.commit();
			
			fileOutputStream.close();
			inputStream.reset();
			inputStream.close();
			session.clear();
			
			return true;

		}

		return false;
	}

}
