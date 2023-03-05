package frc.robot.auto.actions;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.Swerve;
import frc.robot.auto.util.Action;

public class FollowTrajectory  implements Action{
    Timer timer;
    PathPlannerTrajectory trajectory;
    HolonomicDriveController controller;

    Swerve swerve;
    boolean resetOdomety;


    public FollowTrajectory(PathPlannerTrajectory trajectory, Swerve swerve, boolean resetOdomety) {
        timer = new Timer();
        this.trajectory = trajectory;
        this.swerve = swerve;
        this.resetOdomety = resetOdomety;

        PIDController xController = new PIDController(Constants.AutoConstants.kPXController, 0, 0);
        PIDController yController = new PIDController(Constants.AutoConstants.kPYController, 0, 0);
        ProfiledPIDController thetaController = new ProfiledPIDController(
                                                    Constants.AutoConstants.kPThetaController,0, 0,
                                                    Constants.AutoConstants.kThetaControllerConstraints);
        thetaController.enableContinuousInput(-Math.PI, Math.PI);
        this.controller = new HolonomicDriveController(xController, yController, thetaController);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(trajectory.getTotalTimeSeconds());
    }

    @Override
    public void start() {
        swerve.setBrakeMode(true, true);
        timer.reset();
        timer.start();
        Pose2d iniitalPose = trajectory.getInitialPose();
        if(resetOdomety) {
            swerve.resetOdometry(new Pose2d(iniitalPose.getTranslation(), swerve.getRotation2d()));
        }
    }

    @Override
    public void update() {
       double time = timer.get();
       PathPlannerState desiredState = (PathPlannerState) trajectory.sample(time);
       ChassisSpeeds targetSpeeds = controller.calculate(swerve.getPose(), desiredState, new Rotation2d(desiredState.holonomicRotation.getRadians()));

       targetSpeeds.vyMetersPerSecond = targetSpeeds.vyMetersPerSecond;
       targetSpeeds.omegaRadiansPerSecond = targetSpeeds.omegaRadiansPerSecond;

       swerve.drive(targetSpeeds);
    }

    @Override
    public void end(){
        timer.stop();
        swerve.teleopDrive(0, 0, 0, false);
    }
}