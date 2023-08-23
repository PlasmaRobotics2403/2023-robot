package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Elevator;
import frc.robot.auto.util.Action;

public class AutoElevator implements Action {
    private Elevator elevator;

    double position;
    double timeout;
    double startTime;
    double delay;

    public AutoElevator(Elevator elevator, double position, double timeout) {
        this.elevator = elevator;
        this.position = position;
        this.timeout = timeout;
        this.delay = 0.0;
    }

    public AutoElevator(Elevator elevator, double position, double timeout, double delay) {
        this.elevator = elevator;
        this.position = position;
        this.timeout = timeout;
        this.delay = delay;
    }

    @Override
    public boolean isFinished() {
        if ( (elevator.getPosition() <= position + 200 && elevator.getPosition() >= position - 200) 
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
        if(Timer.getFPGATimestamp() > startTime + delay) {
            elevator.magicElevator(position);
        }
    }

    @Override
    public void end() {
    }
    
}
