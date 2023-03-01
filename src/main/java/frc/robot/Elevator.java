package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
    private TalonSRX master;
    private TalonSRX slave;
    private DigitalInput elevatorLimit;

    private boolean isElevatorDown;



    public Elevator(){
        master = new TalonSRX(Constants.ElevatorConstants.master);
        slave = new TalonSRX(Constants.ElevatorConstants.slave);
        slave.set(ControlMode.Follower, master.getDeviceID());
        elevatorLimit = new DigitalInput(Constants.ElevatorConstants.ELEVATOR_LIMIT_ID);


        master.configFactoryDefault();
        slave.configFactoryDefault();

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        master.setSelectedSensorPosition(0, 0, 0);
       
        master.setNeutralMode(NeutralMode.Brake);
        slave.setNeutralMode(NeutralMode.Brake);

        master.setSensorPhase(false);
        
        master.setInverted(true);
        slave.setInverted(false);
        
        /*master motor pid */
        master.config_kF(0, Constants.ElevatorConstants.kF);
        master.config_kP(0, Constants.ElevatorConstants.kP);
        master.config_kI(0, Constants.ElevatorConstants.kI);
        master.config_kD(0, Constants.ElevatorConstants.kD);
        currentLimit(master);

        /*slave motor pid */
        slave.config_kF(0, Constants.ElevatorConstants.kF);
        slave.config_kP(0, Constants.ElevatorConstants.kP);
        slave.config_kI(0, Constants.ElevatorConstants.kI);
        slave.config_kD(0, Constants.ElevatorConstants.kD);
        currentLimit(slave);

    }

    public void currentLimit(final TalonSRX talon) {
        talon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 30,0));
    }

    public void spin(double speed) {
        master.set(ControlMode.PercentOutput, speed);
    }

    /**
     * 
     * @param position in inches
     */
    public void magicElevator(double position) {
        double ticks = position * Constants.ElevatorConstants.inchesToTicks;
        if (ticks <= 0 && master.getSelectedSensorPosition() <= 0 && !elevatorLimit.get()){

            master.setSelectedSensorPosition(0, 0, 0);
            master.set(ControlMode.PercentOutput, 0);

            isElevatorDown = true;
        }
        else if (ticks <= 0 && master.getSelectedSensorPosition() <= 0 && elevatorLimit.get()) {

            master.set(ControlMode.PercentOutput, Constants.ElevatorConstants.elevatorSpeed);
            isElevatorDown = false;
        }
        else {
            master.set(ControlMode.MotionMagic, ticks);
            isElevatorDown = false;
        }
    }

    public void logger() {
        SmartDashboard.putNumber("elevatorEncoder", master.getSelectedSensorPosition(0));
        SmartDashboard.putBoolean("Elevator limit", isElevatorDown);
    }
}
