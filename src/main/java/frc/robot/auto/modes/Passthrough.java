package frc.robot.auto.modes;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.Elevator;
import frc.robot.Grabber;
import frc.robot.Intake;
import frc.robot.Swerve;
import frc.robot.auto.actions.AutoArm;
import frc.robot.auto.actions.AutoElevator;
import frc.robot.auto.actions.AutoGrabber;
import frc.robot.auto.actions.AutoIntake;
import frc.robot.auto.actions.AutoPassthoughScore;
import frc.robot.auto.actions.AutoPassthrough;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.actions.Wait;
import frc.robot.auto.util.Action;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class Passthrough extends AutoMode {

    Grabber grabber;
    Intake intake;
    Elevator elevator;

    PathPlannerTrajectory leaveCommunity;

    public Passthrough(Intake intake, Grabber grabber, Elevator elevator) {
        this.intake = intake;
        this.grabber = grabber;
        this.elevator = elevator;

    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Audience_To_Charge", false);
        //move to scoring position
        runActionsParallel(new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND, 2), (new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 2)));
        //release game piece
        runAction(new AutoGrabber(grabber, -Constants.GrabberConstants.GRABBER_SPEED, 1));
        // go to next game piece
        Action[] collectGamePiece = {new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_BOTTTOM_EXTEND, 2, 1), new AutoArm(grabber, Constants.GrabberConstants.ARM_STOWED_EXTEND, 1), new AutoIntake(intake, true), new Wait(4)};
        parallel(collectGamePiece);
        // stop intaking
        runActionsParallel(new AutoIntake(intake, false), new AutoPassthrough(intake, grabber));
        // move to scoring position
        Action[] moveToScoringPos = {new Wait(4), new AutoPassthoughScore(intake, grabber, elevator, Constants.ElevatorConstants.ELEVATOR_MID_EXTEND, Constants.GrabberConstants.ARM_HIGH_EXTEND, 4)};
        parallel(moveToScoringPos);
        // release game piece
        runAction(new AutoGrabber(grabber, -Constants.GrabberConstants.GRABBER_SPEED, 1));
        DriverStation.reportWarning("Finished Audience_To_Charge", false);

    }
    
}
