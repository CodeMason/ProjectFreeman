package io.github.wreed12345.ui;

import io.github.wreed12345.shared.countries.Country;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CountryDialog extends Dialog{

	/**
	 * Creates a new Country Dialog
	 * @param country country object for this dialog
	 * @param skin skin in use
	 */
	public CountryDialog(Country country, Skin skin) {
		super(country.getName(), skin);
		
		super.button("   Exit   ", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true);
		
		Label informationLabel = new Label("Information:\n" + country.getInfo(), skin);
		informationLabel.setWidth(300);
		informationLabel.setWrap(true);
		getContentTable().add(informationLabel).prefWidth(300).row();
		
		Label countryLabel = new Label("Owner: " + country.getOwner(), skin);
		countryLabel.setWidth(300);
		getContentTable().add(countryLabel).prefWidth(300).row();
		
		getContentTable().add("Health: " + country.getHealth() + "/100");
	}

}
