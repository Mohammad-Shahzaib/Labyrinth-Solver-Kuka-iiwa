package application;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import Util.Gripper;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.conditionModel.JointTorqueCondition;
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
    
    private static int FORCE_STOP_THRESHOLD = 4;
    
    private static int DIRECTION_DEBOUNCE_COUNT_THRESHOLD = 3;
    
    private int yDirection, directionChangeDebounceCount;
    
	@Override
	public void initialize() {
		// initialize your application here
		gripper.attachTo(iiwa.getFlange());
	}
	
	public void initSetup(){
        double homePositionWithTool[] = new double[] { Math.toRadians(5.17), 
				Math.toRadians(-51.81),
				Math.toRadians(-9.72),
				Math.toRadians(78.27),
				Math.toRadians(10.14),
				Math.toRadians(-51.51),
				Math.toRadians(-6.96) };
        yDirection = -1;
        directionChangeDebounceCount = 0;
        mediaFlange.setLEDBlue(false);
        gripper.attachTo(iiwa.getFlange());
    	gripper.close_M();
        getLogger().info("Moving to user defined home position");
        iiwa.moveAsync(linRel().setZOffset(-100).setJointVelocityRel(JOGGING_VELOCITY));
        iiwa.move(ptp((Double) Array.get(homePositionWithTool,0),
        			(Double) Array.get(homePositionWithTool,1),
        			(Double) Array.get(homePositionWithTool,2),
        			(Double) Array.get(homePositionWithTool,3),
        			(Double) Array.get(homePositionWithTool,4),
        			(Double) Array.get(homePositionWithTool,5),
        			(Double) Array.get(homePositionWithTool,6)).setJointVelocityRel(JOGGING_VELOCITY));
        mediaFlange.setLEDBlue(true);
	}
	
	// Returns true if force is detected during motion above provided threshold
	public boolean forceControlCheck(int forceThreshold){
    	while(iiwa.hasActiveMotionCommand()){
        	String logTorque = "";
            ForceSensorData externalData = iiwa.getExternalForceTorque(iiwa.getFlange());
            Vector val = externalData.getForce();
            for(int i = 0; i < 3; i++){
            	logTorque += String.format("%.2f, ", val.get(i));
            }
            for(int i = 0; i < 3; i++){
            	if ((val.get(i) > forceThreshold
            			|| val.get(i) < -forceThreshold)
            			&& directionChangeDebounceCount > DIRECTION_DEBOUNCE_COUNT_THRESHOLD){
                    getLogger().info("External force detected");
                    iiwa.getController().getExecutionService().cancelAll();
                	return true;
            	}
            }
        	directionChangeDebounceCount += 1;
            getLogger().info(logTorque);
        	try {
				TimeUnit.MILLISECONDS.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	return false;
	}
	
	public void moveRobotAsync(boolean changeDirection){
        if(changeDirection){
        	changeDirection = false;
            getLogger().info("Changing direction! yDir=" + yDirection);
            yDirection *= -1;
        	directionChangeDebounceCount = 0;
        }
        iiwa.moveAsync(linRel().setYOffset(yDirection * 1)); // Watch out the speed if you increase the movement step
	}

	@Override
	public void run() {
		initSetup();
        while (true) {
        	boolean changeDirection = forceControlCheck(FORCE_STOP_THRESHOLD);
        	moveRobotAsync(changeDirection);
        }
	}
	
	//dispose
	@Override	
	public void dispose() {
		iiwa.detachAll();
	}
}