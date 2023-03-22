package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.lib.util.COTSFalconSwerveConstants;
import frc.lib.util.SwerveModuleConstants;

public final class Constants {
    public static final double stickDeadband = 0.1;
    public static final int DRIVER_JOYSTICK_PORT = 0;
    public static final int COPILOT_JOYSTICK_PORT = 1;

    public static final int TIMEOUT_MS = 60;

    public static final class Swerve {
        public static final boolean invertGyro = true; // Always ensure Gyro is CCW+ CW-

        public static final COTSFalconSwerveConstants chosenModule =  //TODO: This must be tuned to specific robot
            COTSFalconSwerveConstants.SDSMK4i(COTSFalconSwerveConstants.driveGearRatios.SDSMK4_L2);

        /* Drivetrain Constants */
        public static final double trackWidth = Units.inchesToMeters(23); //TODO: This must be tuned to specific robot
        public static final double wheelBase = Units.inchesToMeters(23); //TODO: This must be tuned to specific robot
        public static final double wheelCircumference = chosenModule.wheelCircumference;

        /* Swerve Kinematics 
         * No need to ever change this unless you are not doing a traditional rectangular/square 4 module swerve */
         public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

        /* Module Gear Ratios */
        public static final double driveGearRatio = chosenModule.driveGearRatio;
        public static final double angleGearRatio = chosenModule.angleGearRatio;

        /* Motor Inverts */
        public static final boolean angleMotorInvert = chosenModule.angleMotorInvert;
        public static final boolean driveMotorInvert = chosenModule.driveMotorInvert;

        /* Angle Encoder Invert */
        public static final boolean canCoderInvert = chosenModule.canCoderInvert;

        /* Swerve Current Limiting */
        public static final int angleContinuousCurrentLimit = 25;
        public static final int anglePeakCurrentLimit = 40;
        public static final double anglePeakCurrentDuration = 0.1;
        public static final boolean angleEnableCurrentLimit = true;

        public static final int driveContinuousCurrentLimit = 35;
        public static final int drivePeakCurrentLimit = 60;
        public static final double drivePeakCurrentDuration = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        public static final double openLoopRamp = 0.5;
        public static final double closedLoopRamp = 0.0;

        /* Angle Motor PID Values */
        public static final double angleKP = chosenModule.angleKP;
        public static final double angleKI = chosenModule.angleKI;
        public static final double angleKD = chosenModule.angleKD;
        public static final double angleKF = chosenModule.angleKF;

        /* Drive Motor PID Values */
        public static final double driveKF = 0.035;
        public static final double driveKP = 0.03; //TODO: This must be tuned to specific robot was 0.2214
        public static final double driveKI = 0;
        public static final double driveKD = 0;

        /* Drive Motor Characterization Values 
         * Divide SYSID values by 12 to convert from volts to percent output for CTRE */
        public static final double driveKS = (0.16215 / 12); //TODO: This must be tuned to specific robot
        public static final double driveKV = (0.73438 / 12);
        public static final double driveKA = (0.11115 / 12);

        /* Swerve Profiling Values */
        /** Meters per Second */
        public static final double maxSpeed = 10; //TODO: This must be tuned to specific robot
        public static final double creepSpeed = 0.3;
        /** Radians per Second */
        public static final double maxAngularVelocity = 10; //TODO: This must be tuned to specific robot

        /* Neutral Modes */
        public static final NeutralMode angleNeutralMode = NeutralMode.Coast;
        public static final NeutralMode driveNeutralMode = NeutralMode.Brake;

        /*Balance Multiplier */
        public static final double balanceMultiplier = 0.019;

        /* Module Specific Constants */
        /* Front Left Module - Module 0 */
        public static final class Mod0 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 0;
            public static final int angleMotorID = 1;
            public static final int canCoderID = 0;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(150.59); //Practice: 250.75    Final: 150.59
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Front Right Module - Module 1 */
        public static final class Mod1 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 2;
            public static final int angleMotorID = 3;
            public static final int canCoderID = 1;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(16.52); //Practice: 136.40    Final: 16.52
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
        
        /* Back Left Module - Module 2 */
        public static final class Mod2 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 4;
            public static final int angleMotorID = 5;
            public static final int canCoderID = 2;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(269.03); //Practice: 111.00    Final: 269.03
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }

