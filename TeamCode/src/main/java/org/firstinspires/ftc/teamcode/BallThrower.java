
package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

// Handles the Ball Throwing mechanism
// Modeled as a finite state machine

@TeleOp(name = "BallThrower", group = "Drive")
public class BallThrower extends OpMode {

    public enum State {LOCKED, REWINDING, TENSIONING, RELEASED};

    DcMotor rewindMotor;
    Servo latchServo;
    State state;
    GameButton rewindButton;
    GameButton tensionButton;
    GameButton releaseButton;

    float TENSION_POWER = 0.25f;
    float REWIND_POWER = -0.25f;

    @Override
    public void init() {

        //Map the hardware
        rewindMotor = hardwareMap.dcMotor.get("rewindMotor");
        latchServo = hardwareMap.servo.get("latchServo");
        //rewindMotor.setDirection(DcMotor.Direction.REVERSE);
        state = State.LOCKED;
    }

    @Override
    public void loop() {
        if (rewindButton.IsDown()) {
            rewind();
        }
        else if (tensionButton.IsDown()) {
            tension();
        }
        else if (releaseButton.Press()) {
            release();
        }
        else {
            idle();
        }

        DisplayState();
    }

    private void rewind() {
        rewindMotor.setPower(REWIND_POWER);
        state = State.REWINDING;
    }

    private void tension() {
        rewindMotor.setPower(TENSION_POWER);
        state = State.TENSIONING;
    }

    public void release() {
        rewindMotor.setPower(0.0f);
        state = State.RELEASED;
    }

    public void idle() {
        rewindMotor.setPower(0.0f);
        if (state == State.REWINDING)
            state = State.LOCKED;
        else
            state = State.RELEASED;
    }

    public void RecordPosition(){

    }

    private boolean IsAtLimit() {
        return false;
    }

    private void DisplayState() {
        telemetry.addData("Thrower State: ", state.toString());
    }
}



