package org.usfirst.frc.team4043.robot.subsystems;

import org.usfirst.frc.team4043.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {
    Talon FlywheelMotor = new Talon(RobotMap.FlywheelMotor);
    Talon Firingpin = new Talon(RobotMap.Firingpin);
   
    public void start() {
    	FlywheelMotor.set(0.8f);
    }
    
    public void reverse() {
    	FlywheelMotor.set(-0.8f);
    }
    
    public void stop() {
    	FlywheelMotor.set(0);
    }
    
    public void push() {
    	if (FlywheelMotor.getSpeed() > 0) {
    		Firingpin.set(0.5f);
    	}
    }
    
    public void retract() {
    	Firingpin.set(0);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

