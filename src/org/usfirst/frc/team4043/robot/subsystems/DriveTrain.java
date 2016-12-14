package org.usfirst.frc.team4043.robot.subsystems;

import org.usfirst.frc.team4043.robot.Robot;
import org.usfirst.frc.team4043.robot.RobotMap;
import org.usfirst.frc.team4043.robot.commands.TankDriveWithJoystick;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {

	private SpeedController left_motor, right_motor;
	public RobotDrive drive;
	//private Encoder left_encoder, right_encoder;
	private AnalogInput rightRangeFinder;
	public AnalogInput frontRangeFinder;

	public ADXRS450_Gyro gyroSPI;
	public DriveTrain() {
		super();
		left_motor = new CANTalon(RobotMap.leftDrivePort);
		right_motor = new CANTalon(RobotMap.rightRightPort);
		drive = new RobotDrive(left_motor, right_motor);
//		left_encoder = new Encoder(1, 2);
//		right_encoder = new Encoder(3, 4);

		// Encoders may measure differently in the real world and in
		// simulation. In this example the robot moves 0.042 barleycorns
		// per tick in the real world, but the simulated encoders
		// simulate 360 tick encoders. This if statement allows for the
		// real robot to handle this difference in devices.
//		if (Robot.isReal()) {
//			left_encoder.setDistancePerPulse(0.042);
//			right_encoder.setDistancePerPulse(0.042);
//		} else {
//			// Circumference in ft = 4in/12(in/ft)*PI
//			left_encoder.setDistancePerPulse((4.0 / 12.0 * Math.PI) / 360.0);
//			right_encoder.setDistancePerPulse((4.0 / 12.0 * Math.PI) / 360.0);
//		}

		frontRangeFinder = new AnalogInput(RobotMap.frontRangeFinder);
		rightRangeFinder = new AnalogInput(RobotMap.rightRangeFinder);
		//rightRangeFinder = new Ultrasonic(0);
//		rightRangeFinder.setEnabled(true);
//		rightRangeFinder.setAutomaticMode(true);
		//gyro = new AnalogGyro(1);
		gyroSPI = new ADXRS450_Gyro();
		gyroSPI.calibrate();
		// Let's show everything on the LiveWindow
		LiveWindow.addActuator("Drive Train", "Left Motor", (CANTalon) left_motor);
		LiveWindow.addActuator("Drive Train", "Right Motor", (CANTalon) right_motor);
//		LiveWindow.addSensor("Drive Train", "Left Encoder", left_encoder);
//		LiveWindow.addSensor("Drive Train", "Right Encoder", right_encoder);
		LiveWindow.addSensor("Drive Train", "Right Range Finder", rightRangeFinder);
		LiveWindow.addSensor("Drive Train", "Gyro", gyroSPI);
		//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	/**
	 * When no other command is running let the operator drive around using the
	 * PS3 joystick.
	 */
	public void initDefaultCommand() {
		setDefaultCommand(new TankDriveWithJoystick());
	}

	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	public void log() {
//		SmartDashboard.putNumber("Left Distance", left_encoder.getDistance());
//		SmartDashboard.putNumber("Right Distance", right_encoder.getDistance());
//		SmartDashboard.putNumber("Left Speed", left_encoder.getRate());
//		SmartDashboard.putNumber("Right Speed", right_encoder.getRate());
		SmartDashboard.putNumber("Gyro", gyroSPI.getAngle());
		SmartDashboard.putNumber("RightRangeFinder", rightRangeFinder.getVoltage());
		//System.out.println(rightRangeFinder.getRangeMM());
	}

	/**
	 * Tank style driving for the DriveTrain.
	 * 
	 * @param left
	 *            Speed in range [-1,1]
	 * @param right
	 *            Speed in range [-1,1]
	 */
	public void drive(double throttle, double turn) {
		drive.arcadeDrive(throttle, turn);
	}

	/**
	 * @param joy
	 *            The ps3 style joystick to use to drive tank style.
	 */
	public float maxSpeed = -1;
	public float maxTurn = 0.85f;
	public float maxautoturn = 0.25f;
	public float currentSpeed;
	//public float currentTurn;
	public double inputSpeed;
	public double inputTurn;
	public boolean autoTurnEnabled;
	public boolean turnStarted = false;
	public double lastTurnRead;
	public double currentTurnRead;
	public double turnStep = 0.1d;
	public double turnSlowThreshhold = 0.25d;
	public double maxSpeedDuringManualTurn = -0.8f;
	
	public void drive(Joystick joy) {
		inputSpeed = joy.getRawAxis(1);
		if (!autoTurnEnabled) {
		  inputTurn = joy.getRawAxis(4);
		}
		
		if (inputSpeed < maxSpeed) {
			inputSpeed = maxSpeed;
		}
		
//		//Stop if there's something in the way
//		if (frontRangeFinder.getVoltage() > 0.7d && inputSpeed < 0) {
//			inputSpeed = 0;
//		}
		
		//Autoturn, currently only set for testing
		//Only turn on if we are moving
//		if (autoTurnEnabled && inputSpeed < -0.1d) {
//			// limit the speed during the turn
//			if (inputSpeed < maxSpeedDuringManualTurn) {
//				inputSpeed =  maxSpeedDuringManualTurn;
//			}
//			if (turnStarted) {
//				if (currentTurnRead < lastTurnRead) {
//					//we're moving away, turn closer
//					inputTurn += turnStep;
//				} else {
//					//We're getting closer or staying the same, go straight
//					inputTurn = 0;
//				}
//			} else {
//				currentTurnRead = rightRangeFinder.getVoltage();
//				if (currentTurnRead < lastTurnRead) {
//					//We passed the object, initiate turn
//					System.out.println("turnStarted");
//					turnStarted = true;
//					inputTurn = 0;
//				} else {
//					lastTurnRead = currentTurnRead + 0.2f;
//				}
//			} 
//		} else {
//			//init values
//			autoTurnEnabled = true;
//			lastTurnRead = rightRangeFinder.getVoltage();
//		}
		
		//If not in auto turn, use normal logic
		if (!autoTurnEnabled) {
			//Limit the speed during a large turn
	        if (Math.abs(inputTurn) > turnSlowThreshhold && inputSpeed < maxSpeedDuringManualTurn) {
	        	inputSpeed = maxSpeedDuringManualTurn;
	        }
	        
	        //Limit turn speed
	        if (Math.abs(inputTurn) > maxTurn) {
	        	if (inputTurn > 0) {
	        		inputTurn = maxTurn;
	        	} else {
	        		inputTurn = -maxTurn;
	        	}
	        }
		}
		  
		drive(inputSpeed, inputTurn);
		//drive(joy.getRawAxis(1),joy.getRawAxis(4));
	}

	/**
	 * @return The robots heading in degrees.
	 */
//	public double getHeading() {
//		return gyro.getAngle();
//	}

	/**
	 * Reset the robots sensors to the zero states.
	 */
	public void reset() {
//		gyro.reset();
//		left_encoder.reset();
//		right_encoder.reset();
	}

	/**
	 * @return The distance driven (average of left and right encoders).
	 */
//	public double getDistance() {
//		return (left_encoder.getDistance() + right_encoder.getDistance()) / 2;
//	}

	/**
	 * @return The distance to the obstacle detected by the rangefinder.
	 */
//	public double getDistanceToObstacle() {
//		// Really meters in simulation since it's a rangefinder...
//		return rangefinder.getAverageVoltage();
//	}

}
