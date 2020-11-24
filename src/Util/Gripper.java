package Util;

import javax.inject.Inject;

import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.EL2809IOGroup;
import com.kuka.generated.ioAccess.MediaFlangeIOGroup;
import com.kuka.roboticsAPI.geometricModel.Tool;

public class Gripper extends Tool {
	@Inject
	private EL2809IOGroup IO;
	@Inject
	private MediaFlangeIOGroup MF;
	
	public Gripper(String name) {
		super(name);
	}
	
	//open function Media Flange
	
	public void open_M()
	{
		MF.setOutputX3Pin1(true);
		MF.setOutputX3Pin11(false);
		ThreadUtil.milliSleep(200);		
		printStatus_M();
	}
	
	//close function Media Flange
	
	public void close_M() {
		MF.setOutputX3Pin11(true);
		MF.setOutputX3Pin1(false);
		ThreadUtil.milliSleep(200);
		printStatus_M();
	}
	
	//Print Status Media Flange
	
	public void printStatus_M() {
		if (MF.getOutputX3Pin1()==true && MF.getOutputX3Pin11()==true){
			System.out.println("the gripper status is unknown");
		}
		else if (MF.getOutputX3Pin1()==false && MF.getOutputX3Pin11()==false){
			System.out.println("the gripper status is unknown");
		}
		else if (MF.getOutputX3Pin11()==true && MF.getOutputX3Pin1()==false){
			System.out.println("the gripper status is close");
		}
		else {
			System.out.println("the gripper status is open");
		}
	}
	
	
	
	
	// Open Function
	
	public void open(){
		IO.setOutPut1(false);
		IO.setOutPut2(true);
		ThreadUtil.milliSleep(200);		
		printStatus();
	}
	
	// Close Function
	
	public void close() {
		IO.setOutPut2(false);
		IO.setOutPut1(true);
		ThreadUtil.milliSleep(200);
		printStatus();
	}
	
	// Print gripper status
	public void printStatus() {
		if (IO.getOutPut1()==true && IO.getOutPut2()==true){
			System.out.println("the gripper status is unknown");
		}
		else if (IO.getOutPut1()==false && IO.getOutPut2()==false){
			System.out.println("the gripper status is unknown");
		}
		else if (IO.getOutPut1()==true && IO.getOutPut2()==false){
			System.out.println("the gripper status is close");
		}
		else {
			System.out.println("the gripper status is open");
		}
	}
	//Homing Function
	public void homing(){
		
	}

}

