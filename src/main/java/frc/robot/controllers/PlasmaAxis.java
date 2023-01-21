package frc.robot.controllers;

import edu.wpi.first.wpilibj.*;

public class PlasmaAxis {
	
	private final int joystickPort;
	private final int axisNum;
	private final int dirMultiplier;
	
	/**
	 * Constructor for plasma axis
	 * 
	 * @param axisNum - ID number of axis
	 * @param joystickPort - Port of joystick that the axis is on
	 * @param isReverse - True if axis input is reversed 
	 * 
	 * @author Nic A
	 */
	public PlasmaAxis(int axisNum, int joystickPort, boolean isReverse) {
		this.joystickPort = joystickPort;
		this.axisNum = axisNum;
		this.dirMultiplier = (isReverse) ? -1 : 1;
	}
	
	/**
	 * Constructor for plasma axis
	 * 
	 * @param axisNum - ID number of axis
	 * @param joystickPort - Port of joystick that the axis is on
	 * 
	 * @author Nic A
	 */
	public PlasmaAxis(int axisNum, int joystickPort) {
		this(axisNum, joystickPort, false);
	}
	
	/**
	 * Returns the raw value from axis after reversal
	 * 
	 * @return Axis value
	 * 
	 * @author Nic A
	 */
	public double getTrueAxis(){
		return DriverStation.getStickAxis(joystickPort, axisNum) * dirMultiplier;
	}
	
	/**
	 * Returns the value of the axis after reversal and deadband calculations
	 * 
	 * @return Axis value after deadband
	 * 
	 * @author Nic A
	 */
	public double getFilteredAxis(){
		if(Math.abs(getTrueAxis()) > JoystickConstants.deadBand){
			return getTrueAxis();
		}
		else{
			return 0;
		}
	}
	

}