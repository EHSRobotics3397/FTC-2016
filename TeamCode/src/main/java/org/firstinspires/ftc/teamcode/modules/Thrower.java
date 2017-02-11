package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GameButton;

/**
 * Created by Willem on 2/2/17.
 *
 * Class for throwing mechanism, manages rewinding, tensioning, launching
 *
 * Pass in DcMotor, TouchSensor, Gamepad, Servo
 */



public class Thrower{

    public enum State {RELEASED, REWINDING, LOCKED, TENSIONING}

    private DcMotor     rewindMotor;
    private TouchSensor rewindSensor;
    private int         encoderStart    = 0;
    private int         encoderVal      = encoderStart;
    private boolean     running         = false;
    private Servo       latchServo;
    private boolean     latchOpen       = false;

    private int         fireVal         = -1080;
    private String      latchState      = "Closed";

    private State       state;
    private String      stateString;
    private GameButton  aButton;
    private long startTime;

    private static final double LATCH_CLOSE_POSITION    = 0.8;
    private static final double LATCH_FIRE_POSITION     = 0.5;
    private static final long   REWIND_WAIT_MS          = 250;
    private static final float LOCK_WAIT_MS = 1000;

    private boolean autoFire = false;

    public void setup(DcMotor rewind,TouchSensor sensor, Gamepad pad, Servo latch){
        rewindMotor     = rewind;
        rewindSensor    = sensor;
        latchServo      = latch;
        aButton         = new GameButton(pad, GameButton.Label.a);

        setState(State.RELEASED);
        latchServo.setPosition(LATCH_CLOSE_POSITION);

    }

    public void update(Telemetry telemetry) {
        aButton.Update();

        encoderVal = rewindMotor.getCurrentPosition();

        //check the sensors to set the state;

        switch (state){

            case RELEASED: release();
                stateString = "Released";
                break;
            case REWINDING: rewind();
                stateString = "Rewinding";
                break;
            case TENSIONING: tension();
                stateString = "Tensioning";
                break;
            case LOCKED: lock();
                stateString = "Locked";
                break;

        }

        latchState = (latchOpen) ? "Open" : "Closed";

        telemetry.addData("Servo ", latchState);
        telemetry.addData("Encoder ", ": " + Integer.toString(encoderVal));
        telemetry.addData("Running ", ": " + Boolean.toString(running));
        telemetry.addData("Power ", Double.toString(rewindMotor.getPower()));
        telemetry.addData("State ", stateString);
    }

    public void fire() {
        autoFire = true;
    }

    public State getState() {
        return state;
    }

    private void closeLatch(){
        if(latchOpen) {
            latchServo.setPosition(LATCH_CLOSE_POSITION);
            latchOpen = false;
        }
        encoderStart = rewindMotor.getCurrentPosition();
    }

    private void openLatch(){
        if(!latchOpen) {
            latchServo.setPosition(LATCH_FIRE_POSITION);
            latchOpen = true;
        }
    }

    private void lock(){
        running = false;
        closeLatch();
        long elapsedTime  = System.currentTimeMillis() - startTime;
        if(aButton.Press() || (autoFire && elapsedTime > LOCK_WAIT_MS)){
            setState(State.TENSIONING);
        }
    }

    private void rewind() {
        openLatch();
        rewindMotor.setPower(0.225f);
        running = true;
        if(rewindSensor.isPressed()){
            rewindMotor.setPower(0.0f);
            setState(State.LOCKED);
        }
    }

    private void tension(){
        running = true;
        autoFire = false;
        encoderVal = rewindMotor.getCurrentPosition();
        rewindMotor.setPower(-0.5f);
        if( (encoderVal- encoderStart) < fireVal){   //fireVal is NEGATIVE
            setState(State.RELEASED);
        }
    }

    private void release(){
        openLatch();
        rewindMotor.setPower(0.0f);

        long elapsedTime  = System.currentTimeMillis() - startTime;
        if(elapsedTime > REWIND_WAIT_MS){
            setState(State.REWINDING);
        }
    }

    public void setState(State newState)
    {
        startTime = System.currentTimeMillis();
        state = newState;
    }

}
