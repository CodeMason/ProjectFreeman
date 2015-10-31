package io.github.wreed12345;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) throws UnsupportedEncodingException {
		// Where's da arg?
//		if (args.length != 1) {
//			System.exit(1);
//		}
//		String realMac = null;
//		String macAd = args[0];
//		InetAddress ip;
//		try {
//			ip = InetAddress.getLocalHost();
//
//			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//
//			byte[] mac = network.getHardwareAddress();
//
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < mac.length; i++) {
//				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//			}
//			realMac = sb.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		if (!macAd.equals(realMac)) {
//			System.exit(0);
//			return;
//		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Project Freeman " + MainGame.VERSION;
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.vSyncEnabled = false;
		cfg.backgroundFPS = 60;
		cfg.foregroundFPS = 60;
		// cfg.resizable = false;

		new LwjglApplication(new MainGame(), cfg);
	}
}
