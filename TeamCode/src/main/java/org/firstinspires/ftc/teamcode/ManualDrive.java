package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.modules.*;
/**
 * Created by greenteam on 1/12/17.
 * Main Drive Opmode
 */

@TeleOp(name = "ManualDrive", group = "Drive")

public class ManualDrive extends OpMode {

    private DcMotor     rightMotor;
    private DcMotor     leftMotor;
    private DcMotor     collectorMotor;
    private DcMotor     rewindMotor;
    private DcMotor     liftMotorRight;
    private DcMotor     liftMotorLeft;
    private Servo       latchServo;
    private TouchSensor rewindSensor;

    // Register Control Buttons for Gamepad2
    private GameButton  collectorForward    = new GameButton(gamepad2, GameButton.Label.RBumper);
    private GameButton  collectorBackward   = new GameButton(gamepad2, GameButton.Label.LBumper);
    private GameButton  rewindButton        = new GameButton(gamepad2, GameButton.Label.dpadDown);
    private GameButton  tensionButton       = new GameButton(gamepad2, GameButton.Label.dpadUp);
    private GameButton  lockButton          = new GameButton(gamepad2, GameButton.Label.a); // for testing purposes
    private GameButton  liftUp              = new GameButton(gamepad2, GameButton.Label.LTrigger);
    private GameButton  liftDown            = new GameButton(gamepad2, GameButton.Label.LBumper);
    private GameButton  fireButton          = new GameButton(gamepad2, GameButton.Label.RTrigger);
    private GameStick   driveStickLeft      = new GameStick(gamepad1, GameStick.Label.Left);
    private GameStick   driveStickRight     = new GameStick(gamepad1, GameStick.Label.Right);

    private CarDrive carDrive;
    private Collector collector;
    private Thrower thrower;
    private Lift lift;
    private Prong prong;
    private Latch latch;
    private ThrowTest throwtest;

    @Override
    public void init(){

        leftMotor       = hardwareMap.dcMotor.get("leftMotor");
        rightMotor      = hardwareMap.dcMotor.get("rightMotor");

        collectorMotor  = hardwareMap.dcMotor.get("collectMotor");

        rewindMotor  = hardwareMap.dcMotor.get("throwMotor");
        latchServo   = hardwareMap.servo.get("latchServo");
        rewindSensor = hardwareMap.touchSensor.get("rewindSensor");

        liftMotorRight  = hardwareMap.dcMotor.get("rightLiftMotor");
        liftMotorLeft   = hardwareMap.dcMotor.get("leftLiftMotor");

        thrower = new Thrower();
        thrower.setup(rewindMotor, rewindSensor, gamepad2, latchServo); //add sensor

        carDrive = new CarDrive();
        carDrive.setup(leftMotor, rightMotor, gamepad1);

        collector = new Collector();
        collector.setup(collectorMotor, gamepad2);

        lift = new Lift();
        lift.setup(liftMotorLeft, liftMotorRight, gamepad2);

        //throwtest = new ThrowTest();
        //throwtest.setup(rewindMotor, rewindSensor, gamepad2, latchServo);

    }

    @Override
    public void loop() {

        collector.update(telemetry);
        carDrive.update(telemetry);
        lift.update(telemetry);
        thrower.update(telemetry);
        //throwtest.update(telemetry);
    }

}
