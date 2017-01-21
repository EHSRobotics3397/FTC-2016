package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//@Autonomous(name = "EncoderTest", group = "Drive")
@TeleOp(name = "EncoderTest", group = "Drive")

public class EncoderEventAutonomousTest extends OpMode {

    //Creates two motor types
    DcMotor motorRight;
    DcMotor motorLeft;

    int leftEncoderStart = 0;
    int rightEncoderStart = 0;
    int leftCount = 0;
    int rightCount = 0;

    boolean motorRun;

    boolean motorLeftRun;
    boolean motorRightRun;

    private int tickTarget = 0;
    private int tickRemain = 0;
    static private int rampRevs = 1;
    static private double wheelDiameter = 7.0;
    static private double revDistance = Math.PI * wheelDiameter;
    static int TICKS_PER_REVOLUTION = 560;

    GameButton runButton;
    GameButton stopButton;

    @Override
    public void init() {

        runButton = new GameButton(gamepad1, GameButton.Label.a);
        stopButton = new GameButton(gamepad1, GameButton.Label.x);

        //Finds the motors on the hardware map and sets the define type to the motor
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorRun = false;
        EncoderStart();
    }

    @Override
    public void loop() {

        runButton.Update();
        if (runButton.Press()) {
            motorRun = !motorRun;
            //if just started motor...
            if (motorRun) {
                EncoderStart();
                double distance = 48.0; //inches
                //tickTarget = (int)(distance/revDistance*TICKS_PER_REVOLUTION);
                tickTarget = 10 *TICKS_PER_REVOLUTION; //10 revs
                motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }

        if (!motorRun)
            StopMotors();
        else {
            double pwr;
            tickRemain = tickTarget - leftCount;
            if (tickRemain > 0 ) {
                pwr = 0.25 * (double) (tickRemain) / tickTarget;
                double minPower = 0.10;
                if (pwr < minPower)
                    pwr = minPower;
            }
            else
                pwr = 0.0;

            motorRight.setPower(pwr);
            motorLeft.setPower(pwr);
        }

        stopButton.Update();
        if (stopButton.Press()) {
            StopMotors();
        }
        ReadEncoders();
    }

    private void ReadEncoders() {
        //read the encoders and display (basic encoder testing)
        leftCount = motorLeft.getCurrentPosition() - leftEncoderStart;
        rightCount = motorRight.getCurrentPosition() - rightEncoderStart;
    }

    private void DisplayData() {
        telemetry.addData("encoder L", ": " + String.format("%d", leftCount));
        telemetry.addData("encoder R", ": " + String.format("%d", rightCount));
        if (motorRun)
            telemetry.addData("Motor is", ": " + (motorRun ? "ON" : "OFF"));
        telemetry.addData("target R", ": " + String.format("%d", tickTarget));
        telemetry.addData("remain R", ": " + String.format("%d", tickRemain));
    }

    private void EncoderStart() {
        leftEncoderStart = motorLeft.getCurrentPosition();
        rightEncoderStart = motorRight.getCurrentPosition();
    }

    private void StopMotors() {
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }
}

