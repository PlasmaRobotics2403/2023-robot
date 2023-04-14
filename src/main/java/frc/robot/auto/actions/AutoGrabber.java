package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Elevator;
import frc.robot.Grabber;
import frc.robot.auto.util.Action;

public class AutoGrabber implements Action {
    private Grabber grabber;

    private double speed;
    private double timeout;
    private double startTime;

    public AutoGrabber(Grabber grabber, double speed, double timeout) {
        this.grabber = grabber;
        this.speed = speed;
        this.timeout = timeout;
    }

    @Override
    public boolean isFinished() {
        if(Timer.getFPGATimestamp() > startTime + timeout) {
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        grabber.runGrabber(speed);
    }

    @Override
    public void update() {
    }

    @Override
    public void end() {
        grabber.runGrabber(0);
    }
    
}
