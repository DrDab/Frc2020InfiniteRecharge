package team492;

import frclib.FrcRemoteVisionProcessor;
import hallib.HalDashboard;
import trclib.TrcDbgTrace;
import trclib.TrcEvent;
import trclib.TrcPath;
import trclib.TrcPose2D;
import trclib.TrcRobot;
import trclib.TrcStateMachine;
import trclib.TrcTimer;
import trclib.TrcUtil;
import trclib.TrcWaypoint;

import java.util.Arrays;

public class CmdShooterAuto implements TrcRobot.RobotCommand
{
    private static final String instanceName = "CmdShooterAuto";
    private static final double relocalizationTimeout = 0.5;

    private enum State
    {
        DELAY, LOCALIZE, MOVE_TO_SHOOT, SHOOT, RELOCALIZE, PICKUP, MOVE_TO_SHOOT_2, SHOOT_2, DONE
    }

    public enum AfterAction
    {
        NOTHING, INTAKE, INTAKE_AND_SHOOT;
    }

    private Robot robot;
    private TrcStateMachine<State> sm;
    private Double relocalizationTimedOutTime = null;
    private TrcEvent event;
    private TrcTimer timer;
    private double delay;
    private FrcAuto.StartPosition startPosition;
    private AfterAction afterAction;
    private TrcDbgTrace dbgTrace;

    public CmdShooterAuto(Robot robot)
    {
        this.robot = robot;
        dbgTrace = TrcDbgTrace.getGlobalTracer();
        sm = new TrcStateMachine<>(instanceName + ".sm");
        event = new TrcEvent(instanceName + ".event");
        timer = new TrcTimer(instanceName + ".timer");
    }

    public void start(double delay, FrcAuto.StartPosition startPosition, AfterAction afterAction)
    {
        this.delay = delay;
        this.startPosition = startPosition;
        this.afterAction = afterAction;
        sm.start(State.DELAY);
        dbgTrace.traceInfo(instanceName + ".start", "Starting with options: delay=%.3f,afterAction=%s", delay,
            afterAction.name());
    }

    private TrcPath createPath(TrcPose2D... poses)
    {
        TrcPath path = new TrcPath(Arrays.stream(poses).map(p -> new TrcWaypoint(p.relativeTo(poses[0], false), null))
            .toArray(TrcWaypoint[]::new));
        TrcPath ret = path.trapezoidVelocity(RobotInfo.ROBOT_MAX_REQ_SPEED, RobotInfo.ROBOT_MAX_ACCEL);
        dbgTrace.traceInfo(instanceName + ".createPath", "TrcPath(degrees=%b)", ret.isInDegrees());
        for (TrcWaypoint waypoint : ret.getAllWaypoints())
        {
            dbgTrace.traceInfo(instanceName + ".createPath", "\t%s", waypoint.toString());
        }
        return ret;
    }

    public TrcPath createToShootPath(TrcPose2D start)
    {
        dbgTrace.traceInfo(instanceName + ".createToShootPath", "Creating to shoot path with start=%s", start);
        if (startPosition != FrcAuto.StartPosition.RIGHT_WALL)
        {
            double targetY = -RobotInfo.ROBOT_LENGTH - 10;
            TrcPose2D target = new TrcPose2D(RobotInfo.TARGET_X_POS, targetY);
            TrcPose2D middle = new TrcPose2D(start.x, target.y);
            return startPosition == FrcAuto.StartPosition.IN_VISION ?
                createPath(start, target) :
                createPath(start, middle, target);
        }
        else
        {
            TrcPose2D target = new TrcPose2D(RobotInfo.TRENCH_RUN_X_POS, -50, -22);
            TrcPose2D middle = new TrcPose2D(target.x, start.y);
            return createPath(start, middle, target);
        }
    }

    public TrcPath createPickupPath(TrcPose2D start)
    {
        dbgTrace.traceInfo(instanceName + ".createToPickupPath", "Creating to pickup path with start=%s", start);
        if (startPosition != FrcAuto.StartPosition.RIGHT_WALL)
        {
            TrcPose2D target = new TrcPose2D(RobotInfo.TRENCH_RUN_X_POS, RobotInfo.LAST_TRENCH_BALL_Y_POS);
            TrcPose2D middle = new TrcPose2D(target.x, start.y);
            return createPath(start, middle, target);
        }
        else
        {
            TrcPose2D middle = new TrcPose2D(RobotInfo.TRENCH_RUN_X_POS, -100, 0);
            TrcPose2D target = new TrcPose2D(RobotInfo.TRENCH_RUN_X_POS, RobotInfo.LAST_TRENCH_BALL_Y_POS);
            return createPath(start, middle, target);
        }
    }

    public TrcPath createToShoot2Path()
    {
        throw new IllegalStateException("Not implemented yet!"); // TODO: implement
    }

