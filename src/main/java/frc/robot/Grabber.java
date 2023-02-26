package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber {
    private TalonSRX arm;

    public Grabber() {
        arm = new TalonSRX(Constants.GrabberConstants.arm_id);

        arm.configFactoryDefault();
        arm.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        arm.setNeutralMode(NeutralMode.Brake);

        arm.setSensorPhase(false);
        arm.setInverted(true);

        arm.config_kF(0, Constants.GrabberConstants.kF);
        arm.config_kP(0, Constants.GrabberConstants.kP);
        arm.config_kI(0, Constants.GrabberConstants.kI);
        arm.config_kD(0, Constants.GrabberConstants.kD);
    }

    public void ArmRot(double armSpeed) {
        arm.set(ControlMode.PercentOutput, armSpeed);
    }

    public void logging() {
        SmartDashboard.putNumber("Arm Encoder", arm.getSelectedSensorPosition());
    }
}