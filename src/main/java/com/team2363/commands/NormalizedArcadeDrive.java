package com.team2363.commands;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 
 * @deprecated Use HelixArcadeDrive instead
 */
@Deprecated
public abstract class NormalizedArcadeDrive extends HelixDrive { 

    public NormalizedArcadeDrive(Subsystem subsystem) {
        requires(subsystem);
    }

    public NormalizedArcadeDrive() { }
}
