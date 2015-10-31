package io.github.wreed12345;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Project Freeman Launcher";
		cfg.useGL20 = false;
		cfg.width = 850;
		cfg.height = 475;
		cfg.resizable = false;
		
		new LwjglApplication(new MainLauncher(), cfg);
	}
}
