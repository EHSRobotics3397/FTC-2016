
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 *  BTOP is a Basic TeleOp program that simply
 */
@TeleOp(name = "Tank Drive", group = "Drive")
//@Disabled
public class TankDrive extends OpMode {

    //Creates two motor types
    DcMotor motorRight;
    DcMotor motorLeft;
    double scaleFactor = 3;

    int leftEncoderStart = 0;
    int rightEncoderStart = 0;

    @Override
    public void init() {

        //Finds the motors on the hardware map and sets the define type to the motor

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        leftEncoderStart = motorLeft.getCurrentPosition();
        rightEncoderStart = motorRight.getCurrentPosition();

        //One motor on each side of the robot so one needs to be reversed

        /* This init is for running the motors using encoders */
        // Do not do RESET_ENCODERS and RUN_WITHOUT_ENCODERS
        // If you do - it will not run.

        // run with encoders
        //        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);

        // run without
        //setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //setDrivePower(0.0, 0.0);


    }

    @Override
    public void loop() {

        if(gamepad1.a) scaleFactor = 1;
        if(gamepad1.b) scaleFactor = 3;
        //Read the joystick
        double right = -gamepad1.right_stick_y / scaleFactor;
        double left = -gamepad1.left_stick_y / scaleFactor;

        //Clip the values to fit within the motor's limits
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        motorRight.setPower(right);
        motorLeft.setPower(left);

        //Write the information to the telemetry screen
        telemetry.addData("tgt pwr", "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("scaleFactor", ": " + String.format("%.2f",scaleFactor));

        ReadEncoders();
    }

    public void ReadEncoders() {
        //read the encoders and display (basic encoder testing)
        int leftCount = motorLeft.getCurrentPosition() - leftEncoderStart;
        int rightCount = motorRight.getCurrentPosition() - rightEncoderStart;

        telemetry.addData("encoder left", ": " + String.format("%d", leftCount));
        telemetry.addData("encoder right", ": " + String.format("%d", rightCount));
    }

}



