package com.team2363.utilities;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class will order your controllers into a list based on the provided
 * controller names. The list will be indexed at 0. This relies on each of the joysticks
 * reporting a different name.
 */
public class ControllerPatroller {

    // The ordered list of user defined joysticks
    private List<Joystick> controllers = new ArrayList<>();

    /**
     * @param controllerNames the list of controller names in the order the user would like to access them
     */
    public ControllerPatroller(String... controllerNames) {
        List<Joystick> unorderedJoysticks = getUnorderedJoysticks();
        orderJoysticks(unorderedJoysticks, controllerNames);
    }

    private void orderJoysticks(List<Joystick> unorderedJoysticks, String[] controllerNames) {
        for (String controllerName : controllerNames) {
            // Find the first controller that matches the provided name
            Joystick matchingJoystick = unorderedJoysticks
                .stream()
                .filter(j -> controllerName.equals(j.getName()))
                .findFirst()
                .get();

            // Add the matching controller to the controller list
            if (matchingJoystick != null) {
                controllers.add(matchingJoystick);
            }
        }
    }

    // Create joysticks for all six slots so we can see what their names are later when ordering
    private List<Joystick> getUnorderedJoysticks() {
        List<Joystick> unorderedJoysticks = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            unorderedJoysticks.add(new Joystick(i));
        }
        return unorderedJoysticks;
    }

    /**
     * @param index the index of the controller to access
     */
    public Joystick get(int index) {
        return controllers.get(index);
    }
}
