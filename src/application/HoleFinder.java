package application;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.positionHold;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import Util.Gripper;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianSineImpedanceControlMode;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;

public class HoleFinder {
	
	@Inject
	private Gripper gripper;
	
	@Inject
	private LBR lbr;
	
	// Very important:
	@Inject
	private IApplicationData appData; //call the frame
	
	@Inject
	private IApplicationUI ui; // call the diag
	

	public void findHoleExample() {
		
		ui.displayModalDialog(
				ApplicationDialogType.WARNING, "Hole function",
				"Start");
		
		

		CartesianSineImpedanceControlMode controlMode = new CartesianSineImpedanceControlMode();
		// X,Y,Z Translations 0-5000
		controlMode.parametrize(CartDOF.X).setStiffness(1000).setDamping(0.5)
				.setFrequency(3).setAmplitude(9);
		controlMode.parametrize(CartDOF.Y).setStiffness(1000).setDamping(0.5)
				.setFrequency(3).setAmplitude(12);
		controlMode.parametrize(CartDOF.Z).setStiffness(1000).setBias(20);

		// X,Y,Z Translations 0-300
		controlMode.parametrize(CartDOF.A).setStiffness(300).setDamping(0.5);
		controlMode.parametrize(CartDOF.B).setStiffness(300).setDamping(0.5);
		controlMode.parametrize(CartDOF.C).setStiffness(300).setDamping(0.5);

		// Force condition
		ForceCondition contact = ForceCondition.createSpatialForceCondition(
				gripper.getDefaultMotionFrame(), 10);
		// your application execution starts here
		Frame StartPoint = appData.getFrame("/P1")
				.copyWithRedundancy();

		// Move to start point
		gripper.move(ptp(StartPoint).setJointVelocityRel(0.1));

		// Move to pre hole position
		gripper.move(ptp(appData.getFrame("/P5/P1"))
				.setJointVelocityRel(0.1));

		// Move relatively
		IMotionContainer contactMotion = gripper.move(linRel(0, 0, 30)
				.setCartVelocity(50).breakWhen(contact));

		// Do I have contact?
		if (contactMotion.hasFired(contact)) {
			double tolerance = 5; // mm
			double conditionHeight = 0;

			System.out.println("Contact has occurred!");
			// gripper.move(linRel(Transformation.ofDeg(0,0,-100,0,0,0)).setCartVelocity(20));
			// System.out.println(stif_Translation + ", " + stif_Rotation);
			// gripper.move(lin(getApplicationData().getFrame("/P4/P1")).setMode(controlMode));

			Frame currentPoseAfterContact = lbr
					.getCurrentCartesianPosition(gripper
							.getDefaultMotionFrame());
			System.out.println("before " + currentPoseAfterContact);
			IMotionContainer positionHoldMotion = gripper
					.moveAsync(positionHold(controlMode, 10, TimeUnit.SECONDS));

			while (conditionHeight < tolerance) {

				Frame currentPose = lbr.getCurrentCartesianPosition(gripper
						.getDefaultMotionFrame());

				conditionHeight = Math.abs(currentPoseAfterContact.getZ()
						- currentPose.getZ());
				// System.out.println(conditionHeight);

				// toinen vaihtoehto kayttaa tallaista:
				// currentPose.isCloseTo(other, maxCartDist, maxRotDist)

				if (positionHoldMotion.isFinished()) {
					System.out.println("break");
					System.out.println(" After " + currentPose);
					break;
				}
			}

			controlMode.parametrize(CartDOF.X).setStiffness(1000)
					.setDamping(0.5);
			controlMode.parametrize(CartDOF.Y).setStiffness(1000)
					.setDamping(0.5);

			IMotionContainer contactMotion2 = gripper
					.moveAsync(positionHold(controlMode, 10, TimeUnit.SECONDS));

			positionHoldMotion.cancel();

		}
		gripper.open();
		ThreadUtil.milliSleep(1000);

		gripper.move(linRel(0, 0, -100).setCartVelocity(50));

		gripper.move(lin(StartPoint).setCartVelocity(150));
	}
	
	
}
