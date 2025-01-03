package springboot.app.webforum.exception;

public class InvalidUsernameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidUsernameException() {
		super();
	}

	public InvalidUsernameException(String message) {
		super(message);
	}
	
}
