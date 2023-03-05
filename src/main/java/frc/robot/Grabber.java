package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber {
    private TalonSRX arm;
    private TalonSRX grabber;

    public Grabber() {

        /* Arm Setup */
        arm = new TalonSRX(Constants.GrabberConstants.arm_id);

        arm.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.GrabberConstants.PID_IDX, Constants.TIMEOUT_MS);
        arm.setSelectedSensorPosition(0, Constants.GrabberConstants.PID_IDX, Constants.TIMEOUT_MS);

        arm.setInverted(true);
        arm.setNeutralMode(NeutralMode.Brake);

        /* Grabber Setup */
        grabber = new TalonSRX(Constants.GrabberConstants.grabber_id);

        grabber.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.GrabberConstants.PID_IDX, Constants.TIMEOUT_MS);
        grabber.setSelectedSensorPosition(0, Constants.GrabberConstants.PID_IDX, Constants.TIMEOUT_MS);

        grabber.setInverted(true);
        grabber.setNeutralMode(NeutralMode.Brake);

       
    }

    public void ArmRot(double armSpeed) {
        if(armSpeed > 0 && arm.getSelectedSensorPosition() >= Constants.GrabberConstants.MAX_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else if (armSpeed < 0 && arm.getSelectedSensorPosition() <= Constants.GrabberConstants.MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.PercentOutput, armSpeed);
        }
    }

    public void magicElevator(double position) {
        if(position >= Constants.GrabberConstants.MAX_EXTEND || position < Constants.GrabberConstants.MIN_EXTEND) {
            arm.set(ControlMode.PercentOutput, 0);
        }
        else {
            arm.set(ControlMode.MotionMagic, position);
        }
    }

    public void grabberRun(double grabberSpeed) {
        grabber.set(ControlMode.PercentOutput, grabberSpeed);
    }

    public void logging() {
        SmartDashboard.putNumber("Arm Encoder", arm.getSelectedSensorPosition());
    }
}