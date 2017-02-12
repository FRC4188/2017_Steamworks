package org.usfirst.frc.team4188.robot;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class CHSJoystick extends Joystick {
    private boolean[] prevState;
    private boolean[] toggleState;
    private boolean wasDead = false;
    private final int numberOfAxes, throttleAxis;
    private static final int TRIGGER_BUTTON = 1, TOP_BUTTON = 2;
    private double XNegDeadZone, XPosDeadZone, XMaxSpeedPercent;
    private double YNegDeadZone, YPosDeadZone, YMaxSpeedPercent;
    private double twistNegDeadZone, twistPosDeadZone, twistMaxSpeedPercent;
    private double XScale, YScale, twistScale;
    private static final double TWIST_FRACTION = 0.5;
	//when the x and y values are greater than the MinPoint, the twist value will be multiplied
    //by the TWIST_FRACTION
    private static final double MinPoint = 0.2;

    /**
     * Construct an instance of CHSJoystick.
     * The joystick index is the USB port on the drivers station.
     * CHSJoystick constructor allows user to configure dead zones, multipliers
     * (square/cube/etc. joystick axis inputs), and maximum outputs.
     * @param port Port on the driver station that the joystick is plugged into.
     * @param numAxes Number of axis types in the Joystick class enumeration.
     * @param numButtons Number of button types in the Joystick class enumeration.
     * @param XDZMin Minimum extreme of the x-axis dead zone, above which values will be returned as 0.
     * @param XDZMax Maximum extreme of the x-axis dead zone, below which values will be returned as 0.
     * @param XMult Multiplier for values from the X axis. 2 will square input, 3 will cube input, etc.
     * @param XMax Maximum (absolute value) output for X axis: should be a percentage (<1) or 1 for 100%.
     *          Inputs will be multiplied by this value before being returned. To invert the axis, make this value negative.
     * @param YDZMin Minimum extreme of the y-axis dead zone, above which values will be returned as 0.
     * @param YDZMax Maximum extreme of the y-axis dead zone, below which values will be returned as 0.
     * @param YMult Multiplier for values from the Y axis. 2 will square input, 3 will cube input, etc.
     * @param YMax Maximum (absolute value) output for Y axis: should be a percentage (<1) or 1 for 100%.
     *          Inputs will be multiplied by this value before being returned. To invert the axis, make this value negative.
     * @param twistDZMin Minimum extreme of the twist or z-axis dead zone, above which values will be returned as 0.
     * @param twistDZMax Maximum extreme of the twist or z-axis dead zone, below which values will be returned as 0.
     * @param twistMult Multiplier for values from the twist/Z axis. 2 will square input, 3 will cube input, etc.
     * @param twistMax Maximum (absolute value) output for twist/Z axis: should be a percentage (<1) or 1 for 100%.
     *          Inputs will be multiplied by this value before being returned. To invert the axis, make this value negative.
     */
    public CHSJoystick(int port, int numAxes, int numButtons, double XDZMin,
            double XDZMax, int XMult, double XMax, double YDZMin, double YDZMax, int YMult,
            double YMax, double twistDZMin, double twistDZMax,
            int twistMult, double twistMax)
    {
        // Tell it how many axes and buttons there are. Add 1 because 0th array index in parent class will be empty.
        super(port,(numAxes+1),(numButtons+1));
        super.setAxisChannel(Joystick.AxisType.kX, 0);
        super.setAxisChannel(Joystick.AxisType.kY, 1);
        super.setAxisChannel(Joystick.AxisType.kZ, 2);           // Unused in this class.
        super.setAxisChannel(Joystick.AxisType.kTwist, 2);       // Will not care if joystick has no twist axis.
        
        if(numAxes>3)
        {
            throttleAxis = 3;
            super.setAxisChannel(Joystick.AxisType.kThrottle, 3);// Unused in this class.
        }
        else{
            throttleAxis = 2;}

        prevState = new boolean[numButtons+1];          // Add 1 because the 0th index will be empty.
        toggleState = new boolean[numButtons+1];        // Add 1 because the 0th index will be empty.
        numberOfAxes = numAxes;
        XNegDeadZone = XDZMin;
        XPosDeadZone = XDZMax;
        XMaxSpeedPercent = XMax;
        YNegDeadZone = YDZMin;
        YPosDeadZone = YDZMax;
        YMaxSpeedPercent = YMax;
        twistNegDeadZone = twistDZMin;
        twistPosDeadZone = twistDZMax;
        twistMaxSpeedPercent = twistMax;
        XScale = XMult;
        YScale = YMult;
        twistScale = twistMult;
    }

    /**
     * CHSJoystick three-parameter constructor. Sets number of axes and number of buttons,
     * and defaults to no dead zones, no limit on max outputs, and squaring inputs.
     * @param port The port on the driver station that the joystick is plugged into.
     */
    public CHSJoystick(int port, int numAxes, int numButtons)
    {
        // Default to no dead zones, no limit on max outputs, and squaring inputs.
        this(port, numAxes, numButtons, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 2, 1);
    }

    /**
     * CHSJoystick default one-parameter constructor. Sets default dead zone, multiplier, and maximum output values.
     * Defaults to 4 axes, 12 buttons, no dead zones, no limit on max outputs, and squaring inputs.
     * @param port The port on the driver station that the joystick is plugged into.
     */
    public CHSJoystick(int port)
    {
        // Default to 5 axes, 12 buttons, no dead zones, no limit on max outputs, and squaring inputs.
        this(port, 5, 12, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 2, 1);
    }
    
    
    /**
     * Builder pattern.  DAR 2016 Jan 26
     * @param XDZMin
     * @param XDZMax
     * @return
     */
    public CHSJoystick xDeadZone(double min, double max) {
        XNegDeadZone = min;
        XPosDeadZone = max;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param YDZMin
     * @param YDZMax
     * @return
     */
    public CHSJoystick yDeadZone(double min, double max) {
        YNegDeadZone = min;
        YPosDeadZone = max;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param xMult
     * @return
     */
    public CHSJoystick xMult(int xMult) {
        XScale = xMult;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param xMax
     * @return
     */
    public CHSJoystick xMaxSpeed(double xMax) {
        XMaxSpeedPercent = xMax;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param yMult
     * @return
     */
    public CHSJoystick yMult(int yMult) {
    	YScale = yMult;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param yMax maximum speed, as a percentage.
     * @return
     */
    public CHSJoystick yMaxSpeed(double yMax) {
        YMaxSpeedPercent = yMax;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param twistDZMin
     * @param twistDZMax
     * @return
     */
    public CHSJoystick twistDeadZone(double twistDZMin, double twistDZMax) {
        twistNegDeadZone = twistDZMin;
        twistPosDeadZone = twistDZMax;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param twistMult
     * @return
     */
    public CHSJoystick twistMult(int twistMult) {
        twistScale = twistMult;
        return this;
    }
    
    /**
     * Builder pattern.  DAR 2016 Jan 26.
     * @param twistMax
     * @return
     */
    public CHSJoystick twistMaxSpeed(double twistMax) {
        twistMaxSpeedPercent = twistMax;
        return this;
    }


    /**
     * Get the X value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * Method implements dead zone, multiplier, and max speed percent initialized in constructor.
     * @override getX() method in parent Joystick class.
     * @param hand Passed to super method - unused.
     * @return The X value of the joystick.
     */
    public double getXOutput()
    {
        double X = super.getX();
        
        // Configure Dead Zone from negDeadZone to posDeadZone on twist axis.
        if (((X*128)>XNegDeadZone)&&((X*128)<XPosDeadZone))
            X = 0.0;
        else
        {
            // Multiply inputs.
            for(int i = 0; i<XScale; i++) {
                X *= Math.abs(X);
            }
            // Multiply by max output.
            X*=XMaxSpeedPercent;
        }
        
        return X;
    }

    /**
     * Get the Y value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * Method implements dead zone, multiplier, and max speed percent initialized in constructor.
     * @override getY() method in parent Joystick class.
     * @param hand Passed to super method - unused.
     * @return The Y value of the joystick.
     */
    public double getYOutput()
    {
        double Y = super.getY();
        
        // Configure Dead Zone from negDeadZone to posDeadZone on twist axis.
        if (((Y*128)>YNegDeadZone)&&((Y*128)<YPosDeadZone))
            Y = 0.0;
        else
        {
            // Multiply inputs.
            for(int i = 0; i< YScale; i++) {
                Y *= Math.abs(Y);
            }
            // Multiply by max output, and invert because Y axis is inverted.
            Y*=YMaxSpeedPercent*-1;
        }
        
        	return Y;
        
    }

    /**
     * Get the twist value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * Method implements dead zone, multiplier, and max speed percent initialized in constructor.
     * @override getTwist() method in parent Joystick class.
     * @return The twist/Z value of the joystick.
     */
    public double getTwist(GenericHID.Hand hand)
    {
        double twist = super.getTwist();
        
        // Configure Dead Zone from negDeadZone to posDeadZone on twist axis.
        if (((twist*128)>twistNegDeadZone)&&((twist*128)<twistPosDeadZone))
            twist = 0.0;
        else
        {
            // Multiply inputs.
            for(int i = 0; i < twistScale; i++) {
                twist *= Math.abs(twist);
            }
            // Multiply by max output.
            twist *= twistMaxSpeedPercent;
        }
       
        if (this.getY(hand) > MinPoint && this.getX(hand) > MinPoint){
        	twist*=TWIST_FRACTION;	
        	return twist; 
        
        } else{
        	
        	return twist;
        }
    }
    
    public double getTwist()
    {
        double twist = super.getTwist();
        
        // Configure Dead Zone from negDeadZone to posDeadZone on twist axis.
        if (((twist*128)>twistNegDeadZone)&&((twist*128)<twistPosDeadZone))
            twist = 0.0;
        else
        {
            // Multiply inputs.
            for(int i = 0; i < twistScale; i++) {
                twist *= Math.abs(twist);
            }
            // Multiply by max output.
            twist *= twistMaxSpeedPercent;
        }
       
        if (this.getYOutput() > MinPoint && this.getXOutput() > MinPoint){
        	twist*=TWIST_FRACTION;	
        	return twist; 
        
        } else{
        	
        	return twist;
        }
    }

    /**
     * Get the throttle value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @override getThrottle() method in parent Joystick class.
     * @return The throttle value of the joystick.
     */
    public double getThrottle()
    {
        // We use getRaw instead of getThrottle because the Joystick class calls
        // on a default axis, which may not work if the joystick used has
        // throttle on a different axis.
        double throttle = super.getRawAxis(throttleAxis);
        
        // Convert throttle from [1,-1] to [1,0].
        throttle = (((throttle - 1) / (-2))*-1);
        throttle = (((throttle*throttle*0.8)+0.2)*-1);
        
        return throttle;
    }

    /**
     * Get the Z value of the current joystick. If the joystick has over 3 axes, it returns
     * the same value as getTwist. Otherwise, it returns the same value as getThrottle.
     * This depends on the mapping of the joystick connected to the current port.
     * @override getZ() method in parent Joystick class.
     * @param hand Unused.
     * @return The twist/throttle/Z value of the joystick.
     */
    public double getZOutput() {
        if (numberOfAxes > 3)
            return getTwist();
        else{
        return getThrottle();
        }
    }

    /**
     * Get the value of the trigger on the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @override getTrigger() method in parent Joystick class.
     * @param hand Unused.
     * @return The trigger value of the joystick.
     */
    public boolean getTrigger(GenericHID.Hand hand)
    {
        return super.getRawButton(TRIGGER_BUTTON);
    }

    /**
     * Get the value of the top button on the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     * @override getTop() method in parent Joystick class.
     * @param hand Unused.
     * @return The top button value of the joystick.
     */
    public boolean getTop(GenericHID.Hand hand)
    {
        return super.getRawButton(TOP_BUTTON);
    }

    /**
     * Returns true if all axes are within their dead zones, meaning all
     * joystick outputs are zero. Does not take throttle into account.
     * @return If the joystick is stopped.
     */
    public boolean isDead()
    {
        boolean dead = true;
        if(getX()!=0.0)                         //changed from this.getX()...
            dead = false;
        if(getY()!=0.0)                         //changed from this.getY()...
            dead = false;
        if(numberOfAxes>3 && getTwist()!=0.0)   //changed from this.getTwist()...
            dead = false;
        if(dead && wasDead)
            return true;
        wasDead = dead;
        return false;
    }

    /**
     * Button toggle method. Press a button once to return true; press it again to return false.
     * Method prevents unintentional toggling by keeping track of the current button state.
     * @param button Which button is used to toggle the state.
     * @return The current toggle state of the button.
     */
    public boolean toggleButton(int button)
    {
        boolean state = super.getRawButton(button);
        if (state&&state!=prevState[button])
        {
            if (toggleState[button])
                toggleState[button] = false;
            else
                toggleState[button] = true;
        }
        prevState[button] = state;
        return toggleState[button];
    }

    /**
     * Reset toggle method makes the toggle state for the button false.
     * Sets previous state to the state before reset.
     * @param button Which button is used for the toggle.
     */
    public void resetToggle(int button)
    {
        prevState[button] = false;
        toggleState[button] = false;
    }

    /**
     * Set a dead zone for the X axis on the current joystick.
     * @param minimum Minimum extreme of the x-axis dead zone, above which values will be returned as 0.
     * @param maximum Maximum extreme of the x-axis dead zone, below which values will be returned as 0.
     */
    public void setXDeadZone(double minimum, double maximum)
    {
        XNegDeadZone = minimum;
        XPosDeadZone = maximum;
    }

    /**
     * Set maximum (absolute value) output for the X axis on the current joystick.
     * maxOutput should be a percentage (<1) or 1 for 100%. Inputs will be multiplied
     * by this value before being returned. To invert the axis, make this value negative.
     * @param maxOutput Maximum (absolute value) output of the x-axis.
     */
    public void setXMaxOutput(double maxOutput)
    {
        XMaxSpeedPercent = maxOutput;
    }

    /**
     * Set rate/curve for values from the X axis. 2 will square input, 3 will cube input, etc.
     * @param curve Scale/Rate for values from the X axis.
     */
    public void setXScale(int curve)
    {
        XScale = curve;
    }

    /**
     * Set a dead zone for the Y axis on the current joystick.
     * @param minimum Minimum extreme of the y-axis dead zone, above which values will be returned as 0.
     * @param maximum Maximum extreme of the y-axis dead zone, below which values will be returned as 0.
     */
    public void setYDeadZone(double minimum, double maximum)
    {
        YNegDeadZone = minimum;
        YPosDeadZone = maximum;
    }

    /**
     * Set maximum (absolute value) output for the Y axis on the current joystick.
     * maxOutput should be a percentage (<1) or 1 for 100%. Inputs will be multiplied
     * by this value before being returned. To invert the axis, make this value negative.
     * @param maxOutput Maximum (absolute value) output of the y-axis.
     */
    public void setYMaxOutput(double maxOutput)
    {
        YMaxSpeedPercent = maxOutput;
    }

    /**
     * Set rate/curve for values from the Y axis. 2 will square input, 3 will cube input, etc.
     * @param curve Scale/Rate for values from the Y axis.
     */
    public void setYScale(int curve)
    {
        YScale = curve;
    }

    /**
     * Set a dead zone for the twist axis on the current joystick.
     * @param minimum Minimum extreme of the twist axis dead zone, above which values will be returned as 0.
     * @param maximum Maximum extreme of the twist axis dead zone, below which values will be returned as 0.
     */
    public void setTwistDeadZone(double minimum, double maximum)
    {
        twistNegDeadZone = minimum;
        twistPosDeadZone = maximum;
    }

    /**
     * Set maximum (absolute value) output for the twist axis on the current joystick.
     * maxOutput should be a percentage (<1) or 1 for 100%. Inputs will be multiplied
     * by this value before being returned. To invert the axis, make this value negative.
     * @param maxOutput Maximum (absolute value) output of the twist axis.
     */
    public void setTwistMaxOutput(double maxOutput)
    {
        twistMaxSpeedPercent = maxOutput;
    }

    /**
     * Set rate/curve for values from the twist axis. 2 will square input, 3 will cube input, etc.
     * @param curve Scale/Rate for values from the twist axis.
     */
    public void setTwistScale(int curve)
    {
        twistScale = curve;
    }

    /**
     * Get rate/curve for values from the X axis. 2 will square input, 3 will cube input, etc.
     * @return Scale/Rate for values from the X axis.
     */
    public int getXScale()
    {
        return (int) XScale;
    }

    /**
     * Get rate/curve for values from the Y axis. 2 will square input, 3 will cube input, etc.
     * @return Scale/Rate for values from the Y axis.
     */
    public int getYScale()
    {
        return (int) YScale;
    }

    /**
     * Get rate/curve for values from the twist axis. 2 will square input, 3 will cube input, etc.
     * @return Scale/Rate for values from the twist axis.
     */
    public int getTwistScale()
    {
        return (int) twistScale;
    }
}