package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
    private TalonSRX linearMotor;
    private VictorSPX frontRoller;
    private VictorSPX backRoller;
    private VictorSPX bottomConveyer;

    public Intake() {
        /* Linear Configs */
        linearMotor = new TalonSRX(Constants.IntakeConstants.sliderMotorID);
        bottomConveyer = new VictorSPX(Constants.IntakeConstants.bottomConveyerID);

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
        frontRoller = new VictorSPX(Constants.IntakeConstants.frontRollerID);
        backRoller = new VictorSPX(Constants.IntakeConstants.backRollerID);

        frontRoller.setInverted(false);
        backRoller.setInverted(true);
        frontRoller.setNeutralMode(NeutralMode.Brake);
        backRoller.setNeutralMode(NeutralMode.Brake);
        bottomConveyer.setInverted(true);
        bottomConveyer.setNeutralMode(NeutralMode.Brake);

    }

    
    /**
     * controls intake extend and retract
     * @param slidingSpeed speed in Percent Output
     */
    public void ActuateIntake(double slidingSpeed) {
        linearMotor.set(ControlMode.PercentOutput, slidingSpeed);
    }

    public void MagicIntake(double extendPosition) {
        if(extendPosition >= 22600 || extendPosition < -200) {
            linearMotor.set(ControlMode.PercentOutput, 0);
        }
        else {
            linearMotor.set(ControlMode.MotionMagic, extendPosition);
        }
    }


    /**
     * runs front roller
     * @param frontRollerSpeed speed in Percent Output
     */
    public void RunFrontRoller(double frontRollerSpeed) {
        frontRoller.set(ControlMode.PercentOutput, frontRollerSpeed);
    }


    /**
     * runs rollers closested to the bumper to bring the game pieces into the robot
     * @param backRollerSpeed speed in Percent Output
     */
    public void RunBackRoller(double backRollerSpeed) {
        backRoller.set(ControlMode.PercentOutput, backRollerSpeed);
    }


    /**
     * runs convyer that brings game pieces to the spindexer
     * @param conveyerSpeed speed in Percent Output
     */
    public void RunConveyer(double conveyerSpeed) {
        bottomConveyer.set(ControlMode.PercentOutput, conveyerSpeed);
    }
    
    public void logging() {
        SmartDashboard.putNumber("Intake Encoder", linearMotor.getSelectedSensorPosition());
    }
}
