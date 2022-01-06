package ca.desktop;


import app.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.width = 1280;
		config.height = 720;
		config.foregroundFPS = 120;
		config.backgroundFPS = 12;
		config.title = "Cellular Automata";
		config.addIcon("./core/assets/logo.png", Files.FileType.Local);
		// prevents exiting with code -1
		config.forceExit = false;

		new LwjglApplication(new Application(),config);
	}
}
