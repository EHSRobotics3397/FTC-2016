package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.GameButton;

/**
 * Created by Willem Hunt on 1/27/2017.
 * Controls the motion of the lift
 */

public class Lift {

    private DcMotor     motorLeft;
    private DcMotor     motorRight;
    private GameButton  up;
    private GameButton  down;
    private double DOWN_SPEED = -0.25;
    private double UP_SPEED   = 0.25;

    public void setup(DcMotor left, DcMotor right, GameButton upButton, GameButton downButton){
        motorLeft  = left;
        motorRight = right;
        up = upButton;
        down = downButton;
        motorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void update(){
        if(up.IsUp() && down.IsUp()){
            liftIdle();
        }else if(up.IsDown()){
            liftUp();
        }else{
            liftDown();
        }
    }

    public void liftIdle(){
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }

    public void liftUp(){
        motorLeft.setPower(UP_SPEED);
        motorRight.setPower(UP_SPEED);
    }

    public void liftDown(){
        motorLeft.setPower(DOWN_SPEED);
        motorRight.setPower(DOWN_SPEED);
    }

}
