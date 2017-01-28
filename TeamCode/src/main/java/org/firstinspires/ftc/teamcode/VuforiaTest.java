package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Vuforia Test
 * Created by Jeff Sprenger - Jan 28, 2017
 * Based on the video tutorial: FTC Tutorials - Vuforia Basics
 * https://www.youtube.com/watch?v=gbcdveLP-Ns
 */

@TeleOp(name = "VuforiaTest", group = "Vision")
public class VuforiaTest extends LinearOpMode {

    VuforiaLocalizer vuforiaLocalizer;
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables visionTargets;
    VuforiaTrackable target;
    VuforiaTrackableDefaultListener listener;

    OpenGLMatrix lastKnownLocation;
    OpenGLMatrix phoneLocation;

    public static final String VUFORIA_KEY = "ARDXCjL/////AAAAGXuBMxMI5EhrvrvaZoqpzmpfBmB1WDZJn56wtltNERZooZAfHDBUmdq10DYuq/f7VYSyV7pEtBGzGANIJJgM+ci+Kc/GrLyuKzoHPdV6VJAozfHadE2vFpBl5HnYUotKhCTC6ocsnEFZ9M1WaFh2KKSXXLOnQiPRWgYTq4o+KcUaY5Ki9BtcbnjSodBmcmW4lS/Qz6qfgdlHA/Dhm/XtLgtW7OhUwxyPg0i3ZKsQ8FyWNGkFZxd7yvo3p+AtzHb86o6hueWdP1mn1jZlcrp5IQILYBc4h11bngmmtUQ8EMsvaTLIDPivzSNjbrnlJ5WZNa2di5mr8tdTgByxmM3cadzS0U9VKG5KB6TCq2pcJvE9";

    public void runOpMode() throws InterruptedException {

        setupVuforia();
        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);
        waitForStart();
        visionTargets.activate();

        while(opModeIsActive()) {
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();
            if (latestLocation != null)
                lastKnownLocation = latestLocation;

            telemetry.addData("Tracking: " + target.getName(), listener.isVisible());
            telemetry.addData("Last known location: ", formatMatrix(lastKnownLocation));
            telemetry.update();
            idle();
        }
    }

    public void setupVuforia() {
        //remove R.id.cameraMonitorViewId) to turn off display - saves batter power for robot controller phone
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");

        target = visionTargets.get(0);
        target.setName("Wheels Target");
        target.setLocation(createMatrix(0, 0, 0, 0, 0, 0));
        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

    }

    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {

        return OpenGLMatrix.translation(x,y,z).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC,
                        AxesOrder.XYZ,
                        AngleUnit.DEGREES,
                        u, v, w));
    }

    public String formatMatrix(OpenGLMatrix matrix) {
        return matrix.formatAsTransform();
    }
}
