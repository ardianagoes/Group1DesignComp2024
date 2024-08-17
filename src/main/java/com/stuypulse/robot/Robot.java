/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import swervelib.parser.SwerveParser;
import com.stuypulse.robot.constants.Settings;
public class Robot extends TimedRobot {

    private RobotContainer robot;
    private Command auto;
    private Timer disabledTimer;
    /*************************/
    /*** ROBOT SCHEDULEING ***/
    /*************************/

    @Override
    public void robotInit() {
        robot = new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    /*********************/
    /*** DISABLED MODE ***/
    /*********************/

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {
        if (disabledTimer.hasElapsed(Settings.Swerve.WHEEL_LOCK_TIME))
        {
          robot.setMotorBrake(false);
          disabledTimer.stop();
        }
    }

    /***********************/
    /*** AUTONOMOUS MODE ***/
    /***********************/  

    @Override
    public void autonomousInit() {
        auto = robot.getAutonomousCommand();

        if (auto != null) {
            auto.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    /*******************/
    /*** TELEOP MODE ***/
    /*******************/

    @Override

    // When teleop is initated, stop autonomous.
    public void teleopInit() {
        if (auto != null) {
            auto.cancel();
        }
        robot.setDriveMode();
        robot.setMotorBrake(true);
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    /*****************/
    /*** TEST MODE ***/
    /*****************/

    @Override
    public void testInit() {
        // Cancels all commands 
    CommandScheduler.getInstance().cancelAll();
        try
    {
        // If there is a missing json file, give an error
      new SwerveParser(new File(Filesystem.getDeployDirectory(), "swerve"));
    } catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}
}
