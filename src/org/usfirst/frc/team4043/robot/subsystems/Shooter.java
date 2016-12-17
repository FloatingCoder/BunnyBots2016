package org.usfirst.frc.team4043.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {
    Talon FlywheelMotor = new Talon(1);
    Talon Firingpin = new Talon(0);
   
    public void start() {
    	FlywheelMotor.set(0.4f);
    }
    
    public void stop() {
    	FlywheelMotor.set(0);
    }
    
    public void push() {
    	Firingpin.set(0.5f);
    }
    
    public void retract() {
    	Firingpin.set(0);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

