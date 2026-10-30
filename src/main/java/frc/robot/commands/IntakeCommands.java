package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.dashboard.Dashboard;
import frc.robot.subsystems.intake.Intake;

public class IntakeCommands {

  public static Command toggleHopper(Intake intake, Dashboard dashboard) {
    switch (Constants.dashboardMode) {
      case DEBUG:
        return Commands.runEnd(
            () -> {
              intake.setHopperRollerPower(dashboard.hopperRollerPowerInput.get());
            },
            () -> {
              intake.setHopperRollerPower(0.0);
            });

      case PROD:
        return Commands.runEnd(
            () -> {
              intake.setHopperRollerPower(IntakeConstants.hopperRollerPower);
            },
            () -> {
              intake.setHopperRollerPower(0.0);
            });

      default:
        return Commands.runEnd(
            () -> {
              intake.setHopperRollerPower(IntakeConstants.hopperRollerPower);
            },
            () -> {
              intake.setHopperRollerPower(0.0);
            });
    }
  }

  public static Command toggleArm(Intake intake) {

    Command extendArm =
        Commands.run(
                () -> {
                  intake.setIntakeArmVelocity((int) IntakeConstants.intakeArmForwardVelocity);
                })
            .until(() -> intake.getForwardLimitSwitch());

    Command retractArm =
        Commands.run(
                () -> {
                  intake.setIntakeArmVelocity((int) IntakeConstants.intakeArmReverseVelocity);
                })
            .until(() -> intake.getReverseLimitSwitch());

    if (intake.getDeployed()) {
      return retractArm;
    }
    return extendArm;
  }

  public static Command startIntake(Intake intake, Dashboard dashboard) {
    switch (Constants.dashboardMode) {
      case DEBUG:
        return Commands.runOnce(
            () -> {
              intake.setIntakeRollerPower(dashboard.feederPowerInput.get());
            });

      case PROD:
        return Commands.runOnce(
            () -> {
              intake.setIntakeRollerPower(IntakeConstants.intakeRollerPower);
            });

      default:
        return Commands.runOnce(
            () -> {
              intake.setIntakeRollerPower(IntakeConstants.intakeRollerPower);
            });
    }
  }

  public static Command stopIntake(Intake intake, Dashboard dashboard) {
    return Commands.runOnce(() -> intake.setIntakeRollerPower(0));
  }

  public static Command toggleIntake(Intake intake, Dashboard dashboard) {
    switch (Constants.dashboardMode) {
      case DEBUG:
        return Commands.runEnd(
            () -> {
              intake.setIntakeRollerPower(dashboard.feederPowerInput.get());
            },
            () -> {
              intake.setIntakeRollerPower(0.0);
            });

      case PROD:
        return Commands.runEnd(
            () -> {
              intake.setIntakeRollerPower(IntakeConstants.intakeRollerPower);
            },
            () -> {
              intake.setIntakeRollerPower(0.0);
            });

      default:
        return Commands.runEnd(
            () -> {
              intake.setIntakeRollerPower(IntakeConstants.intakeRollerPower);
            },
            () -> {
              intake.setIntakeRollerPower(0.0);
            });
    }
  }
}
