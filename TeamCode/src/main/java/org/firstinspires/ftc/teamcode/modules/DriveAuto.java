package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
/**
 * Created by greenteam on 2/8/2017
 * Implements automatic driving such as
 * forward, turn, spin, backup.
 */

public class DriveAuto {

    public enum State {IDLE, DRIVING, SPINNING, COMPLETED, FAILED };
    private State state;
    private String stateName;
    private String failReason;

    private float CLICKS_PER_REV = 560.0f;
    private float WHEEL_DIAMETER = 7.0f;
    private float WHEEL_CIRCUMFERENCE = (float) Math.PI* WHEEL_DIAMETER;

    private float SPIN_DIAMETER = 17.0f;
    private float SPIN_CIRCUMFERENCE = (float) Math.PI * SPIN_DIAMETER;

    private float RAMP_THRESHOLD = 0.80f;
    private double MIN_POWER = 0.15;

    private float DRIVE_TIMEOUT = 5.0f; //secs

    private DcMotor rightMotor;
    private DcMotor leftMotor;
    private Telemetry telemetry;

    long startTime;
    int leftEncoderStart;
    int rightEncoderStart;

    public void setup(DcMotor left, DcMotor right, Telemetry tel){
        leftMotor = left;
        rightMotor = right;
        telemetry = tel;

        ChangeState(State.IDLE);
        Display();
        leftEncoderStart = leftMotor.getCurrentPosition();
        rightEncoderStart = rightMotor.getCurrentPosition();
        failReason = "N/A";
    }

    public void update() {
        if (state == State.IDLE)
            Idle();
        else if (state == State.DRIVING)
            Drive();
    }

    public State getState() {
        return state;
    }

    public String FailReason() {
        return failReason;
    }

    private void Display() {
        telemetry.addData("State: " + stateName;
    }

    private void ChangeState(State newState) {
        state = newState;
        startTime = System.currentTimeMillis();
    }

    private float ElapsedTimeInState() {
        long elapsedTime  = System.currentTimeMillis() - startTime;
        return ((float) elapsedTime)/1000.0f; // time in secs
    }

    //--------- public functions used to direct the driving
    //consider queuing the requests
    //we are either driving for a certain distance or amount of time

    private float desiredPower;
    private float distanceTarget;
    private float revsTarget;
    private float steering;

    public void Reset() {
        failReason = "N/A";
        ChangeState(State.IDLE);
    }

    public void Straight(float distance, float power) {
        distanceTarget = distance;
        desiredPower = power;
        steering = 0.0f;
        ChangeState(State.DRIVING);
    }

    public void Turn(float steer, float distance, float power) {
        distanceTarget = distance;
        desiredPower = power;
        steering = steer;
        ChangeState(State.DRIVING);
    }

    public void Spin(float rotation, float power) {
        //rotation is a float indicating number of spin turns (left is negative);
        revsTarget = rotation;
        desiredPower = Math.signum(rotation)*power;
        ChangeState(State.SPINNING);
    }

    public void Backup(float steer, float distance, float power) {
        distanceTarget = distance;
        desiredPower = -power;
        ChangeState(State.DRIVING);
    }

    //--------- functions handling processoing during State
    private void Idle() {
        //nothing to do
    }

    private void Drive() {
        double rightPower;
        double leftPower;
        float distance = DistanceDriven();
        if (ElapsedTimeInState() > DRIVE_TIMEOUT) {
            failReason = "Drive timed out";
            ChangeState(State.FAILED);
        }
        else if (distance > distanceTarget) {
            ChangeState(State.COMPLETED);
        }
        else {
            double factor = PowerRampFactor(distance, distanceTarget, RAMP_THRESHOLD);
            double rightSteer     = 1.0 - (steering + 1.0) / 2.0;
            double leftSteer      = (steering + 1.0) / 2.0;
            rightPower = factor*desiredPower * rightSteer;
            leftPower = factor*desiredPower * leftSteer;
            PowerDriveMotors(leftPower, rightPower);
        }
    }

    private void Spin() {
        //we stay in this state until we reach the distance (or some reasonable timeout)
        double rightPower = 0.0;
        double leftPower = 0.0;
        float revs = SpinRevs();
        // negative rotation?
        if (ElapsedTimeInState() > DRIVE_TIMEOUT) {
            failReason = "Spin timed out";
            ChangeState(State.FAILED);
        }
        else if (revs > revsTarget) {
            ChangeState(State.COMPLETED);
        }
        else {
            double factor = PowerRampFactor(revs, revsTarget, RAMP_THRESHOLD);
            leftPower = factor*desiredPower;
            rightPower = -leftPower;
            PowerDriveMotors(leftPower, rightPower);
        }
    }

    private double PowerRampFactor(float d1, float d2, float threshold) {
        double factor;
        if (d2 == 0.0f)
            factor = 0.0;
        else if (d1/d2 <= threshold)
            factor = 1.0;
        else {
            float rampSize = (1.0f - threshold)/d2;
            factor = (d2 - d1)/rampSize;
        }
        return factor;
    }

    //ensures that we have some min value that keeps the motor moving
    //this avoids us never getting to the target as we ramp down the power.
    private void PowerDriveMotors(double leftPower, double rightPower) {
        if (leftPower < MIN_POWER && leftPower > -MIN_POWER)
            leftPower = Math.signum(leftPower)*MIN_POWER;
        if (rightPower < MIN_POWER && rightPower > -MIN_POWER)
            rightPower = Math.signum(rightPower)*MIN_POWER;
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }

    // Regular driving, takes the mean of the encoder changes
    // Spinning: takes the mean of the absolute values, since motors are moving in opposite direction.
    private float DistanceDriven() {
        int leftTicks = leftMotor.getCurrentPosition() - leftEncoderStart;
        int rightTicks = rightMotor.getCurrentPosition() - rightEncoderStart;
        int meanTicks = (leftTicks + rightTicks) / 2;
        float distance =  (float) meanTicks/CLICKS_PER_REV * WHEEL_CIRCUMFERENCE;
        return distance;
    }

    private float SpinRevs() {
        int leftTicks = leftMotor.getCurrentPosition() - leftEncoderStart;
        int rightTicks = rightMotor.getCurrentPosition() - rightEncoderStart;
        int meanTicks = (Math.abs(leftTicks) + Math.abs(rightTicks))/2;
        float distance =  (float) meanTicks/CLICKS_PER_REV * WHEEL_CIRCUMFERENCE;
        float revs = distance / SPIN_CIRCUMFERENCE;
        return revs;
    }
}
