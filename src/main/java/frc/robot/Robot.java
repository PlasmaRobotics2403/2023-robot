// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.modes.*;
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
  Intake intake;

  Compressor compressor;

  double elevatorTarget;
  double armTarget;
  double extenderTarget;
  double grabberTarget;
  double intakeTarget;

  String robotState;

  AutoModeRunner autoModeRunner;
  AutoMode[] autoModes;
  int autoModeSelection;
  SendableChooser<Integer> autoChooser;
    
  LEDs leds;
  int hue;
  int value;
  boolean FMS_Connected;
  boolean passthrough = false;

  String gamePiece;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    driver = new PlasmaJoystick(Constants.DRIVER_JOYSTICK_PORT);
    navigator = new PlasmaGuitar(Constants.COPILOT_JOYSTICK_PORT);

    compressor = new Compressor(31, PneumaticsModuleType.REVPH);  

    swerve = new Swerve();
    limelight = new Limelight();
    elevator = new Elevator();
    intake = new Intake();
    grabber = new Grabber();

    elevatorTarget = 0;
    armTarget = 0;

    autoModeRunner = new AutoModeRunner();
    autoModes = new AutoMode[20];
    autoModes[0] = new Nothing();
    autoModes[1] = new ScoringTableToCharge(swerve, elevator, grabber);
    autoModes[2] = new AudienceToCharge(swerve, elevator, grabber);
    autoModes[3] = new LeaveCommunity(swerve, elevator, grabber);
    autoModes[4] = new OverChargedStation(swerve, elevator, grabber);
    autoModes[5] = new Score(swerve, elevator, grabber);
    autoModes[6] = new TwoCubeAuto(intake, grabber, elevator, swerve);
    autoModes[7] = new TwoCubeAutoOverCable(intake, grabber, elevator, swerve);

    // april tag locations
    // tag #1 = red cable
    // tag #2 = red charged station
    // tag #3 = red clear
    // tag #4 = blue feeder
    // tag #5 = red feeder
    // tag #6 = blue clear
    // tag #7 = blue charged station
    // tag #8 = blue cable

    autoChooser = new SendableChooser<>();
    autoChooser.setDefaultOption("nothing", 0);
    limelight.logging();

    if(limelight.getApriltag() == 2 || limelight.getApriltag() == 7) {
      autoChooser.addOption("Balance", 4);
      autoChooser.addOption("One Cube Auto", 5);
    }
    else if(limelight.getApriltag() == 1 || limelight.getApriltag() == 8) {
      autoChooser.addOption("Two cube auto", 7);
      autoChooser.addOption("Leave Community", 3);
      autoChooser.addOption("One Cube Auto", 5);
    }

    else {
      autoChooser.addOption("Two cube auto", 6);
      autoChooser.addOption("Leave Community", 3);
      autoChooser.addOption("One Cube Auto", 5);
    }

    autoModeSelection = 0;
    
    leds = new LEDs();
    hue = 0;   
    value = 255;
    FMS_Connected = false;
    gamePiece = "Cube";
    robotState = "stow";

    DriverStation.silenceJoystickConnectionWarning(true);
    CameraServer.startAutomaticCapture();
    compressor.enableDigital();
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
    SmartDashboard.putData("Auto Selector", autoChooser);
    SmartDashboard.putNumber("Auton Mode", autoModeSelection);
    SmartDashboard.putString("game piece", gamePiece);

    if(gamePiece == "Cone") {
      grabber.closeGrabber();
      hue = 15;
    }
    else if(gamePiece == "Cube") {
      grabber.openGrabber();
      hue = 130;
    }

    swerve.logging();
    limelight.logging();
    elevator.logger();
    grabber.logging();
    intake.logging();

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
    autoModeRunner.stop();

    swerve.defaultNeutralMode(true, true);
    leds.setRGB(0, 0, 0);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    /* Driver Controls */

    /* vision align */
    if(driver.A.isPressed()) {
      double[] speeds = limelight.fullAlign();
      swerve.teleopDrive(limelight.distanceVisionAlign(), limelight.XVisionAlign(), 0, true);
    }
    /* rezero robot heading */
    else if(driver.START.isPressed()) {
      swerve.zeroHeading();
    }
    /* creep drive */
    else if(driver.LT.isPressed()) {
      swerve.teleopDrive(Constants.Swerve.creepSpeed*driver.LeftY.getTrueAxis(), Constants.Swerve.creepSpeed*driver.LeftX.getTrueAxis(), Constants.Swerve.creepSpeed*driver.RightX.getTrueAxis(), false);
    }
    /* regular drive */
    else {
      swerve.teleopDrive(driver.LeftY.getTrueAxis(), driver.LeftX.getTrueAxis(), driver.RightX.getTrueAxis(), false);
    }

    /* robot States */
    elevator.magicElevator(elevatorTarget);
    if(robotState == "stow" && navigator.GREEN.isPressed()) {
      grabber.zeroArm();
    }
    else if(navigator.RED.isPressed()) {
      grabber.ArmRot(0);
    }

    else if(navigator.DPAD.getPOV() == 0) {
      grabber.ArmRot(0.2);
    }
    else if(navigator.DPAD.getPOV() == 180) {
      grabber.ArmRot(-0.2);
        } 

    else {
      grabber.magicArm(armTarget);
        
      }

    // reset arm zero position
    if(navigator.GREEN.isPressed() && robotState == "stow") {
      grabber.zeroArm();
    }

    if(driver.dPad.getPOV() == 0) { /* high score state */
      elevatorTarget = Constants.ElevatorConstants.ELEVATOR_HIGH_EXTEND;
      armTarget = Constants.GrabberConstants.ARM_HIGH_EXTEND;
      extenderTarget = Constants.GrabberConstants.EXTENDER_RETRACTED_POSITION;
      robotState = "high";
    }
    else if(driver.dPad.getPOV() == 90) { /* mid score state */
      elevatorTarget = Constants.ElevatorConstants.ELEVATOR_MID_EXTEND;
      armTarget = Constants.GrabberConstants.ARM_HIGH_EXTEND;
      extenderTarget = Constants.GrabberConstants.EXTENDER_RETRACTED_POSITION;
      robotState = "mid";
    }
    else if(driver.dPad.getPOV() == 270) { /* low score state */
      elevatorTarget = Constants.ElevatorConstants.ELEVATOR_LOW_EXTEND;
      armTarget = Constants.GrabberConstants.ARM_LOW_EXTEND;
      extenderTarget = Constants.GrabberConstants.EXTENDER_RETRACTED_POSITION;
      robotState = "low";
    }
    else if(driver.dPad.getPOV() == 180) { /* stow state */
      elevatorTarget = Constants.ElevatorConstants.ELEVATOR_BOTTTOM_EXTEND;
      armTarget = Constants.GrabberConstants.ARM_STOWED_EXTEND;
      extenderTarget = Constants.GrabberConstants.EXTENDER_RETRACTED_POSITION;
      robotState = "stow";
    }
    else if(driver.Y.isPressed()) { /* feeder state */
      elevatorTarget = Constants.ElevatorConstants.ELEVATOR_FEEDER_EXTEND;
      armTarget = Constants.GrabberConstants.ARM_FEEDER_EXTEND;
      extenderTarget = Constants.GrabberConstants.EXTENDER_EXTENDED_POSITION;
      robotState = "feeder";
    }


    /* grabber open close */
    if(driver.RB.isPressed()) {
      grabber.runGrabber(Constants.GrabberConstants.GRABBER_SPEED);
    }
    
    else if(driver.LB.isPressed()) {
      grabber.runGrabber(-Constants.GrabberConstants.GRABBER_SPEED);
    }
    else if (!passthrough) {
      grabber.runGrabber(0);
    }

    // intake controls
    if(driver.B.isPressed()) {
      intake.ejectGamePiece();
    }
    else if(driver.RT.isPressed()) {
      intake.intakeGamePiece();
    }
    else {
      intake.idleGamePiece();
    }

    
    if(driver.BACK.isPressed()) {
      passthrough = false;
    }

    if(driver.X.isPressed()) {
      if (grabber.getLimitSwitch()){
        passthrough = true;
      }
      
    }

    

    if(passthrough) {
      intake.passthrough(grabber.getLimitSwitch());
      grabber.runGrabber(Constants.GrabberConstants.GRABBER_SPEED);
      gamePiece = "Cube";
      if (!grabber.getLimitSwitch()){
        passthrough = false;
      }
    }



    // leds and game piece determinant
    value = 128 + (int)(navigator.WAMMY.getTrueAxis() * 127);
    leds.setHSV(hue, 255, value);
    if(navigator.BLUE.isPressed()) {
      hue = 130;
      gamePiece = "Cube";
      }
    else if(navigator.YELLOW.isPressed()) {
      hue = 15;
      gamePiece = "Cone";
      }
      else if(navigator.ORANGE.isPressed()) {
        hue = 7;
      }
    }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    value = 128 + (int)(navigator.WAMMY.getTrueAxis() * 127);

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
