package springboot.app.webforum.util;

import java.util.Random;

public class VerificationCodeGenerator {

	public static Random random = new Random();
	
	public static String generateCode() {
		String verificationCode = "";
		for(int i=0;i<6;i++)
			verificationCode += random.nextInt(10);
			
		return verificationCode;
	}

}
