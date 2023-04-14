package frc.robot.auto.actions;

import frc.robot.Constants;
import frc.robot.Grabber;
import frc.robot.Intake;
import frc.robot.auto.util.Action;

public class AutoPassthrough implements Action {
    private Intake intake;
    private Grabber grabber;

    public AutoPassthrough(Intake intake, Grabber grabber) {
        this.intake = intake;
        this.grabber = grabber;
    }

    @Override
    public boolean isFinished() {
        if(!grabber.getLimitSwitch()) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void start() {
        intake.passthrough(grabber.getLimitSwitch());
        grabber.runGrabber(Constants.GrabberConstants.GRABBER_SPEED);
    }

    @Override
    public void update() {
    }

    @Override
    public void end() {
        grabber.runGrabber(0);
        intake.idleGamePiece();
    }
    
}
