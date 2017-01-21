package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


public class CarDriveModule {

	DcMotor leftMotor;
	DcMotor rightMotor;

	public void setup(DcMotor rm, DcMotor lm) {
		leftMotor = lm;
		rightMotor = rm;
	}

	public double[] drive(double x, double y){
		double scaleFactor    = 1;
		double spinThreshhold = 0.2;

		leftMotor.setDirection(DcMotor.Direction.REVERSE);

		double rightSteer     = 1.0 - (x + 1.0) / 2.0;
        double leftSteer      = (x + 1.0) / 2.0;

        double right;
        double left;
		
		double thrust         = -y;


		if(Math.abs(thrust) > spinThreshhold){

			right = thrust * rightSteer / scaleFactor;
			left  = thrust * leftSteer  / scaleFactor;

        } else {

            right = -x / scaleFactor;
			left  = x  / scaleFactor;

        }

        rightMotor.setPower(right);
        leftMotor.setPower(left);

		right               = Range.clip(right, -1, 1);
		left                = Range.clip(left, -1, 1);
		double[] returns    = new double[5];
		returns[0]          = left;
		returns[1]          = right;
		returns[2]          = scaleFactor;
		returns[3]          = x;
		returns[4]          = y;
		return              returns;
	}



}

		
