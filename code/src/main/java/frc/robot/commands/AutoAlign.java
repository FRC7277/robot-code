/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.RobotMap;
import frc.robot.Robot;

/**
 * An example command.  You can replace me with your own command.
 */
public class AutoAlign extends Command {
  
  private double centerX;
  private double difference;
  private double time;

  public AutoAlign() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    this.time = RobotMap.alignTimeout;
    
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    setTimeout(this.time);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // Get the centerX of the vision thread output
    this.centerX = Robot.visionProcess.getTargetCenterX();
    // Use the center x to find the distance from center - in pixels
    this.difference = this.centerX - (RobotMap.cameraWidth / 2);
    // Turn based on difference to align with center
    Robot.drivetrain.getDrive().arcadeDrive(0, difference * RobotMap.alignModifier);
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // Run until an acceptable tolerance or timeout
    return ((Math.abs(this.difference) <= RobotMap.alignTolerance) || isTimedOut());
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drivetrain.getDrive().arcadeDrive(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.end();
  }
}
