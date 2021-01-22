package application;


import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import Util.Gripper;

import com.kuka.generated.ioAccess.MediaFlangeIOGroup;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.math.Vector;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;

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
public class HoleHunter extends RoboticsAPIApplication {
	@Inject
	private LBR iiwa;

	@Inject 
	private Gripper gripper;

	@Inject
	private MediaFlangeIOGroup mediaFlange;
	
    private static double JOGGING_VELOCITY = 0.1; // moving velocity
    
    private static int FORCE_THRESHOLD = 20;
    
    private static int DIRECTION_DEBOUNCE_COUNT_THRESHOLD = 3;
    
    private static double REBOUND_DISTANCE = -2.5;
    
    private int moveDirection, directionChangeDebounceCount, fsmIndex;
    
    private MovementDirection[] movementDirectionArr;
    
    double startingPoint[];
    
	@Override
	public void initialize() {
		// initialize your application here
		gripper.attachTo(iiwa.getFlange());
	}
	
	public void initSetup(){
		movementDirectionArr = new MovementDirection[]{MovementDirection.EAST};
		double homePositionWithTool[] = new double[] { Math.toRadians(-0.93), 
				Math.toRadians(-48.88),
				Math.toRadians(-2.32),
				Math.toRadians(84.25),
				Math.toRadians(4.09),
				Math.toRadians(-46.03),
				Math.toRadians(-2.62) };
        startingPoint = new double[] { Math.toRadians(-6.98), 
				Math.toRadians(-51.28),
				Math.toRadians(0.94),
				Math.toRadians(78.10),
				Math.toRadians(-1.15),
				Math.toRadians(-50.07),
				Math.toRadians(-5.90) };
        directionChangeDebounceCount = 0;
        fsmIndex = 0;
        moveDirection = -1;
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
	public boolean singleDimensionalForceLostCheck(int forceThreshold, int dimension){
    	while(iiwa.hasActiveMotionCommand()){
        	String logTorque = "";
            ForceSensorData externalData = iiwa.getExternalForceTorque(iiwa.getFlange());
            Vector val = externalData.getForce();
            for(int i = 0; i < 3; i++){
            	logTorque += String.format("%.2f, ", val.get(i));
            }
            if ((val.get(dimension) < forceThreshold
        			&& val.get(dimension) > -forceThreshold)
        			&& directionChangeDebounceCount > DIRECTION_DEBOUNCE_COUNT_THRESHOLD){
                getLogger().info("Force detected on " + dimension);
                iiwa.getController().getExecutionService().cancelAll();
            	return true;
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
	
	public void moveRobotAsync(boolean changeState, boolean rebound){

        if(rebound){ // Rebound if need to
            if(movementDirectionArr[fsmIndex] == MovementDirection.NORTH || 
            		movementDirectionArr[fsmIndex] == MovementDirection.SOUTH){
                iiwa.moveAsync(linRel().setXOffset(moveDirection * REBOUND_DISTANCE)); // Watch out the speed if you increase the movement step
            }
            else if(movementDirectionArr[fsmIndex] == MovementDirection.EAST || 
            		movementDirectionArr[fsmIndex] == MovementDirection.WEST){
                iiwa.moveAsync(linRel().setYOffset(moveDirection * REBOUND_DISTANCE)); // Watch out the speed if you increase the movement step
            }
        }
		
        if(changeState){ // transit to next FSM state
            getLogger().info("Changing direction!");
            fsmIndex = fsmIndex + 1;
            if( movementDirectionArr[fsmIndex] == MovementDirection.NORTH || 
            		movementDirectionArr[fsmIndex] == MovementDirection.EAST){
                moveDirection = -1;
            }
            else{
            	moveDirection = 1;
            }
        	directionChangeDebounceCount = 0;
        }
        
        // Normal movement according to current FSM state
        if(movementDirectionArr[fsmIndex] == MovementDirection.NORTH || 
        		movementDirectionArr[fsmIndex] == MovementDirection.SOUTH){
            iiwa.moveAsync(linRel().setXOffset(moveDirection * 1)); // Watch out the speed if you increase the movement step
        }
        else if(movementDirectionArr[fsmIndex] == MovementDirection.EAST || 
        		movementDirectionArr[fsmIndex] == MovementDirection.WEST){
            iiwa.moveAsync(linRel().setYOffset(moveDirection * 1)); // Watch out the speed if you increase the movement step
        }
        mediaFlange.setLEDBlue(true);
	}

	@Override
	public void run() {
		initSetup();
        while (true) {
        	boolean forceLost = singleDimensionalForceLostCheck(FORCE_THRESHOLD, 2);
        	if(forceLost){
                getLogger().info("Moving to starting point");
                mediaFlange.setLEDBlue(false);
                iiwa.moveAsync(linRel().setZOffset(-100).setJointVelocityRel(JOGGING_VELOCITY));
                iiwa.move(ptp((Double) Array.get(startingPoint,0),
            			(Double) Array.get(startingPoint,1),
            			(Double) Array.get(startingPoint,2),
            			(Double) Array.get(startingPoint,3),
            			(Double) Array.get(startingPoint,4),
            			(Double) Array.get(startingPoint,5),
            			(Double) Array.get(startingPoint,6)).setJointVelocityRel(JOGGING_VELOCITY));
                while(true);
        	}
        	moveRobotAsync(false,false);
        }
	}
	
	//dispose
	@Override	
	public void dispose() {
		iiwa.detachAll();
	}
}