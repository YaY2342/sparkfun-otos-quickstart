package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {
    public enum robotStates {
        SCORE,
        SCORE_ELBOW_RETRACT,
        SCORE_SLIDES_EXTEND,
        SCORE_SHOULDER_EXTEND,
        SCORE_ELBOW_EXTEND,
        SCORE_CLAW_OPEN,
        SCORE_SHOULDER_RETRACT,
        SCORE_SLIDES_DOWN,
        // PU means Pick_UP
        PU_SPECIMEN,
        PU_SHOULDER_EXTEND,
        PU_CLAW_OPEN,
        PU_ELBOW_EXTEND,
        PU_CLAW_CLOSE,
        PU_SHOULDER_UP,
        PU_ELBOW_RETRACT,
        PU_SHOULDER_RETRACT,
        //psa stands for pickup sample.
        PSA,
        PSA_SLIDES_LOW,
        PSA_GET_READY_TO_PICK_UP,
        PSA_CLAW_CLOSE,
        PSA_ELBOW_RETRACT,
        DROP_SAMPLE,
    }


    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("left_front");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("left_back");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("right_front");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("back_right");
        DcMotor SL = hardwareMap.dcMotor.get("slidesLeft");
        DcMotor SR = hardwareMap.dcMotor.get("slidesRight");
        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // C(servoname) stands for CLAW(servo) like CWrist, is the claw wrist movement.

        Servo CW = hardwareMap.get(Servo.class, "wrist");
        Servo CE = hardwareMap.get(Servo.class, "elbow");
        Servo claw = hardwareMap.get(Servo.class, "claw");
        Servo CS = hardwareMap.get(Servo.class, "shoulder");
        Servo HSL = hardwareMap.get(Servo.class, "Horizontal Slide Left");
        Servo HSR = hardwareMap.get(Servo.class, "Horizontal Slide Right");

        RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, "brushland color sensor");

        // NONE OF THESE ARE PROBABLY THE RIGHT AMOUNT SINCE I CANNOT TUNE!

        // These are for the SLIDES
        int score = 4;
        int low = 1;
        int pu = 2;

        // There are for the HORIZONTAL SLIDES
        int slideExtension = 0;

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
        // This is a counter so that the whenever the player is down scoring the samples that are placed on the ground,
        // it switches to the mode to pick up samples.
        int counter = 0;

        robotStates robot = robotStates.PU_SPECIMEN;
        ElapsedTime timer = new ElapsedTime();



        // A slow mode so that you can easily control the robot.
        if (gamepad1.right_bumper){
            frontLeftMotor.setPower(.6);
            frontRightMotor.setPower(.6);
            backLeftMotor.setPower(.6);
            backRightMotor.setPower(.6);
        }
        else {
            frontLeftMotor.setPower(.9);
            frontRightMotor.setPower(.9);
            backLeftMotor.setPower(.9);
            backRightMotor.setPower(.9);
        }

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            if (colorSensor instanceof SwitchableLight) {
                ((SwitchableLight) colorSensor).enableLight(true);
            }

            switch(robot){
                case SCORE:
                    CW.setPosition(wrist);
                    if (gamepad1.a){
                        robot = robotStates.SCORE_ELBOW_RETRACT;
                        timer.reset();
                    }
                    break;
                case SCORE_ELBOW_RETRACT:
                    CE.setPosition(elbowRetracted);
                    if (timer.milliseconds() > 800)
                    {
                        robot = robotStates.SCORE_SLIDES_EXTEND;
                        timer.reset();
                    }
                    break;
                case SCORE_SLIDES_EXTEND:
                    SR.setTargetPosition((int) score);
                    SL.setTargetPosition((int) score);
                    if (timer.milliseconds() > 1200)
                    {
                        robot = robotStates.SCORE_SHOULDER_EXTEND;
                        timer.reset();
                    }
                    break;
                case SCORE_SHOULDER_EXTEND:
                    CS.setPosition(shoulderUP);
                    if (timer.milliseconds() > 1200)
                    {
                        robot = robotStates.SCORE_ELBOW_EXTEND;
                        timer.reset();
                    }
                    break;
                case SCORE_ELBOW_EXTEND:
                    CE.setPosition(elbowExtended);
                    if (timer.milliseconds() > 500)
                    {
                        robot = robotStates.SCORE_CLAW_OPEN;
                        timer.reset();
                    }
                    break;
                case SCORE_CLAW_OPEN:
                    claw.setPosition(open);
                    if (timer.milliseconds() > 100)
                    {
                        robot = robotStates.SCORE_SHOULDER_RETRACT;
                        timer.reset();
                    }
                    break;
                case SCORE_SHOULDER_RETRACT:
                    CS.setPosition(shoulderRetracted);
                    if (timer.milliseconds() > 500)
                    {
                        robot = robotStates.SCORE_SLIDES_DOWN;
                        timer.reset();
                    }
                    break;
                case SCORE_SLIDES_DOWN:
                    SR.setTargetPosition((int) low);
                    SL.setTargetPosition((int) low);
                    if (timer.milliseconds() > 500)
                    {
                        if (counter >= 3) {
                            robot = robotStates.PSA;
                            timer.reset();
                        }
                        else
                        {
                            counter = counter + 1;
                            robot = robotStates.PU_SPECIMEN;
                        }
                    }
                    break;
                case PU_SPECIMEN:
                    SR.setTargetPosition((int) pu);
                    SL.setTargetPosition((int) pu);
                    if (gamepad1.b)
                    {
                        robot = robotStates.PU_SHOULDER_EXTEND;
                        timer.reset();
                    }
                    break;
                case PU_SHOULDER_EXTEND:
                    CS.setPosition(shoulderExtended);
                    if (timer.milliseconds() > 300)
                    {
                        robot = robotStates.PU_CLAW_OPEN;
                        timer.reset();
                    }
                    break;
                case PU_CLAW_OPEN:
                    claw.setPosition(open);
                    if (timer.milliseconds() > 100)
                    {
                        robot = robotStates.PU_ELBOW_EXTEND;
                        timer.reset();
                    }
                    break;
                case PU_ELBOW_EXTEND:
                    CE.setPosition(elbowExtended);
                    if (timer.milliseconds() > 500)
                    {
                        robot = robotStates.PU_CLAW_CLOSE;
                        timer.reset();
                    }
                    break;
                case PU_CLAW_CLOSE:
                    claw.setPosition(close);
                    if (timer.milliseconds() > 100)
                    {
                        robot = robotStates.PU_SHOULDER_UP;
                        timer.reset();
                    }
                    break;
                case PU_SHOULDER_UP:
                    CS.setPosition(shoulderUP);
                    if (timer.milliseconds() > 300)
                    {
                        robot = robotStates.PU_ELBOW_RETRACT;
                        timer.reset();
                    }
                    break;
                case PU_ELBOW_RETRACT:
                    CE.setPosition(elbowRetracted);
                    if (timer.milliseconds() > 300)
                    {
                        robot = robotStates.PU_SHOULDER_RETRACT;
                        timer.reset();
                    }
                    break;
                case PU_SHOULDER_RETRACT:
                    CS.setPosition(shoulderRetracted);
                    if (timer.milliseconds() > 500)
                    {
                        robot = robotStates.SCORE;
                        timer.reset();
                    }
                    break;
                case PSA:
                    if (gamepad1.x)
                    {
                        robot = robotStates.PSA_SLIDES_LOW;
                        timer.reset();
                    }
                    break;
                case PSA_SLIDES_LOW:
                    SL.setTargetPosition(low);
                    SR.setTargetPosition(low);
                    if (timer.milliseconds() > 500)
                    {
                        robot = robotStates.PSA_GET_READY_TO_PICK_UP;
                        timer.reset();

                    }
                    break;
                case PSA_GET_READY_TO_PICK_UP:
                    if (gamepad1.dpad_left)
                    {
                        frontLeftMotor.setPower(1);
                        backRightMotor.setPower(1);
                    }
                    if (gamepad1.dpad_right)
                    {
                        frontRightMotor.setPower(1);
                        backLeftMotor.setPower(1);
                    }
                    if (gamepad1.dpad_up)
                    {
                        HSR.setPosition(slideExtension+1);
                        HSL.setPosition(slideExtension+1);
                    }
                    if (gamepad1.dpad_up)
                    {
                        HSR.setPosition(slideExtension-1);
                        HSL.setPosition(slideExtension-1);
                    }
                    if (gamepad1.x)
                    {
                        robot = robotStates.PSA_CLAW_CLOSE;
                        timer.reset();
                    }
                    break;
                case PSA_CLAW_CLOSE:
                    CS.setPosition(elbowRetracted);
                    sleep(500);
                    claw.setPosition(close);
                    if (timer.milliseconds() > 100)
                    {
                        robot = robotStates.PSA_ELBOW_RETRACT;
                        timer.reset();
                    }
                    break;
                case PSA_ELBOW_RETRACT:
                    CE.setPosition(elbowRetracted);
                    if (timer.milliseconds() > 300)
                    {
                        robot = robotStates.DROP_SAMPLE;
                        timer.reset();
                    }
                    break;
                case DROP_SAMPLE:
                    if (gamepad1.y)
                    {
                        CE.setPosition(elbowExtended);
                        sleep(50);
                        claw.setPosition(open);
                        CW.setPosition(wrist);
                        timer.reset();
                        robot = robotStates.PU_SPECIMEN;

                    }



            }

        }
    }
}