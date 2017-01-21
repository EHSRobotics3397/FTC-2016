package org.firstinspires.ftc.teamcode;

/**
 * Created by greenteam on 1/20/17.
 */

import com.qualcomm.robotcore.hardware.DcMotor;

public class SpinDriveModule {

    private GameButton leftButton;
    private GameButton rightButton;
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private int leftEncoderStart = 0;
    private int rightEncoderStart = 0;
    private int leftCount = 0;
    private int rightCount = 0;

    private boolean isSpinning;
    private boolean motorLeftRun;
    private boolean motorRightRun;

    private int tickTarget = 0;
    private int tickRemain = 0;
    static private int rampRevs = 1;
    static private double wheelDiameter = 7.0;
    static private double revDistance = Math.PI * wheelDiameter;
    static int TICKS_PER_REVOLUTION = 560;

    public void setup(GameButton lb, GameButton rb, DcMotor lm, DcMotor rm) {
        leftButton = lb;
        rightButton = rb;
        leftMotor = lm;
        rightMotor = rm;
        EncoderStart();
        isSpinning = false;
    }

    public void update(){

        //motorRun = false;

        leftButton.Update();
        rightButton.Update();

        if (leftButton.Press()) {                 //Spin(sweep turn) Left
            isSpinning = !isSpinning;
            if (isSpinning) {
                EncoderStart();
                double distance = 48.0; //inches
                //tickTarget = (int)(distance/revDistance*TICKS_PER_REVOLUTION);
                tickTarget = (int) (1.3 *TICKS_PER_REVOLUTION); // 1.3 revs is about 22" on a 54" circumference.
                rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }
        else if (!isSpinning) {
            StopMotors();
        }
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

            rightMotor.setPower(pwr);
            leftMotor.setPower(pwr);
        }

        ReadEncoders();
    }


    private void EncoderStart() {
        leftEncoderStart = leftMotor.getCurrentPosition();
        rightEncoderStart = rightMotor.getCurrentPosition();
    }

    private void StopMotors() {
        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);
    }

    private void ReadEncoders() {
        //read the encoders and display (basic encoder testing)
        leftCount = leftMotor.getCurrentPosition() - leftEncoderStart;
        rightCount = rightMotor.getCurrentPosition() - rightEncoderStart;
    }

}
