package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GameButton;
/**
 * Created by Willem on 2/2/17.
 * Main Drive Opmode
 */


public class ThrowTest{

    private DcMotor     rewindMotor;
    private TouchSensor rewindSensor;
    private int encoderStart = 0;
    private int encoderVal = encoderStart;
    private boolean running = false;
    private GameButton dpad_up;
    private GameButton dpad_down;
    private GameButton toggle;
    private Servo   latchServo;
    private boolean latchOpen = false;
    double closePosition = 1.0;
    double firePosition = 0.5;
    String latchState = "Closed";

    public void setup(DcMotor rewind,TouchSensor sensor, Gamepad pad, Servo latch){
        rewindMotor = rewind;
        rewindSensor = sensor;
        latchServo = latch;

        dpad_up = new GameButton(pad, GameButton.Label.dpadUp);
        dpad_down = new GameButton(pad, GameButton.Label.dpadDown);
        toggle = new GameButton(pad, GameButton.Label.a);

        rewindMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        encoderVal = rewind.getCurrentPosition();
        latchServo.setPosition(closePosition);

    }

    public void update(Telemetry telemetry) {
            rewindMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            dpad_up.Update();
            dpad_down.Update();
            float appliedPower = 0.0f;
            encoderVal = rewindMotor.getCurrentPosition();
            if (dpad_down.IsDown() && !rewindSensor.isPressed()) {
                appliedPower = 0.1f;
                running = true;
            } else if (dpad_up.IsDown()) {
                appliedPower = -0.5f;
                running = true;
            } else {
                appliedPower = 0f;
                running = false;
            }
            rewindMotor.setPower(appliedPower);


        toggle.Update();
        if(toggle.Press()){
            if(latchOpen) {
                close();
            }else{
                open();
            }
        }
        latchState = (latchOpen) ? "Open" : "Closed";
        telemetry.addData("Servo ", latchState);

        telemetry.addData("Encoder ", ": " + Integer.toString(encoderVal));

        telemetry.addData("Running ", ": " + Boolean.toString(running));
        telemetry.addData("Appl Power ", ": " + String.format("%2.3f", appliedPower));

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
