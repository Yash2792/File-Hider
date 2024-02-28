package org.jsp.file_hider.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsp.file_hider.service.UserService;

public class Welcome {

	public void welcomeScreen() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		UserService userService = new UserService();

		System.out.println("==========Welcome to the File-Hider==========" + "\nPress 1 to Login"
				+ "\nPress 2 to Signup" + "\nPress 0 to Exit");

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
			userService.login();
			break;
		case 2:
			userService.signup();
			break;
		case 0:
			System.exit(0);

		default:
			System.err.println("Invalid Option..!!!");

		}

	}

}
