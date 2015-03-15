package com.nulldev.lib;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

public class LibNullSensorTranslater {
	static List<Float> calibList = new ArrayList<Float>();;
	static Robot robot = null;
	public static void init() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("Unable to make robots :(");
			e.printStackTrace();
		}
	}
	
	public static void translate(String command) {
		float acX = Float.parseFloat(command.split(";")[0]);
		float acY = Float.parseFloat(command.split(";")[1]);
		if(acX > -0.05 && acX < 0.05)
			acX = 0;
		if(acY > -0.05 && acY < 0.05)
			acY = 0;
		
		Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		robot.mouseMove((int)(mouseLoc.getX() + acX*50), (int)(mouseLoc.getY() - acY*50));
	}
}
