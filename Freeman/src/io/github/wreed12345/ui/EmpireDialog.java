package io.github.wreed12345.ui;

import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.countries.Country;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EmpireDialog extends Dialog {

	public EmpireDialog(String player, Skin skin, Stage stage, Game game) {
		super(player, skin);
		
		super.button("   Exit   ", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
		setWidth(200);
		setHeight(200);
		
		//determine players countries
		Label countriesLabel = new Label("", skin);
		String countries = "Countries: ";
		
		for(Country c : game.getCountries().values()){
			if(c.getOwner().equalsIgnoreCase(player)){
				countries = countries + " " + c.getName();
			}
		}
		countriesLabel.setText(countries);
		countriesLabel.setWrap(true);
		
		getContentTable().add(countriesLabel).prefWidth(300).row();
		
		Label moneyLabel = new Label("", skin);
		String money = String.valueOf(game.getPlayersMoney().get(player));
		moneyLabel.setText("Money: " + money);
		getContentTable().add(moneyLabel);
		//show persons name, their countries, their money
	}

}
