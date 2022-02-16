/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2363.utilities;

import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.Math.PI;

public class HelixMath {

    /**
     * Constrain the input value to the provided range
     * 
     * @param min the lowest value in the range (inclusive)
     * @param max the highest value in the range (inclusive)
     * @param input the input value to limit inside the provided range
     * 
     * @return the input value limited to the range of min <= input <= max
     */
    public static double constrainToRange(double min, double max, double input) {
        return min(max(input, min), max);
    }

    /**
     * Conver from a current encoder tick count to feet
     * 
     * @param ticks the current encoder tick count
     * @param wheelDiameterInInches the wheel diameter in inches
     * @param ticksPerRevolution the tick count for the encoder (for a quad encoder this may be 4x the count)
     * 
     * @return the ticks converted to feet
     */
    public static double convertFromTicksToFeet(int ticks, double wheelDiameterInInches, int ticksPerRevolution) {
        return PI * wheelDiameterInInches * ticks / ticksPerRevolution / 12.0;
    }
    
    /**
     * Conver from a current encoder ticks per 100 ms to feet per second
     * 
     * @param ticksPer10ms the current encoder tick count
     * @param wheelDiameterInInches the wheel diameter in inches
     * @param ticksPerRevolution the tick count for the encoder (for a quad encoder this may be 4x the count)
     * 
     * @return the ticks per 100 ms converted to feet per second
     */
    public static double convertFromTicksPer100MsToFps(int ticksPer10ms, double wheelDiameterInInches, int ticksPerRevolution) {
        return convertFromTicksToFeet(ticksPer10ms * 10, wheelDiameterInInches, ticksPerRevolution);
    }

    /**
     * Convert from a measurement in feet to encoder ticks
     * 
     * @param feet the current measurement in feet
     * @param wheelDiameterInInches the wheel diameter in inches
     * @param ticksPerRevolution the tick count for the encoder (for a quad encoder this may be 4x the count)
     * 
     * @return the feet measurement converted to encoder ticks
     */
    public static double convertFromFeetToTicks(double feet, double wheelDiameterInInches, int ticksPerRevolution) {
        return ticksPerRevolution * feet / (PI * wheelDiameterInInches) * 12;
    }

    /**
     * Convert from a measurement in feet per second to encoder ticks per 100 ms
     * 
     * @param fps the current measurement in feet per second
     * @param wheelDiameterInInches the wheel diameter in inches
     * @param ticksPerRevolution the tick count for the encoder (for a quad encoder this may be 4x the count)
     * 
     * @return the feet per second measurement converted to encoder ticks per 100 ms
     */
    public static double convertFromFpsToTicksPer100Ms(double fps, double wheelDiameterInInches, int ticksPerRevolution) {
        return convertFromFeetToTicks(fps / 10, wheelDiameterInInches, ticksPerRevolution);
    }

    public static void main(String... args) {
        System.out.println(convertFromFpsToTicksPer100Ms(6, 4, (int)(480 * 48.0 / 42.0)));
        System.out.println(convertFromTicksPer100MsToFps(313, 4, (int)(480 * 48.0 / 42.0)));
    }
}
