package com.team319.models;

/**
 * ISmartMotorController
 */
public interface IBobSmartMotorController {

    public void setPercentOutput(double percentOutput);

    public double getPosition();

    public double getVelocity();

    public double getOutputVoltage();

    public void resetToFactoryDefaults();

    public void follow(IBobSmartMotorController leader);

    public void setInverted(boolean inverted);

    public void setPosition(double position);

    public void setBrakeMode(boolean brakeModeEnabled);
}
