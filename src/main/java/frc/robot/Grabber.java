package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber {
    private TalonSRX arm;
    private TalonSRX grabber;
    private TalonSRX extender;

    public Grabber() {

        /* Arm Setup */
        arm = new TalonSRX(Constants.GrabberConstants.arm_id);

        arm.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.GrabberConstants.ARM_PID_IDX, Constants.TIMEOUT_MS);
        arm.setSelectedSensorPosition(0, Constants.GrabberConstants.ARM_PID_IDX, Constants.TIMEOUT_MS);

        arm.setInverted(true);
        arm.setNeutralMode(NeutralMode.Brake);

        arm.config_kF(0, Constants.GrabberConstants.armkF);
        arm.config_kP(0, Constants.GrabberConstants.armkP);
        arm.config_kI(0, Constants.GrabberConstants.armkI);
        arm.config_kD(0, Constants.GrabberConstants.armkD);

        /* Grabber Setup */
        grabber = new TalonSRX(Constants.GrabberConstants.grabber_id);

        grabber.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.GrabberConstants.ARM_PID_IDX, Constants.TIMEOUT_MS);
        grabber.setSelectedSensorPosition(0, Constants.GrabberConstants.ARM_PID_IDX, Constants.TIMEOUT_MS);

        grabber.setInverted(true);
        grabber.setNeutralMode(NeutralMode.Brake);

        grabber.config_kF(0, Constants.GrabberConstants.grabberkF);
        grabber.config_kP(0, Constants.GrabberConstants.grabberkP);
        grabber.config_kI(0, Constants.GrabberConstants.grabberkI);
        grabber.config_kD(0, Constants.GrabberConstants.grabberkD);

        /* Arm Extendtion Setup */
        extender = new TalonSRX(Constants.GrabberConstants.extender_id);

        extender.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.GrabberConstants.ARM_PID_IDX, Constants.TIMEOUT_MS);
        extender.setSelectedSensorPosition(0, Constants.GrabberConstants.ARM_PID_IDX, Constants.TIMEOUT_MS);

        extender.setInverted(true);
        extender.setNeutralMode(NeutralMode.Brake);

        extender.config_kF(0, Constants.GrabberConstants.extenderkF);
        extender.config_kP(0, Constants.GrabberConstants.extenderkP);
        extender.config_kI(0, Constants.GrabberConstants.extenderkI);
        extender.config_kD(0, Constants.GrabberConstants.extenderkD);
    }


    /**
     * rotates the arm at a speed
     * @param armSpeed speed in Percent Output
     */
    public void ArmRot(double armSpeed) {
        if(armSpeed > 0 && arm.getSelectedSensorPosition() >= Constants.GrabberConstants.ARM_MAX_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else if (armSpeed < 0 && arm.getSelectedSensorPosition() <= Constants.GrabberConstants.ARM_MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.PercentOutput, armSpeed);
        }
    }


    /**
     * magicaly rotates the arm to position
     * @param rotPosition
     */
    public void magicArm(double rotPosition) {
        if(rotPosition >= Constants.GrabberConstants.ARM_MAX_EXTEND || rotPosition < Constants.GrabberConstants.ARM_MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.MotionMagic, rotPosition);
        }
    }


    /**
     * grabber opens and closing at a speed
     * @param grabberSpeed speed in Percent Output
     */
    public void grabberRun(double grabberSpeed) {
        if(grabberSpeed > 0 && arm.getSelectedSensorPosition() >= Constants.GrabberConstants.GRABBER_MAX_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else if (grabberSpeed < 0 && arm.getSelectedSensorPosition() <= Constants.GrabberConstants.GRABBER_MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.PercentOutput, grabberSpeed);
        }    
    }


    /**
     * grabber opening-closing at a position
     * @param grabberPosition position it needs to open
     */
    public void grabberPos(double grabberPosition) {
        if(grabberPosition >= Constants.GrabberConstants.ARM_MAX_EXTEND || grabberPosition < Constants.GrabberConstants.ARM_MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.MotionMagic, grabberPosition);
        }
    }


    /**
     * arm extender extends at a speed
     * @param extendSpeed speed in Percent Output
     */
    public void extend(double extendSpeed) {
        if(extendSpeed > 0 && arm.getSelectedSensorPosition() >= Constants.GrabberConstants.ARM_MAX_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else if (extendSpeed < 0 && arm.getSelectedSensorPosition() <= Constants.GrabberConstants.ARM_MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.PercentOutput, extendSpeed);
        }
    }


    /**
     * arm extender extend at a position 
     * @param extendPos position it needs to extend to
     */    
    public void extendPos(double extendPos) {
        if(extendPos >= Constants.GrabberConstants.EXTENDER_MAX_EXTEND || extendPos < Constants.GrabberConstants.EXTENDER_MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.MotionMagic, extendPos);
        }
    }

    
    /**
     * all values that need to be updated periodically
     * and smartdashboard display
     */
    public void logging() {
        SmartDashboard.putNumber("Arm Encoder", arm.getSelectedSensorPosition());
    }
}