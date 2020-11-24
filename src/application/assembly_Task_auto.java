package application;


import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import Util.Gripper;

import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianSineImpedanceControlMode;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;
import com.kuka.task.ITaskLogger;
import com.kuka.common.ThreadUtil;
import com.kuka.generated.flexfellow.FlexFellow;
import com.kuka.generated.ioAccess.MediaFlangeIOGroup;


public class assembly_Task_auto 
{
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
	
	@Inject
	private Robot_application_humza robo;
	
	
	public void main_App()
	
	
	{
		//Initialise the speed for automatic mode
		
		robo.getApplicationControl().setApplicationOverride(0.2); //Speed
		gripper_1.open_M();
		gripper_1.attachTo(kuka_Iiwa.getFlange());
		
		//Execution
		
		auto_Assem_Seq();
		
		
	}
	
	private void auto_Assem_Seq()
	
	{
		kuka_Iiwa.move(ptpHome());
		hrc_Pos();
		idle_Rob();
		fh202_Assem();
		hrc_Pos();
		c6_Assem();
		hrc_Pos();
		d6_Assem();
		hrc_Pos();
		d2_Assem();
		hrc_Pos();
		idle_Rob();
		b6_Assem();
		hrc_Pos();
		b10_Assem();
		hrc_Pos();
		e256_Assem();
		hrc_Pos();
		m1174_Assem();
		hrc_Pos();
		e215_Assem();
		hrc_Pos();
		idle_Rob();
	}
	



	

	public void fastening(ICondition start_Screw_2) {
		// TODO Auto-generated method stub
		CartesianImpedanceControlMode fastening = new CartesianImpedanceControlMode();
		fastening.parametrize(CartDOF.X).setStiffness(800.0);
		fastening.parametrize(CartDOF.Z).setStiffness(100.0);
		fastening.parametrize(CartDOF.Y).setStiffness(100.0);
		Frame fastening_Pos= app_Data.getFrame("/fastening_Pos").copyWithRedundancy();
		gripper_1.move(ptp(fastening_Pos));
		
		ForceCondition start_Screw = ForceCondition.createNormalForceCondition(gripper_1.getDefaultMotionFrame(),CoordinateAxis.X,60);
		IMotionContainer hand_Mani = gripper_1.move(positionHold(fastening, 50, TimeUnit.SECONDS).breakWhen(start_Screw));
		if(hand_Mani.hasFired(start_Screw))
		
		{	
		logger.info("its broken");
		media_Flange.setLEDBlue(true);
		}
		
		CartesianImpedanceControlMode fastening_Op = new CartesianImpedanceControlMode();
		fastening_Op.parametrize(CartDOF.X).setStiffness(5000);
		fastening_Op.parametrize(CartDOF.Z).setStiffness(5000);
		fastening_Op.parametrize(CartDOF.Y).setStiffness(5000);
		fastening_Op.parametrize(CartDOF.C).setDamping(0.7);
		IMotionContainer fast_Screw = gripper_1.move(positionHold(fastening_Op, 50, TimeUnit.SECONDS).breakWhen(start_Screw_2));
		if(fast_Screw.hasFired(start_Screw_2))
		{	
		logger.info("it is soft again");
		media_Flange.setLEDBlue(false);
		}
		
		gripper_1.move(positionHold(fastening, 50, TimeUnit.SECONDS).breakWhen(start_Screw));
	}

	
	
	
	
	
	private void push_Comp()
	
	{
		CartesianImpedanceControlMode softrob_Push = new CartesianImpedanceControlMode();
		softrob_Push.parametrize(CartDOF.X).setStiffness(500.0);
		softrob_Push.parametrize(CartDOF.Y).setStiffness(500.0);
		softrob_Push.parametrize(CartDOF.Z).setStiffness(500.0);
		Frame safelyto_Comp_Orient= app_Data.getFrame("/Assembly_Table/orient_Abbm1174").copyWithRedundancy();
		Frame push_Comp_Orient= app_Data.getFrame("/Assembly_Table/push_Orient").copyWithRedundancy();
		Frame push_Comp= app_Data.getFrame("/Assembly_Table/push_Comp").copyWithRedundancy();
		gripper_1.move(ptp(safelyto_Comp_Orient).setMode(softrob_Push));
		gripper_1.move(ptp(push_Comp_Orient));
		gripper_1.move(lin(push_Comp));
		Frame push_Comp_Child= app_Data.getFrame("/Assembly_Table/push_Comp/push_Lin").copyWithRedundancy();
		gripper_1.move(lin(push_Comp_Child).setCartVelocity(100).setMode(softrob_Push));
		gripper_1.move(lin(push_Comp));
		gripper_1.move(ptp(safelyto_Comp_Orient).setMode(softrob_Push));
		
	}
	
	

	
	private void zdu_grn_Assem() {

		logger.info("Starting Assembly task for ZDU 2.5/4AN GREEN");
		zdu_grn_Pick();
		zdugrn_rail_Assem();
	}
	
	


