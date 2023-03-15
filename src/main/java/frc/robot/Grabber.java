package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
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

        arm.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        arm.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        arm.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        arm.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        arm.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);

        arm.setInverted(true);
        arm.setNeutralMode(NeutralMode.Brake);

        arm.selectProfileSlot(Constants.GrabberConstants.ARM_SLOT_IDX, Constants.GrabberConstants.ARM_PID_IDX);
        arm.config_kF(0, Constants.GrabberConstants.armkF);
        arm.config_kP(0, Constants.GrabberConstants.armkP);
        arm.config_kI(0, Constants.GrabberConstants.armkI);
        arm.config_kD(0, Constants.GrabberConstants.armkD);

        arm.configMotionCruiseVelocity(Constants.GrabberConstants.ARM_MOTION_CRUISE_VELOCITY);
        arm.configMotionAcceleration(Constants.GrabberConstants.ARM_MOTION_ACCELERATION);

        /* Grabber Setup */
        grabber = new TalonSRX(Constants.GrabberConstants.grabber_id);

        grabber.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.GrabberConstants.GRABBER_PID_IDX, Constants.TIMEOUT_MS);
        grabber.setSelectedSensorPosition(0, Constants.GrabberConstants.GRABBER_PID_IDX, Constants.TIMEOUT_MS);

        grabber.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        grabber.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        grabber.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        grabber.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        grabber.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		grabber.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);
        
        grabber.setInverted(true);
        grabber.setNeutralMode(NeutralMode.Brake);

        grabber.selectProfileSlot(Constants.GrabberConstants.GRABBER_SLOT_IDX, Constants.GrabberConstants.GRABBER_PID_IDX);
        grabber.config_kF(0, Constants.GrabberConstants.grabberkF);
        grabber.config_kP(0, Constants.GrabberConstants.grabberkP);
        grabber.config_kI(0, Constants.GrabberConstants.grabberkI);
        grabber.config_kD(0, Constants.GrabberConstants.grabberkD);

        grabber.configMotionCruiseVelocity(Constants.GrabberConstants.GRABBER_MOTION_CRUISE_VELOCITY);
        grabber.configMotionAcceleration(Constants.GrabberConstants.GRABBER_MOTION_ACCELERATION);

        /* Arm Extendtion Setup */
        extender = new TalonSRX(Constants.GrabberConstants.extender_id);

        extender.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		extender.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);

        extender.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        extender.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        extender.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        extender.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        extender.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.GrabberConstants.EXTENDER_PID_IDX, Constants.TIMEOUT_MS);
        extender.setSelectedSensorPosition(0, Constants.GrabberConstants.EXTENDER_PID_IDX, Constants.TIMEOUT_MS);

        extender.setInverted(true);
        extender.setSensorPhase(true);
        extender.setNeutralMode(NeutralMode.Brake);
        
        extender.selectProfileSlot(Constants.GrabberConstants.EXTENDER_SLOT_IDX, Constants.GrabberConstants.EXTENDER_PID_IDX);
        extender.config_kF(0, Constants.GrabberConstants.extenderkF);
        extender.config_kP(0, Constants.GrabberConstants.extenderkP);
        extender.config_kI(0, Constants.GrabberConstants.extenderkI);
        extender.config_kD(0, Constants.GrabberConstants.extenderkD);

        extender.configMotionCruiseVelocity(Constants.GrabberConstants.EXTENDER_MOTION_CRUISE_VELOCITY);
        extender.configMotionAcceleration(Constants.GrabberConstants.EXTENDER_MOTION_ACCELERATION);
    }


    /**
     * rotates the arm at a speed
     * @param armSpeed speed in Percent Output
     */
    public double getArmPosition() {
        return arm.getSelectedSensorPosition();
    }
    public double getGrabberPosition() {
        return grabber.getSelectedSensorPosition();
    }
    public double getExtenderPosition() {
        return extender.getSelectedSensorPosition();
    }
    public double distanceToArmPosition(double position) {
        return Math.abs(position - getArmPosition());
    }
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
        grabber.set(ControlMode.PercentOutput, grabberSpeed);  
    }


    /**
     * grabber opening-closing at a position
     * @param grabberPosition position it needs to open
     */
    public void grabberPos(double grabberPosition) {
        if(grabberPosition >= Constants.GrabberConstants.GRABBER_MAX_EXTEND || grabberPosition < Constants.GrabberConstants.GRABBER_MIN_EXTEND) {
            grabber.set(ControlMode.PercentOutput, 0);
        }
        else {
            grabber.set(ControlMode.MotionMagic, grabberPosition);
        }
    }

    public void grabberCurrent(double current) {
        if(Math.abs(current) > 5) {
            grabber.set(ControlMode.PercentOutput, 0);
        }
        else {
            grabber.set(ControlMode.Current, current);
        }
    }


    /**
     * arm extender extends at a speed
     * @param extendSpeed speed in Percent Output
     */
    public void extend(double extendSpeed) {
        if(extendSpeed > 0 && extender.getSelectedSensorPosition() >= Constants.GrabberConstants.EXTENDER_MAX_EXTEND) {
            extender.set(ControlMode.PercentOutput, 0);
        }
        else if (extendSpeed < 0 && extender.getSelectedSensorPosition() <= Constants.GrabberConstants.EXTENDER_MIN_EXTEND) {
            extender.set(ControlMode.PercentOutput, 0);
        }
        else {
            extender.set(ControlMode.PercentOutput, extendSpeed);
        }
    }


    /**
     * arm extender extend at a position 
     * @param extendPos position it needs to extend to
     */    
    public void extendPos(double extendPos) {
        if(extendPos >= extender.getSelectedSensorPosition() && extender.getSelectedSensorPosition() >= Constants.GrabberConstants.EXTENDER_MAX_EXTEND) {
            extender.set(ControlMode.PercentOutput, 0);
        }
        else if(extendPos <= extender.getSelectedSensorPosition() && extender.getSelectedSensorPosition() <= Constants.GrabberConstants.EXTENDER_MIN_EXTEND) {
            extender.set(ControlMode.PercentOutput, 0);
        }
        else {
            extender.set(ControlMode.MotionMagic, extendPos);
        }
    }

    
    /**
     * all values that need to be updated periodically
     * and smartdashboard display
     */
    public void logging() {
        SmartDashboard.putNumber("Arm Encoder", arm.getSelectedSensorPosition());
        SmartDashboard.putNumber("Arm Extendor Encoder", extender.getSelectedSensorPosition());
        SmartDashboard.putNumber("grabber position", grabber.getSelectedSensorPosition());
    }
}