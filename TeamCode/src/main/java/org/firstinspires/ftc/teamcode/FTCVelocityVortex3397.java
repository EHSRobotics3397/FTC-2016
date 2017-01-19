package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.CarDriveModule;
//import org.firstinspires.ftc.teamcode.CollectorModule;
/**
 * Created by greenteam on 1/12/17.
 */
@TeleOp(name = "UniDrive", group = "Drive")

public class FTCVelocityVortex3397 extends OpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;
    double driveX = 0.0;
    double driveY = 0.0;
    double[] driveReturns = new double[5];

    @Override
    public void init(){
        leftMotor = hardwareMap.dcMotor.get("motorLeft");
        rightMotor = hardwareMap.dcMotor.get("motorRight");
    }

    @Override
    public void loop(){
        driveY = gamepad1.left_stick_y;
        driveX = gamepad1.right_stick_x;

        driveReturns = CarDriveModule.drive(driveX, driveY, rightMotor, leftMotor);
    }

}
