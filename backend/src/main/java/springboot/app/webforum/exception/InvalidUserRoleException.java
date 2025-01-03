package springboot.app.webforum.exception;

public class InvalidUserRoleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidUserRoleException() {
		super();
	}

	public InvalidUserRoleException(String message) {
		super(message);
	}

}
