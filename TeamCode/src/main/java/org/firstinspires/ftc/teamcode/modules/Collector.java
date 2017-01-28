package org.firstinspires.ftc.teamcode.modules;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.GameButton;

/**
 * Controls the toggling of the Collector
 */

public class Collector {

    private DcMotor     collectorMotor;
    private GameButton collectorToggle;
    private boolean on = false;

    public void setup(DcMotor motor, GameButton toggle){
        collectorMotor  = motor;
        collectorToggle = toggle;
    }

    public boolean update(){
        if(collectorToggle.Release()){
            on = !on;
        }

        if(on)
             collectorMotor.setPower(0.5);
        else collectorMotor.setPower(0.0);

        return on;
    }



}
