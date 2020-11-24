package application;


import javax.inject.Inject;

import Util.Gripper;

import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.World;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.AbstractMotionControlMode;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianSineImpedanceControlMode;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;
import com.kuka.task.ITaskLogger;
import com.kuka.generated.flexfellow.FlexFellow;
import com.kuka.generated.ioAccess.MediaFlangeIOGroup;
import com.kuka.common.ThreadUtil;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

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



public class assembly_Tasks {
	@Inject
	private LBR kuka_Iiwa;
	
	@Inject
	private MediaFlangeIOGroup media_Flange;

	@Inject
	private FlexFellow kuka_Ffellow;
	
	@Inject
	private Gripper gripper_1;
	
	@Inject
	private ITaskLogger logger;
	
	// Very important:
	@Inject
	private IApplicationData app_Data; //call the frame
	
	@Inject
	private IApplicationUI user_I; // call the diag
	
	@Inject
	private Frame frame_Kuka;
	
	
	
	public void main_Application()
	{
		//initialize the application
		kuka_Iiwa.move(ptpHome());
		gripper_1.open_M();
		gripper_1.attachTo(kuka_Iiwa.getFlange());
		
		
		
		int choice = user_I.displayModalDialog(ApplicationDialogType.QUESTION, "Please choose the assembly task?","Tray 1","Tray 2", "Magazine type Feeders");
		
		if (choice == 0)
		{
			int tray_1 = user_I.displayModalDialog(ApplicationDialogType.QUESTION, "Please choose the component to be assembled","FN202","ABB M1174","D2");
			
			if (tray_1 == 0)
			
			{
				fn202_assem();
			}
			
			
			if (tray_1 == 1)
			
			{
				ABB_M1174_assem();
			}
			
			if (tray_1 == 2)
			
			{
				D2_assem();
			}
			
			//t1_Task();
		}
		if (choice == 1)
		{
			int tray_2 = user_I.displayModalDialog(ApplicationDialogType.QUESTION, "Please choose the component to be assembled","CHINT C6","CHINT B6","CHINT D6");
			
			if (tray_2 == 0)
			
			{
				c6_assem();
			}
			
			
			if (tray_2 == 1)
			
			{
				b6_assem();
			}
			
			if (tray_2 == 2)
			
			{
				d6_assem();
			}
			//t2_Task();
		}
		if (choice ==2)
		{
			int magazine = user_I.displayModalDialog(ApplicationDialogType.QUESTION, "Please choose the component to be assembled","ZDU 2.5 / 4AN BL","ZPE 2.5 / 4AN","ZDU 2.5 / 4AN");
			
			if (magazine == 0)
			
			{
				zdu_Bl_assem();
			}
			
			
			if (magazine == 1)
			
			{
				zpe_Green_assem();
			}
			
			if (magazine == 2)
			
			{
				zdu_Grey_assem();
			}
			
			//t3_Task();
		}
		

	}
	
	
	private void zdu_Grey_assem() 
	
	{
		// TODO Auto-generated method stub
		
	}
	
	private void zpe_Green_assem() 
	
	{
		// TODO Auto-generated method stub
		
	}
	
	private void zdu_Bl_assem() 
	
	{
		// TODO Auto-generated method stub
		logger.info("Starting Assembly task for the ZDU 2.5 / 4AN BL");
		hrc_Pos();
		idle_Rob();
		shelf_position();
		ZDU_Bl_Pick();
		shelf_position();
		hrc_Pos();
		idle_Rob();
		assem_Rail_mag();
		
	}
	



	private void d6_assem() 
	
	{
		// TODO Auto-generated method stub
		
	}
	
	private void b6_assem() 
	
	{
		// TODO Auto-generated method stub
		
	}
	
	private void c6_assem() 
	
	{
		// TODO Auto-generated method stub
		logger.info("Starting Assembly task for C6");
		hrc_Pos();
		idle_Rob();
		C6_Pick();
		hrc_Pos();
		idle_Rob();
		assem_Rail();
	}
	
	private void D2_assem() 
	
	{
		// TODO Auto-generated method stub
		logger.info("Starting Assembly task for D2");
		hrc_Pos();
		idle_Rob();
		D2_Pick();
		hrc_Pos();
		idle_Rob();
		assem_Rail();
	}
	
	private void ABB_M1174_assem() 
	
