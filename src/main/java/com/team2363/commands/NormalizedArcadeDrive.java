package frc.robot.drivetrain.commands;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.team2363.logger.HelixEvents;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class NormalizedArcadeDrive extends Command {

	public NormalizedArcadeDrive(Subsystem requiredSubsystem) {
		// Use requires() here to declare subsystem dependencies
		requires(requiredSubsystem);
	}
	
	protected abstract double getThrottle();

	protected abstract double getTurn();

	protected abstract void useOutputs(double left, double right);

	@Override
    protected void execute() {
    	//read in joystick values from OI
    	//range [-1, 1]
    	double throttleInput = getThrottle();
		double turnInput = getTurn();
		
    	//find the maximum possible value of (throttle + turn)
    	//along the vector that the arcade joystick is pointing
    	double saturatedInput;
    	double greaterInput = max(abs(throttleInput), abs(turnInput));
    		//range [0, 1]
    	double lesserInput = min(abs(throttleInput), abs(turnInput));
    		//range [0, 1]
    	if (greaterInput > 0.0) {
    		saturatedInput = (lesserInput / greaterInput) + 1.0;
       		//range [1, 2]
    	} else {
    		saturatedInput = 1.0;
    	}
     	
    	//scale down the joystick input values
		//such that (throttle + turn) always has a range [-1, 1]
    	throttleInput = throttleInput / saturatedInput;
		turnInput = turnInput / saturatedInput;
		
		// Now we can calculate the arcade outputs like we normally would
		double left = throttleInput + turnInput;
		double right = throttleInput - turnInput;

	    useOutputs(left, right);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	HelixEvents.getInstance().addEvent("DRIVETRAIN", "Finished starting joystick drive");
    }

}