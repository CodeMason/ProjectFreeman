package io.github.wreed12345;

import io.github.wreed12345.shared.Constants;
import io.github.wreed12345.shared.LoginRequest;
import io.github.wreed12345.shared.NewPlayer;
import io.github.wreed12345.shared.Player;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.BlowfishSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class MainLauncher implements ApplicationListener {
	// TODO: split this garbage into classes....
	// TODO: get some threads going to launch this thing faster.
	// TODO: share jars between both launchers.
	private static final String DOWNLOADED_VERSION = "0.0.3";
	private static String version;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Skin skin;
	private Stage stage;
	private ShapeRenderer shapeRenderer;

	private Client client;

	private static final String GAME_NAME = "freeman_";
	private static final String VERSION_FILE_URL = "http://wreed12345.github.io/resources/freeman/";
	private static final String VERSION_FILE_NAME_AND_EXTENSION = "version.txt";
	private GameUpdater updater;
	private boolean needsUpdating = false;
	private Label updateLabel;

	private Preferences prefs;

	private List<Status> statuses = Collections.synchronizedList(new ArrayList<Status>());

	public static volatile boolean anotherInstanceRunning = false;
	private volatile boolean twitterDone = false;
	private volatile boolean changedLoaded = false;

	@Override
	public void create() {
		Thread singleInstance = new Thread(new SingleInstanceChecker());
		singleInstance.start();
		prefs = Gdx.app.getPreferences("freeman.dat");
		if (prefs.getString("version").equals(null)) {
			prefs.putString("version", DOWNLOADED_VERSION);
			prefs.flush();
			version = DOWNLOADED_VERSION;
		} else {
			version = prefs.getString("version");
		}

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);
		camera.translate(w / 2, h / 2);
		camera.update();
		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();

		// pop in first run after sprite batch
		setupStage();
		setupClient();

		new Thread() {
			public void run() {
				setupTwitter();
				twitterDone = true;
				twitter.setChecked(true);
				twitter.setChecked(false);
			}
		}.start();

		new Thread() {
			public void run() {
				getChanges();
				changedLoaded = true;
			}
		}.start();

		try {
			updater = new GameUpdater(version, VERSION_FILE_URL, VERSION_FILE_NAME_AND_EXTENSION, GAME_NAME);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		needsUpdating = updater.needsUpdating();

		// pop in a method?
		if (needsUpdating) {
			updateLabel = new Label("Update available.", skin);
		} else {
			updateLabel = new Label("Up to date.", skin);
		}
		updateLabel.setPosition(250, 20);
		stage.addActor(updateLabel);

	}

	private int downloadPerctage;
	private boolean first = true;
	private boolean firstRender = true;

	private void update() {
		camera.update();
		stage.act();
		downloadPerctage = updater.getDownloadPercentage().get();

		if (updating)
			updateLabel.setText("Downloading update " + downloadPerctage + "%");

		if (anotherInstanceRunning && first) {
			new Dialog("Another game found running!", skin, "dialog") {
			}.text("You may only run one instance of Project Freeman at a time").show(stage);
			first = false;
		}
		
		if(twitterDone && firstRender){
			synchronized (statuses) {
				for (Status s : statuses) {
					Image icon = new Image(new Texture(Gdx.files.internal("data/twitter-64x64.png")));
					String title = s.getText();
					if (title.length() > 70) {
						String title2 = title.substring(70);
						title = title.replace(title2, "");
						title = title + "-\n" + title2;
					}
					Label label = new Label("Project Freeman : " + s.getCreatedAt() + "\n" + title, skin);

					rightPane.add(icon).size(64).padTop(20).align(Align.left);
					rightPane.add(label).align(Align.left).row();
				}
			}
			rightPane.setPosition(240 + (rightPane.getPrefWidth() / 2),
					64 + (rightPane.getPrefHeight() / 2));
			firstRender = false;
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		shapeRenderer.setProjectionMatrix(camera.combined);

		// vertical gray bar
		shapeRenderer.begin(ShapeType.Line);
		{
			shapeRenderer.setColor(.74509f, .74509f, .74509f, 1);
			shapeRenderer.line(210, 10, 210, 470);

		}
		shapeRenderer.end();

		stage.draw();
		Table.drawDebug(stage);
	}

	private boolean clientEnabled;

	/**
	 * Adds 4 most recent tweets to the list that will be displayed on launcher
	 */
	// TODO: AJASDFGBASDOUGBASD DONT DISTRIBUTE!
	//TODO: create some encrypter magic for this
	private void setupTwitter() {
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey("gj80tnhSczEwdZFxiBZihw").setOAuthConsumerSecret(
					"MZDNN5AvC3R1ELqtLZwVDfurw3j5v7XnTKJL1VHqtU").setOAuthAccessToken(
					"2244945193-kxPIqm602uVuVdkxtl2vdVJbRGtjXyBo342VxEL").setOAuthAccessTokenSecret(
					"AQyPUiXn5wZV9kY59XpM7tS0gaQNPaZZhMil5iXfzqoCW");
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();

			List<Status> allStatuses = twitter.getUserTimeline();

			synchronized (statuses) {
				for (int i = 0; i < 4; i++) {
					statuses.add(allStatuses.get(i));
				}
			}

			allStatuses.clear();

		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	public static final String SERVER_ADRESS = "69.124.180.8";

	/**
	 * Connects client and registers classes
	 */
	private void setupClient() {
		client = new Client();
		client.start();
		try {
			client.connect(20000, SERVER_ADRESS, 54555, 54777);
		} catch (IOException e) {
			Label cantConnect = new Label("Could not connect to server.", skin);
			cantConnect.setPosition(650, 10);
			stage.addActor(cantConnect);
			return;
		}

		Kryo kryo = client.getKryo();
		kryo.register(Game.class, 100);
		kryo.register(Player.class, new BlowfishSerializer(new FieldSerializer<Player>(kryo, Player.class),
				Constants.key), 103);
		kryo.register(NewPlayer.class, new BlowfishSerializer(new FieldSerializer<NewPlayer>(kryo,
				NewPlayer.class), Constants.key), 104);
		kryo.register(String.class);
		kryo.register(LoginRequest.class, new BlowfishSerializer(new FieldSerializer<LoginRequest>(kryo,
				LoginRequest.class), Constants.key), 106);
		clientEnabled = true;
		createListeners();
	}

	TextButton twitter;
	Table rightPane;

	/**
	 * Sets up everything needed for the stage. UI stuff
	 */
	private void setupStage() {
		// be very concerned when we go to android
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		Table leftPane = new Table(skin);
		leftPane.setPosition(100, 340);

		Label login = new Label("Log in", skin);
		leftPane.add(login).row();
		final TextField loginUsername = new TextField("", skin);
		loginUsername.setMessageText("Username");
		leftPane.add(loginUsername).row().padBottom(2f).padTop(2f);
		final TextField loginPassword = new TextField("", skin);
		loginPassword.setMessageText("Password");
		loginPassword.setPasswordCharacter('*');
		loginPassword.setPasswordMode(true);
		leftPane.add(loginPassword).row();
		TextButton loginButton = new TextButton("    Login    ", skin);
		loginButton.setWidth(100);
		leftPane.add(loginButton).row().padTop(16f);

		Label register = new Label("Register", skin);
		leftPane.add(register).row();
		final TextField registerUsername = new TextField("", skin);
		registerUsername.setMessageText("Username");
		leftPane.add(registerUsername).row().padBottom(2f).padTop(2f);
		final TextField registerPassword = new TextField("", skin);
		registerPassword.setMessageText("Password");
		registerPassword.setPasswordCharacter('*');
		registerPassword.setPasswordMode(true);
		leftPane.add(registerPassword).row().row();
		TextButton registerButton = new TextButton("  Register  ", skin);
		registerButton.setWidth(100);
		leftPane.add(registerButton).row().row();

		registerButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {

				if (accountChecker(registerUsername.getText(), registerPassword.getText()) && clientEnabled) {
					// by this point assume that this is good username and
					// password.
					createAccount(registerUsername.getText(), registerPassword.getText());
				}
			}
		});

		loginButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (loginCheck(loginUsername.getText(), loginPassword.getText())) {
					username = loginUsername.getText();
					password = loginPassword.getText();
					loginAttempt(loginUsername.getText(), loginPassword.getText());
				}
				return;
			}
		});

		// right side udpates
		rightPane = new Table(skin);
		// min and max buttons are set to one by default
		twitter = new TextButton("   Twitter   ", skin);
		TextButton changes = new TextButton("   Changes   ", skin);
		rightPane.add(twitter);// .padRight(0);
		rightPane.add(changes).align(Align.left).row();

		// create thing for each status with icon / text
		changes.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (!changedLoaded)
					return;

				if (!inTwitterView)
					e.cancel();

				ArrayList<Actor> rightPaneStatics = new ArrayList<Actor>();
				rightPaneStatics.add(rightPane.getChildren().get(0));
				rightPaneStatics.add(rightPane.getChildren().get(1));

				rightPane.clear();
				rightPane.add(rightPaneStatics.get(0));
				rightPane.add(rightPaneStatics.get(1)).align(Align.left).row();

				// turn arraylist into one string

				Label versionTemp = new Label("V" + version, skin);
				Label changes = new Label(changeText, skin);// TODO: synchronzie
															// changeText

				rightPane.add(versionTemp);
				rightPane.add(changes);// .align(Align.left);
				rightPane.setPosition(240 + (rightPane.getPrefWidth() / 2),
						(475 + (rightPane.getPrefHeight() / 2)) - (rightPane.getPrefHeight() + 48));
				inTwitterView = false;
			}
		});

		twitter.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				if (!twitterDone)
					return;
				if (inTwitterView)
					e.cancel();
				ArrayList<Actor> rightPaneStatics = new ArrayList<Actor>();
				rightPaneStatics.add(rightPane.getChildren().get(0));
				rightPaneStatics.add(rightPane.getChildren().get(1));

				rightPane.clear();
				rightPane.add(rightPaneStatics.get(0));
				rightPane.add(rightPaneStatics.get(1)).align(Align.left).row();

				synchronized (statuses) {
					for (Status s : statuses) {
						Image icon = new Image(new Texture(Gdx.files.internal("data/twitter-64x64.png")));
						String title = s.getText();
						if (title.length() > 70) {
							String title2 = title.substring(70);
							title = title.replace(title2, "");
							title = title + "-\n" + title2;
						}
						Label label = new Label("Project Freeman : " + s.getCreatedAt() + "\n" + title, skin);

						rightPane.add(icon).size(64).padTop(20).align(Align.left);
						rightPane.add(label).align(Align.left).row();
					}
				}
				rightPane.setPosition(240 + (rightPane.getPrefWidth() / 2),
						64 + (rightPane.getPrefHeight() / 2));
				inTwitterView = true;
			}
		});

		// stupid math i dont understand all i know is that it works. apparently
		// the position is the middle of it
		rightPane.setPosition(240 + (rightPane.getPrefWidth() / 2), 64 + (rightPane.getPrefHeight() / 2));
		stage.addActor(rightPane);
		stage.addActor(leftPane);

	}

	private volatile String changeText = "";

	private void getChanges() {
		List<String> updates = new ArrayList<String>();
		try {
			URL url = new URL(
					"https://raw.github.com/wreed12345/wreed12345.github.io/master/resources/freeman/changelog.txt");
			Scanner s = new Scanner(url.openStream());

			String current = null;
			boolean stringDone = false;

			while (s.hasNext()) {
				String currentString = s.next();

				if (currentString.contains("====")) {// end of that change log
														// thing
					break;
				}

				if (currentString.endsWith(",") || currentString.endsWith(";")) {
					current = current + " " + currentString;
					stringDone = true;
				} else {
					current = current + " " + currentString;
				}
				if (stringDone) {
					current = current.replace("null", "");
					current = current.replace(",", "");
					current = current.replace(";", "");
					updates.add(current);
					current = "";
					stringDone = false;
				}
			}
			s.close();

			for (String string : updates) {
				if (changeText.equals("")) // first string
					changeText = string + "\n";
				else
					changeText = changeText + string + "\n";
			}

		} catch (IOException ex) {
			ex.printStackTrace(); // for now, simply output it.
		}
	}

	private boolean inTwitterView = true;

	private boolean loginCheck(String username, String password) {
		if (username.equals("")) {
			new Dialog("Login Failed", skin, "dialog") {
			}.text("Username does not contain text").button("Return", true).key(Keys.ENTER, true).key(
					Keys.ESCAPE, true).show(stage);
			return false;
		}
		if (password.equals("")) {
			new Dialog("Login Failed", skin, "dialog") {
			}.text("Password does not contain text").button("Return", true).key(Keys.ENTER, true).key(
					Keys.ESCAPE, true).show(stage);
			return false;
		}
		return true;
	}

	private void loginAttempt(String username, String password) {
		client.sendTCP(new LoginRequest(username, password));
	}

	private void createAccount(String username, String password) {

		client.sendTCP(new NewPlayer(username, password));

	}

	private String username, password;

	private void createListeners() {
		client.addListener(new Listener() {

			public void received(Connection connection, Object object) {
				if (object instanceof String) {
					String response = (String) object;
					if (response.equals("Username is already in use.")) {
						new Dialog("Account Creation Failed", skin, "dialog") {
						}.text("The username you selected is taken. Please try again").button("Return", true)
								.key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
					} else if (response.equals("Account Created")) {
						new Dialog("Account Creation Succesful", skin, "dialog") {
						}.text("Your account has been sucesfully created. Please log in to play.").button(
								"Return", true)
						// TODO: too much in one line?
								.key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
					} else if (response.equals("Username does not exist")) {
						new Dialog("Username does not exist", skin, "dialog") {
						}.text("The requested username does not exist. Please try again.").button("Return",
								true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
					} else if (response.equals("login sucessful")) {
						// launch new jar.
						prefs.putString("username", username);
						prefs.putString("password", password);
						prefs.flush();
						if (needsUpdating) {
							updating = true;
							updateLabel.setText("Download update " + downloadPerctage + "%");
							Thread t = new Thread() {
								@Override
								public void run() {
									try {
										// download new version
										updater.downloadUpdatedJar(false);
										// get rid of older version
										File f = new File(System.getProperty("user.home") + File.separator
												+ "Freeman" + File.separator + GAME_NAME + version + ".jar");
										System.out.println(f.getName());
										System.out.println(f.getAbsolutePath());
										f.delete();
										System.out.println("old game deleted");

										prefs.putString("version", updater.getMostRecentVersion());
										prefs.flush();
										// launch new jar
										String jarName = System.getProperty("user.home") + File.separator
												+ "Freeman" + File.separator + GAME_NAME
												+ updater.getMostRecentVersion() + ".jar";
										// get max
										InetAddress ip;
										String realMac = null;
										try {
											ip = InetAddress.getLocalHost();

											NetworkInterface network = NetworkInterface.getByInetAddress(ip);

											byte[] mac = network.getHardwareAddress();

											StringBuilder sb = new StringBuilder();
											for (int i = 0; i < mac.length; i++) {
												sb.append(String.format("%02X%s", mac[i],
														(i < mac.length - 1) ? "-" : ""));
											}
											realMac = sb.toString();
											System.out.println(realMac);
										} catch (Exception e) {
											e.printStackTrace();
											System.exit(1);
										}

										Runtime.getRuntime().exec("java -jar " + jarName + " " + realMac);
										Thread.sleep(1000);
										System.exit(0);

									} catch (MalformedURLException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							};
							t.run();
						} else {
							try {
								String jarName = System.getProperty("user.home") + File.separator + "Freeman"
										+ File.separator + GAME_NAME + updater.getMostRecentVersion()
										+ ".jar";

								InetAddress ip;
								String realMac = null;
								try {
									ip = InetAddress.getLocalHost();

									NetworkInterface network = NetworkInterface.getByName("wlan0");

									byte[] mac = network.getHardwareAddress();

									StringBuilder sb = new StringBuilder();
									for (int i = 0; i < mac.length; i++) {
										sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-"
												: ""));
									}
									realMac = sb.toString();
									System.out.println(realMac);
								} catch (Exception e) {
									e.printStackTrace();
									System.exit(1);
								}

								Runtime.getRuntime().exec("java -jar " + jarName + " " + realMac);
								Thread.sleep(1000);
								System.exit(0);

							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						new Dialog(" " + response + " ", skin, "dialog") {
						}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);

					}

				}
			}
		});
	}

	private boolean updating = false;

	/**
	 * Checks to see if account username and password passes the designated
	 * allowed passwords / usernames
	 * 
	 * @param username
	 *            String of username
	 * @param password
	 *            String of password
	 * @return true if all conditions are met
	 */
	private boolean accountChecker(String username, String password) {
		// perform username checks then password checks
		if (username.equals("Username")) {
			new Dialog("Username must contain text", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		if (username.length() < 5) {
			new Dialog("Username must be at least 5 characters", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		if (username.length() > 12) {
			new Dialog("Username must be less than 13 characters", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		if (username.contains(" ")) {
			new Dialog("Username can not contain spaces", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		if (password.equals("Password")) {
			new Dialog("Password must contain text", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		if (password.contains(" ")) {
			new Dialog("Password can not contain spaces", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		if (password.length() < 5) {
			new Dialog("Password must be at least 5 characters", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		if (password.length() > 12) {
			new Dialog("Password must be less than 12 characters", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
			return false;
		}

		return true;
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {
		update();
		draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
