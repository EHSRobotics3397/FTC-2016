package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/*-----------------------------------------------------------------
 Optical Distance Test
 Reads and displays the reflectance value from the OpticalDistanceSensor
 This is not really a distance sensor. It reports the amount of light
 reflected back and depends on the properties of the material reflecting
 the light.
 -----------------------------------------------------------------*/

@TeleOp(name = "OptiSensor", group = "Drive")

public class OpticalDistanceTest extends OpMode {

    OpticalDistanceSensor optiSensor;
    private double reflectance;

    @Override
    public void init() {
        optiSensor = hardwareMap.opticalDistanceSensor.get("ODS");
    }

    @Override
    public void loop() {
        ReadSensor();
    }

    public void ReadSensor() {
        reflectance = optiSensor.getLightDetected();
        telemetry.addData("Reflectance", ": " + String.format("%3.2f", reflectance));
    }
}

