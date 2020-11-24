package com.kuka.generated.ioAccess;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.ioModel.AbstractIOGroup;
import com.kuka.roboticsAPI.ioModel.IOTypes;

/**
 * Automatically generated class to abstract I/O access to I/O group <b>EL2809</b>.<br>
 * <i>Please, do not modify!</i>
 * <p>
 * <b>I/O group description:</b><br>
 * ./.
 */
@Singleton
public class EL2809IOGroup extends AbstractIOGroup
{
	/**
	 * Constructor to create an instance of class 'EL2809'.<br>
	 * <i>This constructor is automatically generated. Please, do not modify!</i>
	 *
	 * @param controller
	 *            the controller, which has access to the I/O group 'EL2809'
	 */
	@Inject
	public EL2809IOGroup(Controller controller)
	{
		super(controller, "EL2809");

		addDigitalOutput("OutPut1", IOTypes.BOOLEAN, 1);
		addDigitalOutput("OutPut2", IOTypes.BOOLEAN, 1);
	}

	/**
	 * Gets the value of the <b>digital output '<i>OutPut1</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'OutPut1'
	 */
	public boolean getOutPut1()
	{
		return getBooleanIOValue("OutPut1", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>OutPut1</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'OutPut1'
	 */
	public void setOutPut1(java.lang.Boolean value)
	{
		setDigitalOutput("OutPut1", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>OutPut2</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'OutPut2'
	 */
	public boolean getOutPut2()
	{
		return getBooleanIOValue("OutPut2", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>OutPut2</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'OutPut2'
	 */
	public void setOutPut2(java.lang.Boolean value)
	{
		setDigitalOutput("OutPut2", value);
	}

}
