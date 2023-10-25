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
import frc.robot.auto.actions.AutoEntakeEject;
import frc.robot.auto.actions.AutoGrabber;
import frc.robot.auto.actions.AutoIntake;
import frc.robot.auto.actions.AutoPIDOnlyArm;
import frc.robot.auto.actions.AutoPassthoughScore;
import frc.robot.auto.actions.AutoPassthrough;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.actions.Wait;
import frc.robot.auto.util.Action;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class TwoCubeAutoOverCable extends AutoMode {

    Grabber grabber;
    Intake intake;
    Elevator elevator;
    Swerve swerve;

    PathPlannerTrajectory moveOneZachShoeForward;
    PathPlannerTrajectory moveToGamePiece;
    PathPlannerTrajectory goBackToGrid;
    PathPlannerTrajectory moveOneZachShoeBackward;


    public TwoCubeAutoOverCable(Intake intake, Grabber grabber, Elevator elevator, Swerve swerve) {
        this.intake = intake;
        this.grabber = grabber;
        this.elevator = elevator;
        this.swerve = swerve;

        try {
            moveOneZachShoeForward = PathPlanner.loadPath("moveOneZachShoeForward", new PathConstraints(1.5, 2));
            moveToGamePiece = PathPlanner.loadPath("grabGamePiece", new PathConstraints(2, 3));
            goBackToGrid = PathPlanner.loadPath("goBackToGridOverCable", new PathConstraints(1.3, 2));
            moveOneZachShoeBackward = PathPlanner.loadPath("moveOneZachShoeBackward", new PathConstraints(2, 3));


        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }

    }

    @Override
    protected void routine() throws AutoModeEndedException {
        //move to scoring position
        Action[] highScorePosition = {new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND, 1), new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 5, 0.5)};
        parallel(highScorePosition);
        runAction(new FollowTrajectory(moveOneZachShoeForward, swerve, true));
        //release game piece
        Action[] outakeGrabber = {new AutoGrabber(grabber, -Constants.GrabberConstants.GRABBER_SPEED, 0.75), new AutoPIDOnlyArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 0.75)};
        parallel(outakeGrabber);
        // go to next game piece
        Action[] collectGamePiece = {new FollowTrajectory(moveToGamePiece, swerve, true), new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_BOTTTOM_EXTEND, 2, 0.1125), new AutoArm(grabber, Constants.GrabberConstants.ARM_STOWED_EXTEND, 5, 1), new AutoIntake(intake, true, 2)};
        parallel(collectGamePiece);
        // stop intaking
        runActionsParallel((new AutoPassthrough(intake)), new AutoIntake(intake, false));
        // move to scoring position
        Action[] moveToScoringPos = {new FollowTrajectory(goBackToGrid, swerve, false)};
        parallel(moveToScoringPos);
        // release game piece
        runAction(new AutoEntakeEject(intake, 15));

    }
    
}