        /* Back Right Module - Module 3 */
        public static final class Mod3 { //TODO: This must be tuned to specific robot
            public static final int driveMotorID = 6;
            public static final int angleMotorID = 7;
            public static final int canCoderID = 3;
            public static final Rotation2d angleOffset = Rotation2d.fromDegrees(28.82); //Practice: 82.61    Final: 28.82
            public static final SwerveModuleConstants constants = 
                new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
        }
    }

    public static final class ElevatorConstants {
        public static int master = 8;
        public static int slave = 9;

        public static double elevatorSpeed = 0.3;
        public static double ticksToInches = 0.000811297;
        public static double inchesToTicks = 1232.5937;

        public static final int ELEVATOR_LIMIT_ID = 0;
        public static final double MAX_EXTEND = 45000;

        public static final double ELEVATOR_HIGH_EXTEND = 44000;
        public static final double ELEVATOR_FEEDER_EXTEND = 32500;
        public static final double ELEVATOR_MID_EXTEND = 21000;
        public static final double ELEVATOR_LOW_EXTEND = 5000;
        public static final double ELEVATOR_BOTTTOM_EXTEND = 0;

        /*Elevator Pid */
        public static final int PID_IDX = 0;
        public static final int SLOT_IDX = 0;

        public static final double MOTION_CRUISE_VELOCITY = 20000;
        public static final double MOTION_ACCELERATION = 7000;

        public static double kF = 0.6;
        public static double kP = 0.4;
        public static double kI = 0;
        public static double kD = 0.04;
    }

    public static final class GrabberConstants{
        public static int arm_id = 10;
        public static int grabber_id = 11;
        public static int extender_id = 16;

        /* Extention Positions */
        public static final double ARM_MAX_EXTEND = 5100;
        public static final double ARM_MIN_EXTEND = 0;

        public static final double EXTENDER_MAX_EXTEND = 3500;
        public static final double EXTENDER_MIN_EXTEND = 0;

        public static final double GRABBER_MAX_EXTEND = 10000;
        public static final double GRABBER_MIN_EXTEND = 0;

        public static final double GRABBER_CLOSED_CONE = 2200;
        public static final double GRABBER_CLOSED_CUBE = 1700;
        public static final double GRABBER_OPEN = 0;

        public static final double EXTENDER_RETRACTED_POSITION = 3100;
        public static final double EXTENDER_EXTENDED_POSITION = 790;

        /* Arm/Grabber Speeds */
        public static final double Up_Arm_Speed = 0.3;
        public static final double Down_Arm_Speed = -0.3;
        public static final double Arm_Rot_Speed = 0.2;
        public static final double Grabber_Speed = 0.3;


        /*Pid */
        public static final int ARM_PID_IDX = 0;
        public static final int ARM_SLOT_IDX = 0;

        public static final int GRABBER_PID_IDX = 0;
        public static final int GRABBER_SLOT_IDX = 0;

        public static final int EXTENDER_PID_IDX = 0;
        public static final int EXTENDER_SLOT_IDX = 0;

        public static final double ARM_MOTION_CRUISE_VELOCITY = 4000;
        public static final double ARM_MOTION_ACCELERATION = 2000;

        public static final double GRABBER_MOTION_CRUISE_VELOCITY = 3000;
        public static final double GRABBER_MOTION_ACCELERATION = 1500;

        public static final double EXTENDER_MOTION_CRUISE_VELOCITY = 2000;
        public static final double EXTENDER_MOTION_ACCELERATION = 1000;

        public static final double ARM_HIGH_EXTEND = 5000;
        public static final double ARM_FEEDER_EXTEND = 4000;
        public static final double ARM_LOW_EXTEND = 2000;
        public static final double ARM_STOWED_EXTEND = 0;


        public static double armkF = 0.6;
        public static double armkP = 0.4;
        public static double armkI = 0;
        public static double armkD = 0.04;

        public static double extenderkF = 0.2;
        public static double extenderkP = 0.3;
        public static double extenderkI = 0.0003;
        public static double extenderkD = 0;

        public static double grabberkF = 0.3;
        public static double grabberkP = 0.45;
        public static double grabberkI = 0;
        public static double grabberkD = 0.04;
    }

    public static final class LimelightConstants {
        public static double maxTanslationalSpeed = 0.25;
        public static double maxSkewSpeed = 0.25;
    }

    public static final class IntakeConstants {
        public static final int sliderMotorID = 12;
        public static final int intakeRollerID = 14;
        public static final int conveyerID = 13;

        public static final int backLimitSwitchID = 1;

        public static final double frontRollerSpeed = 0.7;
        public static final double backRollerSpeed = 0.7;
        public static final double linearMotorSpeed = 0.2;
        public static final double bottomConveyerSpeed = 0.3;

        public static final int SLOT_IDX = 0;
        public static final int PID_IDX = 0;

        public static double linearkF = 0.7;
        public static double linearkP = 0.1;
        public static double linearkI = 0.00003;
        public static double linearkD = 0.0;

        public static final double MOTION_CRUISE_VELOCITY = 30000;
        public static final double MOTION_ACCELERATION = 20000;

        public static final double MAX_INTAKE_EXTEND = 1000;
        public static final double MAX_INTAKE_RETRACT = -100;
    }

    public static final class SpindexerConstants {
        public static final int spindexerMotorID = 16;
    }
    public static final class AutoConstants { //TODO: The below constants are used in the example auto, and must be tuned to specific robot
        public static final double kMaxSpeedMetersPerSecond = 3;
        public static final double kMaxAccelerationMetersPerSecondSquared = 4;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    
        public static final double kPXController = 0.3;
        public static final double kPYController = 0.3;
        public static final double kPThetaController = 6;
    
        /* Constraint for the motion profilied robot angle controller */
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }
}