	private void zdu_grn_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_ZPE_GRN_P = app_Data.getFrame("/Rack/ZPE_GRN_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_ZPE_GRN_P));
		Frame Tray_ZPE_GRN_P_1 = app_Data.getFrame("/Rack/ZPE_GRN_P/ZPE_GRN_P_1").copyWithRedundancy();
		//Tray_ZPE_GRN_P.setX(Tray_ZPE_GRN_P.getX()+50);
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_ZPE_GRN_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_ZPE_GRN_P).setCartVelocity(100));
		
		
		
		
	}

	private void zdugrn_rail_Assem() {
		// TODO Auto-generated method stub
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_orient_zdubl").copyWithRedundancy();
		Frame Green =orient_Assem_Pos.setX(orient_Assem_Pos.getX()-50);
		gripper_1.move(ptp(Green));
		linear_Force();
		int stiff_assembrail = 100;
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.X).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Y).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Z).setStiffness(stiff_assembrail);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_orient_zdubl/P1").copyWithRedundancy();
		Frame green_Fix =fix_Assem_Pos.setX(fix_Assem_Pos.getX()-50);
		gripper_1.move(ptp(green_Fix ));
		gripper_1.open_M();
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob).breakWhen(contact_Assem_Mag));
		//gripper_1.open_M();		
	}
	
	
	private void zdu_bl_Assem() {
		// TODO Auto-generated method stub
		logger.info("Starting Assembly task for ZDU 2.5/4AN Blue");
		zdu_Bl_Pick();
		zdubl_rail_Assem();
	}
	
	
	
	


	private void zdu_Bl_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_ZDU_BL_P = app_Data.getFrame("/Rack/ZDU_BL_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_ZDU_BL_P));
		Frame Tray_ZDU_BL_P_1 = app_Data.getFrame("/Rack/ZDU_BL_P/ZDU_BL_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_ZDU_BL_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_ZDU_BL_P).setCartVelocity(100));
	}

	private void zdubl_rail_Assem() {
		// TODO Auto-generated method stub
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_orient_zdubl").copyWithRedundancy();
		gripper_1.move(ptp(orient_Assem_Pos));
		linear_Force();
		int stiff_assembrail = 100;
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.X).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Y).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Z).setStiffness(stiff_assembrail);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_fix_zdubl").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos ));
		gripper_1.open_M();
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob).breakWhen(contact_Assem_Mag));
		//gripper_1.open_M();
	}
	
	
	
	private void d6_Assem() {
		// TODO Auto-generated method stub
		logger.info("Starting Assembly task for D6");
		d6_Pick();
		m1174_rail_Assem();
	}

	
	
	private void d6_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_D6_P = app_Data.getFrame("/Rack/D6_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_D6_P));
		Frame Tray_D6_P_1 = app_Data.getFrame("/Rack/D6_P/D6_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_D6_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_D6_P).setCartVelocity(100));
	}

	private void b6_Assem() {
		// TODO Auto-generated method stub
		logger.info("Starting Assembly task for B6");
		b6_Pick();
		m1174_rail_Assem_3();
	}
	
	
	
	private void b6_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_B6_P = app_Data.getFrame("/Rack/B6_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_B6_P));
		Frame Tray_B6_P_1 = app_Data.getFrame("/Rack/B6_P/B6_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_B6_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_B6_P).setCartVelocity(100));
	}

	private void b10_Assem() {
		// TODO Auto-generated method stub
		logger.info("Starting Assembly task for B10");
		b10_Pick();
		m1174_rail_Assem_3();
	}
	
	
	private void b10_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_B10_P = app_Data.getFrame("/Rack/B10_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_B10_P));
		Frame Tray_B10_P_1 = app_Data.getFrame("/Rack/B10_P/B10_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_B10_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_B10_P).setCartVelocity(100));
	}

	private void c6_Assem() {
		logger.info("Starting Assembly task for C6");
		c6_Pick();
		m1174_rail_Assem();
		
	}
	
	private void c6_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_C6_P = app_Data.getFrame("/Rack/C6_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_C6_P));
		Frame Tray_C6_P_1 = app_Data.getFrame("/Rack/C6_P/C6_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_C6_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_C6_P).setCartVelocity(100));
	}

	private void e215_Assem() {
		logger.info("Starting Assembly task for E215");
		e215_Pick();
		m1174_rail_Assem_3();
	}
	
	
	private void e215_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_E215_P = app_Data.getFrame("/Rack/E215_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_E215_P));
		Frame Tray_E215_P_1 = app_Data.getFrame("/Rack/E215_P/E215_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		//softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		//softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_E215_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_E215_P).setCartVelocity(100));
	}
	
	private void e215_rail_Assem() {
		// TODO Auto-generated method stub
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_Orient_Large").copyWithRedundancy();
		gripper_1.move(ptp(orient_Assem_Pos));
		linear_Force();
		int stiff_assembrail = 100;
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.X).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Y).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Z).setStiffness(stiff_assembrail);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_Orient_Large/rail_Orient_Large_1").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos ));
		gripper_1.open_M();
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob).breakWhen(contact_Assem_Mag));
		//gripper_1.open_M();		
	}
	
	

	private void e290_Assem() {
		logger.info("Starting Assembly task for E290");
		e290_Pick();
	}
	
	
	
	
	private void e290_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_E290_P = app_Data.getFrame("/Rack/E290_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_E290_P));
		Frame Tray_E290_P_1 = app_Data.getFrame("/Rack/E290_P/E290_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_E290_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_E290_P).setCartVelocity(100));
	}

	private void e256_Assem() {

		logger.info("Starting Assembly task for E256");
		e256_Pick();
		m1174_rail_Assem_3();
		
	}
	
	private void e256_Pick() {
	
		Frame Tray_E256_P = app_Data.getFrame("/Rack/E256_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_E256_P));
		Frame Tray_E256_P_1 = app_Data.getFrame("/Rack/E256_P/E256_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_E256_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_E256_P).setCartVelocity(100));
	}

	private void d2_Assem() 
	{
		logger.info("Starting Assembly task for D2");
		d2_Pick();
		m1174_rail_Assem();
		
	}
	
	
	private void d2_Pick() {
		// TODO Auto-generated method stub
		Frame Tray_d2_P = app_Data.getFrame("/Rack/D2_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_d2_P));
		Frame Tray_d2_P_1 = app_Data.getFrame("/Rack/D2_P/D2_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_d2_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_d2_P).setCartVelocity(100));
	}

	private void m1174_Assem() 
	{
		logger.info("Starting Assembly task for M1174");
		m1174_Pick();
		m1174_rail_Assem_3();

	}
	
	private void m1174_Pick() 
	{
		Frame Tray_M1174_P = app_Data.getFrame("/Rack/M1174_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_M1174_P));
		Frame Tray_M1174_P_1 = app_Data.getFrame("/Rack/M1174_P/M1174_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_M1174_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_M1174_P).setCartVelocity(100));
	}
	private void m1174_rail_Assem() {
		// TODO Auto-generated method stub
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_Orient_Large2").copyWithRedundancy();
		gripper_1.move(lin(orient_Assem_Pos));
		linear_Force();
		int stiff_assembrail = 100;
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.X).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Y).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Z).setStiffness(stiff_assembrail);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_Orient_Large2/rail_Orient_Large2_1").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos ));
		gripper_1.open_M();
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob).breakWhen(contact_Assem_Mag));
		//gripper_1.open_M();		
	}
	
	private void m1174_rail_Assem_3() {
		// TODO Auto-generated method stub
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_Orient_Large").copyWithRedundancy();
		gripper_1.move(lin(orient_Assem_Pos));
		linear_Force_3();
		int stiff_assembrail = 100;
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.X).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Y).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Z).setStiffness(stiff_assembrail);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/rail_Orient_Large/rail_Orient_Large_1").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos ));
		gripper_1.open_M();
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob).breakWhen(contact_Assem_Mag));
		//gripper_1.open_M();		
	}
	
	private void fh202_Assem() 
	{
		logger.info("Starting Assembly task for FH202A");
		fh202_Pick();
		m1174_rail_Assem();

	}
	private void fh202_Pick()
	{
		Frame Tray_FH202_P = app_Data.getFrame("/Rack/FH202_P").copyWithRedundancy();
		gripper_1.move(ptp(Tray_FH202_P));
		Frame Tray_FH202_P_1 = app_Data.getFrame("/Rack/FH202_P/FH202_P_1").copyWithRedundancy();
		
		int stiff_Fh202 = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.Y).setStiffness(stiff_Fh202);
		softMode.parametrize(CartDOF.X).setStiffness(stiff_Fh202);
		gripper_1.move(lin(Tray_FH202_P_1).setCartVelocity(100).setMode(softMode));
		gripper_1.close_M();
		gripper_1.move(lin(Tray_FH202_P).setCartVelocity(100).setMode(softMode));
	}
	
	public void shelf_position()
	
	{
		Frame Shelf_Pos= app_Data.getFrame("/Rack/Shelf_Pos_1").copyWithRedundancy();
		gripper_1.move(ptp(Shelf_Pos));
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
		
		
		Frame hrc_Pos= app_Data.getFrame("/hrc_Pos").copyWithRedundancy();
		gripper_1.move(ptp(hrc_Pos));
	}
	
	public void linear_Force_Shelf()
	
	{
		logger.info("linear motion with force control");
		
		ForceCondition contact_Start_Pick_tr2 = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		IMotionContainer contactMotion_Pick = gripper_1.move(linRel(0, 0, 300).setCartVelocity(100).breakWhen(contact_Start_Pick_tr2));
		if (contactMotion_Pick.hasFired(contact_Start_Pick_tr2))
		{
			logger.info("The contact has occured!");
		}
	}
	
	public void linear_Force()
	
	{
		logger.info("linear motion with force control");
		
		ForceCondition contact_Start_Pick = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 30);
		IMotionContainer contactMotion_Pick = gripper_1.move(linRel(0, 0, 300).setCartVelocity(450).breakWhen(contact_Start_Pick));
		if (contactMotion_Pick.hasFired(contact_Start_Pick))
		{
			logger.info("The contact has occured!");
		}
	}
	public void linear_Force_3()
	
	{
		logger.info("linear motion with force control");
		
		ForceCondition contact_Start_Pick = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 30);
		IMotionContainer contactMotion_Pick = gripper_1.move(linRel(0, 0, 800).setCartVelocity(450).breakWhen(contact_Start_Pick));
		if (contactMotion_Pick.hasFired(contact_Start_Pick))
		{
			logger.info("The contact has occured!");
		}
	}
	public void assem_Rail_ABB_M1174()
	{
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/orient_Abbm1174").copyWithRedundancy();
		gripper_1.move(ptp(orient_Assem_Pos));
		linear_Force();
		int stiff_assembrail = 100;
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.X).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Y).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Z).setStiffness(stiff_assembrail);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/fix_Assem_Pos_M1174").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos ));
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob).breakWhen(contact_Assem_Mag));
		gripper_1.open_M();
	}
	public void assem_Rail_Fh202a()
	{
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/orient_Fh202a").copyWithRedundancy();
		gripper_1.move(ptp(orient_Assem_Pos));
		linear_Force();
		int stiff_assembrail = 100;
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.X).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Y).setStiffness(stiff_assembrail);
		softrob.parametrize(CartDOF.Z).setStiffness(stiff_assembrail);
		
		Frame fix_Assem_Pos = app_Data.getFrame("/Assembly_Table/orient_Fh202a/fix_Pos").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos ));
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		gripper_1.move(ptp(orient_Assem_Pos).setMode(softrob).breakWhen(contact_Assem_Mag));
		gripper_1.open_M();
	}
	
	
	private void assem_Rail_mag() 
	
	{
		Frame orient_Assem_Pos = app_Data.getFrame("/Assembly_Table/orient_Assem_T").copyWithRedundancy();
		
		gripper_1.move(ptp(orient_Assem_Pos));
		linear_Force();
		
		CartesianImpedanceControlMode softrob = new CartesianImpedanceControlMode();
		softrob.parametrize(CartDOF.ALL).setStiffness(100.0);
		
		Frame fix_Assem_Pos_mag = app_Data.getFrame("/Assembly_Table/fix_Assem_Pos_Mag").copyWithRedundancy();
		gripper_1.move(ptp(fix_Assem_Pos_mag));
		
		ForceCondition contact_Assem_Mag = ForceCondition.createSpatialForceCondition(gripper_1.getDefaultMotionFrame(), 25);
		
		gripper_1.move(ptp(fix_Assem_Pos_mag).setMode(softrob).breakWhen(contact_Assem_Mag));
		//gripper_1.move(ptp(orient_Assem_Pos_A7_orient).setMode(softrob).breakWhen(contact_Assem_Mag));
		gripper_1.open_M();



		
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
		
		IMotionContainer positionHoldMotion=gripper_1.move(positionHold(ctrl, 2, TimeUnit.SECONDS));
	}
	
	
}
