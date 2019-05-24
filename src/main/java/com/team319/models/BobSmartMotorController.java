package com.team319.models;

/**
 * SmartMotorController
 */
public interface BobSmartMotorController {

    void setPercentOutput(double percentOutput);

    void setVelocityOutput(double velocity);

    void setPositionOutput(double position);

    double getPosition();

    double getVelocity();

    double getOutputVoltage();

    double getOutputCurrent();

    void resetToFactoryDefaults();

    void follow(BobSmartMotorController leader);

    void setInverted(boolean inverted);

    void setSensorPosition(double position);

    void setBrakeMode(boolean brakeModeEnabled);

    boolean isControllerPresent();
}
