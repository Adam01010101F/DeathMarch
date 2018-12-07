package com.dmr.deathmarch.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dmr.deathmarch.DeathMarch;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="Death March";
		config.width=1280;
		config.height=1280;
		new LwjglApplication(new DeathMarch(), config);
	}
}
