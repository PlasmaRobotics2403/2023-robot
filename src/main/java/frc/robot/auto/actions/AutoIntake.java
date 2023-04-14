package frc.robot.auto.actions;

import frc.robot.Constants;
import frc.robot.Grabber;
import frc.robot.Intake;
import frc.robot.auto.util.Action;

public class AutoIntake implements Action {
    private Intake intake;
    private boolean extend;

    public AutoIntake(Intake intake, boolean extend) {
        this.intake = intake;
        this.extend = extend;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void start() {
        if(extend) {
            intake.intakeGamePiece();
        }
        else {
            intake.idleGamePiece();
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void end() {
    }
    
}
