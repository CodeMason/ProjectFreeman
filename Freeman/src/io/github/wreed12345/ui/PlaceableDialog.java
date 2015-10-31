package io.github.wreed12345.ui;

import io.github.wreed12345.input.FutureClickHandler;
import io.github.wreed12345.input.InputQueue;
import io.github.wreed12345.shared.placeable.Placeable;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PlaceableDialog extends Dialog{
	
	private String playerName;
	
	/**
	 * Creates a new Placeable Dialog
	 * @param country country object for this dialog
	 * @param skin skin in use
	 */
	public PlaceableDialog(Placeable placeable, final Skin skin, final Stage stage) {
		super(placeable.getType().toString(), skin);
		getUsername();
		
		super.button("   Exit   ", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true);
		
		Label countryLabel = new Label("Owner: " + placeable.getOwner(), skin);
		countryLabel.setWidth(300);
		getContentTable().add(countryLabel).prefWidth(300).row();
		
		getContentTable().add(new Label("Cooldown: " + placeable.getCooldown() + "s", skin)).row();
		
		if(placeable.getOwner().equals(playerName)){
			final PlaceableDialog dialog = this;
			TextButton attack = new TextButton(" Attack ", skin);
			getContentTable().add(attack);
			
			attack.addListener(new ClickListener(){
				public void clicked(InputEvent e, float x, float y){
					dialog.hide();
					
					final TextField clickReminder = new TextField("Click the country you wish to attack", skin);
					clickReminder.setPosition(400, 32);
					stage.addActor(clickReminder);
					
					//add to queue
					InputQueue.inputQueue.getClickQueue().add(new FutureClickHandler(){
						@Override
						public void futureClick(float x, float y){
							clickReminder.remove();
							//TODO: determine which country is clicked, and inflict damage there.
							
						}
					});
				}
			});
		}
		
		super.show(stage);
	}
	
	private void getUsername(){
		Preferences prefs = Gdx.app.getPreferences("freeman.dat");
		playerName = prefs.getString("username");
	}
}
