package com.example.meepmeeptestingmodule; // Adjust package name

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MyClass {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800); // Field size in pixels

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15) // Adjust constraints
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, -62, Math.toRadians(90)))
                .strafeTo(new Vector2d(0, -32.5))
                .waitSeconds(.5)

                .setTangent(Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(36, -30), Math.toRadians(90))

                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(47, -11), Math.toRadians(-90))
                .strafeTo(new Vector2d(47, -55))

                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(59, -11), Math.toRadians(90)), Math.toRadians(0))
                .strafeTo(new Vector2d(59, -60.5))

                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(63, -13), Math.toRadians(-90)), Math.toRadians(0))
                .strafeTo(new Vector2d(63, -60.5))


                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(3, -32.5), Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -60.5), Math.toRadians(-90)), Math.toRadians(-90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-3, -32.5), Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -60.5), Math.toRadians(-90)), Math.toRadians(-90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(6, -32.5), Math.toRadians(90)), Math.toRadians(90))

                .waitSeconds(.5)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -60.5), Math.toRadians(-90)), Math.toRadians(-90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-6, -32.5), Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -60.5), Math.toRadians(-90)), Math.toRadians(-90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(-9, -32.5), Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(new Vector2d(58, -60.5), Math.toRadians(-90)), Math.toRadians(-90))
                .waitSeconds(.5)
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(new Vector2d(9, -32.5), Math.toRadians(90)), Math.toRadians(90))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
