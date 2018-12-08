package org.scaffold.sorm.actuator;

public class ActuatorInitException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ActuatorInitException() {
		super();
	}
	
	public ActuatorInitException(String msg) {
		super(msg);
	}
	
	public ActuatorInitException(String msg, Throwable e) {
		super(msg, e);
	}
}
