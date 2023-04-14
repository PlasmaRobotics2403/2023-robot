package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
    private VictorSPX intakeRoller;
    private VictorSPX conveyer;

    private Solenoid intakePiston;

    public Intake() {
        /* Linear Configs */
        conveyer = new VictorSPX(Constants.IntakeConstants.conveyerID);

        intakePiston = new Solenoid(Constants.IntakeConstants.PNUEMATIC_HUB_ID, PneumaticsModuleType.REVPH, Constants.IntakeConstants.INTAKE_SOLENOID_CHANNEL);

        /* Front Roller Configs */
        intakeRoller = new VictorSPX(Constants.IntakeConstants.intakeRollerID);

        intakeRoller.setInverted(false);
        intakeRoller.setNeutralMode(NeutralMode.Brake);
        conveyer.setInverted(true);
        conveyer.setNeutralMode(NeutralMode.Brake);

    }
    

    /**
     * extends the intake with a piston
     */
    public void extendIntake(){
        intakePiston.set(true);
    }

    /**
     * retract the intake with a pistion
     */
    public void retractIntake(){
        intakePiston.set(false);
    }

    /**
     * runs front roller
     * @param rollerSpeed speed in Percent Output
     */
    public void RunRoller(double rollerSpeed) {
        intakeRoller.set(ControlMode.PercentOutput, rollerSpeed);
    }


    /**
     * runs convyer that brings game pieces to the spindexer
     * @param conveyerSpeed speed in Percent Output
     */
    public void RunConveyer(double conveyerSpeed) {
        conveyer.set(ControlMode.PercentOutput, conveyerSpeed);
    }

    /**
     * process to extend intake and pick up game piece
     */
    public void intakeGamePiece() {
        extendIntake();
        intakeRoller.set(ControlMode.PercentOutput, Constants.IntakeConstants.rollerSpeed);
    }

    public void passthrough(boolean lsValue){
        if(!lsValue) {
            conveyer.set(ControlMode.PercentOutput, 0);
        }
        else {
            conveyer.set(ControlMode.PercentOutput, Constants.IntakeConstants.conveyerSpeed);
        }
    }

    /**
     * process when intake isnt active
     */
    public void idleGamePiece() {
            retractIntake();
            intakeRoller.set(ControlMode.PercentOutput, 0);
            conveyer.set(ControlMode.PercentOutput, 0);
    }

    public void ejectGamePiece() {
            extendIntake();
            intakeRoller.set(ControlMode.PercentOutput, -Constants.IntakeConstants.rollerejectSpeed);
            conveyer.set(ControlMode.PercentOutput, -Constants.IntakeConstants.conveyerSpeed);
    }

    
    public void logging() {
        SmartDashboard.putBoolean("Intake Extended?", intakePiston.get());
    }
}
