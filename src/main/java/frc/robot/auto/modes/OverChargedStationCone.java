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
import frc.robot.auto.actions.AutoPIDOnlyArm;
import frc.robot.auto.actions.Balance;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.Action;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class OverChargedStationCone extends AutoMode {

    Swerve swerve;
    Elevator elevator;
    Grabber grabber;
    PathPlannerTrajectory overChargedStation;
    PathPlannerTrajectory moveOneZachShoeForward;

    public OverChargedStationCone(Swerve swerve, Elevator elevator, Grabber grabber) {
        this.swerve = swerve;
        this.elevator = elevator;
        this.grabber = grabber;
        
        try {
            overChargedStation = PathPlanner.loadPath("over charged station two", new PathConstraints(1.5, 2));
            moveOneZachShoeForward = PathPlanner.loadPath("moveOneZachShoeForward", new PathConstraints(0.6 , 2));
            
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        //move to scoring position
        Action[] highScorePosition = {new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND, 1), new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND_AUTO, 5, 0.5)};
        parallel(highScorePosition);
        runAction(new FollowTrajectory(moveOneZachShoeForward, swerve, true));
        //release game piece
        Action[] outakeGrabber = {new AutoGrabber(grabber, -Constants.GrabberConstants.GRABBER_SPEED, 0.75), new AutoPIDOnlyArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 0.75)};
        parallel(outakeGrabber);
        
        Action[] balance = {new FollowTrajectory(overChargedStation, swerve, true), new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_BOTTTOM_EXTEND, 2, 0.07), new AutoArm(grabber, Constants.GrabberConstants.ARM_STOWED_EXTEND, 5, 0.1)};
        parallel(balance);
        runAction(new Balance(swerve));

    }
    
}