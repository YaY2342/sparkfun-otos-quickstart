package org.firstinspires.ftc.teamcode;


import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous
public class rrauto extends LinearOpMode {

    private NormalizedColorSensor colorSensor;

    int red = 0;
    int blue = 0;
    int green = 0;
    int distance = 0;
    int target_distance = 0;

    Servo CS;
    Servo CW;
    Servo CE;
    Servo claw;
    Servo HSL;
    Servo HSR;

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    DcMotor SL;
    DcMotor SR;


    // These are for the VERTICAL slides
    int score = 4;
    int low = 1;
    int pu = 2;

    // These are for the CLAW
    final double open = 1;
    final double close = .2;

    // These are for the ELBOW joint
    final double elbowExtended = 1;
    final double elbowRetracted = .5;

    // These are for the SHOULDER joint
    final double shoulderExtended = .5;
    final double shoulderRetracted = .2;
    final double shoulderUP = 1;

    // These are for the WRIST joint
    final double wrist = .5;
    final double wristDown = .1;

    // These are for the HORIZONTAL slides
    final double HSextension = 0;


    public void setClaw(double pos) {
        claw.setPosition(pos);
    }

    public void setCW(double pos) {
        CW.setPosition(pos);
    }

    public void setCS(double pos) {
        CS.setPosition(pos);
    }

    public void setCE(double pos) {
        CE.setPosition(pos);
    }

    public void setHS(double pos) {
        HSL.setPosition(pos);
        HSR.setPosition(pos);
    }

    public void setVS(double pos) {
        SL.setTargetPosition((int) pos);
        SR.setTargetPosition((int) pos);
    }

    public void goToBlock() {

        int counter = 0;

        while(counter == 0) {

            // counter = counter + 1 (THIS IS FOR THE TRIAL WHEN DOING AUTO SINCE IT CANNOT "DETECT" RED WHEN ON THE PRACTICE FIELD

            if (red > 100 && red > blue && red > green)
            {
                if (distance > target_distance)
                {
                   setHS(+.1);
                }
                else
                {
                    counter = counter + 1;
                }
            }
            else
            {
                frontLeftMotor.setPower(1);
                backRightMotor.setPower(1);
                sleep(1);
            }
        }

    }


    public Action score() {
        return new SequentialAction(
                new InstantAction(() -> {setVS(score);}),
                new InstantAction(() -> {setCE(elbowRetracted);}),
                new SleepAction(1),
                new InstantAction(() -> {setCS(shoulderExtended);}),
                new InstantAction(() -> {setCE(elbowExtended);}),
                new SleepAction(1),
                new InstantAction(() -> {setCS(shoulderRetracted);}),
                new InstantAction(() -> {setClaw(open);}),
                new SleepAction(.2),
                new InstantAction(() -> {setVS(pu);})
        );
    }

    public Action pick() {
        return new SequentialAction(
                new InstantAction(() -> {setCE(shoulderExtended);}),
                new InstantAction(() -> {setClaw(open);}),
                new InstantAction(() -> {setCE(elbowExtended);}),
                new SleepAction(.3),
                new InstantAction(() -> {setClaw(close);}),
                new SleepAction(.1),
                new InstantAction(() -> {setCS(shoulderUP);}),
                new SleepAction(.1),
                new InstantAction(() -> {setCE(elbowRetracted);}),
                new InstantAction(() -> {setCS(shoulderRetracted);}),
                new SleepAction(.5)
        );

    }

    public Action drop() {
        return new SequentialAction(
                new InstantAction(() -> {setVS(low);}),
                new InstantAction(() -> {setCE(elbowExtended);}),
                new SleepAction(2),
                new InstantAction(() -> {setClaw(open);})

        );
    }



    public Action psa() {
        return new SequentialAction(
                new InstantAction(() -> {setVS(low);}),
                new InstantAction(() -> {setCS(shoulderExtended);}),
                new InstantAction(() -> {setCE(elbowExtended);}),
                new InstantAction(() -> {setCW(wristDown);}),
                new InstantAction(() -> {setClaw(open);}),
                new SleepAction(1),
                new InstantAction(this::goToBlock),
                new SleepAction(1),
                new InstantAction(() -> {setCS(shoulderRetracted);}),
                new InstantAction(() -> {setClaw(close);}),
                new InstantAction(() -> {setCS(shoulderExtended);}),
                new InstantAction(() -> {setCE(elbowRetracted);}),
                new InstantAction(() -> {setHS(HSextension);})
        );
    }

    @Override
    public void runOpMode(){

        RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, "brushland color sensor");

