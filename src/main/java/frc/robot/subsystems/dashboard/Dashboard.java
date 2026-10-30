package frc.robot.subsystems.dashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DashboardMode;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

public class Dashboard extends SubsystemBase {
  /* Creates network sendables using AdvantageKit and makes public
  so commands can use them ie: flywheel can call logged network number from here in
  debug mode */

  
  // Network Table Prefix
  private String ntPrefix = "DriverDashboard/";

  // Shooter Info
  public LoggedNetworkNumber flywheelSetpointInput;
  public LoggedNetworkNumber feederPowerInput;
  private Shooter shooter;

  // Intake Stuff
  public LoggedNetworkNumber intakeRollerPowerInput;
  public LoggedNetworkNumber hopperRollerPowerInput;
  private Intake intake;

  // Drive
  public Drive drive;

  public Dashboard(Shooter shooter, Intake intake, Drive drive) {
    // Shooter Vars
    this.shooter = shooter;
    flywheelSetpointInput = new LoggedNetworkNumber("Flywheel Setpoint", 3000);
    feederPowerInput =
        new LoggedNetworkNumber("Feeder Power Setpoint", ShooterConstants.feederPower);

    // Intake Vars
    this.intake = intake;
    intakeRollerPowerInput =
        new LoggedNetworkNumber("Intake Roller Power", IntakeConstants.intakeRollerPower);
    hopperRollerPowerInput =
        new LoggedNetworkNumber("Hopper Roller Power", IntakeConstants.hopperRollerPower);

    // Drive Vars
    this.drive = drive;
  }

  @Override
  public void periodic() {
    // Reset values if in Prod Mode
    if (Constants.dashboardMode == DashboardMode.PROD) {
      flywheelSetpointInput.set(shooter.getFlywheelTargetRPM());
      feederPowerInput.set(shooter.getTopFeederTarget());

      intakeRollerPowerInput.set(IntakeConstants.intakeRollerPower);
      hopperRollerPowerInput.set(IntakeConstants.hopperRollerPower);
    }

    // Readonly Output
    Logger.recordOutput(ntPrefix + "Intake Arm Deployed", intake.getDeployed());
    Logger.recordOutput(ntPrefix + "Intake Arm Position", intake.getArmPosition());
    Logger.recordOutput(ntPrefix + "Hopper Running", intake.getHopperRunning());
    Logger.recordOutput(ntPrefix + "Intake Running", intake.getIntakeRunning());

    Logger.recordOutput(ntPrefix + "Shooter Flywheel Encoder RPM", shooter.getFlywheelEncoderRPM());
    Logger.recordOutput(ntPrefix + "Flywheel Running", shooter.getFlywheelTargetRPM() > 0);

    Logger.recordOutput(ntPrefix + "Robot Pose", drive.getPose());
  }
}
