/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2363.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class HelixConditionalCommand extends ConditionalCommand {

  Supplier<Boolean> condition;

  public HelixConditionalCommand(Command trueCommand, Command falseCommand, Supplier<Boolean> condition) {
    super(trueCommand, falseCommand);
  }

  @Override
  protected boolean condition() {
    return condition.get();
  }
}
