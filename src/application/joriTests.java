package application;


import javax.inject.Inject;

import Util.Gripper;

import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.World;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.AbstractMotionControlMode;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;
import com.kuka.task.ITaskLogger;
import com.kuka.generated.flexfellow.FlexFellow;
import com.kuka.generated.ioAccess.MediaFlangeIOGroup;
import com.kuka.common.ThreadUtil;
import java.util.Vector;

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



public class joriTests {
	@Inject
	private LBR kuka_Iiwa;
	
	//@Inject
	//private MediaFlangeIOGroup media_Flange;

	@Inject
	private FlexFellow kuka_Ffellow;
	
	@Inject
	private Gripper spindle;
	
	@Inject
	private ITaskLogger logger;
	
	// Very important:
	@Inject
	private IApplicationData app_Data_Jori; //call the frame
	
	@Inject
	private IApplicationUI user_I; // call the diag
	
	@Inject
	private Frame frame_Kuka;
	

	public void main_Application()
	{
		spindle.attachTo(kuka_Iiwa.getFlange());
		
		//kuka_Iiwa.move(ptp(0,0,0,0,0,0,0).setJointVelocityRel(0.1));
		
		kuka_Iiwa.move(lin(app_Data_Jori.getFrame("/joriWorkpiece/joriTestPoint")).setCartVelocity(50));
		
		//Frame joriTestPoint = app_Data_Jori.getFrame("/joriWorkpiece/joriTestPoint").copyWithRedundancy();
		//kuka_Iiwa.move(ptp(joriTestPoint);
		//kuka_Iiwa.move(ptpHome().setJointVelocityRel(0.05));
		//kuka_Iiwa.move(ptp(app_Data_Jori.getFrame("/joriWorkpiece/helpFrame")).setJointVelocityRel(0.05));
		//kuka_Iiwa.move(ptp(app_Data_Jori.getFrame("/joriWorkpiece/joriTestPoint")).setJointVelocityRel(0.05));

		
	}
	
	
	
	
	/*
	
	
	
	
	public enum assembly_Seq
	{
		n1, n2
	}
	
	
	public void OLDmain_Application()
	{
		//initialize
		kuka_Iiwa.move(ptpHome());
		gripper_1.open_M();
		gripper_1.close_M();
		gripper_1.attachTo(kuka_Iiwa.getFlange());
		
		
		
		int choice = user_I.displayModalDialog(ApplicationDialogType.QUESTION, "Please choose the assembly task","Assembly Task 1","Assembly Task 2");
		
		if (choice == 0)
		{
			n1_Task();
		}
		if (choice == 1)
		{
			n2_Task();
		}

	}
	public void n1_Task()
	{
		
		logger.info("Starting Assembly Task 1");
		logger.info("Starting Assembly Task 1");
		hrc_Pos();
		idle_Rob();
		kuka_Iiwa.move(ptp(-38.07*(Math.PI/180),0.09*(Math.PI/180),79.18*(Math.PI/180),-26.42*(Math.PI/180),36.60*(Math.PI/180),102.39*(Math.PI/180),-17.84*(Math.PI/180)));
		Frame rack_Base1 = app_Data.getFrame("/Rack/P1").copyWithRedundancy();
		ThreadUtil.milliSleep(1000);
		gripper_1.move(ptp(rack_Base1));
		//assembly_Seq1();
		
		
		
	}
	
	public void n2_Task()
	{
		logger.info("Starting the task n2");
		kuka_Iiwa.move(ptp(66.61*(Math.PI/180),-24.43*(Math.PI/180),-60.98*(Math.PI/180),-90.39*(Math.PI/180),11.48*(Math.PI/180),95.16*(Math.PI/180),-11.77*(Math.PI/180)));
		kuka_Iiwa.move(ptp(-36.78*(Math.PI/180),17.86*(Math.PI/180),-10.57*(Math.PI/180),-69.71*(Math.PI/180),12.61*(Math.PI/180),23.29*(Math.PI/180),0*(Math.PI/180)));
		
		kuka_Iiwa.move(ptp(app_Data.getFrame("/Rack")));
		
	}
	public void n3_Task()
	{
		logger.info("Starting the task n3");
	}
	
	public void n4_Task()
	{
		logger.info("Starting the task n4");
	}
	
	public void idle_Rob()
	{
		logger.info("Tap the robot to start the task");
		ForceCondition contact_Start = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 15);
		
		for(;;)
		{
		media_Flange.setLEDBlue(true);
		IMotionContainer contactMotion_1 = gripper_1.move(linRel(0, 0, 10).setCartVelocity(100).breakWhen(contact_Start));
		IMotionContainer contactMotion_2 = gripper_1.move(linRel(0, 0, -10).setCartVelocity(100).breakWhen(contact_Start));
		if (contactMotion_1.hasFired(contact_Start)||contactMotion_2.hasFired(contact_Start))
		{
			media_Flange.setLEDBlue(false);
			logger.info("The assembly task starts now");
			break;
		}
		}
		
	}
	
	public void hrc_Pos()
	{
		kuka_Iiwa.move(ptp(-54.41*(Math.PI/180),18.10*(Math.PI/180),36.92*(Math.PI/180),-74.82*(Math.PI/180),73.51*(Math.PI/180),102.55*(Math.PI/180),25.25*(Math.PI/180)));
	}
	
	public void assembly_Seq1()
	
	{
		
		kuka_Iiwa.move(ptp(app_Data.getFrame("/Rack/P1")));
		logger.info("/Rack/P1");
		kuka_Iiwa.move(ptp(app_Data.getFrame("/Rack/P1/P1").copyWithRedundancy()));
		logger.info("/Rack/P1/P1");
		
		
	}
	
	
	public void linear_Force()
	{
		logger.info("linear motion with force control");
		
		ForceCondition contact_Start_Pick = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 15);
		IMotionContainer contactMotion_Pick = gripper_1.move(linRel(0, 0, 200).setCartVelocity(100).breakWhen(contact_Start_Pick));
		if (contactMotion_Pick.hasFired(contact_Start_Pick))
		{
			logger.info("The contact has occured!");
		}
	}
	*/
}