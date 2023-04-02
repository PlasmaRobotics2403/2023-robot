package frc.robot.auto.modes;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Elevator;
import frc.robot.Grabber;
import frc.robot.Swerve;
import frc.robot.auto.actions.FollowTrajectory;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeEndedException;

public class DoubleScore extends AutoMode {

    Swerve swerve;
    Elevator elevator;
    Grabber grabber;
    PathPlannerTrajectory two_game_piece;

    public DoubleScore(Swerve swerve) {
        this.swerve = swerve;
        
        try {
            two_game_piece = PathPlanner.loadPath("two game piece", new PathConstraints(1.5, 2));
        }
        catch (Exception e) {
            DriverStation.reportError(e.getMessage(), false);
        }
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        DriverStation.reportWarning("Running Audience_To_Charge", false);
        runAction(new FollowTrajectory(two_game_piece, swerve, true));
        DriverStation.reportWarning("Finished Audience_To_Charge", false);

    }
    
}
