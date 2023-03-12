package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Limelight {
    
    private NetworkTable table;
    private NetworkTableEntry tx;       // x value
    private NetworkTableEntry ta;       // area value
    private NetworkTableEntry botpose;  // bot pose

    private double x_value;
    private double a_value;
    private double[] botposeArray;
    private double[] emptyArray;

    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ta = table.getEntry("ta");
        botpose = table.getEntry("botpose");

        emptyArray = new double[6];
        for (int i = 0; i < 6; i++) {
            emptyArray[i] = 0.0;
        }
    }

    /**
     * all values that need to be updated periodically
     * and smartdashboard display
     */
    public void logging() {
        //read values periodically
        try {
            botposeArray = botpose.getDoubleArray(emptyArray);
            SmartDashboard.putNumber("robot skew", botposeArray[5]);
        }
        catch(Exception e){

        }
            
        x_value = tx.getDouble(0.0);
        a_value = ta.getDouble(0.0);
        

        //post to smart dashboard periodically
        SmartDashboard.putNumber("LimelightX", x_value);
        SmartDashboard.putNumber("LimelightArea", a_value);
    }


    /**
     * offset from the vision target left and right
     * @return speed needed to be centered with the vision target
     */
    public double XVisionAlign() {
        double x_speed = (x_value + 10.5)/-29;
        x_speed = Math.max(x_speed, -Constants.LimelightConstants.maxTanslationalSpeed);
        x_speed = Math.min(x_speed, Constants.LimelightConstants.maxTanslationalSpeed);
        return x_speed;
    }


    /**
     * show how far off in degrees rotation across the horizontal axis
     * @return the speed needed to rotate parallel with the horizontal axis
     */
    public double SkewVisionAlign() {
        try {
            if(Math.abs(botposeArray[5]) > 5) {
                double s_speed = botposeArray[5]/25;
                s_speed = Math.max(s_speed, -Constants.LimelightConstants.maxSkewSpeed);
                s_speed = Math.min(botposeArray[5], Constants.LimelightConstants.maxSkewSpeed);
                DriverStation.reportError(Double.toString(s_speed), false);
                return s_speed;
            }
            else {
                return 0;
            }
        }
        catch(Exception e) {
            return 0;
        }
        
    }


    /**
     * showes how far the limelight is from the vision target
     * centered at 1.2 meters
     * @return speed needed to get to 1.2 meters
     */
    public double distanceVisionAlign() {
        double distance = a_value - 2;
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
        try{
           return botposeArray[5]; 
        }
        catch(Exception e) {
            return 0;
        }
        
    }


    /**
     * left and right from the vision target
     * @return diistance from virtical axis
     */
    public double getX() {
        return x_value;
    }
}
