package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CarDriveModule;
import org.firstinspires.ftc.teamcode.CollectorModule;
import org.firstinspires.ftc.teamcode.ThrowerModule;
import org.firstinspires.ftc.teamcode.GameButton;

/**
 * Created by greenteam on 1/12/17.
 */

@TeleOp(name = "UniDrive", group = "Drive")

public class FTCVelocityVortex3397 extends OpMode {

    DcMotor     rightMotor;
    DcMotor     leftMotor;
    DcMotor     collectorMotor;
    DcMotor     rewindMotor;
    Servo       latchServo;

    double      driveX          = 0.0;
    double      driveY          = 0.0;
    double      rTrigger        = 0.0;
    double[]    driveReturns    = new double[5];
    boolean     collectorOn     = false;

    GameButton  collectorButton     = new GameButton(gamepad2, GameButton.Label.RBumper);
    GameButton  rewindButton        = new GameButton(gamepad2, GameButton.Label.dpadDown);
    GameButton  tensionButton       = new GameButton(gamepad2, GameButton.Label.dpadUp);
    GameButton  lockButton       = new GameButton(gamepad2, GameButton.Label.a);

    GameButton  spinRightButton = new GameButton(gamepad1, GameButton.Label.dpadRight);
    GameButton  spinLeftButton = new GameButton(gamepad1, GameButton.Label.dpadLeft);

    String      launchState     = "Latched";

    private SpinDriveModule spinModule;
    private CarDriveModule driveModule;

    @Override
    public void init(){
        leftMotor      = hardwareMap.dcMotor.get("motorLeft");
        rightMotor     = hardwareMap.dcMotor.get("motorRight");
        collectorMotor = hardwareMap.dcMotor.get("motorCollector");
        rewindMotor    = hardwareMap.dcMotor.get("motorRewind");
        latchServo     = hardwareMap.servo.get("servoLatch");

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        spinModule = new SpinDriveModule();
        spinModule.setup(spinRightButton, spinLeftButton, leftMotor, rightMotor );

        driveModule = new CarDriveModule();
        driveModule.setup(leftMotor, rightMotor);
    }

    @Override
    public void loop(){

        rTrigger    = gamepad2.right_trigger;
        driveY      = gamepad1.left_stick_y;
        driveX      = gamepad1.right_stick_x;

        if(collectorButton.Release()){
            CollectorModule.toggleCollector(collectorMotor, collectorOn);
            collectorOn = !collectorOn;
        }

        driveReturns = driveModule.drive(driveX, driveY);

        spinModule.update();

        Display();
    }

    public void Display(){
        telemetry.addData("Collector Running",   ": " + collectorOn);
        telemetry.addData("Drive Left",          ": " + driveReturns[0]);
        telemetry.addData("Drive Right",         ": " + driveReturns[1]);
        telemetry.addData("Drive Scale",         ": " + driveReturns[2]);
        telemetry.addData("Drive X",             ": " + driveReturns[3]);
        telemetry.addData("Drive Y",             ": " + driveReturns[4]);

    }

}
