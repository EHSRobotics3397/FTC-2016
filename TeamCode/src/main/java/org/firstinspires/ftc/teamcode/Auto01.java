package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.modules.*;
/**
 * Created by greenteam on 2/8/2017
 * Autonomous Drive1 (Test)
 */

@TeleOp(name = "Auto01", group = "Drive")

public class Auto01 extends OpMode {

    private DcMotor     collectorMotor;

    private float CLICKS_PER_REV = 560.0f;
    private float WHEEL_DIAMETER = 7.0f;
    private float INCHES_PER_FOOT = 12.0f;

    private enum State {IDLE, DELAYED, DRIVE1, THROW1, HARVEST, THROW2, DRIVE2, COMPLETED, FAILED };
    private String stateName;
    private String failReason;
    private long startTime;

    private float delayTime = 1.0f; //secs

    private State state;
    private State nextState;
    private int ballsLaunched = 0;

    //Modules
    private Thrower thrower;
    private DriveAuto driver;

    @Override
    public void init(){
        collectorMotor  = hardwareMap.dcMotor.get("collectMotor");

        DcMotor rewindMotor  = hardwareMap.dcMotor.get("throwMotor");
        Servo latchServo   = hardwareMap.servo.get("latchServo");
        TouchSensor rewindSensor = hardwareMap.touchSensor.get("rewindSensor");

        thrower = new Thrower();
        thrower.setup(rewindMotor, rewindSensor, gamepad2, latchServo);

        DcMotor leftMotor       = hardwareMap.dcMotor.get("leftMotor");
        DcMotor rightMotor      = hardwareMap.dcMotor.get("rightMotor");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        driver = new DriveAuto();
        driver.setup(leftMotor, rightMotor, telemetry);

        if(gamepad1.a) {
            delayTime = 1.0f;
        }
        ChangeState(State.IDLE);
        Display();
    }

    @Override
    public void loop() {
        switch(state) {
            case IDLE:
                stateName = "IDLE";
                nextState = State.DRIVE1;
                Idle();
                break;
            case DRIVE1:
                stateName = "DRIVE1";
                nextState = State.THROW1;
                Drive(14.0f, 0.40f);
                break;
            case THROW1:
                stateName = "THROW1";
                nextState = State.HARVEST;
                Throw();
                break;
            case HARVEST:
                stateName = "HARVEST";
                nextState = State.THROW2;
                Harvest();
                break;
            case THROW2:
                stateName = "THROW2";
                nextState = State.IDLE;
                Throw();
                break;
            case DRIVE2:
                stateName = "DRIVE2";
                nextState = State.IDLE;
                Drive(14.0f, 0.40f);
                break;
            case FAILED:
                stateName = "FAILED";
                nextState = State.FAILED;
                Failed();
                break;
            case COMPLETED:
                stateName = "COMPLETED";
                Completed();
                break;
        }
        driver.update();
        Display();
        driver.DisplayEncoders();

    }

    private void Display() {
        telemetry.addData("State: ", stateName);
        telemetry.addData("Time: ",String.format("%3.2f s", ElapsedTimeInState()));

    }

    private void ChangeState(State newState) {
        state = newState;
        startTime = System.currentTimeMillis();
    }

    private float ElapsedTimeInState() {
        long elapsedTime  = System.currentTimeMillis() - startTime;
        return ((float) elapsedTime)/1000.0f; // time in secs
    }

    //--------- functions handling processoing during State
    private void Idle() {
        telemetry.addData("Delayed start: ", String.format("%2.1f", delayTime));
        if (ElapsedTimeInState() > delayTime)
            ChangeState(nextState);
    }

    private void Completed() {
        driver.DisplayEncoders();
    }

    //this can be used to drive straight, turn, spin or backup
    //each driving segments should have it's own state (DRIVE1, DRIVE2..) and
    //matching function here. The DriveAuto class will take care of the driving.
    //This is just used to issue the command and check for completion.
    private void Drive(float distance, float power) {
        if (driver.getState() == DriveAuto.State.IDLE)
            driver.Straight(distance, power);
        else if (driver.getState() == DriveAuto.State.FAILED) {
            failReason = driver.FailReason();
            ChangeState(State.FAILED);
        }
        else if (driver.getState() == DriveAuto.State.COMPLETED){
            driver.Reset();
            ChangeState(nextState);
        }

        driver.DisplayEncoders();
    }

    private void Throw() {
        if (thrower.getState() == Thrower.State.LOCKED) {
            thrower.setState(Thrower.State.TENSIONING); //this will tension then throw the ball.
            ChangeState(nextState);
            ballsLaunched++;
        }
    }

    private void Harvest() {
        //run the collector for t1 secs, wait until t2, then we are done.
        double HARVESTER_PWR = 0.3;
        double t1 = 0.2;
        double t2 = 1.0;

        if (ElapsedTimeInState() > t2) {
            ChangeState(nextState);
        }
        else if (ElapsedTimeInState() > t1){
            collectorMotor.setPower(0.0); //off
        }
        else {
            collectorMotor.setPower(HARVESTER_PWR);
        }
    }

    private void Failed() {
        telemetry.addData("Auto01 Failed: ", failReason);
    }
}
