package frc.robot.auto.modes;

import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
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

public class ScoringTableToCharge extends AutoMode {

    Swerve swerve;
    Elevator elevator;
    Grabber grabber;
    PathPlannerTrajectory scoring_table_to_charge;

    public ScoringTableToCharge(Swerve swerve, Elevator elevator, Grabber grabber) {
        this.swerve = swerve;
        this.elevator = elevator;
        this.grabber = grabber;
        try {
            if(DriverStation.getAlliance() == DriverStation.Alliance.Blue){
                scoring_table_to_charge = PathPlanner.loadPath("left turn charge Copy", new PathConstraints(2, 2));
            }
            else {
                scoring_table_to_charge = PathPlanner.loadPath("right turn charge Copy", new PathConstraints(2, 2));
            }
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Scoring_Table_To_Charge", false);
        runAction(new AutoGrabber(grabber, Constants.GrabberConstants.GRABBER_CLOSED_CUBE, 0.7));
        runAction(new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND, 1));
        runAction (new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 1)) ;
        runAction(new AutoGrabber(grabber, Constants.GrabberConstants.GRABBER_SPEED, 1));
        runActionsParallel(new FollowTrajectory(scoring_table_to_charge, swerve, true), new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_BOTTTOM_EXTEND, 1.5));
        DriverStation.reportError("balancing", false);
        runAction(new Balance(swerve));
        DriverStation.reportWarning("Finished Scoring_Table__To_Charge", false);
    }   
}