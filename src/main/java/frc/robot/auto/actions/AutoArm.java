package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Elevator;
import frc.robot.Grabber;
import frc.robot.auto.util.Action;

public class AutoArm implements Action {
    private Grabber grabber;

    double position;
    double timeout;
    double startTime;

    public AutoArm(Grabber grabber, double position, double timeout) {
        this.grabber = grabber;
        this.position = position;
        this.timeout = timeout;
    }

    @Override
    public boolean isFinished() {
        if ( (grabber.getArmPosition() <= position + 200 && grabber.getArmPosition() >= position - 200) 
        || (Timer.getFPGATimestamp() > startTime + timeout) ){
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
        grabber.magicArm(position);
    }

    @Override
    public void end() {
    }
    
}
