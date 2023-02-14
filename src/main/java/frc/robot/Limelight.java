package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Limelight {
    
    private NetworkTable table;
    private NetworkTableEntry tx;
    private NetworkTableEntry ta;
    private NetworkTableEntry ts;

    private double x_value;
    private double a_value;
    private double s_value;

    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ta = table.getEntry("ta");
        ts = table.getEntry("ts");
    }

    public void logging() {
        //read values periodically
        x_value = tx.getDouble(0.0);
        a_value = ta.getDouble(0.0);
        s_value = ts.getDouble(0.0);

        //post to smart dashboard periodically
        SmartDashboard.putNumber("LimelightX", x_value);
        SmartDashboard.putNumber("LimelightArea", a_value);
        SmartDashboard.putNumber("LimelightSkew", s_value);
    }

    public double getX() {
        return x_value;
    }

    public double XVisionAlign() {
        double x_speed = x_value/-29;
        x_speed = Math.max(x_speed, -0.35);
        x_speed = Math.min(x_speed, 0.35);
        return x_speed;
    }

    public double SkewVisionAlign() {
        double s_speed = s_value/29;
        s_speed = Math.max(s_speed, -0.3);
        s_speed = Math.min(s_value, 0.3);
        return s_speed;
    }
    public double getArea() {
        return a_value;
    }

    public double getSkew() {
        return s_value;
    }
}
