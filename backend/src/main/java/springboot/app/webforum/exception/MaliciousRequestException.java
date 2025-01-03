package springboot.app.webforum.exception;

public class MaliciousRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MaliciousRequestException() {
		super();
	}

	public MaliciousRequestException(String message) {
		super(message);
	}
}
