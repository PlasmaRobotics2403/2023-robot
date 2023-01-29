package frc.robot.auto.util;

import java.io.IOException;

public interface Action {
	
	/**
	 * Must return true when the action is complete and false otherwise
	 * @return boolean
	 */
	public abstract boolean isFinished();
	
	/**
	 * This runs once at the beginning of the action. 
	 * @throws IOException
	 */
	public abstract void start();
	
	/**
	 * Control the action here. Anything in here should be coded using iterative logic.
	 */
	public abstract void update();
	
	/**
	 * This runs once after the action is finished.
	 */
	public abstract void end();
}
