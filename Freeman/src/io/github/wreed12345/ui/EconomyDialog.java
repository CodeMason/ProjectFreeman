package io.github.wreed12345.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class EconomyDialog extends Dialog{

	private Skin skin;
	private Stage stage;
	public EconomyDialog(Skin skin, Stage stage) {
		super("Economy", skin);
		this.skin = skin;
		setupUI();
		
		//super.button("   Exit   ", true).
		super.key(Keys.ENTER, true).key(Keys.ESCAPE, true);
	}
	
	//right buttons
	private TextButton currentNewsButton, portfolioButton;
	private SplitPane splitPane;
	private HorizontalGroup horizontalGroup;
	
	private void setupUI(){
		horizontalGroup = new HorizontalGroup();
		stockMarketButton = new TextButton("Stock Market", skin);
		importsExportsButton = new TextButton("Imports / Exports", skin);
		currentNewsButton = new TextButton("Current News", skin);
		portfolioButton = new TextButton("Portfolio", skin);
		
		//TODO: dose listeners doe
		horizontalGroup.addActor(stockMarketButton);
		horizontalGroup.addActor(importsExportsButton);
		horizontalGroup.addActor(currentNewsButton);
		horizontalGroup.addActor(portfolioButton);
		
		setupStockMarket();
		setupCurrentNews();
		
		
		splitPane = new SplitPane(leftRowTable, rightRowTable, false, skin);
		getContentTable().add(horizontalGroup).row();
		getContentTable().add(splitPane).width(480);
	}
	
	private TextButton stockMarketButton, importsExportsButton;
	private ScrollPane stockMarketScrollPane;
	private Table leftTable;
	private RowTable leftRowTable;
	
	//will be called more than once
	private void setupStockMarket(){
		leftTable = new Table(skin);
		leftRowTable = new RowTable(4);
		
		leftRowTable.add(new Label("Goldspring Stock Exchange", skin), 1);
		
		leftTable.add("Company");
		leftTable.add("Price per share");
		leftTable.add("Buy");
		leftTable.add("Sell").row();
		
		addStock("GoldSpring Games", 500.24);
		addStock("Apples", 124.45);
		addStock("Microsofts", 500.24);
		addStock("G00gles", 500.24);
		addStock("Spooderman Corperation", 51200.24);
		addStock("No Sweg", 196234.24);
		addStock("Dell", 234.24);
		addStock("Panasonic", 45.24);
		addStock("Acer", 344.24);
		addStock("Hewlett Packer", 272.24);
		addStock("Dunder Mifflin", 781.24);
		
		stockMarketScrollPane = new ScrollPane(leftTable);
		leftRowTable.add(stockMarketScrollPane, 2);
	}
	
	private RowTable rightRowTable;
	
	private ScrollPane newsScrollPane;
	private List list;
	private void setupCurrentNews(){
		rightRowTable = new RowTable(1);
		list = new List(new String[] {"derp 1", "odoyle", "james", "is", "a", "goon"}, skin);
		newsScrollPane = new ScrollPane(list);
		
		rightRowTable.add(newsScrollPane, 0);
		
	}
	
	private void addStock(String company, double pricePerShare){
		leftTable.add(company);
		leftTable.add(String.valueOf(pricePerShare));
		
		//TODO: click listeners
		leftTable.add(new TextButton("Buy", skin));
		leftTable.add(new TextButton("Sell", skin)).row();
	}

}
