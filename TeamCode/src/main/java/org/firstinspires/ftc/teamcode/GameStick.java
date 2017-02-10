package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * EssexRobotics - Jan 2017
 * Wraps the Gamepad Stick
 */

public class GameStick {

    private boolean release;
    private Gamepad pad;

    public enum Label{Left, Right, LeftTrigger, RightTrigger}

    private Label buttonLabel;

    public GameStick(Gamepad somePad, Label someLabel) {
        pad         = somePad;
        buttonLabel = someLabel;
    }

    public float x() {
        float value = 0.0f;
        if (buttonLabel== Label.Left) {
            value = pad.left_stick_x;
        }
        else if (buttonLabel== Label.Right) {
            value = pad.right_stick_x;
        }
        return value;
    }

    public float y() {
        float value = 0.0f;
        if (buttonLabel== Label.Left) {
            value = pad.left_stick_y;
        }
        else if (buttonLabel== Label.Right) {
            value = pad.right_stick_y;
        }
        return value;
    }

    public float trigger() {
        float value = 0.0f;
        if (buttonLabel== Label.LeftTrigger) {
            value = pad.left_trigger;
        }
        else if (buttonLabel== Label.RightTrigger) {
            value = pad.right_trigger;
        }
        return value;
    }
}
