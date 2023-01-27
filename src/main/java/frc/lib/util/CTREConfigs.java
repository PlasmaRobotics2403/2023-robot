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
            Constants.SwerveConstants.angleEnableCurrentLimit, 
            Constants.SwerveConstants.angleContinuousCurrentLimit, 
            Constants.SwerveConstants.anglePeakCurrentLimit, 
            Constants.SwerveConstants.anglePeakCurrentDuration);

        config.slot0.kP = Constants.SwerveConstants.angleKP;
        config.slot0.kI = Constants.SwerveConstants.angleKI;
        config.slot0.kD = Constants.SwerveConstants.angleKD;
        config.slot0.kF = Constants.SwerveConstants.angleKF;
        config.supplyCurrLimit = angleSupplyLimit;

        return config;
    }

    public static TalonFXConfiguration swerveDriveFXConfig() {
        TalonFXConfiguration config = new TalonFXConfiguration();
        
        SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
            Constants.SwerveConstants.driveEnableCurrentLimit, 
            Constants.SwerveConstants.driveContinuousCurrentLimit, 
            Constants.SwerveConstants.drivePeakCurrentLimit, 
            Constants.SwerveConstants.drivePeakCurrentDuration);

        config.slot0.kP = Constants.SwerveConstants.driveKP;
        config.slot0.kI = Constants.SwerveConstants.driveKI;
        config.slot0.kD = Constants.SwerveConstants.driveKD;
        config.slot0.kF = Constants.SwerveConstants.driveKF;        
        config.supplyCurrLimit = driveSupplyLimit;
        config.openloopRamp = Constants.SwerveConstants.openLoopRamp;
        config.closedloopRamp = Constants.SwerveConstants.closedLoopRamp;
        
        return config;
    }

    public static CANCoderConfiguration swerveCanCoderConfig() {
        CANCoderConfiguration config = new CANCoderConfiguration();

        config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        config.sensorDirection = Constants.SwerveConstants.canCoderInvert;
        config.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        config.sensorTimeBase = SensorTimeBase.PerSecond;
        return config;
}
    }

        /* Swerve Angle Motor Configurations */
        

        /* Swerve Drive Motor Configuration */
        
        
        /* Swerve CANCoder Configuration */
        