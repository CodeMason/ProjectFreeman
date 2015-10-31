package io.github.wreed12345.ui;

import io.github.wreed12345.shared.placeable.AttackType;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class PlaceableInfoDialog extends Dialog{

	public PlaceableInfoDialog(Skin skin, AttackType attackType) {
		super(attackType.getName(), skin);
		super.button("   Exit   ", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true);
		
		addContent(attackType, skin);
	}
	
	private void addContent(AttackType attackType, Skin skin){
		getContentTable().add("Cost: " + attackType.getCost()).row();
		getContentTable().add("Cooldown: " + attackType.getCoolDown()).row();
		getContentTable().add("Price: " + attackType.getCost()).row();
		
		Label description  = new Label("Description: " + attackType.getDescription(), skin);
		getContentTable().add(description).align(Align.left).prefWidth(200);
		description.setWrap(true);
	}
	
}
