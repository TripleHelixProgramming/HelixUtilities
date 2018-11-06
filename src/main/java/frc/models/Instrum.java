package frc.models;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Instrum {

	private static int _loops = 0;
	private static int _timesInMotionMagic = 0;
	private static int PID_PROFILE = 0;

	public static void Process(TalonSRX tal, StringBuilder sb) {
		/* smart dash plots */
		SmartDashboard.putNumber("SensorVel", tal.getSelectedSensorVelocity(PID_PROFILE));
		SmartDashboard.putNumber("SensorPos", tal.getSelectedSensorPosition(PID_PROFILE));
		SmartDashboard.putNumber("MotorOutputPercent", tal.getMotorOutputPercent());
		SmartDashboard.putNumber("ClosedLoopError", tal.getClosedLoopError(PID_PROFILE));
		SmartDashboard.putNumber("ClosedLoopTarget", tal.getClosedLoopTarget(PID_PROFILE));

		/* check if we are motion-magic-ing */
		if (tal.getControlMode() == ControlMode.MotionMagic) {
			++_timesInMotionMagic;
		} else {
			_timesInMotionMagic = 0;
		}
		if (_timesInMotionMagic > 10) {
			/* print the Active Trajectory Point Motion Magic is servoing towards */
			SmartDashboard.putNumber("ActTrajVelocity", tal.getActiveTrajectoryVelocity());
			SmartDashboard.putNumber("ActTrajPosition", tal.getActiveTrajectoryPosition());
			SmartDashboard.putNumber("ActTrajHeading", tal.getActiveTrajectoryHeading());
		}
		/* periodically print to console */
		if (++_loops >= 10) {
			_loops = 0;
			System.out.println(sb.toString());
		}
		/* clear line cache */
		sb.setLength(0);
	}
}
