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
import frc.robot.auto.actions.Balance;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class CableToCharge extends AutoMode {

    Swerve swerve;
    Elevator elevator;
    Grabber grabber;
    PathPlannerTrajectory cable_run_to_charge;

    public CableToCharge(Swerve swerve) {
        this.swerve = swerve;
        try {
            cable_run_to_charge = PathPlanner.loadPath("cable run to charge", new PathConstraints(1.5, 2));
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Cable_To_Charge", false);
        runAction(new AutoElevator(elevator, Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND));
        runAction(new AutoArm(grabber, Constants.GrabberConstants.ARM_HIGH_EXTEND));
        runAction(new FollowTrajectory(cable_run_to_charge, swerve, true));
        runAction(new Balance(swerve));
        DriverStation.reportWarning("Finished Cable_To_Charge", false);

    }
    
}
