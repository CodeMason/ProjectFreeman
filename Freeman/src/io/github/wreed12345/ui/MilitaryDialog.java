package io.github.wreed12345.ui;

import io.github.wreed12345.input.FutureClickHandler;
import io.github.wreed12345.input.InputQueue;
import io.github.wreed12345.placable.PlaceableCreatorAndDestroyer;
import io.github.wreed12345.shared.placeable.AttackType;
import io.github.wreed12345.util.Utils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MilitaryDialog extends Dialog{

	private Skin skin;
	private Stage stage;
	private static final String TITLE = "Military";
	private PlaceableCreatorAndDestroyer placeableCreatorAndDestroyer;
	private OrthographicCamera camera;
	
	public MilitaryDialog(Skin skin, Stage stage, PlaceableCreatorAndDestroyer placeableCreatorAndDestroyer, OrthographicCamera camera) {
		super(TITLE, skin);
		super.button("   Exit   ", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true);
		this.skin = skin;
		this.stage = stage;
		this.placeableCreatorAndDestroyer = placeableCreatorAndDestroyer;
		this.camera = camera;
		
		addLabels();
		addScrollingPane();
	}
	
	private Label empirePowerLabel, weaponsAndDefense;
	private void addLabels(){
		empirePowerLabel = new Label("Empire Power: #", skin);
		getContentTable().add(empirePowerLabel).row();
		
		weaponsAndDefense = new Label("Weapons and Defense", skin);
		getContentTable().add(weaponsAndDefense).row();
	}
	
	private ScrollPane weaponsAndDefensePane;
	private Table weaponsTable;
	private void addScrollingPane(){
		weaponsTable = new Table(skin);
		addWeapons();
		
		weaponsAndDefensePane = new ScrollPane(weaponsTable);
		getContentTable().add(weaponsAndDefensePane);
	}
	
	/**Add list of weapons to table*/
	private void addWeapons(){
		weaponsTable.add("Name").padRight(4);
		weaponsTable.add("Amount").padRight(4);
		weaponsTable.add("Buy").padRight(4);
		weaponsTable.add("Sell").padRight(4);
		weaponsTable.add("Deploy").padRight(4).row();
		
		addItemWithListeners(50, AttackType.CRUISE_MISSILE);
	}
		
	/**
	 * Adds a row of weapon stuff to the table which scrolls in this dialog
	 * @param name Name of the item
	 * @param maxAmount Maximum amount allowed
	 */
	private void addItemWithListeners(int maxAmount, final AttackType attackType){
		Label nameLabel = new Label(attackType.getName(), skin);
		weaponsTable.add(nameLabel).padRight(4);
		nameLabel.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				PlaceableInfoDialog piDialog = new PlaceableInfoDialog(skin, attackType);
				piDialog.show(stage);
				piDialog.setWidth(300);
				piDialog.setHeight(300);
			}
		});
		
		Label amountLabel = new Label("0/" + maxAmount, skin);
		amountLabel.setName(attackType.getName() + "-amount label");
		weaponsTable.add(amountLabel).padRight(4);
		
		//TODO: change to support more then just a rocket.
		TextButton buyButton = new TextButton("Buy", skin);
		weaponsTable.add(buyButton).padRight(4);
		final MilitaryDialog militaryDialog = this;
		buyButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				final Label clickWarnLabel = new Label("Click where you would like to place your item.", skin);
				stage.addActor(clickWarnLabel);
				clickWarnLabel.setPosition(400, 20);
				hide();
				InputQueue.inputQueue.getClickQueue().add(new FutureClickHandler(){
					@Override
					public void futureClick(float x, float y){
						placeableCreatorAndDestroyer.createRocket((int)x, (int)y, militaryDialog);
						clickWarnLabel.remove();
					}
				});
			}
		});
		
		TextButton sellButton = new TextButton("Sell", skin);
		weaponsTable.add(sellButton).padRight(4);
		sellButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				final Label clickWarnLabel = new Label("Click the item you wish to sell.", skin);
				stage.addActor(clickWarnLabel);
				clickWarnLabel.setPosition(400, 20);
				hide();
				InputQueue.inputQueue.getClickQueue().add(new FutureClickHandler(){
					@Override
					public void futureClick(float x, float y){
						Vector2 cords = Utils.convertToMapCordinates(x, y, camera);
						placeableCreatorAndDestroyer.sellRocket((int)cords.x, (int)cords.y, militaryDialog);
						clickWarnLabel.remove();
					}
				});
			}
		});
		
		TextButton deployButton = new TextButton("Deploy", skin);
		deployButton.setColor(Color.BLUE);
		weaponsTable.add(deployButton).padRight(4).row();
		deployButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				final Label clickWarnLabel = new Label("Click the country you wish to attack.", skin);
				stage.addActor(clickWarnLabel);
				clickWarnLabel.setPosition(400, 20);
				hide();
				InputQueue.inputQueue.getClickQueue().add(new FutureClickHandler(){
					@Override
					public void futureClick(float x, float y){
						placeableCreatorAndDestroyer.rocketAttack((int)x, (int)y);
						clickWarnLabel.remove();
					}
				});
			}
		});
	}
}