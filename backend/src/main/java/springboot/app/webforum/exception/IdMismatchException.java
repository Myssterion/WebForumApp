package springboot.app.webforum.exception;

public class IdMismatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IdMismatchException() {
		super();
	}

	public IdMismatchException(String message) {
		super(message);
	}

}
