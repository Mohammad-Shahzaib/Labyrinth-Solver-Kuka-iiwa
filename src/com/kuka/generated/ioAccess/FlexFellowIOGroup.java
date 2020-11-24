package com.kuka.generated.ioAccess;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.ioModel.AbstractIOGroup;
import com.kuka.roboticsAPI.ioModel.IOTypes;

/**
 * Automatically generated class to abstract I/O access to I/O group <b>FlexFellow</b>.<br>
 * <i>Please, do not modify!</i>
 * <p>
 * <b>I/O group description:</b><br>
 * This IO group contains all input and output signals which are provided by the flexFELLOW chosen in the StationSetup of the Sunrise-Project.
 */
@Singleton
public class FlexFellowIOGroup extends AbstractIOGroup
{
	/**
	 * Constructor to create an instance of class 'FlexFellow'.<br>
	 * <i>This constructor is automatically generated. Please, do not modify!</i>
	 *
	 * @param controller
	 *            the controller, which has access to the I/O group 'FlexFellow'
	 */
	@Inject
	public FlexFellowIOGroup(Controller controller)
	{
		super(controller, "FlexFellow");

		addInput("DoorClosed", IOTypes.BOOLEAN, 1);
		addDigitalOutput("RedLight", IOTypes.BOOLEAN, 1);
		addDigitalOutput("YellowLight", IOTypes.BOOLEAN, 1);
		addDigitalOutput("GreenLight", IOTypes.BOOLEAN, 1);
		addDigitalOutput("BlueLight", IOTypes.BOOLEAN, 1);
		addDigitalOutput("Buzzer", IOTypes.BOOLEAN, 1);
	}

	/**
	 * Gets the value of the <b>digital input '<i>DoorClosed</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital input
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Indicates whether the side door of the flexFELLOW is closed (TRUE) or opened (FALSE).
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital input 'DoorClosed'
	 */
	public boolean getDoorClosed()
	{
		return getBooleanIOValue("DoorClosed", false);
	}

	/**
	 * Gets the value of the <b>digital output '<i>RedLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the RED signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'RedLight'
	 */
	public boolean getRedLight()
	{
		return getBooleanIOValue("RedLight", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>RedLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the RED signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'RedLight'
	 */
	public void setRedLight(java.lang.Boolean value)
	{
		setDigitalOutput("RedLight", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>YellowLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the YELLOW signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'YellowLight'
	 */
	public boolean getYellowLight()
	{
		return getBooleanIOValue("YellowLight", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>YellowLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the YELLOW signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'YellowLight'
	 */
	public void setYellowLight(java.lang.Boolean value)
	{
		setDigitalOutput("YellowLight", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>GreenLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the GREEN signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'GreenLight'
	 */
	public boolean getGreenLight()
	{
		return getBooleanIOValue("GreenLight", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>GreenLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the GREEN signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'GreenLight'
	 */
	public void setGreenLight(java.lang.Boolean value)
	{
		setDigitalOutput("GreenLight", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>BlueLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the BLUE signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'BlueLight'
	 */
	public boolean getBlueLight()
	{
		return getBooleanIOValue("BlueLight", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>BlueLight</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the BLUE signal light. TRUE -> light is powered, FALSE -> light is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'BlueLight'
	 */
	public void setBlueLight(java.lang.Boolean value)
	{
		setDigitalOutput("BlueLight", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>Buzzer</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the buzzer (acoustic signal). TRUE -> buzzer is powered, FALSE -> buzzer is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'Buzzer'
	 */
	public boolean getBuzzer()
	{
		return getBooleanIOValue("Buzzer", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>Buzzer</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * Enables the buzzer (acoustic signal). TRUE -> buzzer is powered, FALSE -> buzzer is unpowered.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'Buzzer'
	 */
	public void setBuzzer(java.lang.Boolean value)
	{
		setDigitalOutput("Buzzer", value);
	}

}
