package org.firstinspires.ftc.teamcode.modules;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.GameButton;


public class CarDrive {

	private DcMotor 	leftMotor;
	private DcMotor 	rightMotor;
	private GameButton leftStick;
	private GameButton rightStick;
    double rightPower;
    double leftPower;

	public void setup(DcMotor motor1, DcMotor motor2, GameButton left, GameButton right) {
		rightMotor = motor1;
        leftMotor  = motor2;
		leftStick = left;
		rightStick = right;
	}

	public double[] update(){
		double scaleFactor    = 1.0;
		double spinThreshold = 0.2;

		leftMotor.setDirection(DcMotor.Direction.REVERSE);

		double rightSteer     = 1.0 - (leftStick.analogRead() + 1.0) / 2.0;
        double leftSteer      = (leftStick.analogRead() + 1.0) / 2.0;
		double thrust         = -rightStick.analogRead();

		if(Math.abs(thrust) > spinThreshold){

			rightPower = thrust * rightSteer / scaleFactor;
			leftPower = thrust * leftSteer  / scaleFactor;

        } else {

            rightPower = -leftStick.analogRead() / scaleFactor;
			leftPower = leftStick.analogRead()  / scaleFactor;

        }

        rightMotor.setPower(rightPower);
        leftMotor.setPower(leftPower);

		rightPower = Range.clip(rightPower, -1, 1);
		leftPower = Range.clip(leftPower, -1, 1);
		double[] returns    = new double[5];
		returns[0]          = leftPower;
		returns[1]          = rightPower;
		returns[2]          = scaleFactor;
		returns[3]          = leftStick.analogRead();
		returns[4]          = rightStick.analogRead();
		return              returns;
	}



}

		
