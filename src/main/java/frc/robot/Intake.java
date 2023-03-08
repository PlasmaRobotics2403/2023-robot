package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Intake {
    private TalonSRX linearMotor;

    public Intake() {
        linearMotor = new TalonSRX(Constants.IntakeConstants.sliderMotorId);

        linearMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.IntakeConstants.INTAKE_PID_IDX, Constants.TIMEOUT_MS);
        linearMotor.setSelectedSensorPosition(0, Constants.IntakeConstants.INTAKE_PID_IDX, Constants.TIMEOUT_MS);

        linearMotor.setInverted(true);
        linearMotor.setNeutralMode(NeutralMode.Brake);

        linearMotor.config_kF(0, Constants.IntakeConstants.linearkF);
        linearMotor.config_kP(0, Constants.IntakeConstants.linearkP);
        linearMotor.config_kI(0, Constants.IntakeConstants.linearkI);
        linearMotor.config_kD(0, Constants.IntakeConstants.linearkD);
    }
    public void ActuateIntake(double slidingSpeed) {
        linearMotor.set(ControlMode.PercentOutput, slidingSpeed);
    }
}
