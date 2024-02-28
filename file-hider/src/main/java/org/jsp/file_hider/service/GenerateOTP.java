package org.jsp.file_hider.service;

import java.util.Random;

public class GenerateOTP {

	public static String getOTP() {
		return String.format("%04d", new Random().nextInt(10000));
	}
}
