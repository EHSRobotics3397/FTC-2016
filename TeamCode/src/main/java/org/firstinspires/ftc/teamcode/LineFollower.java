package org.firstinspires.ftc.robotcontroller.internal;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by greenteam on 1/7/17.
 */

@TeleOp(name = "LineSensor", group = "Sensor")

public class LineFollower extends OpMode {

    I2cDevice device;
    boolean ready;
    I2cAddr address;
    I2cDeviceSynchImpl sync;
    byte sensorData;

    @Override
    public void init(){
        device = hardwareMap.i2cDevice.get("LineFollower");
        address = I2cAddr.create7bit(0x3E);
        sync = new I2cDeviceSynchImpl(device, address, false);
        sync.engage();
    }

    @Override
    public void loop(){
        ready = device.isI2cPortReady();
        if(ready){
            byte sensorData = sync.read8(0x00);
        }
        DisplayData();
    }

    public void DisplayData() {
        telemetry.addData("Ready: ", ready ? "True" : "False");
        String s = String.format("%02X", sensorData);
        telemetry.addData("Data: ", s);
    }

}
