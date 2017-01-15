
package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 *  BTOP is a Basic TeleOp program that simply
 */
//@TeleOp(name = "Car Drive", group = "Drive")
//@Disabled
public class Collector extends OpMode {
    int test = 0; //
    //Creates two motor types
    DcMotor motor;
    boolean on = false;

    //@Override
    public void init() {

        //Finds the motors on the hardware map and sets the define type to the motor

        motor = hardwareMap.dcMotor.get("motor");
        //motor.setDirection(DcMotor.Direction.REVERSE);

    }

    //@Override
    public void loop() {

        boolean rb = gamepad1.right_bumper;

        if(rb){
           on = !on;
        }

        if(on){
            motor.setPower(0.5);
        }else{
            motor.setPower(0.0);
        }
        //Write the information to the telemetry screen


        //telemetry.addData("MotorOn: ", on ? "True" : "False");


    }
    public static void tester(){

    }




}



