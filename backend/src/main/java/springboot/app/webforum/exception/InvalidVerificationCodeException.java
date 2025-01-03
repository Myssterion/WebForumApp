package springboot.app.webforum.exception;

public class InvalidVerificationCodeException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidVerificationCodeException() {
		super();
	}

	public InvalidVerificationCodeException(String message) {
		super(message);
	}
	

}
