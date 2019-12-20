/*
 * Copyright (c) 2020 Titan Robotics Club (http://www.titanrobotics.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHEPIXYRWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package team492;

public class RobotInfo
{
    //
    // Field dimensions in inches.
    //
    public static final double FIELD_LENGTH                     = 54*12.0;
    public static final double FIELD_WIDTH                      = 27*12.0;

    public static final double BATTERY_CAPACITY_WATT_HOUR       = 18.0*12.0;

    public static final double BATTERY_NOMINAL_VOLTAGE          = 12.0;

    //
    // Robot dimensions.
    //

    //
    // Joystick ports.
    //
    public static final int JSPORT_LEFT_DRIVESTICK              = 0;
    public static final int JSPORT_RIGHT_DRIVESTICK             = 1;
    public static final int JSPORT_OPERATORSTICK                = 2;
    public static final int JSPORT_BUTTON_PANEL                 = 3;
    public static final int JSPORT_SWITCH_PANEL                 = 4;

    //
    // CAN IDs.
    //
    public static final int CANID_LEFTFRONTWHEEL                = 3;    // 40A: Orange
    public static final int CANID_RIGHTFRONTWHEEL               = 4;    // 40A: Yellow
    public static final int CANID_LEFTREARWHEEL                 = 5;    // 40A: Green
    public static final int CANID_RIGHTREARWHEEL                = 6;    // 40A: Blue

    public static final int CANID_PIGEON                        = 15;
    public static final int CANID_PDP                           = 16;
    public static final int CANID_PCM1                          = 17;
    public static final int CANID_PCM2                          = 18;

    //
    // PDP Channels.
    //
    public static final int PDP_CHANNEL_RIGHT_REAR_WHEEL        = 0;
    public static final int PDP_CHANNEL_RIGHT_FRONT_WHEEL       = 3;
    public static final int PDP_CHANNEL_PIGEON                  = 8;
    public static final int PDP_CHANNEL_BLINKIN                 = 9;
    public static final int PDP_CHANNEL_RING_LIGHT              = 10;
    public static final int PDP_CHANNEL_LEFT_FRONT_WHEEL        = 12;
    public static final int PDP_CHANNEL_LEFT_REAR_WHEEL         = 15;

    //
    // Analog Input ports.
    //
    public static final int AIN_PRESSURE_SENSOR                 = 0;

    //
    // Digital Input/Output ports.
    //

    //
    // PWM Channels.
    //
    public static final int PWM_REV_BLINKIN                     = 0;

    //
    // Relay channels.
    //
    public static final int RELAY_RINGLIGHT_POWER               = 0;    // 20A: Green
    //
    // Solenoid channels.
    //

    //
    // Vision subsystem.
    //
    public static final double CAMERA_DEPTH                     = -17; // Inches from center of EE to center of camera, + = forward
    public static final double CAMERA_OFFSET                    = 3; // Inches from center of EE to center of camera, + = right
    public static final double CAMERA_PITCH                     = 0.0; // Down is positive
    public static final double CAMERA_DATA_TIMEOUT              = 0.5; // 500ms
    public static final double CAMERA_CENTERED_THRESHOLD        = 2; // +- 2 inches in x axis

    //
    // Ultrasonic sensors.
    //
    public static final double SONAR_INCHES_PER_VOLT            = 1.0/0.0098; //9.8mV per inch
    public static final double SONAR_ERROR_THRESHOLD            = 50.0; //value should not jump 50-in per time slice.

    //
    // DriveBase subsystem.
    //
    public static final double DRIVE_STALL_TIMEOUT              = 0.5;

    public static final double DRIVE_SLOW_XSCALE                = 0.5;
    public static final double DRIVE_SLOW_YSCALE                = 0.5;
    public static final double DRIVE_SLOW_TURNSCALE             = 0.4;

    public static final double DRIVE_MEDIUM_XSCALE              = 0.75;
    public static final double DRIVE_MEDIUM_YSCALE              = 0.75;
    public static final double DRIVE_MEDIUM_TURNSCALE           = 0.6;

    public static final double DRIVE_FAST_XSCALE                = 1.0;
    public static final double DRIVE_FAST_YSCALE                = 1.0;
    public static final double DRIVE_FAST_TURNSCALE             = 0.8;

    public static final double DRIVE_GYRO_ASSIST_KP             = 1.5;
    public static final double DRIVE_MAX_ROTATION_RATE          = 6.5;      //radians per second

    public static final double DRIVE_MAX_XPID_POWER             = 0.5;
    public static final double DRIVE_MAX_YPID_POWER             = 0.6;
    public static final double DRIVE_MAX_TURNPID_POWER          = 1.0;

    public static final double DRIVE_MAX_XPID_RAMP_RATE         = 0.5;
    public static final double DRIVE_MAX_YPID_RAMP_RATE         = 0.6;
    public static final double DRIVE_MAX_TURNPID_RAMP_RATE      = 1.0;

    // practice robot: 0.012/0.0/0.0
    public static final double ENCODER_X_INCHES_PER_COUNT       = 1.6577438;
    public static final double ENCODER_X_KP                     = 0.017;
    public static final double ENCODER_X_KI                     = 0.0;
    public static final double ENCODER_X_KD                     = 0.0;
    public static final double ENCODER_X_KF                     = 0.0;
    public static final double ENCODER_X_TOLERANCE              = 2.0;

    // practice robot: 0.024/0.0/0.0
    public static final double ENCODER_X_KP_SMALL               = 0.022;
    public static final double ENCODER_X_KI_SMALL               = 0.0;
    public static final double ENCODER_X_KD_SMALL               = 0.0;
    public static final double ENCODER_X_KF_SMALL               = 0.0;
    public static final double ENCODER_X_TOLERANCE_SMALL        = 1.5;

    // comp robot: 0.02/0.0/0.0016
    // practice robot: 0.01/0.0/0.001
    public static final double ENCODER_Y_INCHES_PER_COUNT       = 2.355935875;
    public static final double ENCODER_Y_KP                     = 0.011;
    public static final double ENCODER_Y_KI                     = 0.0;
    public static final double ENCODER_Y_KD                     = 0.001;
    public static final double ENCODER_Y_KF                     = 0.0;
    public static final double ENCODER_Y_TOLERANCE              = 2.0;

    // Comp robot: 0.015/0.0/0.001
    // practice robot: 0.008/0.0/0.0007
    // 3/24 comp robot: 0.0055/0.0/0.00008
    public static final double GYRO_TURN_KP                     = 0.0055;
    public static final double GYRO_TURN_KI                     = 0.0;
    public static final double GYRO_TURN_KD                     = 0.00008;
    public static final double GYRO_TURN_KF                     = 0.0;
    public static final double GYRO_TURN_TOLERANCE              = 2.0;

    // 3/24 compr robot: 0.0085/0.0/0.0
    public static final double GYRO_TURN_KP_SMALL               = 0.0085;
    public static final double GYRO_TURN_KI_SMALL               = 0.0;
    public static final double GYRO_TURN_KD_SMALL               = 0.0;
    public static final double GYRO_TURN_KF_SMALL               = 0.0;
    public static final double GYRO_TURN_TOLERANCE_SMALL        = 1.5;

    public static final double VISION_X_KP                      = 0.022;    //TODO: Need to tune this
    public static final double VISION_X_KI                      = 0.0;
    public static final double VISION_X_KD                      = 0.001;
    public static final double VISION_X_TOLERANCE               = 1.0;

    public static final double VISION_Y_KP                      = 0.011;    //TODO: Need to tune this
    public static final double VISION_Y_KI                      = 0.0;
    public static final double VISION_Y_KD                      = 0.0;
    public static final double VISION_Y_TOLERANCE               = 1.0;

    public static final double VISION_TURN_KP                   = 0.0085;   //TODO: Need to tune this
    public static final double VISION_TURN_KI                   = 0.0;
    public static final double VISION_TURN_KD                   = 0.0;
    public static final double VISION_TURN_TOLERANCE            = 1.0;

}   // class RobotInfo
