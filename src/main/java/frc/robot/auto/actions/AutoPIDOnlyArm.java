package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Grabber;
import frc.robot.auto.util.Action;

public class AutoPIDOnlyArm  implements Action{
    private Grabber grabber;

    double timeout;
    double starTime;
    double position;

    public AutoPIDOnlyArm(Grabber grabber, double position, double timeout) {
        this.grabber = grabber;
        this.position = position;
        this.timeout = timeout;
    }

    @Override
    public boolean isFinished() {
        DriverStation.reportError(Double.toString(grabber.getAbsoluteArmPosition())+"-"+Double.toString(position), false);
        if (Timer.getFPGATimestamp() > starTime + timeout) {
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        starTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {   
        grabber.magicArm(position);
        
    }

    @Override
    public void end() {
        grabber.magicArm(position);
    }
    
}