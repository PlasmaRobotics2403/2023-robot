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
import frc.robot.auto.actions.AutoGrabber;
import frc.robot.auto.actions.Balance;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class LeaveCommunity extends AutoMode {

    Swerve swerve;
    Elevator elevator;
    Grabber grabber;
    PathPlannerTrajectory leaveCommunity;

    public LeaveCommunity(Swerve swerve, Elevator elevator, Grabber grabber) {
        this.swerve = swerve;
        this.elevator = elevator;
        this.grabber = grabber;
        
        try {
            leaveCommunity = PathPlanner.loadPath("leave community", new PathConstraints(1.5, 2));
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Audience_To_Charge", false);
        runAction(new AutoGrabber(grabber, Constants.GrabberConstants.GRABBER_CLOSED_CUBE, 0.7));
        runAction(new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND, 1));
        runAction (new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 1)) ;
        runAction(new AutoGrabber(grabber, Constants.GrabberConstants.GRABBER_SPEED, 1));
        runActionsParallel(new FollowTrajectory(leaveCommunity, swerve, true), new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_BOTTTOM_EXTEND, 1.5));
        DriverStation.reportWarning("Finished Audience_To_Charge", false);

    }
    
}
