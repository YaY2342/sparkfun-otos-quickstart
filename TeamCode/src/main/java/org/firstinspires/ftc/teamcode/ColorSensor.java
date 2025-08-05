/* package org.firstinspires.ftc.teamcode;

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


        waitForStart();

        while(opModeIsActive()) {
            int red = colorSensor.red();
            int green = colorSensor.green();
            int blue = colorSensor.blue();
            double distance = colorSensor.getDistance(DistanceUnit.CM);
            final double target_distance = 5;

            NormalizedRGBA colors = colorSensor.getNormalizedColors();
            telemetry.addData("Red", ".3f", colors.red);
            telemetry.addData("Green", ".3f", colors.green);
            telemetry.addData("Blue", ".3f", colors.blue);
            telemetry.addData("Distance", ".2f", distance);
            telemetry.update();

            if (red > 100 && red > blue && red > green)
            {
                if(distance >= target_distance)
                {

                }
            }

        }





        }
    }
} */