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
    double delay;

    public AutoArm(Grabber grabber, double position, double timeout) {
        this.grabber = grabber;
        this.position = position;
        this.timeout = timeout;
        this.delay = 0.0;
    }

    public AutoArm(Grabber grabber, double position, double timeout, double delay) {
        this.grabber = grabber;
        this.position = position;
        this.timeout = timeout;
        this.delay = delay;
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
        if(Timer.getFPGATimestamp() > startTime + delay){
            grabber.magicArm(position);
        }
    }

    @Override
    public void end() {
    }
    
}
