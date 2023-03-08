// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.modes.DriveForward;
import frc.robot.auto.util.AutoMode;
import frc.robot.auto.util.AutoModeRunner;
import frc.robot.controllers.PlasmaGuitar;
import frc.robot.controllers.PlasmaJoystick;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  PlasmaJoystick driver;
  PlasmaGuitar navigator;

  Swerve swerve;
  Limelight limelight;
  Elevator elevator;
  Grabber grabber;

  double elevatorTarget;
  double armTarget;

  Intake intake;

  AutoModeRunner autoModeRunner;
  AutoMode[] autoModes;
  int autoModeSelection;
    
  LEDs leds;
  int hue;
  boolean FMS_Connected;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    driver = new PlasmaJoystick(Constants.DRIVER_JOYSTICK_PORT);
    navigator = new PlasmaGuitar(Constants.COPILOT_JOYSTICK_PORT);

    swerve = new Swerve();
    limelight = new Limelight();
    elevator = new Elevator();
    intake = new Intake();
    grabber = new Grabber();

    elevatorTarget = 0;
    armTarget = 0;

    autoModeRunner = new AutoModeRunner();
    autoModes = new AutoMode[20];
    autoModes[0] = new DriveForward(swerve);
    
    autoModeSelection = 0;
    
    leds = new LEDs();
    hue = 0;   
    FMS_Connected = false;
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

    leds.sendData();
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
    leds.setRGB(0, 0, 0);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    /* Driver Controls */
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

    grabber.magicArm(armTarget);
    if(driver.RB.isPressed()) {
      armTarget = 3000;
    }
    else if(driver.LB.isPressed()){
      armTarget = 0;
    }

    if(driver.LT.isPressed()) {
      intake.ActuateIntake(Constants.IntakeConstants.horizontalSpeed);
    }
    else if(driver.RB.isPressed()) {
      intake.ActuateIntake(-Constants.IntakeConstants.horizontalSpeed);
    }

    //elevator.magicElevator(elevatorTarget);
    if(driver.dPad.getPOV() == 0) {
      elevatorTarget = 40;
      elevator.spin(Constants.GrabberConstants.Up_Arm_Speed);
    }
    else if(driver.dPad.getPOV() == 90 || driver.dPad.getPOV() == 270) {
      elevatorTarget = 20;
    }
    else if(driver.dPad.getPOV() == 180){
      elevatorTarget = 0;
      elevator.spin(Constants.GrabberConstants.Down_Arm_Speed);
    }
    else {
      elevator.spin(0);
    }

    if(driver.X.isPressed()) {
      grabber.grabberRun(Constants.GrabberConstants.Grabber_Speed);
    }
    else if(driver.Y.isPressed()) {
      grabber.grabberRun(-Constants.GrabberConstants.Grabber_Speed);
    }
    else {
      grabber.grabberRun(0);
    }

    /* Navigator Controls */
    if(navigator.YELLOW.isPressed()) {
      leds.setHSV(15, 255, 255);
    }
    else if(navigator.BLUE.isPressed()) {
      leds.setHSV(130, 255, 255);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    int value = 128 + (int)(navigator.WAMMY.getTrueAxis() * 127);

    if(FMS_Connected) {
      if(navigator.GREEN.isPressed()) {
        hue = 60;
      }
      else if(navigator.RED.isPressed()) {
        hue = 0;
      }
      else if(navigator.YELLOW.isPressed()) {
        hue = 15;
      }
      else if(navigator.BLUE.isPressed()) {
        hue = 130;
      }
      else if(navigator.ORANGE.isPressed()) {
        hue = 7;
      }
      leds.setHSV(hue, 255, value);
    }
    else {
      if(DriverStation.getAlliance() == Alliance.Red && DriverStation.isDSAttached()) {
        hue = 0;
        FMS_Connected = true;
      }
      else if(DriverStation.getAlliance() == Alliance.Blue && DriverStation.isDSAttached()) {
        hue = 130;
        FMS_Connected = true;
      }
    }
  }

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
