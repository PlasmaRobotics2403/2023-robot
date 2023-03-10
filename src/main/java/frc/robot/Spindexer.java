package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Spindexer {
    private VictorSPX spindexerMotor;

    public Spindexer() {
        spindexerMotor = new VictorSPX(Constants.SpindexerConstants.spindexerMotorID);

        spindexerMotor.setInverted(true);
        spindexerMotor.setNeutralMode(NeutralMode.Brake);
    }

    public void spin(double spinSpeed) {
        spindexerMotor.set(ControlMode.PercentOutput, spinSpeed);
    }
}
