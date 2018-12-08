package org.scaffold.sorm.actuator;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ActuatorFactroy {
	
	private static IActuator actuator;
	
	public static synchronized IActuator getActuator() throws ActuatorInitException {
		if(actuator == null) {
			throw new ActuatorInitException("SQL执行器未初始化, 请先通过ActuatorFactroy.initActuator(JdbcTemplate jdbcTemplate)初始化SQL执行器 ... ");
		}
		return actuator;
	}
	
	public static synchronized void initActuator(JdbcTemplate jdbcTemplate) {
		if(actuator == null) {
			actuator = SqlActuator.getInstance(jdbcTemplate);
		}
	}

}
