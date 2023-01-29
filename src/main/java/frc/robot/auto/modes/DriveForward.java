package frc.robot.auto.modes;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Swerve;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class DriveForward extends AutoMode {

    Swerve swerve;
    PathPlannerTrajectory drive_forward;

    public DriveForward(Swerve swerve) {
        this.swerve = swerve;
        try {
            drive_forward = PathPlanner.loadPath("drive forward", new PathConstraints(1, 0.5));
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Drive_Forward", false);
        runAction(new FollowTrajectory(drive_forward, swerve, true));
        DriverStation.reportWarning("Finished Drive_Forward", false);

    }
    
}
