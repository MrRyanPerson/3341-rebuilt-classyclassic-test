package frc.robot.subsystems.dashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DashboardMode;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.shooter.Shooter;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

public class Dashboard extends SubsystemBase {
  /* Creates network sendables using AdvantageKit and makes public
  so commands can use them ie: flywheel can call logged network number from here in
  debug mode */

  // Shooter Info
  public LoggedNetworkNumber flywheelSetpointInput;
  public LoggedNetworkNumber feederPowerInput;
  public Shooter shooter;

  public Dashboard(Shooter shooter) {
    // Shooter Vars
    this.shooter = shooter;
    flywheelSetpointInput = new LoggedNetworkNumber("Flywheel Setpoint", 3000);
    feederPowerInput =
        new LoggedNetworkNumber("Feeder Power Setpoint", ShooterConstants.feederPower);
  }

  @Override
  public void periodic() {
    // Reset values if in Prod Mode
    if (Constants.dashboardMode == DashboardMode.PROD) {
      flywheelSetpointInput.set(shooter.getFlywheelTargetRPM());
      feederPowerInput.set(shooter.getTopFeederTarget());
    }
  }
}
