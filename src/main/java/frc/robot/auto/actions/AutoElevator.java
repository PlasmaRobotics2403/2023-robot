package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Elevator;
import frc.robot.auto.util.Action;

public class AutoElevator implements Action {
    private Elevator elevator;

    double position;
    double timeout;
    double startTime;

    public AutoElevator(Elevator elevator, double position, double timeout) {
        this.elevator = elevator;
        this.position = position;
        this.timeout = timeout;
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
        startTime =Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
        elevator.magicElevator(position);
    }

    @Override
    public void end() {
    }
    
}
