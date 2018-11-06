package com.team319.models;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 *
 * @author mwtidd
 */
public class XboxLeftTrigger extends Button {
	private final Joystick joystick;

	public XboxLeftTrigger(Joystick joystick) {
		this.joystick = joystick;

	}

	public boolean get() {
		return joystick.getRawAxis(2) > .25;
	}
}
