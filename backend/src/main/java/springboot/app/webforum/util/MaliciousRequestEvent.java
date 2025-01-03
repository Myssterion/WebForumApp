package springboot.app.webforum.util;


import org.springframework.context.ApplicationEvent;

public class MaliciousRequestEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String context;

	public MaliciousRequestEvent(Object source, String context) {
		super(source);
		this.context = context;
	}

	public String getContext() {
		return context;
	}
}
