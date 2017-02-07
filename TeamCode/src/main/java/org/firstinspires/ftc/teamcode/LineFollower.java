package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by Jeff Sprenger - Essex Robotic 3397 Team on 1/7/17.
 * Based on information from the SparkFun Arduino library
 * https://github.com/sparkfun/SparkFun_Line_Follower_Array_Arduino_Library
 *   loook at the file: sx1509_registers.h
 *
 *   Pinout for the ModernRobotics i2c ports:
 *   Left to right [+5V Data Clock Gnd]
 */

@TeleOp(name = "LineSensor", group = "Sensor")

public class LineFollower extends OpMode {

    static int REG_DATA_A = 0x11;
    static int REG_DATA_B = 0x10;
    static byte NOT_SET = (byte) 0x00;

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
            byte sensorData = sync.read8(REG_DATA_A);
        }
        DisplayData();
    }

    public void DisplayData() {
        telemetry.addData("Ready: ", ready ? "True" : "False");
        //String s = String.format("%02X", sensorData);
        telemetry.addData("Data: ", bitPattern(sensorData));
    }

    public String bitPattern(byte b) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<8; i++) {
            if ((b & 0x01 << i) == NOT_SET)
                sb.append("-");
            else
                sb.append("X");
        }
        return sb.toString();
    }
}
