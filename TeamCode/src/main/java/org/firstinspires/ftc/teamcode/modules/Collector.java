package org.firstinspires.ftc.teamcode.modules;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GameButton;

/**
 * Controls the toggling of the Collector
 */

public class Collector {
    private String     collectMode;
    private DcMotor    collectorMotor;
    GameButton rBumper;
    GameButton lBumper;

    public void setup(DcMotor motor, Gamepad pad){
        collectorMotor  = motor;
        rBumper = new GameButton(pad, GameButton.Label.RBumper);
        lBumper = new GameButton(pad, GameButton.Label.LBumper);

    }

    public void update(Telemetry telemetry){
        lBumper.Update();
        rBumper.Update();
        if(rBumper.IsDown()){
            collectorMotor.setPower(-0.3);
            collectMode = "Back";
        }else if(lBumper.IsDown()){
            collectorMotor.setPower(0.3);
            collectMode = "Forward";
        }else{
            collectorMotor.setPower((0.0));
            collectMode = "Idle";
        }
        telemetry.addData("Collector",  ": " + collectMode);

    }



}
