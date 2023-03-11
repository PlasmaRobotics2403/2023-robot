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

    /**
     * all values that need to be updated periodically
     * and smartdashboard display
     */
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


    /**
     * offset from the vision target left and right
     * @return speed needed to be centered with the vision target
     */
    public double XVisionAlign() {
        double x_speed = x_value/-29;
        x_speed = Math.max(x_speed, -Constants.LimelightConstants.maxTanslationalSpeed);
        x_speed = Math.min(x_speed, Constants.LimelightConstants.maxTanslationalSpeed);
        return x_speed;
    }


    /**
     * show how far off in degrees rotation across the horizontal axis
     * @return the speed needed to rotate parallel with the horizontal axis
     */
    public double SkewVisionAlign() {
        double s_speed = s_value/29;
        s_speed = Math.max(s_speed, -Constants.LimelightConstants.maxSkewSpeed);
        s_speed = Math.min(s_value, Constants.LimelightConstants.maxSkewSpeed);
        return s_speed;
    }


    /**
     * showes how far the limelight is from the vision target
     * centered at 1.2 meters
     * @return speed needed to get to 1.2 meters
     */
    public double distanceVisionAlign() {
        double distance = a_value - 1.2;
        double y_speed = distance * 1;
        y_speed = Math.max(y_speed, -Constants.LimelightConstants.maxTanslationalSpeed);
        y_speed = Math.min(y_speed, Constants.LimelightConstants.maxTanslationalSpeed);
        
        return y_speed;
    }


    /**
     * shows how much of the screen is taken up by the vision target
     * @return area of vision taget in percentage
     */
    public double getArea() {
        return a_value;
    }


    /**
     * rotation off compared to the horizontal axis of the vision taget
     * @return angle from horizontal axis
     */
    public double getSkew() {
        return s_value;
    }


    /**
     * left and right from the vision target
     * @return diistance from virtical axis
     */
    public double getX() {
        return x_value;
    }
}
