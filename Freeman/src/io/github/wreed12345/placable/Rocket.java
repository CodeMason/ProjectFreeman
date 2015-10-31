package io.github.wreed12345.placable;

import io.github.wreed12345.shared.placeable.AttackType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;

public class Rocket extends LibgdxPlaceable {
	private static final long serialVersionUID = 2188880496962229881L;
	private transient SpriteBatch batch;

	public Rocket(Texture icon, String owner, SpriteBatch batch, OrthographicCamera camera, long gameID,
			Client client) {
		setPrice(AttackType.CRUISE_MISSILE.getCost());
		setCooldown(AttackType.CRUISE_MISSILE.getCoolDown());
		setDamage(5);
		setOwner(owner);
		setType(AttackType.CRUISE_MISSILE);
		setGameID(gameID);
		setId(Long.toHexString(Double.doubleToLongBits(Math.random())));

		addObjects(icon, camera, client);

		this.batch = batch;
		this.camera = camera;
		this.client = client;
		this.icon = icon;
		
		smoke.load(Gdx.files.internal("data/GameScreen/images/smoke.p"), Gdx.files.internal("data/GameScreen/images/"));
		smoke.setPosition(100, 100);
		smoke.start();
	}

	@Override
	public void draw() {
		batch.draw(icon, getX(), getY());
		
		smoke.start();
		smoke.draw(batch);
		smoke.setPosition(100, 100);
		smoke.update(Gdx.graphics.getDeltaTime());
	}

	ParticleEffect smoke = new ParticleEffect();
	public void spawnRocket(Vector2 startLocation, Vector2 stopLocation) {
		//animation stuffz
	}
}
