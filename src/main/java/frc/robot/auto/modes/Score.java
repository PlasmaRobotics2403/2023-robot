package frc.robot.auto.modes;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.Elevator;
import frc.robot.Grabber;
import frc.robot.Swerve;
import frc.robot.auto.actions.AutoArm;
import frc.robot.auto.actions.AutoElevator;
import frc.robot.auto.actions.AutoExtender;
import frc.robot.auto.actions.AutoGrabber;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class Score extends AutoMode {

    Swerve swerve;
    Elevator elevator;
    Grabber grabber;
    PathPlannerTrajectory leaveCommunity;

    public Score(Swerve swerve, Elevator elevator, Grabber grabber) {
        this.swerve = swerve;
        this.elevator = elevator;
        this.grabber = grabber;
        

    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Audience_To_Charge", false);
        runActionsRace(new AutoGrabber(grabber, Constants.GrabberConstants.GRABBER_CLOSED_CUBE, 0.7), new AutoExtender(grabber, Constants.GrabberConstants.EXTENDER_RETRACTED_POSITION, 0.7));
        runAction(new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND, 1));
        runAction (new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 1)) ;
        runAction(new AutoExtender(grabber, Constants.GrabberConstants.EXTENDER_EXTENDED_POSITION, 0.3));
        runAction(new AutoGrabber(grabber, Constants.GrabberConstants.GRABBER_OPEN, 0.3));
        runActionsParallel(new AutoExtender(grabber, Constants.GrabberConstants.EXTENDER_RETRACTED_POSITION, 0.5), new AutoArm(grabber, Constants.GrabberConstants.ARM_STOWED_EXTEND, 0.5));
        DriverStation.reportWarning("Finished Audience_To_Charge", false);

    }
    
}
