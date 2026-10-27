// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.LimitSwitchConfig.Behavior;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * This class defines the runtime mode used by AdvantageKit. The mode is always "real" when running
 * on a roboRIO. Change the value of "simMode" to switch between "sim" (physics sim) and "replay"
 * (log replay from a file).
 */
public final class Constants {
  public static final DashboardMode dashboardMode = DashboardMode.DEBUG;
  public static final Mode simMode = Mode.SIM;
  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }

  public static enum DashboardMode {
    // Competition Mode
    PROD,
    // Allows you to manually change values ie: flywheel speed
    DEBUG
  }

  public static final class IntakeConstants {
    // Can IDs
    public static final int intakeArmCanID = 13;
    public static final int intakeRollerCanID = 15;
    public static final int hopperRollerCanID = 14;

    // Motor Controller Config
    public static final SparkFlexConfig intakeRollersConfig = new SparkFlexConfig();
    public static final SparkFlexConfig hopperRollerConfig = new SparkFlexConfig();
    public static final SparkMaxConfig intakeArmConfig = new SparkMaxConfig();

    public static final double kPintakeArm = 0.0001;
    public static final double kIintakeArm = 0.0;
    public static final double kDintakeArm = 0.0;
    public static final double kVintakeArm = 12.0 / 5767;
    public static final double kGintakeArm = 0.5;

    public static final double intakeRollerPower = 0.25;
    public static final double hopperRollerPower = 0.25;

    public static final double intakeArmForwardLimit = 0.22;
    public static final double intakeArmReverseLimit = 0.405;

    public static final boolean intakeRollerReverse = true;
    public static final boolean intakeArmReverse = false;
    public static final boolean hopperRollerReverse = false;

    static {
      intakeArmConfig
          .closedLoop
          .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
          .p(kPintakeArm, ClosedLoopSlot.kSlot0)
          .i(kIintakeArm, ClosedLoopSlot.kSlot0)
          .d(kDintakeArm, ClosedLoopSlot.kSlot0)
          .outputRange(-1, 1, ClosedLoopSlot.kSlot0)
          .feedForward
          .kV(kVintakeArm, ClosedLoopSlot.kSlot0)
          .kG(kGintakeArm);

      intakeArmConfig
          .limitSwitch
          .forwardLimitSwitchType(Type.kNormallyClosed)
          .forwardLimitSwitchTriggerBehavior(Behavior.kStopMovingMotor)
          .reverseLimitSwitchType(Type.kNormallyClosed)
          .reverseLimitSwitchTriggerBehavior(Behavior.kStopMovingMotor);

      intakeArmConfig
          .softLimit
          .forwardSoftLimitEnabled(true)
          .reverseSoftLimitEnabled(true)
          .forwardSoftLimit(intakeArmForwardLimit)
          .reverseSoftLimit(intakeArmReverseLimit);

      intakeArmConfig.inverted(intakeArmReverse).idleMode(IdleMode.kBrake);
      intakeRollersConfig.inverted(intakeRollerReverse).idleMode(IdleMode.kCoast);
      hopperRollerConfig.inverted(hopperRollerReverse).idleMode(IdleMode.kCoast);
    }
  }

  public static final class ShooterConstants {

    public static final int lowerFeederCanID = 9;
    public static final int topFeederCadID = 12;
    public static final int primaryFlywheelCanID = 10;
    public static final int secondaryFlywheelCanID = 11;

    public static final double kPflywheel = 0.0001;
    public static final double kIflywheel = 0.0;
    public static final double kDflywheel = 0.0;

    public static final double kPfeeder = 0.0001;
    public static final double kIfeeder = 0.0;
    public static final double kDfeeder = 0.0;

    // Feedforward config
    // kV: volts/rpm
    public static final double kVflywheel = 12.0 / 5767;
    public static final double kVfeeder = 12.0 / 5767;

    public static final SparkFlexConfig primaryFlywheelConfig = new SparkFlexConfig();
    public static final SparkFlexConfig secondaryFlywheelConfig = new SparkFlexConfig();
    public static final SparkFlexConfig feederConfig = new SparkFlexConfig();

    public static final double feederRPM = 500;

    public static final boolean reverseFlywheel = false;
    public static final boolean reverseLowerFeeder = false;
    public static final boolean reverseTopFeeder = true;

    static {
      // Configure Motors.
      primaryFlywheelConfig.smartCurrentLimit(80).idleMode(IdleMode.kCoast);

      primaryFlywheelConfig.encoder.positionConversionFactor(1).velocityConversionFactor(1);

      primaryFlywheelConfig
          .closedLoop
          .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
          .p(kPflywheel, ClosedLoopSlot.kSlot0)
          .i(kIflywheel, ClosedLoopSlot.kSlot0)
          .d(kDflywheel, ClosedLoopSlot.kSlot0)
          .outputRange(-1, 1, ClosedLoopSlot.kSlot0)
          .feedForward
          .kV(kVflywheel, ClosedLoopSlot.kSlot0);

      primaryFlywheelConfig.inverted(reverseFlywheel);

      secondaryFlywheelConfig.follow(primaryFlywheelCanID, true).idleMode(IdleMode.kCoast);

      feederConfig.smartCurrentLimit(80).idleMode(IdleMode.kBrake);

      feederConfig.encoder.positionConversionFactor(1).velocityConversionFactor(1);

      feederConfig
          .closedLoop
          .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
          .p(kPfeeder, ClosedLoopSlot.kSlot0)
          .i(kIfeeder, ClosedLoopSlot.kSlot0)
          .d(kDfeeder, ClosedLoopSlot.kSlot0)
          .outputRange(-1, 1, ClosedLoopSlot.kSlot0)
          .feedForward
          .kV(kVfeeder, ClosedLoopSlot.kSlot0);
    }

    // Interpolation for shooter
    public static final InterpolatingDoubleTreeMap shooterMap = new InterpolatingDoubleTreeMap();

    static {
      // Update Shooter Tree Map
      // Key: distance (in meters)
      // Value: flywheel RPM
      shooterMap.put(0.0, 0.0);
    }
  }
}
