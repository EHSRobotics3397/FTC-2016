package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Willem Hunt on 2/9/2017.
 */

public class LineFollower {
    static int  REG_DATA_A  = 0x11;
    static int  REG_DATA_B  = 0x10;
    static byte NOT_SET     = (byte) 0x00;

    I2cDevice           device;
    boolean             ready;
    I2cAddr             address;
    I2cDeviceSynchImpl  sync;
    byte                sensorData;

    public void setup(I2cDevice sensor){
        address = I2cAddr.create7bit(0x3E);
        sync = new I2cDeviceSynchImpl(device, address, false);
        sync.engage();
    }

    public void update(Telemetry telemetry){
        ready = device.isI2cPortReady();
        if(ready){
            byte sensorData = sync.read8(REG_DATA_A);
        }
        DisplayData(telemetry);
    }

    public void DisplayData(Telemetry telemetry){
        telemetry.addData("Ready: ", ready ? "True" : "False");

        telemetry.addData("Data: ", bitPattern(sensorData));
    }

    public String bitPattern(byte b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 8; i++){
            if((b & 0x01 << i) == NOT_SET){
                sb.append(".");
            }else{
                sb.append("X");
            }
        }
        return sb.toString();
    }
}
