package frc.robot;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Swerve extends SubsystemBase {
    private SwerveDriveOdometry swerveOdometry;
    private SwerveModule[] mSwerveMods;
    private AHRS navX;

    public Swerve() {
        navX = new AHRS(SerialPort.Port.kMXP);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                zeroHeading();
            } catch (Exception e) {
            }
        }).start();

        mSwerveMods = new SwerveModule[] {
            new SwerveModule(0, Constants.Swerve.Mod0.constants),
            new SwerveModule(1, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)
        };

        resetModulesToAbsolute();

        swerveOdometry = new SwerveDriveOdometry(Constants.Swerve.swerveKinematics, getYaw(), getModulePositions());
        drive(new Translation2d(0, 0), 0, true, true);
    }

    /**
     * sets brake mode for motors
     * @param drive set drive motor in brake mode
     * @param angle set angle motor in brake mode
     */
    public void setBrakeMode(boolean drive, boolean angle) {
        for(SwerveModule mod : mSwerveMods) {
            mod.setBrakeMode(drive, angle);
        }
    }

    /**
     * set default neutral modes for motors
     * @param drive set default (brake)
     * @param angle set default (coast)
     */
    public void defaultNeutralMode(boolean drive, boolean angle) {
        for(SwerveModule mod : mSwerveMods) {
            mod.defaultNeutralMode(drive, angle);
        }
    }

    /**
     * reset the gyro heading
     */
    public void zeroHeading() {
        navX.reset();
    }

    /**
     * returns the current heading of th gyro
     * @return heading in degrees
     */
    public double getHeading() {
        return Math.IEEEremainder(navX.getAngle(), 360);
    }

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());
    }

    public Pose2d getPose() {
        return swerveOdometry.getPoseMeters();
    }

    public void zeroGyro(){
        navX.zeroYaw();
    }

    public Rotation2d getYaw() {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - navX.getYaw()) : Rotation2d.fromDegrees(navX.getYaw());
    }

    public Rotation2d getPitch() {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - navX.getPitch()) : Rotation2d.fromDegrees(navX.getPitch());
    }

    public Rotation2d getRoll() {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - navX.getRoll()) : Rotation2d.fromDegrees(navX.getRoll());
    }

    public void resetModulesToAbsolute(){
        for(SwerveModule mod : mSwerveMods){
            mod.resetToAbsolute();
        }
    }
    
    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(getYaw(), getModulePositions(), pose);
    }

    public void balance() {
        double yVal = (getPitch().getDegrees() - 360) * Math.cos(getYaw().getRadians()) * Constants.Swerve.balanceMultiplier;
        double xVal = (getRoll().getDegrees() - 360) * Math.sin(getYaw().getRadians()) * Constants.Swerve.balanceMultiplier;

        double transVal = yVal + xVal;

        if( ( Math.abs(getPitch().getDegrees() - 360) > 2 ) || ( Math.abs(getRoll().getDegrees() - 360) > 2) ) {
            teleopDrive(transVal, 0, 0, false);
        }
        else {
            teleopDrive(0, 0, 0, false);
        }
    }

    public void teleopDrive(double translation, double strafe, double rotation, boolean robotCentric) {
        double translationVal = MathUtil.applyDeadband(translation, Constants.stickDeadband);
        double strafeVal = MathUtil.applyDeadband(strafe, Constants.stickDeadband);
        double rotationVal = MathUtil.applyDeadband(rotation, Constants.stickDeadband);

        drive(
            new Translation2d(translationVal, strafeVal).times(Constants.Swerve.maxSpeed), 
            rotationVal * Constants.Swerve.maxAngularVelocity, 
            !robotCentric, 
            true
        );    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    -translation.getY(), 
                                    -rotation, 
                                    getYaw()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(), 
                                    -translation.getY(), 
                                    -rotation)
                                );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    } 
    
    public void drive(ChassisSpeeds speeds) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], false);
        }
    }

    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }    

    public SwerveModuleState[] getModuleStates(){
        SwerveModuleState[] states = new SwerveModuleState[4];
        for(SwerveModule mod : mSwerveMods){
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods){
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    public void logging(){
        swerveOdometry.update(getYaw(), getModulePositions());  

        for(SwerveModule mod : mSwerveMods){
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.getState().speedMetersPerSecond);    
        }

        SmartDashboard.putNumber("navX Yaw", getYaw().getDegrees() - 360);
        SmartDashboard.putNumber("navx Pitch", getPitch().getDegrees() - 360);
        SmartDashboard.putNumber("navX Roll", getRoll().getDegrees() - 360);
    }
}