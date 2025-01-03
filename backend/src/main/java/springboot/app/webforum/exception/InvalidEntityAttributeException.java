package springboot.app.webforum.exception;

public class InvalidEntityAttributeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEntityAttributeException() {
		super();
	}

	public InvalidEntityAttributeException(String message) {
		super(message);
	}

}
