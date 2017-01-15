
package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 *  BTOP is a Basic TeleOp program that simply
 */
@TeleOp(name = "Car Drive", group = "Drive")
//@Disabled
public class CarDrive extends OpMode {

    //Creates two motor types
    DcMotor motorRight;
    DcMotor motorLeft;
    double scaleFactor = 1;
    double spinThreshold = 0.2;
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
    }

    @Override
    public void loop() {

        //Read the joystick
        double y = gamepad1.left_stick_y;
        double x = gamepad1.right_stick_x;

        double rightSteer = 1.0 - (x + 1.0) / 2.0;
        double leftSteer = (x + 1.0) / 2.0;

        double right;
        double left;

        double thrust = -y;
        if (Math.abs(thrust) > spinThreshold) {
            right = thrust * rightSteer / scaleFactor;
            left = thrust * leftSteer / scaleFactor;
        }
        else {
            right = -x/scaleFactor;
            left  = x/scaleFactor;
        }

        //Clip the values to fit within the motor's limits
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        motorRight.setPower(right);
        motorLeft.setPower(left);

        //Write the information to the telemetry screen
        telemetry.addData("tgt pwr", "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("scaleFactor", ": " + String.format("%.2f",scaleFactor));
        telemetry.addData("x,y", ": " + String.format("%.2f", x) + ", " + String.format("%.2f", y));

        ReadEncoders();
    }

    public void ReadEncoders() {
        //read the encoders and display (basic encoder testing)
        int leftCount = motorLeft.getCurrentPosition() - leftEncoderStart;
        int rightCount = motorRight.getCurrentPosition() - rightEncoderStart;

        telemetry.addData("encoder left", ": " + String.format("%d", leftCount));
        telemetry.addData("encoder right", ": " + String.format("%d", rightCount));

        int testvar = 0;
    }

}



