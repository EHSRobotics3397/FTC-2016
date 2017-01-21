package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;
/*-----------------------------------------------------------------
 Optical Distance Test
 Reads and displays the reflectance value from the OpticalDistanceSensor
 This is not really a distance sensor. It reports the amount of light
 reflected back and depends on the properties of the material reflecting
 the light.
 -----------------------------------------------------------------*/

@TeleOp(name = "SonarSensor", group = "Drive")

public class SonarDistanceTest extends OpMode {

    DigitalChannel sensorTrigger;
    DigitalChannel sensorEcho;
    GameButton buttonA;
    enum State  {IDLE, TRIGGER, LISTEN};
    State state;
    ElapsedTime timer;
    static double SOUND_VELOCITY = 340.0;
    String msg = "";
    static double T10US = 0.000010; //10 us

    @Override
    public void init() {
        sensorTrigger = hardwareMap.digitalChannel.get("SonarTrigger");
        sensorEcho = hardwareMap.digitalChannel.get("SonarEcho");
        buttonA = new GameButton(gamepad1, GameButton.Label.a);
        timer = new ElapsedTime();
        changeState(State.IDLE);

    }

    @Override
    public void loop() {
        buttonA.Update();
        if (buttonA.Press() && state == State.IDLE) {
            changeState(State.TRIGGER);
            timer.reset();

        }
        switch (state){
            case IDLE: runIdle();
                break;
            case TRIGGER: runTrigger();
                break;
            case LISTEN: runListen();
                break;
            default:
                break;
        }
        DisplayData();

    }

    public void DisplayData() {
        telemetry.addData("Msg: ", msg);
        telemetry.addData("State: ", state.toString());
        telemetry.addData("Timer: ", Double.toString(timer.time()));
    }

    public void runIdle(){

    }

    public void runTrigger(){
        if(!sensorTrigger.getState()) {
            sensorTrigger.setState(true);
            timer.reset();
        }else{
            double timeDelay = timer.time();
            if (timeDelay > T10US) {
                sensorTrigger.setState(false);
                changeState(State.LISTEN);
            }
        }
    }
    public void runListen(){
        if(sensorEcho.getState()){
            double timeDelay = timer.time();
            double distance = timeDelay*SOUND_VELOCITY/2.0;  //return trip
            double inches = distance*39.3701;
            msg = "Distance: "+Double.toString(inches);
            changeState(State.IDLE);
        }
    }
    public void changeState(State newState){
        state = newState;
        timer.reset();
    }
}