	{
		logger.info("Starting Assembly task for ABB M1174");
		hrc_Pos();
		idle_Rob();
		ABB_M1174_Pick();
		hrc_Pos();
		idle_Rob();
		assem_Rail();
		
	}
	
	private void fn202_assem() 
	
	{
		logger.info("Starting Assembly task for FN202");
		hrc_Pos();
		idle_Rob();
		fn202_Pick();
		hrc_Pos();
		idle_Rob();
		assem_Rail();
	}
	
	
	
	
	
	
	public void t1_Task()
	{
		
		logger.info("Starting Assembly task for tray 1");
		hrc_Pos();
		idle_Rob();
		fn202_Pick();
		//ABB_M1174_Pick();
		//E215_Pick();
		//D2_Pick();
		hrc_Pos();
		idle_Rob();
		
	}
	
	public void t2_Task()
	{
		logger.info("Starting Assembly task for tray 2");
		hrc_Pos();
		idle_Rob();
		C6_Pick();
		//B6_Pick();
		//D6_Pick();
		//E251_Pick();
		//E266_Pick();
		hrc_Pos();
		idle_Rob();
	}
	public void t3_Task()
	{
		logger.info("Starting Assembly task for the shelf Components");
		hrc_Pos();
		idle_Rob();
		shelf_position();
		//ZDU_Bl_Pick();
		ZPE_Gr_Pick();
		shelf_position();
		//ZDU_Grey_Pick();
		//ZPE_Green_Pick();
		hrc_Pos();
		idle_Rob();
	}

	
	public void assem_Rail()
	{
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/orient_Assem_T").copyWithRedundancy();
		gripper_1.move(ptp(orient_Assem_Pos));
		linear_Force();
		
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.ALL).setStiffness(100.0);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/fix_Assem_Pos").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos ));
		gripper_1.open_M();
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob));



	}
	private void assem_Rail_mag() 
	
	{
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/orient_Assem_T").copyWithRedundancy();
		Frame orient_Assem_Pos_A7_orient = app_Data.getFrame("/Assembly_Table/orient_A7").copyWithRedundancy();
		
		gripper_1.move(ptp(orient_Assem_Pos));
		gripper_1.move(ptp(orient_Assem_Pos_A7_orient));
		linear_Force();
		
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.ALL).setStiffness(100.0);
		
		Frame fix_Assem_Pos_mag = app_Data.getFrame("/Assembly_Table/fix_Assem_Pos_Mag").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos_mag));
		gripper_1.open_M();
		gripper_1.move(ptp(orient_Assem_Pos_A7_orient).setMode(softrob));



		
	}
	
	
	
	
	public void fn202_Pick()
	{
		Frame Tray1_Fn202_Pos = app_Data.getFrame("/Rack/FN202_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray1_Fn202_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray1_Fn202_Pos));
	}
	public void ABB_M1174_Pick()
	{
		Frame Tray1_ABB_M1174_Pos = app_Data.getFrame("/Rack/ABB_M1174_Pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray1_ABB_M1174_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray1_ABB_M1174_Pos));
	}
	public void E215_Pick()
	{
		Frame Tray1_E215_Pos= app_Data.getFrame("/Rack/E215_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray1_E215_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray1_E215_Pos));
	}
	
	public void D2_Pick()
	{
		Frame Tray1_D2_Pos= app_Data.getFrame("/Rack/D2_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray1_D2_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray1_D2_Pos));
	}
	public void C6_Pick()
	
	{
		Frame Tray2_C6_Pos= app_Data.getFrame("/Rack/C6_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray2_C6_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray2_C6_Pos));
	}
	
	public void B6_Pick()
	
	{
		Frame Tray2_B6_Pos= app_Data.getFrame("/Rack/B6_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray2_B6_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray2_B6_Pos));
	}
	
	public void D6_Pick()
	
	{
		Frame Tray2_D6_Pos= app_Data.getFrame("/Rack/D6_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray2_D6_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray2_D6_Pos));
	}
	
	public void E251_Pick()
	
	{
		Frame Tray2_E251_Pos= app_Data.getFrame("/Rack/E251_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray2_E251_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray2_E251_Pos));
	}
	
	public void E266_Pick()
	
	{
		Frame Tray2_E266_Pos= app_Data.getFrame("/Rack/E266_pos").copyWithRedundancy();
		gripper_1.move(ptp(Tray2_E266_Pos));
		linear_Force();
		p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Tray2_E266_Pos));
	}
	
	public void ZDU_Bl_Pick()
	
	{
		Frame Shelf_Zdu_Bl_Pos= app_Data.getFrame("/Rack/ZDU_BL_pos").copyWithRedundancy();
		gripper_1.move(ptp(Shelf_Zdu_Bl_Pos));
		linear_Force_Shelf();
		//p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Shelf_Zdu_Bl_Pos));
	}
	
	public void ZPE_Gr_Pick()
	
	{
		Frame Shelf_ZPE_Gr_Pos= app_Data.getFrame("/Rack/ZPE_GREEN_pos").copyWithRedundancy();
		gripper_1.move(ptp(Shelf_ZPE_Gr_Pos));
		ThreadUtil.milliSleep(10);
		linear_Force_Shelf();
		//p_grip_h();
		gripper_1.close_M();
		gripper_1.move(ptp(Shelf_ZPE_Gr_Pos));
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
	
	public void shelf_position()
	
	{
		Frame Shelf_Pos= app_Data.getFrame("/Rack/Shelf_Pos_1").copyWithRedundancy();
		gripper_1.move(ptp(Shelf_Pos));
	}
	
	
	public void linear_Force()
	
	{
		logger.info("linear motion with force control");
		
		ForceCondition contact_Start_Pick = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 20);
		IMotionContainer contactMotion_Pick = gripper_1.move(linRel(0, 0, 500).setCartVelocity(100).breakWhen(contact_Start_Pick));
		if (contactMotion_Pick.hasFired(contact_Start_Pick))
		{
			logger.info("The contact has occured!");
		}
	}
	
	public void linear_Force_Shelf()
	
	{
		logger.info("linear motion with force control");
		
		ForceCondition contact_Start_Pick_tr2 = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		IMotionContainer contactMotion_Pick = gripper_1.move(linRel(0, 0, 200).setCartVelocity(100).breakWhen(contact_Start_Pick_tr2));
		if (contactMotion_Pick.hasFired(contact_Start_Pick_tr2))
		{
			logger.info("The contact has occured!");
		}
	}
	
	public void p_grip()
	
	{
		CartesianSineImpedanceControlMode grip = new CartesianSineImpedanceControlMode();
		// X,Y,Z Translations 0-5000
		grip.parametrize(CartDOF.X).setStiffness(1000).setDamping(0.5)
				.setFrequency(3).setAmplitude(9);
		grip.parametrize(CartDOF.Y).setStiffness(1000).setDamping(0.5)
				.setFrequency(3).setAmplitude(12);
		grip.parametrize(CartDOF.Z).setStiffness(1000).setBias(20);

		// X,Y,Z Translations 0-300
		grip.parametrize(CartDOF.A).setStiffness(300).setDamping(0.5);
		grip.parametrize(CartDOF.B).setStiffness(300).setDamping(0.5);
		grip.parametrize(CartDOF.C).setStiffness(300).setDamping(0.5);
		
		Frame currentPoseAfterContact = kuka_Iiwa.getCurrentCartesianPosition(gripper_1.getDefaultMotionFrame());
		
		IMotionContainer grip_Contact_Pos_Hold = gripper_1.moveAsync(positionHold(grip, 100000, TimeUnit.SECONDS));
	}
	
	
	public void p_grip_h()
	
	{
		CartesianSineImpedanceControlMode ctrl = new CartesianSineImpedanceControlMode();
		ctrl.parametrize(CartDOF.X).setStiffness(300).setDamping(0.5);
		ctrl.parametrize(CartDOF.Y).setStiffness(100).setDamping(0.5).setFrequency(2).setAmplitude(2);
		ctrl.parametrize(CartDOF.Z).setStiffness(100).setDamping(0.5).setFrequency(2).setAmplitude(3);
		// X,Y,Z Translations 0-300
		ctrl.parametrize(CartDOF.A).setStiffness(200).setDamping(0.5);
		ctrl.parametrize(CartDOF.B).setStiffness(200).setDamping(0.5);
		ctrl.parametrize(CartDOF.C).setStiffness(20).setDamping(0.5).setFrequency(4).setAmplitude(3);
		
		IMotionContainer positionHoldMotion=gripper_1.move(positionHold(ctrl, 3, TimeUnit.SECONDS));
	}
	
	
	
}