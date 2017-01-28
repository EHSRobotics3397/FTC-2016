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

@TeleOp(name = "UniDrive", group = "Drive")

public class ManualDrive extends OpMode {

    private DcMotor     rightMotor;
    private DcMotor     leftMotor;
    private DcMotor     collectorMotor;
    private DcMotor     rewindMotor;
    private DcMotor     liftMotorRight;
    private DcMotor     liftMotorLeft;
    private Servo       latchServo;
    private TouchSensor rewindSensor;

    private double[]    driveReturns    = new double[5];
    private boolean     collectorOn     = false;


    // Register Control Buttons for Gamepad2
    private GameButton  collectorButton     = new GameButton(gamepad2, GameButton.Label.RBumper);
    private GameButton  rewindButton        = new GameButton(gamepad2, GameButton.Label.dpadDown);
    private GameButton  tensionButton       = new GameButton(gamepad2, GameButton.Label.dpadUp);
    private GameButton  lockButton          = new GameButton(gamepad2, GameButton.Label.a); // for testing purposes
    private GameButton  liftUp              = new GameButton(gamepad2, GameButton.Label.LTrigger);
    private GameButton  liftDown            = new GameButton(gamepad2, GameButton.Label.LBumper);
    private GameButton  fireButton          = new GameButton(gamepad2, GameButton.Label.RTrigger);
    private GameButton  prongButton         = new GameButton(gamepad2, GameButton.Label.b);

    // Register Drive buttons for Gamepad1
    private GameButton  spinRightButton     = new GameButton(gamepad1, GameButton.Label.dpadRight);
    private GameButton  spinLeftButton      = new GameButton(gamepad1, GameButton.Label.dpadLeft);
    private GameButton  driveStickLeft      = new GameButton(gamepad1, GameButton.Label.analogLeft);
    private GameButton  driveStickRight     = new GameButton(gamepad1, GameButton.Label.analogRight);

    private SpinDrive spinDrive;
    private CarDrive carDrive;
    private Collector collector;
    private Thrower thrower;
    private Lift lift;
    private Prong prong;

    @Override
    public void init(){
        leftMotor      = hardwareMap.dcMotor.get("leftMotor");
        rightMotor     = hardwareMap.dcMotor.get("rightMotor");
        collectorMotor = hardwareMap.dcMotor.get("collectMotor");
        rewindMotor    = hardwareMap.dcMotor.get("throwMotor");
        liftMotorRight = hardwareMap.dcMotor.get("rightLiftMotor");
        liftMotorLeft  = hardwareMap.dcMotor.get("leftLiftMotor");
        latchServo     = hardwareMap.servo.get("latchServo");
        rewindSensor   = hardwareMap.touchSensor.get("rewindSensor");


        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        thrower = new Thrower();
        thrower.setup(rewindMotor, latchServo, rewindButton, tensionButton, fireButton, rewindSensor); //add sensor

        spinDrive = new SpinDrive();
        spinDrive.setup(spinRightButton, spinLeftButton, leftMotor, rightMotor );

        carDrive = new CarDrive();
        carDrive.setup(leftMotor, rightMotor, driveStickLeft, driveStickRight);

        collector = new Collector();
        collector.setup(collectorMotor, collectorButton);

        lift = new Lift();
        lift.setup(liftMotorLeft, liftMotorRight, liftUp, liftDown);

        prong = new Prong();
        prong.setup(prongButton, liftMotorLeft, liftMotorRight);


    }

    @Override
    public void loop(){
        collector.update();
        carDrive.update();   //not using returns for now
        spinDrive.update();
        lift.update();
        thrower.update();
        Display();
    }//Lel <-- carl

    private void Display(){
        telemetry.addData("Collector Running",   ": " + collectorOn);
        //telemetry.addData("Drive Left",          ": " + driveReturns[0]);
        //telemetry.addData("Drive Right",         ": " + driveReturns[1]);
        //telemetry.addData("Drive Scale",         ": " + driveReturns[2]);
        //telemetry.addData("Drive X",             ": " + driveReturns[3]);
        //telemetry.addData("Drive Y",             ": " + driveReturns[4]);

    }

}
