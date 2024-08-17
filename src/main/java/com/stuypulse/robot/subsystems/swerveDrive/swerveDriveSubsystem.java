//https://yagsl.gitbook.io/yagsl/overview/what-we-do//


package com.stuypulse.robot.subsystems.swerveDrive;

import java.io.File;
import java.util.function.DoubleSupplier;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import swervelib.SwerveDrive;
import swervelib.math.SwerveMath;
import swervelib.parser.SwerveControllerConfiguration;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class swerveDriveSubsystem extends SubsystemBase
{

    private final SwerveDrive swerveDriveInstance; 

    public swerveDriveSubsystem(File directory)
    {

        double driveConversionFactor = SwerveMath.calculateMetersPerRotation(Settings.Swerve.WHEEL_DIAMETER, Settings.Swerve.DRIVE_GEAR_RATIO);
        double angleConversionFactor = SwerveMath.calculateDegreesPerSteeringRotation(Settings.Swerve.ANGLE_GEAR_RATIO);
        
        // Only machine readable data is sent --- controls how much information is logged
        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;

        //SwerveParser reads JSON files found in deploy/swerve
        try
        {
           swerveDriveInstance = new SwerveParser(directory).createSwerveDrive(Settings.Swerve.MAX_SPEED);
        } 
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Headingcorrection will prevent rotation while the robot is moving
        swerveDriveInstance.setHeadingCorrection(Settings.Swerve.headingCorrection);
        swerveDriveInstance.setCosineCompensator(Settings.Swerve.consineCompensator);   
    }

    //Creates swervedrive 
    public swerveDriveSubsystem(SwerveDriveConfiguration driveConfiguration, SwerveControllerConfiguration controllerConfiguration)
    {
        swerveDriveInstance = new SwerveDrive(driveConfiguration, controllerConfiguration, Settings.Swerve.MAX_SPEED);
    }

    // Drive command, x translation, y translation, joystickangles.
    public Command drive(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier directionX, DoubleSupplier directionY)
    {
        return run(() -> {
        Translation2d scaledInputs = SwerveMath.scaleTranslation(new Translation2d(translationX.getAsDouble(),
        translationY.getAsDouble()), 0.8);

        driveFieldOriented(swerveDriveInstance.swerveController.getTargetSpeeds(scaledInputs.getX(), scaledInputs.getY(),
        directionX.getAsDouble(), directionY.getAsDouble(), swerveDriveInstance.getOdometryHeading().getRadians(), swerveDriveInstance.getMaximumVelocity()));
        });
    }
    
    public Command simDrive(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier roatation)
    {
        return run(() -> {
            //Calculates speed with joystick translation, joystick angle, current robot direction, and 
        driveFieldOriented(swerveDriveInstance.swerveController.getTargetSpeeds
        (translationX.getAsDouble(), translationY.getAsDouble(), roatation.getAsDouble() * Math.PI, 
        swerveDriveInstance.getOdometryHeading().getRadians(), swerveDriveInstance.getMaximumVelocity()));
     });
    }

    public void driveFieldOriented(ChassisSpeeds velocity)
    {
        swerveDriveInstance.driveFieldOriented(velocity);
    }

    public void setMotorBrake(boolean brake)
    {
        swerveDriveInstance.setMotorIdleMode(brake);
    }
}

