package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;

public class Intake {
    private TalonSRX linearMotor;
    private VictorSPX frontRoller;
    private VictorSPX backRoller;
    private VictorSPX bottomConveyer;

    public Intake() {
        /* Linear Configs */
        linearMotor = new TalonSRX(Constants.IntakeConstants.sliderMotorID);
        bottomConveyer = new VictorSPX(Constants.IntakeConstants.bottomConveyerID);

        linearMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.IntakeConstants.INTAKE_PID_IDX, Constants.TIMEOUT_MS);
        linearMotor.setSelectedSensorPosition(0, Constants.IntakeConstants.INTAKE_PID_IDX, Constants.TIMEOUT_MS);

        linearMotor.setInverted(true);
        linearMotor.setNeutralMode(NeutralMode.Brake);

        linearMotor.config_kF(0, Constants.IntakeConstants.linearkF);
        linearMotor.config_kP(0, Constants.IntakeConstants.linearkP);
        linearMotor.config_kI(0, Constants.IntakeConstants.linearkI);
        linearMotor.config_kD(0, Constants.IntakeConstants.linearkD);

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
    public void ActuateIntake(double slidingSpeed) {
        linearMotor.set(ControlMode.PercentOutput, slidingSpeed);
    }

    public void RunFrontRoller(double frontRollerSpeed) {
        frontRoller.set(ControlMode.PercentOutput, frontRollerSpeed);
    }

    public void RunBackRoller(double backRollerSpeed) {
        backRoller.set(ControlMode.PercentOutput, backRollerSpeed);
    }

    public void RunBottomConveyer(double bottomConveyerSpeed) {
        bottomConveyer.set(ControlMode.PercentOutput, bottomConveyerSpeed);
    }
}
