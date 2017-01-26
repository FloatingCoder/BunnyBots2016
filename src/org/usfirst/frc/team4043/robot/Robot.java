
package org.usfirst.frc.team4043.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.usfirst.frc.team4043.robot.commands.ExampleCommand;
import org.usfirst.frc.team4043.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4043.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team4043.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	public static DriveTrain drivetrain;
	public static Shooter shooter;
	public static Ini ini;
	Command autonomousCommand;
	SendableChooser chooser;

	private static void readSettings() {
		try {
			ini = new Ini(new File("/home/lvuser/RoboSettings/roboSettings.ini"));
			System.out.println("Runtime = " + ini.get("auto", "autoRunTime"));

		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateDashboardValues() {
		SmartDashboard.putString("DB/String 5", Double.toString( drivetrain.gyroSPI.getAngle()));
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {	
		readSettings();
		shooter = new Shooter();

		oi = new OI();
		drivetrain = new DriveTrain();

		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		// CameraServer server = CameraServer.getInstance();
		// server.setQuality(50);

		// server.startAutomaticCapture("cam0");
	}
	

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		updateDashboardValues();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	// varible for the start time, time to drive, step
	private int Step = 1;
	private int Time = 0;
	private int Time2 = 10000;
	private long TargetTime;
	private double Speed = -0.5d;
	private double Angle;
	private double AngleSpeed = -0.6d;
	private double AngleMinSpeed = -.4d;
	private double TargetTime2;
	private double Speed2 = -0.5d;
	public double gyroAngle;
	private double VoltageLimit = 0.4d;
	public int autoSettingInt = 1;

	private int targetAngle;

	public void autonomousInit() {
		readSettings();
		// autonomousCommand = (Command) chooser.getSelected();
		System.out.println("Auto init");
	   	String autoSettingString = SmartDashboard.getString("DB/String 1", "myDefaultData");
   	    autoSettingInt = Integer.parseInt(autoSettingString);
    	
    	if (autoSettingInt == 2){
    		Time = 3000;
    	}
    	else if(autoSettingInt == 1){
    		Time = 5000;
    	}
		System.out.println("time = " + Time);
		System.out.println("auti setting " + autoSettingInt);

		Step = 1;
		// set the start time
		TargetTime = System.currentTimeMillis() + Time;
		String gyroSetting = SmartDashboard.getString("DB/String 0", "myDefaultData");
		Angle = Integer.parseInt(gyroSetting);
		drivetrain.gyroSPI.reset();
		
		targetAngle = Integer.parseInt(ini.get("auto", "turnAngle"));
		AngleSpeed = Double.parseDouble(ini.get("auto", "AngleSpeed"));
		AngleMinSpeed = Double.parseDouble(ini.get("auto", "AngleMinSpeed"));
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		updateDashboardValues();
		//autonomous1();
		auto3();
	}
	
	public void auto2() {
		gyroAngle = drivetrain.gyroSPI.getAngle();
		if (Step == 1) {
			if (Math.abs(gyroAngle) < Math.abs(targetAngle)) {
				if (targetAngle < 0){
				    drivetrain.drive.arcadeDrive(0, AngleSpeed);
				}
				else{
					drivetrain.drive.arcadeDrive(0, -AngleSpeed);
				}
			} else {
				drivetrain.drive.arcadeDrive(0, 0);
				Step = 2;
				System.out.println("Step2");
			}
		}
		if (Step == 2) {
				if (targetAngle < 0){
					if (gyroAngle < 0) {
						drivetrain.drive.arcadeDrive(0, -AngleSpeed);
					} else {
						drivetrain.drive.arcadeDrive(0, 0);
						Step = 1;
					}
				}
				else{
					if (gyroAngle > 0) {
						drivetrain.drive.arcadeDrive(0, -AngleSpeed);
					} else {
						drivetrain.drive.arcadeDrive(0, 0);
						Step = 1;
					}
				}
		
		}
	}
	
	public void auto3() {
		gyroAngle = drivetrain.gyroSPI.getAngle();
		if (gyroAngle > targetAngle + 1.5){
			drivetrain.drive.arcadeDrive(0, findAngleSpeed(gyroAngle));
		}
		if (gyroAngle < targetAngle -1.5){
			drivetrain.drive.arcadeDrive(0, findAngleSpeed(gyroAngle));
		}
	}
	
	double figuredSpeed = 0;
	public double findAngleSpeed(double gyroAngle) {
		if (gyroAngle < targetAngle + 10 && gyroAngle > targetAngle){
			return AngleMinSpeed;
		}
		if (gyroAngle > targetAngle -10 && gyroAngle < targetAngle){
			return -AngleMinSpeed;
		}
		figuredSpeed = (gyroAngle - targetAngle) * AngleSpeed;
		if (Math.abs(figuredSpeed) > Math.abs(AngleSpeed)) {
			if (figuredSpeed > 0) {
				return -AngleSpeed;
			} else {
				return AngleSpeed;
			}
		} else {
			return figuredSpeed;
		}		
	}

	public void autonomous1() {
		gyroAngle = drivetrain.gyroSPI.getAngle();
		SmartDashboard.putString("DB/String 5", Double.toString(gyroAngle));

		if (Step == 1) {

			if (System.currentTimeMillis() < TargetTime) {
				if (Math.abs(drivetrain.gyroSPI.getAngle()) > Math.abs(20) ) {
					Step = 99;
					System.out.println("AngleStop");

				}
				if (drivetrain.frontRangeFinder.getVoltage() > VoltageLimit) {
					drivetrain.drive.arcadeDrive(0, 0);
					TargetTime2 = System.currentTimeMillis() + Time2;
					Step = 3;
					System.out.println("Triggered");
					System.out.println("Step3");
				} else {
					drivetrain.drive.arcadeDrive(Speed, 0);
				}
			} else {
				drivetrain.drive.arcadeDrive(0, 0);
				Step = 2;
				System.out.println("Step2");
			}
		}
		if (Step == 2) {
			if (Math.abs(drivetrain.gyroSPI.getAngle()) < Math.abs(Angle)) {
				if (Angle < 0){
				drivetrain.drive.arcadeDrive(0, AngleSpeed);
				}
				else{
					drivetrain.drive.arcadeDrive(0, -AngleSpeed);
				}
			} else  {
				if (Math.abs(drivetrain.gyroSPI.getAngle()) > Math.abs(10) ) {
					Step = 99;
				}
				drivetrain.drive.arcadeDrive(0, 0);
				TargetTime2 = System.currentTimeMillis() + Time2;
				Step = 3;
				drivetrain.gyroSPI.reset();
				System.out.println("Step3");
			}
		}
		if (Step == 3) {
			if (System.currentTimeMillis() < TargetTime2) {
				if (Math.abs(drivetrain.gyroSPI.getAngle()) > Math.abs(15) ) {
					Step = 99;
					System.out.println("AngleStop2");
				}
				if (drivetrain.frontRangeFinder.getVoltage() > VoltageLimit) {
					drivetrain.drive.arcadeDrive(0, 0);
				} else {
					drivetrain.drive.arcadeDrive(Speed2, 0);
				}
			} else {
				drivetrain.drive.arcadeDrive(0, 0);
				Step = 4;
			}
		}

		// Scheduler.getInstance().run();

	}

	public void teleopInit() {
		readSettings();
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	public void teleopPeriodic() {
		updateDashboardValues();
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
		updateDashboardValues();
		LiveWindow.run();
	}
}
