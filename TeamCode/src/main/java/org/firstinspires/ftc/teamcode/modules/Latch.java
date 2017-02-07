package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GameButton;

/**
 * Created by Willem Hunt on 2/2/2017.
 *
 */

public class Latch {
    private Servo latchServo;
    boolean latchOpen = false;
    double closePosition = 1.0;
    double firePosition = 0.5;
    private Gamepad gamepad;
    private GameButton toggle;

    public void setup(Gamepad pad, Servo servo){
        latchServo = servo;
        gamepad = pad;
        latchServo.setDirection(com.qualcomm.robotcore.hardware.Servo.Direction.FORWARD);
        latchServo.setPosition(closePosition);
        toggle = new GameButton(gamepad, GameButton.Label.a);

    }

    public void update(Telemetry telemetry) {
        telemetry.addData("Servo Position", Double.toString(latchServo.getPosition()));
        toggle.Update();
        if(toggle.Press()){
            if(latchOpen) {
                close();
            }else{
                open();
            }
        }
    }

    public void close(){
        latchServo.setPosition(closePosition);
        latchOpen = !latchOpen;
    }

    public void open(){
        latchServo.setPosition(firePosition);
        latchOpen = !latchOpen;
    }

}
