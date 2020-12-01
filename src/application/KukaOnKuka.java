package application;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import Util.Gripper;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;
import com.kuka.roboticsAPI.geometricModel.math.Vector;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianSineImpedanceControlMode;
import com.kuka.roboticsAPI.persistenceModel.processDataModel.IProcessData;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.kuka.roboticsAPI.sensorModel.TorqueSensorData;
import com.kuka.generated.flexfellow.FlexFellow;
import com.kuka.generated.ioAccess.MediaFlangeIOGroup;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see UseRoboticsAPIContext
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class KukaOnKuka extends RoboticsAPIApplication {
	@Inject
	private LBR iiwa;

	@Inject 
	private Gripper gripper;

	@Inject
	private MediaFlangeIOGroup mediaFlange;
	
    private static double JOGGING_VELOCITY = 0.1; // moving velocity
    // Recommended velocity for labyrinth solving: TBD
    
    private static double CARTESIAN_JERK = 500;
    // Recommended jerk 50

    private static double FORCE_STOP_THRESHOLD = 4;
    
    private static double MOVEMENT_STEP = 3;
    
	@Override
	public void initialize() {
		// initialize your application here
		gripper.attachTo(iiwa.getFlange());
	}

	@Override
	public void run() {
        mediaFlange.setLEDBlue(false);
		// your application execution starts here
        int i = 0;
        double arr[] = new double[] { Math.toRadians(-17.55), 
        								Math.toRadians(-57.36),
        								Math.toRadians(7.7),
        								Math.toRadians(73.49),
        								Math.toRadians(-8.85),
        								Math.toRadians(-50.97),
        								Math.toRadians(-7.22) };
        boolean stopRobot = false;
        gripper.attachTo(iiwa.getFlange());
    	gripper.close_M();
//         move to home position
//        getLogger().info("Moving to home position");
//        iiwa.move(ptpHome().setJointVelocityRel(joggingVelocity));
        getLogger().info("Moving to operation postion");
        iiwa.move(ptp((Double) Array.get(arr,0),
        			(Double) Array.get(arr,1),
        			(Double) Array.get(arr,2),
        			(Double) Array.get(arr,3),
        			(Double) Array.get(arr,4),
        			(Double) Array.get(arr,5),
        			(Double) Array.get(arr,6)).setJointVelocityRel(JOGGING_VELOCITY));
//        iiwa.move(ptpHome().setJointVelocityRel(JOGGING_VELOCITY));
        mediaFlange.setLEDBlue(true);
        while ( true ){

        	// Open close flange
//            if(isFlangeOpen){
//            	gripper.close_M();
//            	isFlangeOpen = false;
//            } else {
//            	gripper.open_M();
//            	isFlangeOpen = true;
//            }
        	while(iiwa.hasActiveMotionCommand()){
        		getLogger().info("Motion in progress");
            	try {
    				TimeUnit.MILLISECONDS.sleep(500);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        	String logTorque = "";
            ForceSensorData externalData = iiwa.getExternalForceTorque(iiwa.getFlange());
            Vector val = externalData.getForce();
            int len = (int) val.length();
            getLogger().info( String.valueOf((int) len) );
            for(i = 0; i < 3; i++){
            	logTorque += String.format("%.2f, ", val.get(i));
            }
            for(i = 0; i < 3; i++){
            	if(val.get(i) > FORCE_STOP_THRESHOLD || val.get(i) < -FORCE_STOP_THRESHOLD){
            		stopRobot = true;
            	}
            }
            getLogger().info(logTorque);
            iiwa.isReadyToMove();
            if(stopRobot){
                getLogger().info("External force detected, Stopping!");
                mediaFlange.setLEDBlue(false);
                System.exit(0);
                return;
            }
            iiwa.move(linRel().setXOffset(-MOVEMENT_STEP).setJointVelocityRel(JOGGING_VELOCITY).setCartJerk(CARTESIAN_JERK));
            iiwa.moveAsync(linRel().setYOffset(-MOVEMENT_STEP).setJointVelocityRel(JOGGING_VELOCITY).setCartJerk(CARTESIAN_JERK));
//            iiwa.moveAsync(linRel().setYOffset(-20));
            // Y initial -1083.19, Y final -1077.93
        }
	}
	
	//dispose
	@Override	
	public void dispose() {
		iiwa.detachAll();
	}
}