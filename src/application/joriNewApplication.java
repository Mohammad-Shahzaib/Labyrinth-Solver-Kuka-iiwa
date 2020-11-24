package application;

import javax.inject.Inject;

import Util.Gripper;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.deviceModel.LBRE1Redundancy;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.World;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;

import com.kuka.roboticsAPI.motionModel.LIN;
import com.kuka.roboticsAPI.motionModel.MotionBatch;
import com.kuka.roboticsAPI.motionModel.PTP;
import com.kuka.roboticsAPI.motionModel.RelativeLIN;
import com.kuka.roboticsAPI.motionModel.Spline;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;

public class joriNewApplication extends RoboticsAPIApplication {
	@Inject
	private LBR robot;
	@Inject
	private Gripper gripper;
	@Inject
	private Frame frame;
	@Inject
	private IApplicationData app_Data; //call the frame
	@Inject
	private IApplicationUI UI; // call the dialog
	
	public void initialize() {		
		robot = getContext().getDeviceFromType(LBR.class);
		gripper.attachTo(robot.getFlange());
	}
	public void run() {
		
		int currentPass = 0;
		int numberOfPasses = 0;
		
		int grindingCount = UI.displayModalDialog(ApplicationDialogType.QUESTION, "Valitse hiontakertojen lkm.","10","25","50","100","1");
		
		if (grindingCount == 0)
		{
			numberOfPasses = 10;
		}
		
		if (grindingCount == 1)
		{
			numberOfPasses = 25;
		}
		
		if (grindingCount == 2)
		{
			numberOfPasses = 50;
		}
		
		if (grindingCount == 3)
		{
			numberOfPasses = 100;
		}
		
		if (grindingCount == 4)
		{
			numberOfPasses = 1;
		}
		
		int grindingProg = UI.displayModalDialog(ApplicationDialogType.QUESTION, "Valitse hiontaohjelma","Suora","Vino","Pieni","PieniReuna","IsoReunaSuora","IsoReunaVino");
		
		if (grindingProg == 0)
		{
			while (currentPass < numberOfPasses) {
				
				iiwaTaskuhiontaSuora();
				
				++currentPass;
				}
		}
		
		if (grindingProg == 1)
		{
			while (currentPass < numberOfPasses) {
					
				iiwaTaskuhiontaVino();
				
				++currentPass;
				}
		}
		
		if (grindingProg == 2)
		{
			while (currentPass < numberOfPasses) {
				
				iiwaTaskuhiontaPieni();
				
				++currentPass;
				}
		}
		
		if (grindingProg == 3)
		{
			while (currentPass < numberOfPasses) {
				
				iiwaReunahiontaPieni();
				
				++currentPass;
				}
		}
		
		if (grindingProg == 4)
		{
			while (currentPass < numberOfPasses) {
				
				iiwaReunahiontaIsoSuora();
				
				++currentPass;
				}
		}
		
		if (grindingProg == 5)
		{
			while (currentPass < numberOfPasses) {
				
				iiwaReunahiontaIsoVino();
				
				++currentPass;
				}
		}
		
	}
	
	public void voimaTesti() {
		
		int grindspeed = 30;
		int blendingcart = 4;
		int stiffness = 1;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiffness).setAdditionalControlForce(-5);
		
