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

    private enum State {IDLE, DELAYED, DRIVE, THROW1, HARVEST, THROW2, FAILED };
    private String stateName;
    private String failReason;
    private long startTime;

    private float delayTime = 0.0f; //secs

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
        driver = new DriveAuto();
        driver.setup(leftMotor, rightMotor, telemetry);

        if(gamepad1.a) {
            delayTime = 5.0f;
        }
        ChangeState(State.IDLE);
        Display();
    }

    @Override
    public void loop() {
        switch(state) {
            case IDLE:
                stateName = "IDLE";
                nextState = State.DRIVE;
                Idle();
                break;
            case DRIVE:
                stateName = "DRIVE";
                nextState = State.THROW1;
                Drive();
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
            case FAILED:
                stateName = "FAILED";
                nextState = State.FAILED;
                Failed();
                break;
        }
        driver.update();
        Display();
    }

    private void Display() {
        telemetry.addData("State: ",stateName;
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

    //this can be used to drive straight, turn, spin or backup
    //each driving segments should have it's own state (DRIVE1, DRIVE2..) and
    //matching function here. The DriveAuto class will take care of the driving.
    //This is just used to issue the command and check for completion.
    private void Drive() {
        if (driver.getState() == DriveAuto.State.IDLE)
            driver.Straight(3.1f, 0.50f);
        else if (driver.getState() == DriveAuto.State.FAILED) {
            failReason = driver.FailReason();
            ChangeState(State.FAILED);
        }
        else if (driver.getState() == DriveAuto.State.COMPLETED)
            ChangeState(nextState);
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
        double HARVESTER_PWR = 0.5;
        double t1 = 0.40;
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
