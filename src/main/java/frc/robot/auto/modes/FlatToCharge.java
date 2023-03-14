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
import frc.robot.auto.actions.Balance;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class FlatToCharge extends AutoMode {

    Swerve swerve;
    Elevator elevator;
    Grabber grabber;
    PathPlannerTrajectory flat_to_charge;

    public FlatToCharge(Swerve swerve, Elevator elevator, Grabber grabber) {
        this.swerve = swerve;
        this.elevator = elevator;
        this.grabber = grabber;
        try {
            flat_to_charge = PathPlanner.loadPath("flat to charge", new PathConstraints(1.5, 2));
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Flat_To_Charge", false);
        runActionsRace(new AutoGrabber(grabber, 2500, 1.5), new AutoExtender(grabber, Constants.GrabberConstants.EXTENDER_UP_POSITION, 1));
        DriverStation.reportWarning("1", false);
        runActionsParallel(new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND, 1.5),
                            new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND, 1.5) );
        DriverStation.reportWarning("2", false);
        runAction(new AutoGrabber(grabber, Constants.GrabberConstants.GRABBER_OPEN, 1));
        DriverStation.reportWarning("3", false);
        //runAction(new FollowTrajectory(flat_to_charge, swerve, true));
        //runAction(new Balance(swerve));
        DriverStation.reportWarning("Finished Flat_to_Charge", false);

    }
    
}
