package io.github.wreed12345.client;

import io.github.wreed12345.MainGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1080, 720);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new MainGame();
	}
}