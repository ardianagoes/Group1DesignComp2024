/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.Unit;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public interface Settings {


public interface Swerve
{
    double WHEEL_LOCK_TIME = 10; 
    double WHEEL_DIAMETER = 4;
    double DRIVE_GEAR_RATIO = 0;
    double ANGLE_GEAR_RATIO = 0;
    double MAX_SPEED = Units.feetToMeters(14.5);

    boolean headingCorrection = false;
    //Disable when using sims
    boolean consineCompensator = false;

  
}
  



}
