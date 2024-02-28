package org.jsp.file_hider.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsp.file_hider.dao.UserDAO;
import org.jsp.file_hider.model.User;
import org.jsp.file_hider.service.UserService;

public class UserView {

	private String email;

	public UserView(String email) {
		this.email = email;
	}

	public void home() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		User user = UserDAO.isExist(email);
		UserService userService = new UserService();

		boolean run = true;

		do {
			System.out.println("\nGreeting " + user.getName() + "!" + "\nPress 1 to show hidden files"
					+ "\nPress 2 to hide a file" + "\nPress 3 to restore a flie" + "\nPress 0 to exit");

			Integer ip = -1;

			while (ip >= -1) {
				try {

					ip = Integer.parseInt(bufferedReader.readLine());
					if (ip > -1)
						break;

				} catch (IOException | NumberFormatException ex) {
					System.err.println("Please press correct key..!!!");
				}
			}

			switch (ip) {
			case 1:
				userService.showFiles(user.getId());
				break;
			case 2:
				userService.hideFile(user.getId());
				break;
			case 3:
				userService.restoreFile(user.getId());
				break;
			case 0:
				run = false;
				break;

			default:
				System.err.println("Invalid Option..!!!");

			}

		} while (run);
	}

}
