// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
 
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.modes.DriveForward;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeRunner;
import frc.robot.controllers.PlasmaDPad;
import frc.robot.controllers.PlasmaJoystick;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  PlasmaJoystick driver;
  Swerve swerve;
  Limelight limelight;
  AHRS navX;
  Elevator elevator;
  Grabber grabber;

  double elevatorTarget;

  AutoModeRunner autoModeRunner;
  AutoMode[] autoModes;
  int autoModeSelection;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    driver = new PlasmaJoystick(Constants.DRIVER_JOYSTICK_PORT);
    swerve = new Swerve();
    limelight = new Limelight();
    elevator = new Elevator();
    grabber = new Grabber();

    elevatorTarget = 0;
    
    autoModeRunner = new AutoModeRunner();
    autoModes = new AutoMode[20];
        autoModes[0] = new DriveForward(swerve);

    autoModeSelection = 0;
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    autoModeSelection = (int) SmartDashboard.getNumber("Auton Mode", 0.0);
    SmartDashboard.putNumber("Auton Mode", autoModeSelection);


    swerve.logging();
    limelight.logging();
    elevator.logger();
    grabber.logging();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    autoModeRunner.chooseAutoMode(autoModes[autoModeSelection]);
    autoModeRunner.start();   
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    swerve.defaultNeutralMode(true, true);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(driver.A.isPressed()) {
      swerve.teleopDrive(limelight.distanceVisionAlign(), limelight.XVisionAlign(), /*limelight.SkewVisionAlign()*/0, true);
    }
    else if(driver.B.isPressed()) {
      swerve.balance();
    }
    else {
      swerve.teleopDrive(driver.LeftY.getTrueAxis(), driver.LeftX.getTrueAxis(), driver.RightX.getTrueAxis(), driver.START.isPressed());
    }

    if (driver.BACK.isPressed()) {
      swerve.zeroGyro();
    }

    if(driver.RB.isPressed()) {
      grabber.ArmRot(0.9);
    }

    else if(driver.LB.isPressed()){
      grabber.ArmRot(-0.9);
    }

    else {
      grabber.ArmRot(0);  
    }

    //elevator.magicElevator(elevatorTarget);
    if(driver.dPad.getPOV() == 0) {
      elevatorTarget = 15;
      elevator.spin(0.7);
    }
    else if(driver.dPad.getPOV() == 90 || driver.dPad.getPOV() == 270) {
      elevatorTarget = 10;
    }
    else if(driver.dPad.getPOV() == 180){
      elevatorTarget = 0;
      elevator.spin(-0.3);
    }
    else {
      elevator.spin(0);
    }

    
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
