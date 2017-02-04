package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GameButton;

/**
 * Created by Willem Hunt on 1/27/2017.
 * Controls the motion of the lift
 */

public class Lift {

    private String      liftStatus;
    private DcMotor     motorLeft;
    private DcMotor     motorRight;
    private GameButton  x;
    private GameButton  y;


    public void setup(DcMotor left, DcMotor right, Gamepad pad){
        motorLeft  = left;
        motorRight = right;
        y = new GameButton(pad, GameButton.Label.y);
        x = new GameButton(pad, GameButton.Label.x);

        motorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void update(Telemetry telemetry){

        x.Update();
        y.Update();

        if(!x.IsDown() && !y.IsDown()){
            liftIdle();
        }else if(x.IsDown()){
            liftDown();
        }else{
            liftUp();
        }
        telemetry.addData("Lift",  ": " + liftStatus);
    }

    public void liftIdle(){
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        liftStatus = "Idle";
    }

    public void liftUp(){
        double UP_SPEED = 0.1;
        motorLeft.setPower(UP_SPEED);
        motorRight.setPower(UP_SPEED);
        liftStatus = "Up";
    }

    public void liftDown(){
        double DOWN_SPEED = -0.1;
        motorLeft.setPower(DOWN_SPEED);
        motorRight.setPower(DOWN_SPEED);
        liftStatus = "Down";
    }




}
