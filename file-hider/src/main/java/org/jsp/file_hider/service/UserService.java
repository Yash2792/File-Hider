package org.jsp.file_hider.service;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.jsp.file_hider.dao.DataDAO;
import org.jsp.file_hider.dao.UserDAO;
import org.jsp.file_hider.model.Data;
import org.jsp.file_hider.model.User;
import org.jsp.file_hider.views.UserView;

public class UserService {

	Scanner scan = new Scanner(System.in);
	DataDAO dataDAO = new DataDAO();

	public void login() {

		String user_email = "";
		while (true) {
			System.out.print("Enter your email: ");
			user_email = scan.next();
			if (!(Pattern.compile("[a-z0-9]+@[a-z]+[.][a-z]+").matcher(user_email).matches())) {
				System.err.println("Enter valid email..!!!");
			} else {
				break;
			}
		}

		if (UserDAO.isExist(user_email) != null) {

			boolean resendOTP;
			do {
				resendOTP = false;

				String genOTP = GenerateOTP.getOTP();
				SendOTPService.sendOTP(user_email, genOTP);

				boolean run = true;
				while (run) {
					System.out.print("Enter the OTP: ");
					String userOTP = scan.next();

					if (!userOTP.equals(genOTP)) {
						System.err.println("Invalid OTP..!!!");

						boolean run2;
						do {
							run2 = false;
							try {
								System.out.println("Press 1 to enter current OTP\nPress 2 to resend new OTP");

								switch (scan.nextByte()) {
								case 1:
									break;

								case 2:
									resendOTP = true;
									run = false;
									break;

								default:
									System.err.println("Invalid Option..!!!");
									run2 = true;

								}
							} catch (InputMismatchException ex) {
								System.err.println("Please press valid key..!!!");
								run2 = true;
								scan.nextLine();
							}
						} while (run2);

					} else {
						System.out.println("LogIn");
						new UserView(user_email).home();
						run = false;
					}
				}
			} while (resendOTP);

		} else {
			System.err.println("User not found..!!!");
		}
		scan.nextLine();
	}

	public void signup() {

		String user_name = "";
		while (true) {
			System.out.print("Enter your name: ");
			user_name = scan.nextLine();
			if (user_name.length() > 50) {
				System.err.println("Name should be contain only 50 charcters");
			} else if (user_name.charAt(0) == ' ') {
				System.err.println("Name should be start with alpha-character");
			} else if (Pattern.compile(".*[^a-zA-Z ].*").matcher(user_name).matches()) {
				System.err.println("Name does not contain numeric/special charcter");
			} else {
				break;
			}
		}

		String user_email = "";
		while (true) {
			System.out.print("Enter your email: ");
			user_email = scan.next();
			if (!(Pattern.compile("[a-z0-9]+@[a-z]+[.][a-z]+").matcher(user_email).matches())) {
				System.err.println("Enter valid email..!!!");
			} else {
				break;
			}
		}

		User user = new User();
		user.setName(user_name);
		user.setEmail(user_email);

		if (UserDAO.saveUser(user) == false) {
			System.err.println("User already exists...");
		} else {
			boolean resendOTP;
			do {
				resendOTP = false;

				String genOTP = GenerateOTP.getOTP();
				SendOTPService.sendOTP(user_email, genOTP);

				boolean run = true;
				while (run) {
					System.out.print("Enter the OTP: ");
					String userOTP = scan.next();

					if (!userOTP.equals(genOTP)) {
						System.err.println("Invalid OTP..!!!");

						boolean run2;
						do {
							run2 = false;
							try {
								System.out.println("Press 1 to enter current OTP\nPress 2 to resend new OTP");

								switch (scan.nextByte()) {
								case 1:
									break;

								case 2:
									resendOTP = true;
									run = false;
									break;

								default:
									System.err.println("Invalid Option..!!!");
									run2 = true;

								}
							} catch (InputMismatchException ex) {
								System.err.println("Please press valid key..!!!");
								run2 = true;
								scan.nextLine();
							}
						} while (run2);

					} else {

						System.out.println("\nUser registered successful...");
						new UserView(user_email).home();
						run = false;
					}
				}
			} while (resendOTP);
		}

		scan.nextLine();
	}

	public void showFiles(Integer user_id) {

		List<Data> files = dataDAO.getAllFiles(user_id);

		if (files.size() > 0) {
			System.out.println("File-Id\tFile-name");
			for (Data file : files) {
				System.out.println(file.getId() + "\t" + file.getFileName());
			}
		} else {
			System.err.println("No file found on this account...");
		}
	}

	public void hideFile(Integer user_id) {

		try {
			System.out.print("Enter the file path: ");
			String path = scan.nextLine();

			if (dataDAO.hide(path, user_id)) {
				System.out.println("File hide successfully...");
			} else {
				System.err.println("Error to hide file..!!!");
			}

		} catch (IOException ex) {
			System.err.println("File not found..!!!");
			System.out.println("Please recheck file path...");
		}
	}

	public void restoreFile(Integer user_id) {

		try {
			showFiles(user_id);

			System.out.println("Enter the file id to restore...");
			Integer file_id = Integer.parseInt(scan.nextLine());

			List<Data> files = dataDAO.getAllFiles(user_id);

			boolean isValidId = false;
			for (Data file : files) {
				if (file.getId() == file_id) {
					isValidId = true;
					break;
				}
			}

			if (isValidId) {
				if (dataDAO.restore(file_id)) {
					System.out.println("File restore successfully...");
				} else {
					System.err.println("Error to restore file..!!!");
				}
			} else
				System.err.println("Invalid file id..!!!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
