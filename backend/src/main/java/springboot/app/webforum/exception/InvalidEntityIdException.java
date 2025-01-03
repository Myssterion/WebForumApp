package springboot.app.webforum.exception;

public class InvalidEntityIdException  extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEntityIdException() {
		super();
	}

	public InvalidEntityIdException(String message) {
		super(message);
	}

}
