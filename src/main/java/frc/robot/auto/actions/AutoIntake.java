package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.Grabber;
import frc.robot.Intake;
import frc.robot.auto.util.Action;

public class AutoIntake implements Action {
    private Intake intake;
    private boolean extend;
    private double delay;
    private double startTime;

    public AutoIntake(Intake intake, boolean extend) {
        this.intake = intake;
        this.extend = extend;
        delay = 0;
    }

    public AutoIntake(Intake intake, boolean extend, double delay) {
        this.intake = intake;
        this.extend = extend;
        this.delay = delay;
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() > startTime + delay;
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
    }

    @Override
    public void end() {
        if(extend) {
            intake.intakeGamePiece();
        }
        else {
            intake.idleGamePiece();
        }
    }
    
}
