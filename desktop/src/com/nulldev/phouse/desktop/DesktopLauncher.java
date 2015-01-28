package com.nulldev.phouse.desktop;

import javax.swing.UIManager;

/* No longer used... Game libraries.
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nulldev.phouse.Phouse;
*/

public class DesktopLauncher {
	public static void main (String[] arg) {
		/* No longer used... We don't want to launch a game now do we?
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Phouse(), config);
		*/
		
		//Set the system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to get system look and feel.");
			e.printStackTrace();
		}
		
		//Open the Main Menu
		MainMenu.main(null);
	}
}
