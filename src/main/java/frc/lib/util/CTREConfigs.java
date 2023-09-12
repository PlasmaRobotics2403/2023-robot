package frc.lib.util;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;
import frc.robot.Constants;

public final class CTREConfigs {
    public static TalonFXConfiguration swerveAngleFXConfig() {
        TalonFXConfiguration config = new TalonFXConfiguration();
        
        SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
            Constants.Swerve.angleEnableCurrentLimit, 
            Constants.Swerve.angleContinuousCurrentLimit, 
            Constants.Swerve.anglePeakCurrentLimit, 
            Constants.Swerve.anglePeakCurrentDuration);

        config.slot0.kP = Constants.Swerve.angleKP;
        config.slot0.kI = Constants.Swerve.angleKI;
        config.slot0.kD = Constants.Swerve.angleKD;
        config.slot0.kF = Constants.Swerve.angleKF;
        config.supplyCurrLimit = angleSupplyLimit;

        return config;
    }

    public static TalonFXConfiguration swerveDriveFXConfig() {
        TalonFXConfiguration config = new TalonFXConfiguration();
        
        SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
            Constants.Swerve.driveEnableCurrentLimit, 
            Constants.Swerve.driveContinuousCurrentLimit, 
            Constants.Swerve.drivePeakCurrentLimit, 
            Constants.Swerve.drivePeakCurrentDuration);

        config.slot0.kP = Constants.Swerve.driveKP;
        config.slot0.kI = Constants.Swerve.driveKI;
        config.slot0.kD = Constants.Swerve.driveKD;
        config.slot0.kF = Constants.Swerve.driveKF;        
        config.supplyCurrLimit = driveSupplyLimit;
        config.openloopRamp = Constants.Swerve.openLoopRamp;
        config.closedloopRamp = Constants.Swerve.closedLoopRamp;
        
        return config;
    }

    public static CANCoderConfiguration swerveCanCoderConfig() {
        CANCoderConfiguration config = new CANCoderConfiguration();

        config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        config.sensorDirection = Constants.Swerve.canCoderInvert;
        config.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        config.sensorTimeBase = SensorTimeBase.PerSecond;
        return config;
    }

    public static CANCoderConfiguration armCanCoderConfig() {
        CANCoderConfiguration config = new CANCoderConfiguration();

        config.absoluteSensorRange = AbsoluteSensorRange.Signed_PlusMinus180;
        config.sensorDirection = Constants.GrabberConstants.cancoder_invert;
        config.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        config.sensorTimeBase = SensorTimeBase.PerSecond;
        config.magnetOffsetDegrees = Constants.GrabberConstants.grabberCCoffset;

        return config;
    }
}