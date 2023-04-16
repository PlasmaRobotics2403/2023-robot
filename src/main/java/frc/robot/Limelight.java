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
    private NetworkTableEntry tid;      // april tag number

    private double x_value;
    private double a_value;
    private double[] botposeArray;
    private double[] emptyArray;
    private double apriltagVal;

    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ta = table.getEntry("ta");
        botpose = table.getEntry("botpose");
        tid = table.getEntry("tid");

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

        apriltagVal = tid.getDouble(0);   
        x_value = tx.getDouble(0.0);
        a_value = ta.getDouble(0.0);
        

        //post to smart dashboard periodically
        SmartDashboard.putNumber("LimelightX", x_value);
        SmartDashboard.putNumber("LimelightArea", a_value);
        SmartDashboard.putNumber("april tag id", apriltagVal);
    }


    /**
     * offset from the vision target left and right
     * @return speed needed to be centered with the vision target
     */
    public double XVisionAlign() {
        double targetX = -15;
        if(targetX - 1 < x_value && x_value < targetX + 1) {

            return 0;
        }
        else {
            double x_speed = (targetX - x_value) * 0.05;
            x_speed = Math.max(x_speed, -Constants.LimelightConstants.maxTanslationalSpeed);
            x_speed = Math.min(x_speed, Constants.LimelightConstants.maxTanslationalSpeed);        
            return x_speed;
        }

    }


    /**
     * show how far off in degrees rotation across the horizontal axis
     * @return the speed needed to rotate parallel with the horizontal axis
     */
    public double SkewVisionAlign() {
        try {
            if(Math.abs(botposeArray[5]) > 1.5) {
                double s_speed = botposeArray[5] * 0.11;
                s_speed = Math.max(s_speed, -Constants.LimelightConstants.maxSkewSpeed);
                s_speed = Math.min(s_speed, Constants.LimelightConstants.maxSkewSpeed);
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
        double targetDistance = 2.5;
        if(targetDistance - 0.2 < a_value && a_value < targetDistance + 0.2) {
            
            return 0;
        }
        else {
            double y_speed = (a_value - targetDistance) * 0.8;
            y_speed = Math.max(y_speed, -Constants.LimelightConstants.maxTanslationalSpeed);
            y_speed = Math.min(y_speed, Constants.LimelightConstants.maxTanslationalSpeed);
            return y_speed;
        }
    }

    public double[] fullAlign() {
        double[] speeds = {0.0, 0.0, 0.0};

        speeds[2] = SkewVisionAlign();
        speeds[0] = distanceVisionAlign();
        if(speeds[2] == 0){
            speeds[1] = XVisionAlign();
        }    
        return speeds;
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

    public double getApriltag() {
        return apriltagVal;
    }
}
