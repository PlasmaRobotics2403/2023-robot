package frc.robot.auto.modes;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Swerve;
import frc.robot.auto.actions.Balance;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class FlatToCharge extends AutoMode {

    Swerve swerve;
    PathPlannerTrajectory flat_to_charge;

    public FlatToCharge(Swerve swerve) {
        this.swerve = swerve;
        try {
            flat_to_charge = PathPlanner.loadPath("flat to charge", new PathConstraints(1.5, 2));
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Drive_Forward", false);
        runAction(new FollowTrajectory(flat_to_charge, swerve, true));
        runAction(new Balance(swerve));
        DriverStation.reportWarning("Finished Drive_Forward", false);

    }
    
}
