package io.github.wreed12345;

import io.github.wreed12345.input.InputQueue;
import io.github.wreed12345.placable.LibgdxPlaceable;
import io.github.wreed12345.placable.PlaceableCreatorAndDestroyer;
import io.github.wreed12345.placable.Rocket;
import io.github.wreed12345.shared.GameUpdates;
import io.github.wreed12345.shared.countries.Country;
import io.github.wreed12345.shared.placeable.AttackType;
import io.github.wreed12345.shared.placeable.Placeable;
import io.github.wreed12345.ui.CollapsableWindow;
import io.github.wreed12345.ui.EmpireDialog;
import io.github.wreed12345.ui.MilitaryDialog;
import io.github.wreed12345.ui.TopMenu;
import io.github.wreed12345.util.Utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameScreen implements PlaceableCreatorAndDestroyer, Screen {

	private AssetManager assetManager;
	private com.badlogic.gdx.Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Client client;
	private io.github.wreed12345.shared.Game playableGame;
	private Texture realMap, rocketIcon;
	private Skin skin;
	private Stage stage;
	private Preferences prefs;
	private String username;
	private InputMultiplexer inputMultiplexer;
	private Map map;
	private Pixmap colorMap;

	private ArrayList<LibgdxPlaceable> placeables = new ArrayList<LibgdxPlaceable>();

	public GameScreen(AssetManager assetManager, com.badlogic.gdx.Game game, Client client,
			io.github.wreed12345.shared.Game playableGame) {
		this.assetManager = assetManager;
		this.game = game;
		this.client = client;
		this.playableGame = playableGame;
		prefs = Gdx.app.getPreferences("freeman.dat");
		username = prefs.getString("username");
		inputMultiplexer = new InputMultiplexer();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);
		camera.translate(w / 2, h / 2);
		camera.update();

		batch = new SpriteBatch();
		realMap = assetManager.get("data/GameScreen/images/map.png", Texture.class);
		skin = assetManager.get("data/JoinGameScreen/ui/uiskin.json", Skin.class);
		rocketIcon = assetManager.get("data/GameScreen/images/rocketIcon.png", Texture.class);
		colorMap = assetManager.get("data/GameScreen/images/mapWithColors.png", Pixmap.class);

		setupStage();
		setupListeners();

		map = new Map(realMap, colorMap,
				camera, stage, skin, playableGame);

		inputMultiplexer.addProcessor(map);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		updatePlacablesFromGame();
		addInputQueue();
	}
	
	private void addInputQueue(){
		int index = -1;
		for(InputProcessor input : inputMultiplexer.getProcessors())
			if(input.getClass().equals(Stage.class))
				index = inputMultiplexer.getProcessors().indexOf(input, false);
		
		if(index == -1) return; //stage not added? 0_o
		
		System.out.println("index");
		inputMultiplexer.getProcessors().insert(index + 1, InputQueue.inputQueue);
	}

	private Listener listener;

	private void setupListeners() {
		listener = new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof io.github.wreed12345.shared.Game) {
					playableGame = (io.github.wreed12345.shared.Game) object;
					updateGameStuff();
				} else if (object instanceof Placeable) {
					processPlaceable((Placeable) object);
				}
			}
		};
		client.addListener(listener);
	}

	/**Create class that handles this junk*/
	private void processPlaceable(Placeable placeable) {
		if(placeable.getOwner().equals("!@!MARKED_FOR_DEATH")){
			for(Placeable p : placeables){
				if(p.getId().equals(placeable.getId())){
					placeables.remove(p);
					return;
				}
			}
			return;
		}
		
		boolean exists = false;

		AttackType attackType = placeable.getType();
		LibgdxPlaceable current = null;
		switch (attackType) {
		/*case BOMBING_RUN:
			break;*/
		case CRUISE_MISSILE:
			current = new Rocket(rocketIcon, placeable.getOwner(), batch, camera, playableGame.getId(),
					client);
			current.setInGame(true);
			current.setX(placeable.getX());
			current.setY(placeable.getY());
			current.setId(placeable.getId());
			

			break;
			/*
		case DESTROYER_FLEET:
			break;
		// TODO: implement the rest
		case F18_ATTACK_UNIT:
			break;
		case GROUND_FORCE:
			break;
		case GROUND_FORCE_VEHICLE:
			break;
		case HACKS:
			break;
		case HEAVY_ARMORED_GROUND_FORCE:
			break;
		case HEAVY_ARMORED_GROUND_FORCE_VEHICLE_ESCORT:
			break;
		case NAPALM_ATTACK_UNIT:
			break;
		case SABOTEUR:
			break;
		case SUBMARINE_FLEET:
			break;
		case TANK:
			break;*/
		default:
			break;
		}

		for (Placeable p : placeables) {
			if (p.getId().equals(placeable.getId())) {
				p = current;
				exists = true;
				return;
			}
		}
		
		if (!exists) {
			placeables.add(current);
			
			int index = -1;
			for(InputProcessor p : inputMultiplexer.getProcessors()){
				if(p.getClass().equals(Map.class)){
					index = inputMultiplexer.getProcessors().indexOf(p, false);
					break;
				}
			}
			
			if(index != -1 ){
				inputMultiplexer.getProcessors().insert(index, current);
			}
		}
	}

	private void updateGameStuff() {
		// update player list
		String[] usernames = playableGame.getUsernames().toArray(
				new String[playableGame.getUsernames().size()]);
		playerList.setItems(usernames);

		// update chat messages
		String[] chatMessages = playableGame.getMessages().toArray(
				new String[playableGame.getMessages().size()]);
		chatList.setItems(chatMessages);
		chatScrollPane.setScrollPercentY(100);
		chatScrollPane.setScrollBarPositions(true, false);//TODO: added 1/31 check if works

		//TODO: clean up method. name it better v
		map.updateCountry(playableGame);
		
		updatePlacablesFromGame();
	}

	private void updatePlacablesFromGame() {
		for (Placeable p : playableGame.getPlaceables()) {
			processPlaceable(p);
		}
		int amount = 0;
		for(Placeable p : placeables){
			if(p.getType().equals(AttackType.CRUISE_MISSILE) && p.isInGame() && p.getOwner().equals(username)){
				amount++;
			}
		}
		Label label = (Label)topMenu.getMilitaryDialog().getContentTable().findActor("Cruise Missile-amount label");
		label.setText(amount + "/" + AttackType.CRUISE_MISSILE.getMaxAmount());
	}

	private CollapsableWindow playerListWindow, chatWindow;
	private ScrollPane chatScrollPane;
	private List playerList, chatList;
	private TextField chatField;
	
	private TopMenu topMenu;
	private Texture flag;

	private void setupStage() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		inputMultiplexer.addProcessor(stage);

		playerListWindow = new CollapsableWindow("Players in Game", skin);
		String[] usernames = playableGame.getUsernames().toArray(
				new String[playableGame.getUsernames().size()]);
		playerList = new List(usernames, skin);
		playerListWindow.add(playerList);
		playerListWindow.setPosition(Gdx.graphics.getWidth() - playerListWindow.getWidth(), 0);

		stage.addActor(playerListWindow);
		playerList.setSelectedIndex(-1);
		
		playerList.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int index = playerList.getSelectedIndex();
				String player = playableGame.getUsernames().get(index);
				
				new EmpireDialog(player, skin, stage, playableGame);
				
				playerList.setSelectedIndex(-1);
			}
		});
		

		// chat box
		chatWindow = new CollapsableWindow("Chat Box", skin);
		chatWindow.setWidth(400);
		String[] chatMessages = playableGame.getMessages().toArray(
				new String[playableGame.getMessages().size()]);
		chatList = new List(chatMessages, skin);
		chatScrollPane = new ScrollPane(chatList);
		chatWindow.add(chatScrollPane).align(Align.left).height(80).row();
		chatField = new TextField("", skin);
		chatField.setMessageText("Enter chat message...");
		chatField.setMaxLength(135);
		chatWindow.add(chatField).width(380).align(Align.left);
		stage.addActor(chatWindow);

		chatField.setTextFieldListener(new TextFieldListener() {
			public void keyTyped(TextField textField, char key) {
				if (key == '\r') {
					if (chatField.getText().equals("") || chatField.getText().equals(null)
							|| chatField.getText().equals("null") || chatField.getText().equals(" ")) {
						return;
					}
					client.sendTCP(GameUpdates.chatMessage(playableGame.getId(), username, chatField
							.getText()));
					chatField.setText("");
				}
			}
		});
		chatField.clearSelection();
		
		flag = assetManager.get("data/GameScreen/images/flag.png", Texture.class);
		topMenu = new TopMenu(stage, skin, flag, this, camera);
	}

	private void draw(float delta) {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, .3f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		{
			map.draw(batch);

			for (Placeable p : placeables) {
				p.draw();
			}
		}
		batch.end();
		stage.draw();
		
	}

	private void update(float delta) {
		map.input();
		stage.act();
		chatScrollPane.setScrollBarPositions(true, false);//TODO: does this work?
		
		for (Placeable p : placeables) {
			p.update();
		}
	}

	@Override
	public void render(float delta) {
		draw(delta);
		update(delta);
	}

	private boolean firstResize = true;
	
	@Override
	public void resize(int width, int height) {
		if(firstResize) {
			firstResize = false;
			return;
		}
		
		playerListWindow.setPosition(Gdx.graphics.getWidth() - playerListWindow.getWidth(), 0);
		topMenu.setPosition(0, Gdx.graphics.getHeight() - topMenu.getHeight());
		
		stage.setViewport(width, height);

		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
	
	//TODO: maybe add one gross class that takes in a poop load of arguments and handles all the stuff.....
	//TODO: what to do with this...
	@Override
	public void createRocket(int x, int y, MilitaryDialog militaryDialog){
		//TODO: get rid of placing stage stuff. jump over that now
		Vector2 realMapCords = Utils.convertToMapCordinates(x, y, camera);
		Rocket rocket = new Rocket(rocketIcon, username, batch, camera, playableGame.getId(), client);
		//subtract 11. width and height are 22 so i want this operation so you cant make places that look like they are over youur border.
		rocket.setX((int)realMapCords.x - 11);
		rocket.setY((int)realMapCords.y - 11);
		rocket.setInGame(true);
		placeables.add(rocket);
		
		int amount = 0;
		for(Placeable p : placeables){
			if(p.getType().equals(AttackType.CRUISE_MISSILE) && p.isInGame() && p.getOwner().equals(username)){
				amount++;
			}
		}
		if(amount > AttackType.CRUISE_MISSILE.getMaxAmount()){
			//cancel
			placeables.remove(rocket);
			new Dialog("Max amount reached", skin, "dialog") {
			}.text("You have reached the most rockets available").button("    Exit    ", true).key(Keys.ENTER, true).key(
					Keys.ESCAPE, true).show(stage);
			
			return;
		}
		
		Country country = determineCountry(x, y);
		
		if(country == null){
			placeables.remove(rocket);
			new Dialog("Invalid country clicked", skin, "dialog") {
			}.text("Please try again, an invalid country was clicked. (you may have clicked on water or a border)").button("    Exit    ", true).key(Keys.ENTER, true).key(
					Keys.ESCAPE, true).show(stage);
			
			return;
		}
		
		if(!country.getOwner().equals(username)){
			//cancel
			placeables.remove(rocket);
			new Dialog("You do not own this country", skin, "dialog") {
			}.text("You are only able to place items on countries that you own!").button("    Exit    ", true).key(Keys.ENTER, true).key(
					Keys.ESCAPE, true).show(stage);
			
			return;
		}
		
		Label label = (Label)militaryDialog.getContentTable().findActor("Cruise Missile-amount label");
		label.setText(amount + "/" + AttackType.CRUISE_MISSILE.getMaxAmount());
		rocket.sendToServer();
		//TODO: deduct players money
	}

	@Override
	public void sellRocket(int x, int y, MilitaryDialog militaryDialog) {
		boolean somethingClicked = false;
		Placeable placeable = null;
		for(Placeable p : placeables){
			if(Utils.clickedPosInBox(x, y, 22, 22, p.getX(), p.getY())){
				placeables.remove(p);
				//tell server this is removed.
				somethingClicked = true;
				placeable = p;
				break;
			}
		}
		
		if(!somethingClicked) return;
		int amount = 0;
		for(Placeable p : placeables){
			if(p.getType().equals(AttackType.CRUISE_MISSILE) && p.isInGame() && p.getOwner().equals(username) ){
				amount++;
			}
		}
		Label label = (Label)militaryDialog.getContentTable().findActor("Cruise Missile-amount label");
		label.setText(amount + "/" + AttackType.CRUISE_MISSILE.getMaxAmount());
		placeable.setOwner("!@!MARKED_FOR_DEATH");
		
		client.sendTCP(Placeable.convertToRawPlaceable(placeable));
		
		//TODO: return half of the players money
	}
	
	public void rocketAttack(int x, int y){
		//TODO: make sure player has more than one rocket before attacking
		Country c = determineCountry(x, y);
		if(c == null){
			new Dialog("Invalid country clicked", skin, "dialog") {
			}.text("Please try again, an invalid country was clicked. (you may have clicked on water or a border)").button("    Exit    ", true).key(Keys.ENTER, true).key(
					Keys.ESCAPE, true).show(stage);
			return;
		}else if(c.getOwner().equals("Unowned")){
			new Dialog("Unowned country...", skin, "dialog") {
			}.text("You cannot attack a country that is unowned!!").button("    Exit    ", true).key(Keys.ENTER, true).key(
				Keys.ESCAPE, true).show(stage);
			return;
		}else if(c.getOwner().equals(username)){
			new Dialog("You own this country...", skin, "dialog") {
			}.text("You cannot attack your own country!").button("    Exit    ", true).key(Keys.ENTER, true).key(
					Keys.ESCAPE, true).show(stage);
			return;
		}
		
		//TODO: change to rockets actual strength
		//send to ze server
		client.sendTCP(GameUpdates.countryLooseHealth(playableGame.getId(), c.getName(), 5));
		
	}
	
	private Country determineCountry(int screenX, int screenY) {
		Vector3 color = getRGBValues(screenX, screenY);

		for (Country c : playableGame.getCountries().values()) {
			if (c.getR() == color.x && c.getG() == color.y && c.getB() == color.z) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Gets the RGB values of the clicked pixel
	 * 
	 * @param screenX
	 *            X clicked position
	 * @param screenY
	 *            Y clicked position
	 * @return Vector3f of the RGB values.
	 */
	private Vector3 getRGBValues(int screenX, int screenY) {
		Vector2 clickedPos = Utils.convertToMapCordinates(screenX, screenY, camera);
		float newY = colorMap.getHeight() - clickedPos.y;
		int value = colorMap.getPixel((int) clickedPos.x, (int) newY);
		int R = ((value & 0xff000000) >>> 24);
		int G = ((value & 0x00ff0000) >>> 16);
		int B = ((value & 0x0000ff00) >>> 8);

		return new Vector3(R, G, B);
	}
}