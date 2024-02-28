package org.jsp.file_hider.model;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "userData")
public class Data {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String fileName, path;

	@Lob
	@Column(nullable = false)
	private Blob bin_data;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// Getters
	public Integer getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public String getPath() {
		return path;
	}

	public User getUser() {
		return user;
	}

	public Blob getBin_data() {
		return bin_data;
	}


	// Setters
	public void setBin_data(Blob bin_data) {
		this.bin_data = bin_data;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
