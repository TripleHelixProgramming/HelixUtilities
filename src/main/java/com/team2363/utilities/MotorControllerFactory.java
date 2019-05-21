/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2363.utilities;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.team319.models.BobSparkMax;
import com.team319.models.BobTalonSRX;
import com.team319.models.BobVictorSPX;

/**
 * Add your docs here.
 */
public class MotorControllerFactory {

    public enum MotorControllerType {
        Talon, Victor, Auto;
    }

    public BaseMotorController getCtreController(MotorControllerType type, int channel) {
        switch(type) {
            case Talon:
                return getTalon(channel);
            case Victor:
                return getVictor(channel);
            default:
                return getAuto(channel);

        }
    }

    private BaseMotorController getAuto(int channel) {
        BaseMotorController controller = getTalon(channel);
        if (controller.getFirmwareVersion() != 0) {
            return controller;
        }

        controller = getVictor(channel);
        return controller;
    }

    public static BobTalonSRX getTalon(int channel) {
        return new BobTalonSRX(channel);
    }

    public static BobVictorSPX getVictor(int channel) {
        return new BobVictorSPX(channel);
    }

    public static BobSparkMax getSparkMax(int channel, MotorType type) {
        return new BobSparkMax(channel, type);
    }
}
