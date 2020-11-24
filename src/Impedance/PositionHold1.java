package Impedance;


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
public class PositionHold1 extends RoboticsAPIApplication {
	@Inject
	private LBR lBR;

	@Inject 
	private Gripper gripper;
	
	@Inject
	@Named("stif_Translation")
	private IProcessData _pStif_Translation;
	
	@Inject
	@Named("stif_Rotation")
	private IProcessData _pStif_Rotation;
	
	@Inject
	private FlexFellow flexFELLOW_1;

	@Override
	public void initialize() {
		// initialize your application here
		gripper.attachTo(lBR.getFlange());
		
	}

	@Override
	public void run() {
		// your application execution starts here
		
		int stif_Translation = _pStif_Translation.getValue();
		int stif_Rotation = _pStif_Rotation.getValue();
		
		CartesianSineImpedanceControlMode controlMode = new CartesianSineImpedanceControlMode();
		// X,Y,Z Translations 0-5000
		controlMode.parametrize(CartDOF.X).setStiffness(300).setDamping(0.5);
		controlMode.parametrize(CartDOF.Y).setStiffness(300).setDamping(0.5);
		controlMode.parametrize(CartDOF.Z).setStiffness(300).setDamping(0.5);
		// X,Y,Z Translations 0-300
		controlMode.parametrize(CartDOF.A).setStiffness(20).setDamping(0.5).setFrequency(2).setAmplitude(2);
		controlMode.parametrize(CartDOF.B).setStiffness(20).setDamping(0.5);
		controlMode.parametrize(CartDOF.C).setStiffness(20).setDamping(0.5);
		
		System.out.println(stif_Translation + ", " + stif_Rotation);
		IMotionContainer positionHoldMotion=gripper.move(positionHold(controlMode, 10, TimeUnit.SECONDS));
	}
	
	//dispose
	@Override	
	public void dispose() {
		lBR.detachAll();
	}
}