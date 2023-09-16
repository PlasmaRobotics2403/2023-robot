package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.Elevator;
import frc.robot.Grabber;
import frc.robot.Intake;
import frc.robot.auto.util.Action;

public class AutoPassthoughScore implements Action {
    private Intake intake;
    private Grabber grabber;
    private Elevator elevator;

    private double elevatorPos;
    private double armPos;
    private double timeout;
    private double startTime;
    private double armDelay;

    public AutoPassthoughScore(Intake intake, Grabber grabber, Elevator elevator, double elevatorPos, double armPos, double timeout, double armDelay) {
        this.intake = intake;
        this.grabber = grabber;
        this.elevator = elevator;

        this.elevatorPos = elevatorPos;
        this.armPos = armPos;
        this.timeout = timeout;
        this.armDelay = armDelay;
    }   

    @Override
    public boolean isFinished() {
        if(( !grabber.getLimitSwitch() &&
             (elevator.getPosition() <= elevatorPos + 200 && elevator.getPosition() >= elevatorPos - 200) &&
             (grabber.getAbsoluteArmPosition() <= armPos + 10 && grabber.getAbsoluteArmPosition() >= armPos - 10)
           ) || (Timer.getFPGATimestamp() > startTime + timeout))
            {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void start() {
        startTime =Timer.getFPGATimestamp();
        intake.passthrough(grabber.getLimitSwitch());
        grabber.runGrabber(Constants.GrabberConstants.GRABBER_SPEED);
    }

    @Override
    public void update() {
        if(!grabber.getLimitSwitch() || (Timer.getFPGATimestamp() > startTime + (timeout - 0.5))) {
            intake.idleGamePiece();
            elevator.magicElevator(elevatorPos);
            grabber.runGrabber(0);
            if(Timer.getFPGATimestamp() > startTime + armDelay){            
                grabber.magicArm(armPos);
            }
        }
    }

    @Override
    public void end() {
        grabber.runGrabber(0);
    }
    
}
