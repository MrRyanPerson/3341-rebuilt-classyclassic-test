package frc.robot.subsystems.intake;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  private SparkFlex intakeArmMotor;
  private SparkFlex intakeRollerMotor;
  private SparkFlex hopperRollerMotor;

  private AbsoluteEncoder intakeArmEncoder;
  private RelativeEncoder intakeRollerEncoder;
  private RelativeEncoder hopperRollerEncoder;

  private SparkClosedLoopController intakeArmController;
  private SparkClosedLoopController intakeRollerController;
  private SparkClosedLoopController hopperRollerController;

  private SparkLimitSwitch intakeArmForwardLimitSwitch;
  private SparkLimitSwitch intakeArmReverseLimitSwitch;

  private boolean armDeployed;

  public Intake() {
    intakeArmMotor = new SparkFlex(IntakeConstants.intakeArmCanID, MotorType.kBrushless);
    intakeRollerMotor = new SparkFlex(IntakeConstants.intakeRollerCanID, MotorType.kBrushless);
    hopperRollerMotor = new SparkFlex(IntakeConstants.hopperRollerCanID, MotorType.kBrushless);

    intakeArmEncoder = intakeArmMotor.getAbsoluteEncoder();
    intakeRollerEncoder = intakeRollerMotor.getEncoder();
    hopperRollerEncoder = hopperRollerMotor.getEncoder();

    intakeArmController = intakeArmMotor.getClosedLoopController();
    intakeRollerController = intakeRollerMotor.getClosedLoopController();
    hopperRollerController = hopperRollerMotor.getClosedLoopController();

    intakeArmForwardLimitSwitch = intakeArmMotor.getForwardLimitSwitch();
    intakeArmReverseLimitSwitch = intakeArmMotor.getReverseLimitSwitch();

    armDeployed = getDeployed();
  }

  public void setIntakeArmVelocity(int rpm) {
    intakeArmController.setSetpoint(rpm, ControlType.kVelocity);
  }

  public void setIntakeRollerPower(double power) {
    intakeRollerController.setSetpoint(power, ControlType.kDutyCycle);
  }

  public void setHopperRollerPower(double power) {
    hopperRollerController.setSetpoint(power, ControlType.kDutyCycle);
  }

  public double getArmPosition() {
    return intakeArmEncoder.getPosition();
  }

  public boolean getForwardLimitSwitch() {
    return intakeArmForwardLimitSwitch.isPressed();
  }

  public boolean getReverseLimitSwitch() {
    return intakeArmReverseLimitSwitch.isPressed();
  }

  public boolean getDeployed() {
    if (getArmPosition() >= 0.22 || getForwardLimitSwitch()) {
      return true;
    }
    return false;
  }

  public boolean getHopperRunning() {
    return hopperRollerController.getSetpoint() > 0.0;
  }

  public boolean getIntakeRunning() {
    return intakeRollerController.getSetpoint() > 0.0;
  }
}
