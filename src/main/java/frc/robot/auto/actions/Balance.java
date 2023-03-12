package frc.robot.auto.actions;

import frc.robot.Swerve;
import frc.robot.auto.util.Action;

public class Balance implements Action {
    private Swerve swerve;

    public Balance(Swerve swerve) {
        this.swerve = swerve;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        swerve.balance();        
    }

    @Override
    public void end() {
        swerve.teleopDrive(0, 0, 0, false);        
    }
    
}
