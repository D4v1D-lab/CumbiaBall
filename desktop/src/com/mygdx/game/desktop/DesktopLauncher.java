package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("CumbiaBall");
		config.setWindowedMode(640, 360);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new MainGame(), config);
	}
}
