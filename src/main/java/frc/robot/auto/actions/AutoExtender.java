package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Elevator;
import frc.robot.Grabber;
import frc.robot.auto.util.Action;

public class AutoExtender implements Action {
    private Grabber grabber;

    private double position;
    private double timeout;
    private double startTime;

    public AutoExtender(Grabber grabber, double position, double timeout) {
        this.grabber = grabber;
        this.position = position;
        this.timeout = timeout;
    }

    @Override
    public boolean isFinished() {
        /*if((grabber.getExtenderPosition() <= position + 300 && grabber.getExtenderPosition() >= position - 300)
        || (Timer.getFPGATimestamp() > startTime + timeout) ) {
            return true;
        }
        return false;
    }*/return true; }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
        //grabber.extendPos(position);
    }

    @Override
    public void end() {
    }
    
}
