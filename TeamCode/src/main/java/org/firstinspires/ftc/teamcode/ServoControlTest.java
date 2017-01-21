package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/*-----------------------------------------------------------------
 Servo rotates between 0.0 and 1.0, this code toggles between the two with servoMove();
 -----------------------------------------------------------------*/

@TeleOp(name = "ServoControl", group = "Drive")

public class ServoControlTest extends OpMode {

    Servo servo;
    GameButton buttonA;
    String msg = "";
    Boolean servoOpen = false;

    @Override
    public void init() {
        servo = hardwareMap.servo.get("TriggerServo");
        buttonA = new GameButton(gamepad1, GameButton.Label.a);
        servo.setPosition(0.0);
    }

    @Override
    public void loop() {
        buttonA.Update();

        //servo position1
        if (buttonA.Press()) {
            servoOpen = !servoOpen;
            servoMove(servoOpen);
        }
        DisplayData();
    }

    public void DisplayData() {
        telemetry.addData("Msg", ": " + msg);
    }
    public void servoMove(boolean servoOpen) {
        if (servoOpen) {
            servo.setDirection(Servo.Direction.FORWARD);
            servo.setPosition(Servo.MAX_POSITION);
            msg = "Servo Open";
        } else {
            servo.setDirection(Servo.Direction.FORWARD);
            servo.setPosition(Servo.MIN_POSITION);
            msg = "Servo Closed";
        }
    }
}

