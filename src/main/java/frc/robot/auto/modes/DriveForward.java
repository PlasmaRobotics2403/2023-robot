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
    PathPlannerTrajectory drive_backward;
    PathPlannerTrajectory test_path;
    PathPlannerTrajectory fake_auto;

    public DriveForward(Swerve swerve) {
        this.swerve = swerve;
        try {
            drive_forward = PathPlanner.loadPath("drive forward", new PathConstraints(2, 3));
            drive_backward = PathPlanner.loadPath("drive backwards", new PathConstraints(1, 3));
            test_path = PathPlanner.loadPath("New Path", new PathConstraints(1, 3));
            fake_auto = PathPlanner.loadPath("fake auto score", new PathConstraints(1, 3));

        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Drive_Forward", false);
        runAction(new FollowTrajectory(drive_forward, swerve, true));
        //runAction(new FollowTrajectory(drive_backward, swerve, false));
        //runAction(new FollowTrajectory(test_path, swerve, true));
        //runAction(new FollowTrajectory(fake_auto, swerve, true));
        DriverStation.reportWarning("Finished Drive_Forward", false);

    }
    
}
