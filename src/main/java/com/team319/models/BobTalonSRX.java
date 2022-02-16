package com.team319.models;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class BobTalonSRX extends TalonSRX implements BobSmartMotorController{

	private int defaultTimeoutMs = 0;
	private int defaultPidIndex = 0;

	private int primaryPidIndex = 0;
	private int secondaryPidIndex = 1;

	private FeedbackDevice primaryFeedbackDevice;
	private FeedbackDevice secondaryFeedbackDevice;

	public BobTalonSRX(int deviceNumber) {
		super(deviceNumber);
		this.configNominalOutputForward(0.0);
		this.configNominalOutputReverse(0.0);
		this.configPeakOutputForward(1);
		this.configPeakOutputReverse(-1);
		this.configMotionProfileTrajectoryPeriod(0);
	}

	public int getPrimaryPidIndex() {
		return primaryPidIndex;
	}

	public int getSecondaryPidIndex() {
		return secondaryPidIndex;
	}

	public int getDefaultTimeoutMs() {
		return this.defaultTimeoutMs;
	}

	public int getDefaultPidIndex() {
		return this.defaultPidIndex;
	}

	public void setDefaultTimeoutMs(int timeoutMs) {
		this.defaultTimeoutMs = timeoutMs;
	}

	public void setDefaultPidIndex(int pidIndex) {
		this.defaultPidIndex = pidIndex;
	}

	public FeedbackDevice getPrimaryFeedbackDevice() {
		return this.primaryFeedbackDevice;
	}

	public FeedbackDevice getSecondaryFeedbackDevice() {
		return this.secondaryFeedbackDevice;
	}

	// ------------- HELPER METHODS -------- //

	public ErrorCode configPIDF(int slotIdx, double P, double I, double D, double F) {
		ErrorCode errorCode = ErrorCode.OK;

		errorCode = this.config_kP(slotIdx, P);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}

		errorCode = this.config_kI(slotIdx, I);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}

		errorCode = this.config_kD(slotIdx, D);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}

		errorCode = this.config_kF(slotIdx, F);
		return errorCode;
	}

	public ErrorCode configPIDF(int slotIdx, double P, double I, double D, double F, int iZone) {
		ErrorCode eCode = this.configPIDF(slotIdx, P, I, D, F);
		eCode = this.config_IntegralZone(slotIdx, iZone);
		return eCode;
	}

	public ErrorCode configPIDF(SRXGains gains) {
		return this.configPIDF(gains.parameterSlot, gains.P, gains.I, gains.D, gains.F, gains.iZone);
	}

	public void configMotionParameters(MotionParameters parameters) {
		this.configMotionAcceleration(parameters.getAcceleration());
		this.configMotionCruiseVelocity(parameters.getCruiseVelocity());
		this.setGains(parameters.getGains());
	}

	public void selectMotionParameters(MotionParameters parameters) {
		this.selectProfileSlot(parameters.getGains().parameterSlot);
		this.configMotionAcceleration(parameters.getAcceleration());
		this.configMotionCruiseVelocity(parameters.getCruiseVelocity());
	}

	public ErrorCode setGains(SRXGains gains) {
		return this.configPIDF(gains.parameterSlot, gains.P, gains.I, gains.D, gains.F, gains.iZone);
	}

	public void configPrimaryFeedbackDevice(FeedbackDevice feedbackDevice) {
		this.configSelectedFeedbackSensor(feedbackDevice, primaryPidIndex);
		this.primaryFeedbackDevice = feedbackDevice;
	}

	public void configPrimaryFeedbackCoefficient(double coefficient) {
		this.configSelectedFeedbackCoefficient(coefficient, primaryPidIndex);
	}

	public void configPrimaryFeedbackDevice(FeedbackDevice feedbackDevice, double coefficient) {
		this.configPrimaryFeedbackDevice(feedbackDevice);
		this.configPrimaryFeedbackCoefficient(coefficient);
	}

	public void configSecondaryFeedbackDevice(FeedbackDevice feedbackDevice) {
		this.configSelectedFeedbackSensor(feedbackDevice, this.secondaryPidIndex);
		this.secondaryFeedbackDevice = feedbackDevice;
	}

	public void configSecondaryFeedbackCoefficient(double coefficient) {
		this.configSelectedFeedbackCoefficient(coefficient, secondaryPidIndex);
	}

	public void configSecondaryFeedbackDevice(FeedbackDevice feedbackDevice, double coefficient) {
		this.configSecondaryFeedbackDevice(feedbackDevice);
		this.configSecondaryFeedbackCoefficient(coefficient);
	}

	public void configSensorSum(FeedbackDevice feedbackDevice0, FeedbackDevice feedbackDevice1) {
		this.configSensorTerm(SensorTerm.Sum0, feedbackDevice0);
		this.configSensorTerm(SensorTerm.Sum1, feedbackDevice1);
	}

	public void configRemoteSensor0(int remoteDeviceId, RemoteSensorSource remoteSensorSource) {
		this.configRemoteFeedbackFilter(remoteDeviceId, remoteSensorSource, 0);
	}

	public void configRemoteSensor1(int remoteDeviceId, RemoteSensorSource remoteSensorSource) {
		this.configRemoteFeedbackFilter(remoteDeviceId, remoteSensorSource, 1);
	}

	public double getPrimarySensorPosition() {
		return this.getSelectedSensorPosition(primaryPidIndex);
	}

	public double getSecondarySensorPosition() {
		return this.getSelectedSensorPosition(secondaryPidIndex);
	}

	public double getPrimarySensorVelocity() {
		return this.getSelectedSensorVelocity(primaryPidIndex);
	}

	public double getSecondarySensorVelocity() {
		return this.getSelectedSensorVelocity(secondaryPidIndex);
	}
	// ----------- ADDING DEFAULT VALUES --------- //

	public ErrorCode config_IntegralZone(int slotIdx, int izone) {
		return super.config_IntegralZone(slotIdx, izone, defaultTimeoutMs);
	}

	public ErrorCode configNominalOutputForward(double percentOut) {
		return super.configNominalOutputForward(percentOut, defaultTimeoutMs);
	}

	public ErrorCode configNominalOutputReverse(double percentOut) {
		return super.configNominalOutputReverse(percentOut, defaultTimeoutMs);
	}

	public ErrorCode configPeakOutputForward(double percentOut) {
		return super.configPeakOutputForward(percentOut, defaultTimeoutMs);
	}

	public ErrorCode configPeakOutputReverse(double percentOut) {
		return super.configPeakOutputReverse(percentOut, defaultTimeoutMs);
	}

	public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice) {
		return super.configSelectedFeedbackSensor(feedbackDevice, defaultPidIndex, defaultTimeoutMs);
	}

	public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx) {
		return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, defaultTimeoutMs);
	}

	public ErrorCode configContinuousCurrentLimit(int amps) {
		return super.configContinuousCurrentLimit(amps, defaultTimeoutMs);
	}

	public ErrorCode configOpenloopRamp(double secondsFromNeutralToFull) {
		return super.configOpenloopRamp(secondsFromNeutralToFull, defaultTimeoutMs);
	}

	public ErrorCode config_kP(int slotIdx, double value) {
		return super.config_kP(slotIdx, value, defaultTimeoutMs);
	}

	public ErrorCode config_kD(int slotIdx, double value) {
		return super.config_kD(slotIdx, value, defaultTimeoutMs);
	}

	public ErrorCode config_kF(int slotIdx, double value) {
		return super.config_kF(slotIdx, value, defaultTimeoutMs);
	}

	public ErrorCode config_kI(int slotIdx, double value) {
		return super.config_kI(slotIdx, value, defaultTimeoutMs);
	}

	public ErrorCode setSelectedSensorPosition(int sensorPos) {
		return super.setSelectedSensorPosition(sensorPos, defaultPidIndex, defaultTimeoutMs);
	}

	public ErrorCode setSelectedSensorPosition(int sensorPos, int pidIdx) {
		return super.setSelectedSensorPosition(sensorPos, pidIdx, defaultTimeoutMs);
	}

	public ErrorCode configMotionAcceleration(int sensorUnitsPer100msPerSec) {
		return super.configMotionAcceleration(sensorUnitsPer100msPerSec, defaultTimeoutMs);
	}

	public ErrorCode configMotionCruiseVelocity(int sensorUnitsPer100ms) {
		return super.configMotionCruiseVelocity(sensorUnitsPer100ms, defaultTimeoutMs);
	}

	public ErrorCode setStatusFramePeriod(StatusFrameEnhanced status13BasePidf0, int periodMs) {
		return super.setStatusFramePeriod(status13BasePidf0, periodMs, defaultTimeoutMs);
	}

	public ErrorCode configMotionProfileTrajectoryPeriod(int baseTrajDurationMs) {
		return super.configMotionProfileTrajectoryPeriod(baseTrajDurationMs, defaultTimeoutMs);
	}

	public ErrorCode configForwardSoftLimitEnable(boolean enable) {
		return super.configForwardSoftLimitEnable(enable, defaultTimeoutMs);
	}

	public ErrorCode configForwardSoftLimitThreshold(int forwardSensorLimit) {
		return super.configForwardSoftLimitThreshold(forwardSensorLimit, defaultTimeoutMs);
	}

	public ErrorCode configReverseSoftLimitEnable(boolean enable) {
		return super.configReverseSoftLimitEnable(enable, defaultTimeoutMs);
	}

	public ErrorCode configReverseSoftLimitThreshold(int reverseSensorLimit) {
		return super.configReverseSoftLimitThreshold(reverseSensorLimit, defaultTimeoutMs);
	}

	public double configGetParameter(ParamEnum param, int ordinal) {
		return super.configGetParameter(param, ordinal, defaultTimeoutMs);
	}

	public double getSelectedSensorPosition() {
		return super.getSelectedSensorPosition(defaultPidIndex);
	}

	public double getSelectedSensorVelocity() {
		return super.getSelectedSensorVelocity(defaultPidIndex);
	}

	public void selectProfileSlot(int slotIdx) {
		super.selectProfileSlot(slotIdx, defaultPidIndex);
	}

	public double getClosedLoopError() {
		return super.getClosedLoopError(defaultPidIndex);
	}

	public ErrorCode configRemoteFeedbackFilter(int deviceID, RemoteSensorSource remoteSensorSource,
			int remoteOrdinal) {
		return super.configRemoteFeedbackFilter(deviceID, remoteSensorSource, remoteOrdinal, defaultPidIndex);
	}

	public ErrorCode configSensorTerm(SensorTerm sensorTerm, FeedbackDevice feedbackDevice) {
		return super.configSensorTerm(sensorTerm, feedbackDevice, defaultTimeoutMs);
	}

	public ErrorCode configSelectedFeedbackCoefficient(double coefficient, int pidIdx) {
		return super.configSelectedFeedbackCoefficient(coefficient, pidIdx, defaultTimeoutMs);
	}

	public ErrorCode configClosedloopRamp(double secondsFromNeutralToFull) {
		return super.configClosedloopRamp(secondsFromNeutralToFull, defaultTimeoutMs);
	}

	public ErrorCode configVoltageCompSaturation(double voltage) {
		return super.configVoltageCompSaturation(voltage, defaultTimeoutMs);
	}

	public ErrorCode configMaxIntegralAccumulator(int slotIdx, double iaccum) {
		return super.configMaxIntegralAccumulator(slotIdx, iaccum, defaultTimeoutMs);
	}

	@Override
	public void setPercentOutput(double percentOutput) {
		set(ControlMode.PercentOutput, percentOutput);
	}

	@Override
	public void setVelocityOutput(double velocity) {
		set(ControlMode.Velocity, velocity);
	}

	@Override
	public void setPositionOutput(double position) {
		set(ControlMode.Position, position);
	}

	@Override
	public double getPosition() {
		return getSelectedSensorPosition();
	}

	@Override
	public double getVelocity() {
		return getSelectedSensorVelocity();
	}

	@Override
	public double getOutputVoltage() {
		return getMotorOutputVoltage();
	}

	@Override
	public void resetToFactoryDefaults() {
		configFactoryDefault();
	}

	@Override
	public void follow(BobSmartMotorController leader) {
		follow((IMotorController) leader);

	}

	@Override
	public void setSensorPosition(double position) {
		setSelectedSensorPosition((int)position);
	}

	@Override
	public void setBrakeMode(boolean brakeModeEnabled) {
		if (brakeModeEnabled) {
			setNeutralMode(NeutralMode.Brake);
		} else {
			setNeutralMode(NeutralMode.Coast);
		}
	}

	@Override
    public boolean isControllerPresent() {
        return getFirmwareVersion() != 0;
    }
}