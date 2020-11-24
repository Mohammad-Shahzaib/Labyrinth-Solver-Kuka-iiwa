package application;


import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
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
public class Robot_application_humza extends RoboticsAPIApplication {
	@Inject
	private LBR lBR_iiwa_7_R800_1;
	
	@Inject
	private assembly_Tasks assembly_1;
	
	@Inject
	private assembly_Task_auto auto_Seq;
	

	@Inject
	private FlexFellow flexFELLOW_1;

	@Override
	public void initialize() {
		// initialize your application here
	}

	@Override
	public void run() {
		//getApplicationControl().setApplicationOverride(0.3);
		//assembly_1.main_Application();
		auto_Seq.main_App();
		//getApplicationControl().setApplicationOverride(0);
		
	}
}