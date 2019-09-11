package com.team2363.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class will create joysticks for all 6 slots and allow you to retrieve them based on their display name.
 */
public class ControllerPatroller {

    private static ControllerPatroller patroller = new ControllerPatroller();

    /**
     * Retrieve the singleton instance of the ControllerPatroller
     * 
     * @return the instance of the ControllerPatroller
     */
    public static ControllerPatroller getPatroller() {
        if (patroller == null) {
            patroller = new ControllerPatroller();
        }

        return patroller;
    }

    private List<Joystick> controllers = new ArrayList<>();

    /**
     * @param controllerNames the list of controller names in the order the user would like to access them
     */
    private ControllerPatroller() {
        // Create a joystick at each port so we can check their names later
        // Most of these objects will go unused
        for (int i = 0; i < 6; i++) {
            controllers.add(new Joystick(i));
        } 
    }

    /**
     * @param name the name of the controller to access
     * @param defaultPort the port to access if a controller with the provided name is not found
     * 
     * @return the joystick with the provided name or at the provided defaultPort
     */
    public Joystick get(String name, int defaultPort) {
        // Filter the list of controllers for ones that contain the provided name
        Optional<Joystick> joystick = controllers.stream()
            .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
            .findFirst();

        if (joystick.isPresent()) {
            return joystick.get();
        }

        // If we didn't find a controller with the provided name return the one at the default port
        return controllers.get(defaultPort);
    }
}
