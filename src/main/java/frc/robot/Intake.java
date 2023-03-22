package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
    private TalonSRX linearMotor;
    private VictorSPX intakeRoller;
    private VictorSPX conveyer;

    private DigitalInput limitSwitch;

    public Intake() {
        /* Linear Configs */
        linearMotor = new TalonSRX(Constants.IntakeConstants.sliderMotorID);
        conveyer = new VictorSPX(Constants.IntakeConstants.conveyerID);

        linearMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.IntakeConstants.PID_IDX, Constants.TIMEOUT_MS);
        linearMotor.setSelectedSensorPosition(0, Constants.IntakeConstants.PID_IDX, Constants.TIMEOUT_MS);

        linearMotor.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        linearMotor.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        linearMotor.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        linearMotor.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        linearMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		linearMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);

        linearMotor.setInverted(true);
        linearMotor.setNeutralMode(NeutralMode.Brake);

        linearMotor.selectProfileSlot(Constants.IntakeConstants.SLOT_IDX, Constants.IntakeConstants.PID_IDX);
        linearMotor.config_kF(0, Constants.IntakeConstants.linearkF);
        linearMotor.config_kP(0, Constants.IntakeConstants.linearkP);
        linearMotor.config_kI(0, Constants.IntakeConstants.linearkI);
        linearMotor.config_kD(0, Constants.IntakeConstants.linearkD);

        linearMotor.configMotionCruiseVelocity(Constants.IntakeConstants.MOTION_CRUISE_VELOCITY);
        linearMotor.configMotionAcceleration(Constants.IntakeConstants.MOTION_ACCELERATION);

        /* Front Roller Configs */
        intakeRoller = new VictorSPX(Constants.IntakeConstants.intakeRollerID);

        intakeRoller.setInverted(false);
        intakeRoller.setNeutralMode(NeutralMode.Brake);
        conveyer.setInverted(true);
        conveyer.setNeutralMode(NeutralMode.Brake);

        limitSwitch = new DigitalInput(Constants.IntakeConstants.backLimitSwitchID);

    }

    
    /**
     * controls intake extend and retract
     * @param slidingSpeed speed in Percent Output
     */
    public void ActuateIntake(double slidingSpeed) {
        if(limitSwitch.get() && slidingSpeed < 0) {
            linearMotor.set(ControlMode.PercentOutput, 0);
        }
        else {
           linearMotor.set(ControlMode.PercentOutput, slidingSpeed); 
        }
    }

    /**
     * 
     * @param extendPosition
     */
    public void MagicIntake(double extendPosition) {
        if(extendPosition >= Constants.IntakeConstants.MAX_INTAKE_EXTEND || extendPosition < Constants.IntakeConstants.MAX_INTAKE_RETRACT) {
            linearMotor.set(ControlMode.PercentOutput, 0);
        }
        else if(extendPosition <= 0 && limitSwitch.get()) {
            linearMotor.setSelectedSensorPosition(0);
            linearMotor.set(ControlMode.PercentOutput, 0);
        }
        else {
            linearMotor.set(ControlMode.MotionMagic, extendPosition);
        }
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
        if(linearMotor.getSelectedSensorPosition() < Constants.IntakeConstants.INTAKE_EXTEND_POS) {
            linearMotor.set(ControlMode.PercentOutput, Constants.IntakeConstants.linearMotorSpeed);
        }
        else {
            linearMotor.set(ControlMode.PercentOutput, 0);
        }

        conveyer.set(ControlMode.PercentOutput, Constants.IntakeConstants.conveyerSpeed);
        intakeRoller.set(ControlMode.PercentOutput, Constants.IntakeConstants.rollerSpeed);
    }

    /**
     * process when intake isnt active
     */
    public void idleGamePiece() {
        if(limitSwitch.get()) {
            linearMotor.set(ControlMode.PercentOutput, 0);
            intakeRoller.set(ControlMode.PercentOutput, 0);
            conveyer.set(ControlMode.PercentOutput, 0);
            linearMotor.setSelectedSensorPosition(0);
        }
        else {
            linearMotor.set(ControlMode.PercentOutput, -Constants.IntakeConstants.linearMotorSpeed);
            intakeRoller.set(ControlMode.PercentOutput, 0);
            conveyer.set(ControlMode.PercentOutput, 0);
        }
    }

    public void ejectGamePiece() {
        // need to run it out
        if(linearMotor.getSelectedSensorPosition() < Constants.IntakeConstants.INTAKE_EXTEND_POS) {
            linearMotor.set(ControlMode.PercentOutput, Constants.IntakeConstants.linearMotorSpeed);
            intakeRoller.set(ControlMode.PercentOutput, 0);
            conveyer.set(ControlMode.PercentOutput, -Constants.IntakeConstants.conveyerSpeed);
        }
        else {
            linearMotor.set(ControlMode.PercentOutput, 0);
            intakeRoller.set(ControlMode.PercentOutput, -Constants.IntakeConstants.rollerSpeed);
            conveyer.set(ControlMode.PercentOutput, -Constants.IntakeConstants.conveyerSpeed);
        }
    }

    
    public void logging() {
        SmartDashboard.putNumber("Intake Encoder", linearMotor.getSelectedSensorPosition());
        SmartDashboard.putBoolean("intake limit switch", limitSwitch.get());
    }
}
