package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;


public class MainGame extends Game {
	private AssetManager manager;
	public GameScreen gameScreen;
	public GameOverScreen gameOverScreen;
	public GameScreen2 gameScreen2;
	public  MenuScreen menuScreen;
	public LoadingScreen loadingScreen;
	public AssetManager getManager() {
		return  manager;
	}
	@Override
	public void create() {
		manager = new AssetManager();
		manager.load("floor.png", Texture.class);
		manager.load("spikes1.png", Texture.class);
		manager.load("ballgamer.png", Texture.class);
		manager.load("player1.png", Texture.class);
		manager.load("overfloor.png", Texture.class);
		manager.load("song1.mp3", Music.class);
		manager.load("song2.mp3", Music.class);
		manager.load("jump.mp3", Sound.class);
		manager.load("death.mp3", Sound.class);
		manager.load("gameover.png", Texture.class);
		manager.load("plybtn.png", Texture.class);
		manager.load("bg.png", Texture.class);
		manager.load("bground.png", Texture.class);
		manager.load("bground2.png", Texture.class);
		manager.load("intro.png", Texture.class);
		manager.load("finish.png", Texture.class);

		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);

	}
		public void finishloading() {
			gameScreen = new GameScreen(this);
			gameOverScreen = new GameOverScreen(this);
			gameScreen2 = new GameScreen2(this);
			menuScreen = new MenuScreen(this);
			setScreen(menuScreen);
		}
}
