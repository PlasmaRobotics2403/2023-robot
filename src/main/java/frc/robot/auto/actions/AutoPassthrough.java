package frc.robot.auto.actions;

import frc.robot.Constants;
import frc.robot.Grabber;
import frc.robot.Intake;
import frc.robot.auto.util.Action;

public class AutoPassthrough implements Action {
    private Intake intake;

    public AutoPassthrough(Intake intake) {
        this.intake = intake;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void start() {
        intake.passthrough(true);
    }

    @Override
    public void update() {
    }

    @Override
    public void end() {
    }
    
}