		MotionBatch motion = new MotionBatch(

			lin(getFrame("/joriWorkpiece/P1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/P2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/P3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/P4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode)
		);
		
		gripper.move(motion);
	}
	
	
	public void iiwaTaskuhiontaSuora() {
		
		int hispeed = 100;
		int grindspeed = 30;
		int blendingcart = 4;
		int stiffness = 1000;
		int stiffnessHarder = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiffness);
		
		CartesianImpedanceControlMode softModeHarder = new CartesianImpedanceControlMode();
		softModeHarder.parametrize(CartDOF.Z).setStiffness(stiffnessHarder);
		
		MotionBatch motion = new MotionBatch(
			
			lin(getFrame("/joriWorkpiece/suoraTaskuA1")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuA9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuB1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuB2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuB3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/suoraTaskuB4")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode)
		);

		gripper.move(motion);
		//gripper.move(lin(getFrame("/joriWorkpiece/suoraTaskuB4")).setCartVelocity(hispeed).setBlendingCart(blendingcart));
	}
	
	public void iiwaTaskuhiontaVino() {
		
		int hispeed = 100;
		int grindspeed = 30;
		int blendingcart = 4;
		int stiffness = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiffness);

		
		MotionBatch motion = new MotionBatch(
			
			lin(getFrame("/joriWorkpiece/vinoTaskuA1")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuA9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuB1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuB2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuB3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/vinoTaskuB4")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode)
		);
				
		gripper.move(motion);	
	}
	
	
	public void iiwaTaskuhiontaPieni() {
		
		int hispeed = 100;
		int grindspeed = 30;
		int blendingcart = 4;
		int stiffness = 1000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiffness);
		
		MotionBatch motion = new MotionBatch(
			
			lin(getFrame("/joriWorkpiece/pieniTaskuA1")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuA9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuB1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuB2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniTaskuB3")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode)
		);
				
		gripper.move(motion);	
	}
	
	public void iiwaReunahiontaPieni() {
		
		int hispeed = 100;
		int grindspeed = 30;
		int slowspeed = 5;
		int blendingcart = 4;
		int stiffness = 2500;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiffness);
		softMode.parametrize(CartDOF.X).setStiffness(stiffness);
		softMode.parametrize(CartDOF.Y).setStiffness(stiffness);
		
		MotionBatch motion = new MotionBatch(
			
			lin(getFrame("/joriWorkpiece/pieniReunaA00")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA01")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaA9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB7")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpiece/pieniReunaB8")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode)
		);
				
		gripper.move(motion);	
	}
	
	public void iiwaReunahiontaIsoSuora() {
		
		int hispeed = 100;
		int grindspeed = 30;
		int slowspeed = 5;
		int blendingcart = 4;
		int stiffness = 5000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiffness);
		softMode.parametrize(CartDOF.X).setStiffness(stiffness);
		softMode.parametrize(CartDOF.Y).setStiffness(stiffness);
		
		MotionBatch motion = new MotionBatch(
			
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA1")).setCartVelocity(hispeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart)
			
			
			/*
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA1")).setCartVelocity(hispeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuA9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuB9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart).setMode(softMode)
			 */
		);
				
		gripper.move(motion);	
	}
	
	
public void iiwaReunahiontaIsoVino() {
		
		int hispeed = 100;
		int grindspeed = 30;
		int slowspeed = 5;
		int blendingcart = 4;
		int stiffness = 5000;
		
		CartesianImpedanceControlMode softMode = new CartesianImpedanceControlMode();
		softMode.parametrize(CartDOF.Z).setStiffness(stiffness);
		softMode.parametrize(CartDOF.X).setStiffness(stiffness);
		softMode.parametrize(CartDOF.Y).setStiffness(stiffness);
		
		MotionBatch motion = new MotionBatch(
			
			ptp(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC5")).setJointVelocityRel(0.5),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuC9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuD9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE4")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE5")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE6")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE7")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE8")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuE9")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuF1")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuF2")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuF3")).setCartVelocity(grindspeed).setBlendingCart(blendingcart),
			lin(getFrame("/joriWorkpieceRotated/reunaIsoTaskuF4")).setCartVelocity(hispeed).setBlendingCart(blendingcart)
			
		);
				
		gripper.move(motion);	
	}
	
	/*
	Spline tasoHiontaSuora = new Spline(		
		lin(getFrame("/joriWorkpiece/suoraTaskuP2")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP3")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP4")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP5")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP6")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP7")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP8")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP9")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP10")),
		lin(getFrame("/joriWorkpiece/suoraTaskuP11"))
	).setCartVelocity(100).setMode(softMode).setBlendingCart(10);
	
	gripper.move(tasoHiontaSuora);
	*/
	
}