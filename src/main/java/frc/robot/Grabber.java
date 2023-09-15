package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.util.CTREConfigs;

public class Grabber {
    private TalonSRX arm;
    private Solenoid grabberSolenoid;
    private CANSparkMax grabberMotor;
    private WPI_CANCoder absoluteArm;

    private DigitalInput limitSwitch;
    private DigitalInput beamBreakOutside;
    private DigitalInput beamBreakInside;

    private PIDController controller;
    private TrapezoidProfile.Constraints constraints;
    private ArmFeedforward feedForward;

    public Grabber() {
        controller = new PIDController(Constants.GrabberConstants.armkP, 
                                       Constants.GrabberConstants.armkI,
                                       Constants.GrabberConstants.armkD);
        feedForward = new ArmFeedforward(Constants.GrabberConstants.ARM_KS,
                                         Constants.GrabberConstants.ARM_KG,
                                         Constants.GrabberConstants.ARM_KV);

        absoluteArm = new WPI_CANCoder(Constants.GrabberConstants.absolute_arm_id);
        absoluteArm.configFactoryDefault();
        absoluteArm.configAllSettings(CTREConfigs.armCanCoderConfig());

        /* Arm Setup */
        arm = new TalonSRX(Constants.GrabberConstants.arm_id);
        arm.configFactoryDefault();
        arm.setInverted(true);
        arm.setNeutralMode(NeutralMode.Brake);

        arm.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Constants.GrabberConstants.ARM_PID_IDX, Constants.TIMEOUT_MS);

        arm.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        arm.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        arm.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        arm.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        arm.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		arm.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);

        arm.selectProfileSlot(Constants.GrabberConstants.ARM_SLOT_IDX, Constants.GrabberConstants.ARM_PID_IDX);

        arm.configMotionCruiseVelocity(Constants.GrabberConstants.ARM_MOTION_CRUISE_VELOCITY);
        arm.configMotionAcceleration(Constants.GrabberConstants.ARM_MOTION_ACCELERATION);

        /* Grabber Setup */
        grabberMotor = new CANSparkMax(33, MotorType.kBrushless);

        grabberMotor.setIdleMode(IdleMode.kCoast);
        grabberMotor.setInverted(true);
        grabberSolenoid = new Solenoid(Constants.IntakeConstants.PNUEMATIC_HUB_ID, PneumaticsModuleType.REVPH,  1);

        limitSwitch = new DigitalInput(Constants.GrabberConstants.LIMIT_SWITCH_ID);
        beamBreakOutside = new DigitalInput(Constants.GrabberConstants.BEAM_BREAK_OUTSIDE_ID);
        beamBreakInside = new DigitalInput(Constants.GrabberConstants.BEAM_BREAK_INSIDE_ID);
    }

    public void runGrabber(double grabberSpeed) {
        if(grabberSpeed > 0 && !getBeamBreakOutside()){
            Timer.delay(0.2);
            grabberMotor.set(0);
        }     

        else{
            grabberMotor.set(grabberSpeed);
        }
    }

    public void runGrabberPassthrough(double speed) {
        if(speed > 0 && !limitSwitch.get()) {
            grabberMotor.set(0);
        }
        else{
            grabberMotor.set(speed);
        }
    }

    /*public void runGrabber(double speed) {
        if(speed > 0 && !limitSwitch.get()){
            grabberMotor.set(0);
        }
        else if(speed > 0 && !beamBreakGrabberOutside.get()) {
            Timer.delay(0.25);
            grabberMotor.set(0);
        }
        else {
            grabberMotor.set(speed);
        }
    }*/


    public Rotation2d getCanCoder(){
        return Rotation2d.fromDegrees(absoluteArm.getAbsolutePosition());
    }

    public boolean getLimitSwitch() {
        return limitSwitch.get();
    }

    public boolean getBeamBreakOutside() {
        return beamBreakOutside.get();
    }

    public boolean getBeamBreakInside() {
        return beamBreakInside.get();
    }

    /**
     * rotates the arm at a speed
     * @param armSpeed speed in Percent Output
     */
    public double getArmPosition() {
        return arm.getSelectedSensorPosition();
    }

    public double getAbsoluteArmPosition() {
        return absoluteArm.getAbsolutePosition();
    }

    public double distanceToArmPosition(double position) {
        return Math.abs(position - getArmPosition());
    }
    
    public void ArmRot(double armSpeed) {
        arm.set(ControlMode.PercentOutput, armSpeed);
    }

    public void openGrabber() {
        grabberSolenoid.set(false);
    }

    public void closeGrabber(){
        grabberSolenoid.set(true);
    }

    /**
     * magicaly rotates the arm to position
     * @param rotPosition
     */
    public void magicArm(double rotPosition) {
        ///controller.setGoal(Math.toRadians(rotPosition));
        //double speed =  controller.calculate(Math.toRadians(absoluteArm.getAbsolutePosition()));
        //speed -= feedForward.calculate(Math.toRadians(absoluteArm.getAbsolutePosition()) - Math.PI/2, 0);
        double feed = feedForward.calculate(Math.toRadians(rotPosition)-Math.toRadians(92), 0);
        double pid = controller.calculate(absoluteArm.getAbsolutePosition(), rotPosition);
        double speed = feed + pid;
        if(pid > 0) {
            pid = Math.min(pid, 0.5);
        }
        else {
            pid = Math.max(pid, -0.5);
        }
        arm.set(ControlMode.PercentOutput, pid);

        DriverStation.reportError(Double.toString(pid), false);
    }

    public void zeroArm() {
        absoluteArm.setPosition(0);
    }

    
    /**
     * all values that need to be updated periodically
     * and smartdashboard display
     */
    public void logging() {
        SmartDashboard.putNumber("Arm Encoder", arm.getSelectedSensorPosition());
        SmartDashboard.putBoolean("Beam Break Outside", getBeamBreakOutside());
        SmartDashboard.putBoolean("Beam Break Insikde", getBeamBreakInside());
        SmartDashboard.putBoolean("Grabber Limit Switch", getLimitSwitch());
        SmartDashboard.putNumber("GrabberCC", getAbsoluteArmPosition());
    }
}