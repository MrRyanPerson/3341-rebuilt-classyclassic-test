package frc.robot.subsystems.shooter;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {

  private SparkFlex primaryFlywheelMotor;
  private SparkFlex secondaryFlywheelMotor;
  private SparkFlex topFeederMotor;
  private SparkFlex lowerFeederMotor;

  private RelativeEncoder flywheelRelativeEncoder;
  private RelativeEncoder topFeederRelativeEncoder;
  private RelativeEncoder lowerFeederRelativeEncoder;

  private SparkClosedLoopController primaryFlywheelController;
  private SparkClosedLoopController topFeederController;
  private SparkClosedLoopController lowerFeederController;

  private int flywheelTargetRPM = 0;
  private double topFeederTargetPower = 0.0;
  private double lowerFeederTargetPower = 0.0;

  public Shooter() {
    // Create Motors
    primaryFlywheelMotor =
        new SparkFlex(ShooterConstants.primaryFlywheelCanID, MotorType.kBrushless);
    secondaryFlywheelMotor =
        new SparkFlex(ShooterConstants.secondaryFlywheelCanID, MotorType.kBrushless);
    topFeederMotor = new SparkFlex(ShooterConstants.topFeederCadID, MotorType.kBrushless);
    lowerFeederMotor = new SparkFlex(ShooterConstants.lowerFeederCanID, MotorType.kBrushless);

    // Encoders
    flywheelRelativeEncoder = primaryFlywheelMotor.getEncoder();
    topFeederRelativeEncoder = topFeederMotor.getEncoder();
    lowerFeederRelativeEncoder = lowerFeederMotor.getEncoder();

    // Motor Configs
    primaryFlywheelMotor.configure(
        ShooterConstants.primaryFlywheelConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kNoPersistParameters);
    secondaryFlywheelMotor.configure(
        ShooterConstants.secondaryFlywheelConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kNoPersistParameters);
    topFeederMotor.configure(
        ShooterConstants.feederConfig.inverted(ShooterConstants.reverseTopFeeder),
        ResetMode.kResetSafeParameters,
        PersistMode.kNoPersistParameters);
    lowerFeederMotor.configure(
        ShooterConstants.feederConfig.inverted(ShooterConstants.reverseLowerFeeder),
        ResetMode.kResetSafeParameters,
        PersistMode.kNoPersistParameters);

    // Closed Loop Controller
    primaryFlywheelController = primaryFlywheelMotor.getClosedLoopController();
  }

  // Motor Control
  public void setFlywheelRPM(int rpm) {
    this.flywheelTargetRPM = rpm;
  }

  public void setTopFeederPower(double power) {
    this.topFeederTargetPower = power;
  }

  public void setLowerFeederPower(double power) {
    this.lowerFeederTargetPower = power;
  }

  // Getters
  public int getFlywheelTargetRPM() {
    return flywheelTargetRPM;
  }

  public double getFlywheelEncoderRPM() {
    return flywheelRelativeEncoder.getVelocity();
  }

  public double getTopFeederTarget() {
    return topFeederTargetPower;
  }

  public double getLowerFeederTarget() {
    return lowerFeederTargetPower;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    primaryFlywheelController.setSetpoint(flywheelTargetRPM, ControlType.kVelocity);
    topFeederController.setSetpoint(topFeederTargetPower, ControlType.kDutyCycle);
    lowerFeederController.setSetpoint(lowerFeederTargetPower, ControlType.kDutyCycle);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
