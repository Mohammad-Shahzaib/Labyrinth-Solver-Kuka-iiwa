package com.kuka.generated.flexfellow;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.kuka.flexfellow.AbstractFlexFellow;
import com.kuka.generated.ioAccess.FlexFellowIOGroup;
import com.kuka.roboticsAPI.controllerModel.Controller;

/**
 * Automatically generated class to facilitate access to the flexFELLOW's functionality.
 * <i>Please, do not modify!</i>
 *
 * Represents a KUKA Sunrise.flexFELLOW H750 Extended platform and gives access to its IOs.
 */
@Singleton
public class FlexFellow extends AbstractFlexFellow
{
    /**
     * Creates a new KUKA flexFELLOW instance.
     * 
     * @param controller
     *            the controller to be used
     * 
     * @param name
     *            the name for the flexFELLOW instance
     */
	@Inject FlexFellowIOGroup flexIO;
	
	
    public FlexFellow(Controller controller, String name)
    {
        super(controller, name);
    }
    
    

}
