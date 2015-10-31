package io.github.wreed12345.ui;

import io.github.wreed12345.placable.PlaceableCreatorAndDestroyer;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TopMenu extends Window{
	
	private Stage stage;
	private final MilitaryDialog militaryDialog;
	private final EconomyDialog economyDialog;
	
	public TopMenu(Stage stage, Skin skin, Texture flag, PlaceableCreatorAndDestroyer placeableCreatorAndDestroyer, OrthographicCamera camera){
		super("Freeman", skin);
		this.stage = stage;
		stage.addActor(this);
		addActors(skin, flag);
		militaryDialog = new MilitaryDialog(skin, stage, placeableCreatorAndDestroyer, camera);
		economyDialog = new EconomyDialog(skin, stage);
		
		setPosition(0, Gdx.graphics.getHeight() - getHeight());
		stage.addActor(this);
	}
	
	private Queue<String> eventQueue = new LinkedList<String>();
	
	public void update(){
		
	}
	
	private void addActors(Skin skin, Texture flag){
		setupFlag(flag);
		setupEventLabel(skin);
		buttons(skin);
		
		setWidth(600);
	}
	
	private Image flagImage;
	private void setupFlag(Texture flag){
		flagImage = new Image(flag);
		add(flagImage).padRight(20);
	}
	
	private Label eventLabel;
	private void setupEventLabel(Skin skin){
		eventLabel = new Label("Events will appear here...", skin);
		add(eventLabel).padRight(20).width(250);
	}
	
	private TextButton militaryButton, economyButton;
	private void buttons(Skin skin){
		militaryButton = new TextButton("Military", skin);
		militaryButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				militaryDialog.show(stage);
				militaryDialog.setWidth(400);
				militaryDialog.setHeight(300);
			}
		});
		
		add(militaryButton).padRight(20);
		
		economyButton = new TextButton("Economy", skin);
		economyButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				economyDialog.show(stage);
				economyDialog.setWidth(500);
				economyDialog.setHeight(400);
			}
		});
		
		add(economyButton).padRight(20);
	}
	
	public MilitaryDialog getMilitaryDialog(){
		return militaryDialog;
	}
}