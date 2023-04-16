package frc.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.Grabber;
import frc.robot.Intake;
import frc.robot.auto.util.Action;

public class AutoEntakeEject implements Action {
    private Intake intake;
    private boolean extend;
    private double timeout;
    private double startTime;

    public AutoEntakeEject(Intake intake, double timeout) {
        this.intake = intake;
        this.extend = extend;
        this.timeout = timeout;
    }

    @Override
    public boolean isFinished() {
        if (Timer.getFPGATimestamp() > startTime + timeout){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        intake.ejectGamePiece();

    }

    @Override
    public void update() {
    }

    @Override
    public void end() {
        intake.idleGamePiece();
    }
    
}