    private void localizeAtStart()
    {
        double x = startPosition != FrcAuto.StartPosition.CUSTOM ?
            startPosition.getXPos() :
            HalDashboard.getNumber(FrcAuto.CUSTOM_XPOS_KEY, 0);
        double y = RobotInfo.INITIATION_LINE_TO_ALLIANCE_WALL - robot.alignment.getShortestDistanceToWall()
            - RobotInfo.ROBOT_LENGTH / 2;
        double angle = -robot.alignment.getAngleToWall();
        if (startPosition == FrcAuto.StartPosition.IN_VISION)
        {
            // if no vision, assume perfectly centered to goal
            FrcRemoteVisionProcessor.RelativePose pose = robot.vision.getLastPose();
            if (pose != null)
            {
                TrcPose2D p = new TrcPose2D(pose.x, pose.y);
                x = RobotInfo.INITIATION_LINE_TO_ALLIANCE_WALL - p.relativeTo(new TrcPose2D(0, 0, -angle)).x;
            }
        }
        else if (startPosition == FrcAuto.StartPosition.RIGHT_WALL)
        {
            x = startPosition.getXPos();
            y = -RobotInfo.ROBOT_LENGTH / 2;
            angle = 0;
        }
        TrcPose2D pose = new TrcPose2D(x, y, angle);
        dbgTrace.traceInfo(instanceName + ".localizeAtStart", "Localizing to pose: %s", pose.toString());
        robot.driveBase.setFieldPosition(pose);
    }

    @Override
    public boolean cmdPeriodic(double elapsedTime)
    {
        if (!sm.isEnabled())
            return true;
        State state = sm.checkReadyAndGetState();
        if (state != null)
        {
            dbgTrace.traceInfo(instanceName + ".cmdPeriodic", "[%.3f] CurrState=%s", state.name());
            TrcPath path;
            switch (state)
            {
                case DELAY:
                    robot.shooter.setFlywheelVelocity(RobotInfo.FLYWHEEL_HIGH_SPEED);
                    robot.shooter.setPitch(RobotInfo.FLYWHEEL_HIGH_ANGLE);
                    if (delay > 0)
                    {
                        timer.set(delay, event);
                        sm.waitForSingleEvent(event, State.LOCALIZE);
                    }
                    else
                    {
                        sm.setState(State.LOCALIZE);
                    }
                    break;

                case LOCALIZE:
                    localizeAtStart();
                    sm.setState(State.MOVE_TO_SHOOT);
                    break;

                case MOVE_TO_SHOOT:
                    path = createToShootPath(robot.driveBase.getFieldPosition());
                    robot.purePursuit.start(path, event, 4);
                    sm.waitForSingleEvent(event, State.SHOOT);
                    break;

                case SHOOT:
                    robot.autoShooter.shoot(instanceName, 3, 2, TaskAutoShooter.Mode.BOTH, event);
                    relocalizationTimedOutTime = null;
                    sm.waitForSingleEvent(event, State.RELOCALIZE);
                    break;

                case RELOCALIZE:
                    if (relocalizationTimedOutTime == null)
                    {
                        relocalizationTimedOutTime = TrcUtil.getCurrentTime() + relocalizationTimeout;
                    }
                    if (relocalizeWithVision() || TrcUtil.getCurrentTime() >= relocalizationTimedOutTime)
                    {
                        sm.setState(afterAction == AfterAction.NOTHING ? State.DONE : State.PICKUP);
                    }
                    break;

                case PICKUP:
                    robot.shooter.setPitch(0);
                    path = createPickupPath(robot.driveBase.getFieldPosition());
                    robot.intake.intakeMultiple();
                    robot.purePursuit.start(path, event, 5);
                    sm.waitForSingleEvent(event,
                        afterAction == AfterAction.INTAKE ? State.DONE : State.MOVE_TO_SHOOT_2);
                    break;

                case MOVE_TO_SHOOT_2:
                    robot.intake.stopIntake();
                    robot.shooter.setPitch(RobotInfo.FLYWHEEL_HIGH_ANGLE);
                    robot.shooter.setFlywheelVelocity(RobotInfo.FLYWHEEL_HIGH_SPEED);
                    path = createToShoot2Path();
                    robot.purePursuit.start(path, event, 4);
                    sm.waitForSingleEvent(event, State.SHOOT_2);
                    break;

                case SHOOT_2:
                    robot.autoShooter.shoot(instanceName, 3, 2, TaskAutoShooter.Mode.BOTH, event);
                    sm.waitForSingleEvent(event, State.DONE);
                    break;

                case DONE:
                    cancel();
                    return true;
            }
        }
        return false;
    }

    private boolean relocalizeWithVision()
    {
        FrcRemoteVisionProcessor.RelativePose relPose = robot.vision.getLastPose();
        if (relPose == null)
            return false;
        TrcPose2D p = new TrcPose2D(relPose.x, relPose.y);
        double heading = robot.driveBase.getHeading();
        TrcPose2D inFieldFrame = p.relativeTo(new TrcPose2D(0, 0, -heading));
        TrcPose2D pose = new TrcPose2D(RobotInfo.TARGET_X_POS - inFieldFrame.x,
            RobotInfo.INITIATION_LINE_TO_ALLIANCE_WALL - inFieldFrame.y, heading);
        dbgTrace.traceInfo(instanceName + ".relocalizeWithVision", "CameraRelPose=%s,NewPose=%s", p.toString(),
            pose.toString());
        robot.driveBase.setFieldPosition(pose);
        return true;
    }

    @Override
    public boolean isActive()
    {
        return sm.isEnabled();
    }

    @Override
    public void cancel()
    {
        sm.stop();
        robot.shooter.stopFlywheel();
        robot.shooter.setPitch(0);
    }
}
