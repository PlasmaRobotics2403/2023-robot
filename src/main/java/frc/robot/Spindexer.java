package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;

public class Spindexer {
    private VictorSPX spinnerMotor;
    private DigitalInput cwLimitSwitch;
    private DigitalInput ccwLimitSwitch;
    
    private String spinDirection;

    public Spindexer() {
        spinnerMotor = new VictorSPX(Constants.spindexerConstants.spinnerMotorID);
        cwLimitSwitch = new DigitalInput(Constants.spindexerConstants.cwLimitSwitchID);
        ccwLimitSwitch = new DigitalInput(Constants.spindexerConstants.ccwLimitSwitchID);

        spinDirection = "stop";
    }
    
    public void spin() {
        switch(spinDirection) {
            case "ccw":
                spinnerMotor.set(ControlMode.PercentOutput, -Constants.spindexerConstants.spinnerMotorSpeed);
                if (ccwLimitSwitch.get()) {
                    spinDirection = "cw";
                }
            break;

            case "cw":
                spinnerMotor.set(ControlMode.PercentOutput, Constants.spindexerConstants.spinnerMotorSpeed);
                if (cwLimitSwitch.get()) {
                    spinDirection = "stop";
                }
            break;

            case "stop":
                spinnerMotor.set(ControlMode.PercentOutput, 0);
            break;
        }
    }

    public void resetSpinState() {
        spinDirection = "ccw";
    }
    
    public void stopSpinner() {
        spinDirection = "stop";
    }
}
