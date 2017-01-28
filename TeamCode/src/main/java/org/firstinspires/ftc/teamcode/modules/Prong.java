package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.GameButton;

import java.util.concurrent.TimeUnit;

/**
 * Created by Willem Hunt on 1/28/2017.
 * extends the prongs on button press
 */

public class Prong {
    private boolean out = false;
    private GameButton button;

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    public void setup(GameButton button, DcMotor leftMotor, DcMotor rightMotor){
        this.button     = button;
        this.leftMotor  = leftMotor;
        this.rightMotor = rightMotor;
    }

    public void update() throws InterruptedException {
        if(button.Press()&& !out){
            leftMotor.setPower(0.25);
            rightMotor.setPower(0.25);
            TimeUnit.SECONDS.sleep(1);
            leftMotor.setPower(-0.25);
            rightMotor.setPower(-0.25);
            TimeUnit.SECONDS.sleep(1);
            leftMotor.setPower(0.00);
            rightMotor.setPower(0.00);
            out = true;
        }
    }
}
