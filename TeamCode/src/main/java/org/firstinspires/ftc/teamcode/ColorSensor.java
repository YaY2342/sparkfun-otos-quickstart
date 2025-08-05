package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorSensor extends LinearOpMode {
    private NormalizedColorSensor colorSensor;

    @Override
    public void runOpMode() {
        RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, "brushland color sensor");

        // You might need to enable the sensor's light for better readings
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }
        int red = colorSensor.red();
        int green = colorSensor.green();
        int blue = colorSensor.blue();
        int blockFound = 0;
        double distance = colorSensor.getDistance(DistanceUnit.CM);

        waitForStart();
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        telemetry.addData("Red", colors.red);
        telemetry.addData("Green", colors.green);
        telemetry.addData("Blue", colors.blue);
        telemetry.addData("Distance", distance);
        telemetry.update();


        while (blockFound == 0) {
            if (red > green)
            {
                blockFound = blockFound + 1;
            }
            else if (green > blue)
            {

            }
            else
            {

            }
        }
    }
}