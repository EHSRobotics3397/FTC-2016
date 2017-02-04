package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
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
 *
 * Beacon/Image Targets
 *     Blue Beacon1: WHEELS
 *     Blue Beacon2: LEGOS
 *     RedBeacon1: GEARS
 *     RedBeacon2: TOOLS
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

    private float Inches2mm = 25.4f;
    private float Mm2Inches = 1.0f/25.4f;

    private OpenGLMatrix blueBeacon1;
    private OpenGLMatrix blueBeacon2;
    private OpenGLMatrix blueStart1;
    private OpenGLMatrix blueStart2;
    private OpenGLMatrix blueLargeBall;

    private OpenGLMatrix redBeacon1;
    private OpenGLMatrix redBeacon2;
    private OpenGLMatrix redStart1;
    private OpenGLMatrix refStart2;
    private OpenGLMatrix redLargeBall;

    static final int WHEELS = 0;
    static final int TOOLS = 1;
    static final int LEGOS = 2;
    static final int GEARS = 3;

    public static final String VUFORIA_KEY = "ARDXCjL/////AAAAGXuBMxMI5EhrvrvaZoqpzmpfBmB1WDZJn56wtltNERZooZAfHDBUmdq10DYuq/f7VYSyV7pEtBGzGANIJJgM+ci+Kc/GrLyuKzoHPdV6VJAozfHadE2vFpBl5HnYUotKhCTC6ocsnEFZ9M1WaFh2KKSXXLOnQiPRWgYTq4o+KcUaY5Ki9BtcbnjSodBmcmW4lS/Qz6qfgdlHA/Dhm/XtLgtW7OhUwxyPg0i3ZKsQ8FyWNGkFZxd7yvo3p+AtzHb86o6hueWdP1mn1jZlcrp5IQILYBc4h11bngmmtUQ8EMsvaTLIDPivzSNjbrnlJ5WZNa2di5mr8tdTgByxmM3cadzS0U9VKG5KB6TCq2pcJvE9";

    public void runOpMode() throws InterruptedException {

        setObjectLocations();
        setupVuforia();
        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);
        waitForStart();
        visionTargets.activate();

        while(opModeIsActive()) {
            //a more robust approach - figure out what images we have in the field of view and
            //track using the closest. Want to avoid having to check all four.
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();
            if (latestLocation != null)
                lastKnownLocation = latestLocation;

            float distance = Mm2Inches*DistanceFlat(latestLocation, target.getLocation());

            telemetry.addData("Tracking: " + target.getName(), listener.isVisible());
            telemetry.addData("Last known location: ", formatMatrix(lastKnownLocation));
            telemetry.addData("Distance to target: ", String.format("%2.3f", distance));
            telemetry.update();
            idle();
        }
    }

    private void setObjectLocations() {
        blueBeacon1 = createMatrix(0, 84*Inches2mm, 0, 0, -90, -90);
        blueBeacon2 = createMatrix(0, 36*Inches2mm, 0, 0, -90, -90);
        blueStart1 = createMatrix(60*Inches2mm, 144*Inches2mm, 0, 0, 0, 0);
        blueStart2 = createMatrix(84*Inches2mm, 144*Inches2mm, 0, 0, 0, 0);
        blueLargeBall= createMatrix(60*Inches2mm, 84*Inches2mm, 0, 0, 0, 0);
    }

    private void setupVuforia() {
        //remove R.id.cameraMonitorViewId) to turn off display - saves batter power for robot controller phone
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");

        target = visionTargets.get(WHEELS);
        target.setName("Wheels Target");
        target.setLocation(blueBeacon1);
        phoneLocation = blueStart1;

        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

    }

    private OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {

        return OpenGLMatrix.translation(x,y,z).multiplied(
                Orientation.getRotationMatrix(AxesReference.EXTRINSIC,
                        AxesOrder.XYZ,
                        AngleUnit.DEGREES,
                        u, v, w));
    }

    //calculate the XY distance between two objects
    private float DistanceFlat(OpenGLMatrix pos1, OpenGLMatrix pos2) {
        VectorF v1 = pos1.getTranslation();
        VectorF v2 = pos2.getTranslation();
        VectorF offset = v1;
        offset.subtract(v2);

        double xDist = (double) offset.getData()[0];
        double yDist = (double) offset.getData()[1];
        double zDist = (double) offset.getData()[2];
        float distance = (float) Math.sqrt(xDist*xDist + yDist*yDist);
        return distance;
    }

    private String formatMatrix(OpenGLMatrix matrix) {
        return matrix.formatAsTransform();
    }
}
