/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2363.utilities;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.team2363.logger.HelixEvents;
import com.team319.models.BobSmartMotorController;
import com.team319.models.BobTalonSRX;
import com.team319.models.BobVictorSPX;

public class MotorControllerFactory {

    public enum MotorControllerType {
        Talon, Victor, SparkMax;
    }

    public static BobSmartMotorController getController(int channel) {
        for (MotorControllerType type : MotorControllerType.values()) {
            BobSmartMotorController controller = getByType(type, channel);
            if (controller.isControllerPresent()) {
                HelixEvents.getInstance().addEvent("START_UP", "Found a " + type + " on channel " + channel);
                return controller;
            }
        }
        HelixEvents.getInstance().addEvent("START_UP", "Could not find a motor controller on channel " + channel);
        return null;
    }
    

    private static BobSmartMotorController getByType(MotorControllerType type, int channel) {
        switch(type) {
            case Talon:
                return getTalon(channel);
            case Victor:
                return getVictor(channel);
            default:
                return null;
        }
    }

    public static BobTalonSRX getTalon(int channel) {
        return new BobTalonSRX(channel);
    }

    public static BobVictorSPX getVictor(int channel) {
        return new BobVictorSPX(channel);
    }
}
