package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.GameStick;

/*
 * Class for Manual Car Drive
 *
 * Manages main drive functions
 *
 * Pass in 2 DcMotors, Gamepad
 */

public class CarDrive {

	private DcMotor 	leftMotor;
	private DcMotor 	rightMotor;
    private GameStick   left;
    private GameStick   right;

    public void setup(DcMotor motor1, DcMotor motor2, Gamepad gamepad) {

        rightMotor  = motor1;
        leftMotor   = motor2;
        left        = new GameStick(gamepad, GameStick.Label.Left);
        right       = new GameStick(gamepad, GameStick.Label.Right);

        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

	}

	public void update(Telemetry telemetry){
		double scaleFactor    = 1.0;
		double spinThreshold  = 0.2;

		double rightSteer     = 1.0 - (right.x() + 1.0) / 2.0;
        double leftSteer      = (right.x() + 1.0) / 2.0;
		double thrust         = -left.y();

        double rightPower;
        double leftPower;
        if(Math.abs(thrust) > spinThreshold){

			rightPower  = thrust * rightSteer / scaleFactor;
			leftPower   = thrust * leftSteer  / scaleFactor;

        } else {

            rightPower  = -right.x() / scaleFactor;
			leftPower   = right.x()  / scaleFactor;
        }

        leftPower   = Range.clip(leftPower, -1, 1);
        rightPower  = Range.clip(rightPower, -1, 1);

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

        int leftEncoder     = leftMotor.getCurrentPosition();
        int rightEncoder    = rightMotor.getCurrentPosition();

        telemetry.addData("Power L: ", String.format("%.2f", leftPower));
        telemetry.addData("Power R: ", String.format("%.2f", rightPower));

        telemetry.addData("Steer L: ", String.format("%.2f", leftSteer));
        telemetry.addData("Steer R: ", String.format("%.2f", rightSteer));

        telemetry.addData("Drive Encoder L: ", String.format("%d", leftEncoder) + "  R: " + String.format("%d", rightEncoder));
	}
}

		