        // You might need to enable the sensor's light for better readings
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }

        Pose2d Initial_Pose = new Pose2d(0, -62, Math.toRadians(90));
        PinpointDrive drive = new PinpointDrive(hardwareMap, Initial_Pose);


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
            }




        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("left_front");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("left_back");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("right_front");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("back_right");
        DcMotor SL = hardwareMap.dcMotor.get("slidesLeft");
        DcMotor SR = hardwareMap.dcMotor.get("slidesRight");

        CW = hardwareMap.get(Servo.class, "wrist");
        CE = hardwareMap.get(Servo.class, "elbow");
        claw = hardwareMap.get(Servo.class, "claw");
        CS = hardwareMap.get(Servo.class, "shoulder");
        HSL = hardwareMap.get(Servo.class, "Horizontal Slide Left");
        HSR = hardwareMap.get(Servo.class, "Horizontal Slide Right");


        Action scorePreload = drive.actionBuilder(Initial_Pose)
                .strafeTo(new Vector2d(0, -30.3))
                .build();

        Action push = drive.actionBuilder(new Pose2d(0, -32.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(36, -30), Math.toRadians(90))

                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(47, -11), Math.toRadians(-90))
                .waitSeconds(.2)
                .strafeTo(new Vector2d(47, -55))

                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(59, -11), Math.toRadians(90)), Math.toRadians(0))
                .waitSeconds(.2)
                .strafeTo(new Vector2d(59, -59.5))

                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(63, -11), Math.toRadians(-90)), Math.toRadians(0))
                .waitSeconds(.2)
                .strafeTo(new Vector2d(63, -59.5))

                .build();

        Action scoreCycle1 = drive.actionBuilder(new Pose2d(58, -60.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(3, -30.3), Math.toRadians(90)), Math.toRadians(90))
                .build();

        Action pickCycle1 = drive.actionBuilder(new Pose2d(3, -32.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -59.5), Math.toRadians(-90)), Math.toRadians(-90))
                .build();

        Action scoreCycle2 = drive.actionBuilder(new Pose2d(58, -60.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-6, -30.3), Math.toRadians(90)), Math.toRadians(90))
                .build();

        Action pickCycle2 = drive.actionBuilder(new Pose2d(-3, -32.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -59.5), Math.toRadians(-90)), Math.toRadians(-90))
                .build();

        Action scoreCycle3 = drive.actionBuilder(new Pose2d(58, -60.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(6, -30.3), Math.toRadians(90)), Math.toRadians(90))
                .build();

        Action pickCycle3 = drive.actionBuilder(new Pose2d(-3, -32.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -59.5), Math.toRadians(-90)), Math.toRadians(-90))
                .build();

        Action scoreCycle4 = drive.actionBuilder(new Pose2d(58, -60.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-6, -30.3), Math.toRadians(90)), Math.toRadians(90))
                .build();

        Action PSA1 = drive.actionBuilder(new Pose2d(-6, -30.3, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -59.5), Math.toRadians(-90)), Math.toRadians(-90))
                .build();

        Action scoreCycle5 = drive.actionBuilder(new Pose2d(58, -60.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-9, -30.3), Math.toRadians(90)), Math.toRadians(90))
                .build();

        Action PSA2 = drive.actionBuilder(new Pose2d(-9, -30.3, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -59.5), Math.toRadians(-90)), Math.toRadians(-90))
                .build();

        Action scoreCycle6 = drive.actionBuilder(new Pose2d(58, -60.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(9, -30.3), Math.toRadians(90)), Math.toRadians(90))
                .build();

        Action PSA3 = drive.actionBuilder(new Pose2d(9, -30.3, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -59.5), Math.toRadians(-90)), Math.toRadians(-90))
                .build();

        Action scoreCycle7 = drive.actionBuilder(new Pose2d(58, -60.5, Math.toRadians(90)))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(12, -30.3), Math.toRadians(90)), Math.toRadians(90))
                .build();





        waitForStart();

        if (isStopRequested()){
            return;

        }

        Actions.runBlocking(
                new SequentialAction(
                        scorePreload,
                        score(),
                        push,
                        pick(),
                        scoreCycle1,
                        score(),
                        pickCycle1,
                        pick(),
                        scoreCycle2,
                        score(),
                        pickCycle2,
                        pick(),
                        scoreCycle3,
                        score(),
                        pickCycle3,
                        pick(),
                        scoreCycle4,
                        score(),
                        psa(),
                        PSA1,
                        drop(),
                        pick(),
                        scoreCycle5,
                        score(),
                        psa(),
                        PSA2,
                        drop(),
                        pick(),
                        scoreCycle6,
                        score(),
                        psa(),
                        PSA3,
                        drop(),
                        pick(),
                        scoreCycle7,
                        score()



                )

        );

    }



}
