package frc.robot.controllers;

import edu.wpi.first.wpilibj.*;

public class PlasmaDPad {

	private final int joystickPort;
	
	/**
	 * Constructor for Plasma D-Pad
	 * 
	 * @param dPadNum - ID number of d-pad
	 * @param joystickPort - Port of joystick that d-pad is connected to
	 * 
	 * @author Nic A
	 */
	public PlasmaDPad(int dPadNum, int joystickPort) {
		this.joystickPort = joystickPort;
	}
	
	/**
	 * Gets angle of d-pad
	 * 
	 * @return value from 0-360 degrees based on angle, -1 if not pressed
	 * 
	 * @author Nic A
	 */
	public int getPOV(){
		return DriverStation.getStickPOV(joystickPort, 0);
	}

}