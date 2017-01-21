package org.firstinspires.ftc.teamcode;

/**
 * Created by greenteam on 1/20/17.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ServoControlTest;

public class ThrowerModule {

    public enum State {LOCKED, REWINDING, TENSIONING, RELEASED};
    float TENSION_POWER = 0.25f;
    float REWIND_POWER  = -0.25f;

    State state = State.LOCKED;

    DcMotor     _rewindMotor;
    Servo       _latchServo;
    GameButton _rewindButton;
    GameButton _tensionButton;
    GameButton _latchButton;
    GameButton _lockButton;
    double _releaseTrigger;

    public void setButtonAndMotors(DcMotor rewindMotor, Servo latchServo,
                                   GameButton rewindButton, GameButton tensionButton,
                                   GameButton latchButton, GameButton lockButton, double releaseTrigger) {

        _rewindMotor = rewindMotor;
        _latchServo = latchServo;
        _rewindButton = rewindButton;
        _tensionButton = tensionButton;
        _latchButton = latchButton;
        _lockButton = latchButton;
        _releaseTrigger = releaseTrigger;






    }

    public void updateThrower(){

        if(_rewindButton.IsDown()){
            rewind(_rewindMotor);
        }else if(_tensionButton.IsDown()){
            tension(_rewindMotor);
        }else if(_releaseTrigger >= 0.5){
            releaseLatch(_latchServo);
        }else if(_lockButton.IsDown()){
            lockLatch(_latchServo);
        }else{
            idle();
        }
    }

    private void rewind(DcMotor motor){
        motor.setPower(REWIND_POWER);
        //sensor needed?
    }

    private void tension(DcMotor motor){
        motor.setPower(TENSION_POWER);
        //encoder needed
    }

    private void releaseLatch(Servo servo) {

    }
    private void lockLatch(Servo servo) {

    }

    public void idle() {
        
    }

}
