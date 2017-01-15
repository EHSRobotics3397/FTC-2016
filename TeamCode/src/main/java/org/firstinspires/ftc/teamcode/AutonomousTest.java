package org.firstinspires.ftc.robotcontroller.internal;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name = "AutonomousTest", group = "Drive")

public class AutonomousTest extends OpMode {

    //Creates two motor types
    DcMotor motorRight;
    DcMotor motorLeft;
    int leftEncoderStart = 0;
    int rightEncoderStart = 0;

    boolean motorRun;

    GameButton myButton;

    @Override
    public void init() {

        myButton = new GameButton(gamepad1, GameButton.Label.a);
        //Finds the motors on the hardware map and sets the define type to the motor

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        leftEncoderStart = motorLeft.getCurrentPosition();
        rightEncoderStart = motorRight.getCurrentPosition();

        motorRun = false;
    }

    @Override
    public void loop() {
        myButton.Update();

        if (myButton.Press()) {
            motorRun = !motorRun;
        }


        if (motorRun) {
            motorLeft.setPower(.25);
            motorRight.setPower(.25);
        }
        else {
            motorLeft.setPower(0);
            motorRight.setPower(0);
        }

    }

}

