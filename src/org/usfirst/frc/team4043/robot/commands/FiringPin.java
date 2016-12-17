package org.usfirst.frc.team4043.robot.commands;

import org.usfirst.frc.team4043.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FiringPin extends Command {

    public FiringPin() {
    	requires(Robot.shooter);
    	setTimeout(0.2);
    }

    protected void initialize() {
    	Robot.shooter.push();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
    	Robot.shooter.retract();
    }

    protected void interrupted() {
    	end();
    }
}
