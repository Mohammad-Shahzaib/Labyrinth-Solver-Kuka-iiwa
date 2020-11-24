package cyclicTasks;

import java.util.HashMap;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import sun.font.CreatedFontTracker;



import Util.Gripper;

import com.kuka.generated.ioAccess.EL2809IOGroup;
import com.kuka.generated.ioAccess.FlexFellowIOGroup;
import com.kuka.generated.ioAccess.MediaFlangeIOGroup;
import com.kuka.roboticsAPI.applicationModel.tasks.CycleBehavior;
import com.kuka.roboticsAPI.applicationModel.tasks.RoboticsAPIBackgroundTask;
import com.kuka.roboticsAPI.applicationModel.tasks.RoboticsAPICyclicBackgroundTask;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseSafetyState.EmergencyStop;
import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseSafetyState.SafetyStopType;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.World;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;
import com.kuka.roboticsAPI.geometricModel.math.Vector;
import com.kuka.roboticsAPI.persistenceModel.processDataModel.IProcessData;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.kuka.roboticsAPI.sensorModel.SensorForExternalForce;
import com.kuka.roboticsAPI.uiModel.ITaskUI;
import com.kuka.roboticsAPI.uiModel.userKeys.IUserKey;
import com.kuka.roboticsAPI.uiModel.userKeys.IUserKeyBar;
import com.kuka.roboticsAPI.uiModel.userKeys.IUserKeyListener;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyAlignment;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyEvent;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyLED;
import com.kuka.roboticsAPI.uiModel.userKeys.UserKeyLEDSize;
import com.kuka.task.ITaskLogger;
import com.kuka.task.properties.TaskFunctionProvider;

/**
 * Implementation of a cyclic background task.
 * <p>
 * It provides the {@link RoboticsAPICyclicBackgroundTask#runCyclic} method
 * which will be called cyclically with the specified period.<br>
 * Cycle period and initial delay can be set by calling
 * {@link RoboticsAPICyclicBackgroundTask#initializeCyclic} method in the
 * {@link RoboticsAPIBackgroundTask#initialize()} method of the inheriting
 * class.<br>
 * The cyclic background task can be terminated via
 * {@link RoboticsAPICyclicBackgroundTask#getCyclicFuture()#cancel()} method or
 * stopping of the task.
 */

public class CPBackGround extends RoboticsAPICyclicBackgroundTask {
	// fields of the background task
	@Inject
	private LBR lbr;
	@Inject
	private EL2809IOGroup _FlexFellowIOs;
	
	@Inject
	private MediaFlangeIOGroup X3;

	@Inject Gripper gripper;
	
	@Inject
	private ITaskUI _ui;

	public void initialize() {
		// // fields of the method
		long initialDelay = 500;
		long period = 100;

		// parameterize cyclic task
		initializeCyclic(initialDelay, period, TimeUnit.MILLISECONDS,
				CycleBehavior.BestEffort);

		// create UserKeys
		userKey();
		//userGripperKey();

	}

	
	public void runCyclic() {

	}
	
	/*
	public void userGripperKey() {
		IUserKeyBar gripperUserKeyBar = _ui.createUserKeyBar("Gripper");
		
		// Open gripper
		IUserKeyListener openKeyListener = new IUserKeyListener() {
			
			@Override
			public void onKeyEvent(IUserKey key, UserKeyEvent event) {
				// TODO Auto-generated method stub
				
				if(event == UserKeyEvent.KeyDown) {
					key.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Green, UserKeyLEDSize.Normal);
					gripper.close_M();
				}
				
				else if(event == UserKeyEvent.KeyUp) {
					key.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Grey, UserKeyLEDSize.Normal);
					gripper.open_M();
				}
			}
		};
		
		// // design gripper key
		IUserKey openUserKey = gripperUserKeyBar.addUserKey(0,
				openKeyListener, false);
		openUserKey.setText(UserKeyAlignment.TopMiddle, "Open");
		openUserKey.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Grey,
				UserKeyLEDSize.Normal);
		
		// publish UserKey bar
		gripperUserKeyBar.publish();
		
	}

	*/
	//
	// // Cp user Key
	public void userKey() {
		// create UserKey bar
		IUserKeyBar ventileUserKeyBar = _ui.createUserKeyBar("GL");

		//
		// listener release cartridge
		IUserKeyListener valveOpenKeyListener = new IUserKeyListener() {
			@Override
			public void onKeyEvent(IUserKey key, UserKeyEvent event) {
				if (event == UserKeyEvent.KeyDown) {
					
					X3.setOutputX3Pin11(false);
					X3.setOutputX3Pin1(true);

					key.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Green,
							UserKeyLEDSize.Normal);
					System.out.println("UserKeyEvent.KeyDown 1");
				}
				if (event == UserKeyEvent.KeyUp) {
					key.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Grey,
							UserKeyLEDSize.Normal);
					System.out.println("UserKeyEvent.KeyUp 1");
				}
			}
		};

		IUserKeyListener valveCloseKeyListener = new IUserKeyListener() {
			@Override
			public void onKeyEvent(IUserKey key, UserKeyEvent event) {
				if (event == UserKeyEvent.KeyDown) {

					X3.setOutputX3Pin11(true);
					X3.setOutputX3Pin1(false);

					key.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Green,
							UserKeyLEDSize.Normal);
					System.out.println("UserKeyEvent.KeyDown 1");
				}
				if (event == UserKeyEvent.KeyUp) {
					key.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Grey,
							UserKeyLEDSize.Normal);
					System.out.println("UserKeyEvent.KeyUp 1");
				}
			}
		};

		// // design release key
		IUserKey openUserKey = ventileUserKeyBar.addUserKey(0,
				valveOpenKeyListener, false);
		openUserKey.setText(UserKeyAlignment.TopMiddle, "Open");
		openUserKey.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Grey,
				UserKeyLEDSize.Normal);
		//
		// // design tension key
		IUserKey closeUserKey = ventileUserKeyBar.addUserKey(1,
				valveCloseKeyListener, false);
		closeUserKey.setText(UserKeyAlignment.TopMiddle, "Close");
		closeUserKey.setLED(UserKeyAlignment.BottomMiddle, UserKeyLED.Grey,
				UserKeyLEDSize.Normal);
		// //

		// publish UserKey bar
		ventileUserKeyBar.publish();
	}
	
}
