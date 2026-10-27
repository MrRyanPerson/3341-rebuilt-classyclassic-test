package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.dashboard.Dashboard;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.Shooter;

public class ShooterCommands {

  private ShooterCommands() {}

  private static double getShooterMapRPM(double distance) {
    return ShooterConstants.shooterMap.get(distance);
  }

  public static Command toggleFlywheel(Shooter shooter, Dashboard dashboard, Drive drive) {
    switch (Constants.dashboardMode) {
      case DEBUG:
        return Commands.runEnd(
            () -> {
              shooter.setFlywheelRPM((int) dashboard.flywheelSetpointInput.get());
            },
            () -> {
              shooter.setFlywheelRPM(0);
            });
      case PROD:
        return Commands.runEnd(
            () -> {
              // TODO: Create pose estimation to get shooter distance
              shooter.setFlywheelRPM((int) getShooterMapRPM(0.0));
            },
            () -> {
              shooter.setFlywheelRPM(0);
            });
      default:
        return Commands.runEnd(
            () -> {
              shooter.setFlywheelRPM((int) getShooterMapRPM(0.0));
            },
            () -> {
              shooter.setFlywheelRPM(0);
            });
    }
  }

  public static Command toggleFeed(Shooter shooter, Dashboard dashboard) {
    switch (Constants.dashboardMode) {
      case DEBUG:
        return Commands.runEnd(
            () -> {
              shooter.setTopFeederPower(dashboard.feederPowerInput.get());
              shooter.setLowerFeederPower(dashboard.feederPowerInput.get());
            },
            () -> {
              shooter.setTopFeederPower(0);
              shooter.setLowerFeederPower(0);
            });
      case PROD:
        return Commands.runEnd(
            () -> {
              shooter.setTopFeederPower(ShooterConstants.feederPower);
              shooter.setLowerFeederPower(ShooterConstants.feederPower);
            },
            () -> {
              shooter.setTopFeederPower(0);
              shooter.setLowerFeederPower(0);
            });
      default:
        return Commands.runEnd(
            () -> {
              shooter.setTopFeederPower(ShooterConstants.feederPower);
              shooter.setLowerFeederPower(ShooterConstants.feederPower);
            },
            () -> {
              shooter.setTopFeederPower(0);
              shooter.setLowerFeederPower(0);
            });
    }
  }
}
