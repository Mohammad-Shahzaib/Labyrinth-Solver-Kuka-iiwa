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
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianSineImpedanceControlMode;
import com.kuka.roboticsAPI.persistenceModel.processDataModel.IProcessData;
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
	
    private static double joggingVelocity = 0.05; // moving velocity
    // Recommended velocity for labyrinth solving: 0.01
    
	@Override
	public void initialize() {
		// initialize your application here
		gripper.attachTo(iiwa.getFlange());
	}

	@Override
	public void run() {
		// your application execution starts here
        double UPPER_JOINT_LIMIT = 1.7;
        double LOWER_JOINT_LIMIT = -1.7;
        double STEP = 0.25;
        int i = 0;
        double arr[] = new double[] { Math.toRadians(-19.42), 
        								Math.toRadians(-33.65),
        								Math.toRadians(18.35),
        								Math.toRadians(117.39),
        								Math.toRadians(-16),
        								Math.toRadians(-30.69),
        								Math.toRadians(8.09) };
        boolean dir[] = new boolean[] {true, false, true, true, false, true, true};
        int multiplier = 1;
        boolean isFlangeOpen = false;
        gripper.attachTo(iiwa.getFlange());
    	gripper.close_M();
        
        // move to home position
        getLogger().info("Moving to home position");
        iiwa.move(ptpHome().setJointVelocityRel(joggingVelocity));
        iiwa.move(ptp((Double) Array.get(arr,0),
        			(Double) Array.get(arr,1),
        			(Double) Array.get(arr,2),
        			(Double) Array.get(arr,3),
        			(Double) Array.get(arr,4),
        			(Double) Array.get(arr,5),
        			(Double) Array.get(arr,6)).setJointVelocityRel(joggingVelocity));
        iiwa.move(ptpHome().setJointVelocityRel(joggingVelocity));
        mediaFlange.setLEDBlue(true);
        Arrays.fill(arr, 0.0);
        while ( true ){

        	// Open close flange
            if(isFlangeOpen){
            	gripper.close_M();
            	isFlangeOpen = false;
            } else {
            	gripper.open_M();
            	isFlangeOpen = true;
            }
            
            // Calculate joint values
        	for(i = 0; i < 7; i++){
	        	if( (Boolean) Array.get(dir, i) ){
	        		multiplier = 1;
	        	}
	        	else {
	        		multiplier = -1;
	        	}
	        	Array.set(arr, i, (Double) Array.get(arr,i) + (multiplier * STEP) );
	 
	        	if((Double) Array.get(arr,i) > UPPER_JOINT_LIMIT){
	        		Array.set(dir, i, false);
	        	}
	        	else if((Double) Array.get(arr,i) < LOWER_JOINT_LIMIT){
	        		Array.set(dir, i, true);
	        	}
        	}
        	
        	// Set joint values and move
            String toLog = "Moving to: " + 
            		String.valueOf((Double) Array.get(arr,0)) + ", " + 
            		String.valueOf((Double) Array.get(arr,1)) + ", " + 
            		String.valueOf((Double) Array.get(arr,2)) + ", " + 
            		String.valueOf((Double) Array.get(arr,3)) + ", " + 
            		String.valueOf((Double) Array.get(arr,4)) + ", " + 
            		String.valueOf((Double) Array.get(arr,5)) + ", " + 
            		String.valueOf((Double) Array.get(arr,6));
            getLogger().info(toLog);
            iiwa.move(ptp((Double) Array.get(arr,0),
            			(Double) Array.get(arr,1),
            			(Double) Array.get(arr,2),
            			(Double) Array.get(arr,3),
            			(Double) Array.get(arr,4),
            			(Double) Array.get(arr,5),
            			(Double) Array.get(arr,6)).setJointVelocityRel(joggingVelocity));
        }
	}
	
	//dispose
	@Override	
	public void dispose() {
		iiwa.detachAll();
	}
}