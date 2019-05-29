package com.neet.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.neet.entities.Asteroid;
import com.neet.game.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Save;

public class MenuState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();
	private final String title = "Asteroids";
	
	private int currentItem;
	private String[] menuItems;
	
	private ArrayList<Asteroid> asteroids;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
			Gdx.files.internal("fonts/Hyperspace Bold.ttf")
		);
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 56;
		titleFont = gen.generateFont(parameter);
		titleFont.setColor(Color.WHITE);
		parameter.size = 20;
		font = gen.generateFont(parameter);
		
		menuItems = new String[] {
			"Play",
			"Highscores",
			"Quit"
		};
		
		asteroids = new ArrayList<Asteroid>();
		for(int i = 0; i < 6; i++) {
			asteroids.add(
				new Asteroid(
					MathUtils.random(Game.WIDTH),
					MathUtils.random(Game.HEIGHT),
					Asteroid.LARGE
				)
			);
		}
		
		Save.load();
		
	}
	
	public void update(float dt) {
		
		handleInput();
		
		for(int i = 0 ; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
		}
		
	}
	
	public void draw() {
		
		sb.setProjectionMatrix(Game.cam.combined);
		sr.setProjectionMatrix(Game.cam.combined);
		
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
		sb.begin();
		layout.setText(titleFont, title);
		float width = layout.width;
		titleFont.draw(
			sb,
			title,
			(Game.WIDTH - width) / 2,
			300
		);
		
		for(int i = 0; i < menuItems.length; i++) {
			layout.setText(font,menuItems[i]);
			width = layout.width;
			if(currentItem == i) font.setColor(Color.RED);
			else font.setColor(Color.WHITE);
			font.draw(
				sb,
				menuItems[i],
				(Game.WIDTH - width) / 2,
				180 - 35 * i
			);
		}
		
		sb.end();
		
	}
	
	public void handleInput() {
		
		if(GameKeys.isPressed(GameKeys.UP)) {
			if(currentItem > 0) {
				currentItem--;
			}
		}
		if(GameKeys.isPressed(GameKeys.DOWN)) {
			if(currentItem < menuItems.length - 1) {
				currentItem++;
			}
		}
		if(GameKeys.isPressed(GameKeys.ENTER)) {
			select();
		}
		
	}
	
	private void select() {
		if(currentItem == 0) {
			gsm.setState(GameStateManager.PLAY);
		}
		else if(currentItem == 1) {
			gsm.setState(GameStateManager.HIGHSCORE);
		}
		else if(currentItem == 2) {
			Gdx.app.exit();
		}
	}
	
	public void dispose() {
		sb.dispose();
		sr.dispose();
		titleFont.dispose();
		font.dispose();
	}

}










