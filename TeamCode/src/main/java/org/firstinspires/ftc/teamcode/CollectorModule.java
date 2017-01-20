package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
/**
 * Created by greenteam on 1/19/17.
 */

public class CollectorModule {
    public static void toggleCollector(DcMotor motor, boolean on){
        if(on)
            motor.setPower(0.5);
        else motor.setPower(0.0);
    }

}
