package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber {
    private TalonSRX arm;
    private Solenoid grabberSolenoid;
    private CANSparkMax grabberMotor;

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
        grabberMotor = new CANSparkMax(33, MotorType.kBrushless);

        grabberMotor.setIdleMode(IdleMode.kBrake);
        grabberMotor.setInverted(true);
        grabberSolenoid = new Solenoid(Constants.IntakeConstants.PNUEMATIC_HUB_ID, PneumaticsModuleType.REVPH,  1);
    }

    public void runGrabber(double speed) {
        grabberMotor.set(speed);
    }

    /**
     * rotates the arm at a speed
     * @param armSpeed speed in Percent Output
     */
    public double getArmPosition() {
        return arm.getSelectedSensorPosition();
    }
    public double distanceToArmPosition(double position) {
        return Math.abs(position - getArmPosition());
    }
    public void ArmRot(double armSpeed) {
        arm.set(ControlMode.PercentOutput, armSpeed);
    }

    public void openGrabber() {
        grabberSolenoid.set(true);
    }

    public void closeGrabber(){
        grabberSolenoid.set(false);
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

    public void zeroArm() {
        arm.setSelectedSensorPosition(0);
    }

    
    /**
     * all values that need to be updated periodically
     * and smartdashboard display
     */
    public void logging() {
        SmartDashboard.putNumber("Arm Encoder", arm.getSelectedSensorPosition());
    }
}