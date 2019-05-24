package com.team319.models;

import com.revrobotics.CANSparkMax;

public class BobSparkMax extends CANSparkMax implements BobSmartMotorController {

    public BobSparkMax(int deviceId, MotorType motorType) {
        super(deviceId, motorType);
    }

    @Override
    public void setPercentOutput(double percentOutput) {
        set(percentOutput);
    }

    @Override
    public void setVelocityOutput(double velocity) {
        
    }

    @Override
    public void setPositionOutput(double position) {

    }

    @Override
    public double getPosition() {
        return super.getEncoder().getPosition();
    }

    @Override
    public double getVelocity() {
        return super.getEncoder().getVelocity();
    }

    @Override
    public void resetToFactoryDefaults() {
        super.restoreFactoryDefaults();
    }

    @Override
    public void follow(BobSmartMotorController leader) {
        super.follow((CANSparkMax) leader);
    }

    @Override
    public void setSensorPosition(double position) {
        super.getEncoder().setPosition(position);
    }

    @Override
    public double getOutputVoltage() {
        return super.getAppliedOutput();
    }

    @Override
    public void setBrakeMode(boolean brakeModeEnabled) {
        super.setIdleMode(brakeModeEnabled ? IdleMode.kBrake : IdleMode.kCoast);
    }

    @Override
    public boolean isControllerPresent() {
        return getFirmwareVersion() != 0;
    }
}