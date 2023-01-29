package frc.robot.auto.modes;

import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 */
public class Nothing extends AutoMode {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.usfirst.frc.team2403.robot.auto.util.AutoMode#routine()
	 */
	@Override
	protected void routine() throws AutoModeEndedException {
		DriverStation.reportWarning("LOL YOU DIDNT DO ANYTHING SCRUB", false);
	}

}