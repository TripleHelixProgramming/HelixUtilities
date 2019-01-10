package com.team319.models;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class BobController extends Joystick {

	public BobController(int port) {
		super(port);
	}

	public JoystickButton xButton = new JoystickButton(this, 3);
	public JoystickButton yButton = new JoystickButton(this, 4);
	public JoystickButton aButton = new JoystickButton(this, 1);
	public JoystickButton bButton = new JoystickButton(this, 2);
	public JoystickButton rightBumper = new JoystickButton(this, 6);
	public JoystickButton leftBumper = new JoystickButton(this, 5);
	public JoystickButton startButton = new JoystickButton(this, 8);
	public JoystickButton selectButton = new JoystickButton(this, 7);
	public JoystickButton leftStickButton = new JoystickButton(this, 9);
	public JoystickButton rightStickButton = new JoystickButton(this, 10);

	public Button leftTriggerButton = new XboxLeftTrigger(this);
	public Button rightTriggerButton = new XboxRightTrigger(this);

	private int m_outputs;
	private short m_leftRumble;
	private short m_rightRumble;

	// public DPadUp dPadUp = new DPadUp(this);
	// public DPadDown dPadDown = new DPadDown(this);

	public double getTriggerTwist() {
		double leftTriggerValue = this.getRawAxis(2);
		double rightTriggerValue = -1 * this.getRawAxis(3);

		return leftTriggerValue + rightTriggerValue;

	}

	public double getLeftStickX() {
		return this.getRawAxis(0);
	}

	public double getLeftStickY() {
		return -this.getRawAxis(1);
	}

	public double getRightStickX() {
		return this.getRawAxis(4); // 4
	}

	public double getRightStickY() {
		return -this.getRawAxis(5);
	}

	public void setRumble(double leftValue, double rightValue) {
		setRumble(RumbleType.kLeftRumble, leftValue);
		setRumble(RumbleType.kRightRumble, rightValue);
	}

	public void setRumble(RumbleType type, double value) {
		if (value < 0) {
			value = 0;
		} else if (value > 1) {
			value = 1;
		}
		if (type == RumbleType.kLeftRumble) {
			m_leftRumble = (short) (value * 65535);
		} else {
			m_rightRumble = (short) (value * 65535);
		}
		HAL.setJoystickOutputs((byte) getPort(), m_outputs, m_leftRumble, m_rightRumble);
	}

}
