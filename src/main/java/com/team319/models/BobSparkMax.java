package com.team319.models;

import com.revrobotics.CANSparkMax;

public class BobSparkMax extends CANSparkMax implements IBobSmartMotorController {

    public BobSparkMax(int deviceId, MotorType motorType) {
        super(deviceId, motorType);
    }

    @Override
    public void setPercentOutput(double percentOutput) {
        super.set(percentOutput);
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
    public void follow(IBobSmartMotorController leader) {
        super.follow((CANSparkMax) leader);
    }

    @Override
    public void setPosition(double position) {
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
}