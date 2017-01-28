package org.firstinspires.ftc.teamcode.modules;

/**
 * Created by greenteam on 1/20/17.
 * Used to throw the ball.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.GameButton;

public class Thrower {

   // private enum    State           {LOCKED, REWINDING, TENSIONING, RELEASED, IDLE}

    private float   TENSION_POWER = 0.25f;
    private float   REWIND_POWER  = -0.25f;
    private double  RELEASED_POS; // add later from testing
    private double  LOCKED_POS; // add later

   // private State state = State.LOCKED;

    private DcMotor     rewindMotor;
    private Servo       latchServo;
    private GameButton  rewindButton;
    private GameButton  tensionButton;
    private TouchSensor sensor;
    private GameButton  releaseTrigger;
    private int         EncoderEnd; // add later
    private int         EncoderStart = 0;
    private int         EncoderCount = 0;


    public void setup(DcMotor rewindMotor, Servo latchServo, GameButton rewindButton,
                      GameButton tensionButton, GameButton releaseTrigger, TouchSensor sensor) {

        this.rewindMotor = rewindMotor;
        this.latchServo = latchServo;
        this.rewindButton = rewindButton;
        this.tensionButton = tensionButton;
        this.releaseTrigger = releaseTrigger;
        this.sensor = sensor;
        EncoderStart();
    }

    public void update(){

        if(rewindButton.IsDown()){
            rewind();
        }else if(tensionButton.IsDown()){
            tension();
        }else if(releaseTrigger.analogRead() >= 0.5){
            releaseLatch();
        }else{
           idle();
        }
    }

    private void rewind(){
        if(!sensor.isPressed()){
            rewindMotor.setPower(REWIND_POWER);
        } else {
            rewindMotor.setPower(0);
            lockLatch();
            // state = State.LOCKED;
        }
    }

    private void tension(){
        while(EncoderCount < EncoderEnd){
            // state = State.TENSIONING;
            rewindMotor.setPower(TENSION_POWER);
            readEncoders();
        }
        rewindMotor.setPower(0.0);
        // state = State.IDLE;
    }

    private void releaseLatch() {
        latchServo.setPosition(RELEASED_POS);
        //  state = State.RELEASED;
    }
    private void lockLatch() {
        latchServo.setPosition(LOCKED_POS);
        // state = State.LOCKED;
    }

    private void idle() {
        rewindMotor.setPower(0.0);
        // state = State.IDLE;
    }

    private void EncoderStart() {
        EncoderStart = rewindMotor.getCurrentPosition();
    }

    private void readEncoders() {
        EncoderCount = rewindMotor.getCurrentPosition() - EncoderStart;
    }

}
