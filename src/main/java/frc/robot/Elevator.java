package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
    private TalonSRX master;
    private TalonSRX slave;

    DigitalInput limitSwitch;

    public Elevator(){
        master = new TalonSRX(Constants.ElevatorConstants.master);
        slave = new TalonSRX(Constants.ElevatorConstants.slave);
        slave.set(ControlMode.Follower, master.getDeviceID());

        master.configFactoryDefault();
        slave.configFactoryDefault();

        currentLimit(master);
        currentLimit(slave);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.ElevatorConstants.PID_IDX, Constants.TIMEOUT_MS);
        master.setSelectedSensorPosition(0, Constants.ElevatorConstants.PID_IDX, Constants.TIMEOUT_MS);

        master.configNominalOutputForward(0, Constants.TIMEOUT_MS);
        master.configNominalOutputReverse(0, Constants.TIMEOUT_MS);
        master.configPeakOutputForward(1, Constants.TIMEOUT_MS);
        master.configPeakOutputReverse(-1, Constants.TIMEOUT_MS);

        master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.TIMEOUT_MS);
		master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.TIMEOUT_MS);

        master.selectProfileSlot(Constants.ElevatorConstants.SLOT_IDX, Constants.ElevatorConstants.PID_IDX);
        master.config_kF(Constants.ElevatorConstants.PID_IDX, Constants.ElevatorConstants.kF, Constants.TIMEOUT_MS);
        master.config_kP(Constants.ElevatorConstants.PID_IDX, Constants.ElevatorConstants.kP, Constants.TIMEOUT_MS);
        master.config_kI(Constants.ElevatorConstants.PID_IDX, Constants.ElevatorConstants.kI, Constants.TIMEOUT_MS);
        master.config_kD(Constants.ElevatorConstants.PID_IDX, Constants.ElevatorConstants.kD, Constants.TIMEOUT_MS);

        master.configMotionCruiseVelocity(Constants.ElevatorConstants.MOTION_CRUISE_VELOCITY);
        master.configMotionAcceleration(Constants.ElevatorConstants.MOTION_ACCELERATION);
       
        master.setNeutralMode(NeutralMode.Brake);
        slave.setNeutralMode(NeutralMode.Brake);

        master.setSensorPhase(false);
        
        master.setInverted(true);
        slave.setInverted(false);

        limitSwitch = new DigitalInput(Constants.ElevatorConstants.ELEVATOR_LIMIT_ID);
    }
    /**
     * limits current the talon can draw from the PDH
     * @param talon
     */
    private void currentLimit(final TalonSRX talon) {
        talon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 30,0));
    }


    /**
     * extend-retract the elevator
     * @param speed
     */
    /**
     * returns a position of the elevator
     */
    public double getPosition() {
        return master.getSelectedSensorPosition();
    }
    public double distanceToPosition(double position) {
        return Math.abs(position - getPosition());
    }
    public void elevatorExtend(double speed) {
        if(speed > 0 && master.getSelectedSensorPosition() >= Constants.ElevatorConstants.MAX_EXTEND) {
            master.set(ControlMode.PercentOutput, 0);
        }
        else if(speed < 0 && master.getSelectedSensorPosition() <= 500 && !limitSwitch.get()) {
            master.setSelectedSensorPosition(0);
            master.set(ControlMode.PercentOutput, 0);
        }
        else {
            master.set(ControlMode.PercentOutput, speed);
        }
    }


    /**
     * magicly put the elevator ina set position
     * @param position in inches
     */
    public void magicElevator(double position) {
        if(position > Constants.ElevatorConstants.MAX_EXTEND || position < 0) {
            master.set(ControlMode.PercentOutput, 0);
        }

        else if(position <= 0 && master.getSelectedSensorPosition() <= 500 && !limitSwitch.get()) {
            master.setSelectedSensorPosition(0);
            master.set(ControlMode.PercentOutput, 0);
        }
        else {
            master.set(ControlMode.MotionMagic, position);
        }
    }

    public void killElevator() {
        master.DestroyObject();
        slave.DestroyObject();
    }


    /**
     * all values that need to be updated periodically
     * and smartdashboard display
     */
    public void logger() {
        SmartDashboard.putNumber("elevatorEncoder", master.getSelectedSensorPosition(0));
        SmartDashboard.putBoolean("Elevator limit", limitSwitch.get());
    }
}
