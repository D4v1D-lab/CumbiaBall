package com.mygdx.game.scene2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.BaseScreen;
import com.mygdx.game.MainGame;

public class Scene2DScreen extends BaseScreen {
    public Scene2DScreen(MainGame game) {
        super(game);
        texturePlayer = new Texture("player5.png");
        textureSpikes = new Texture("spikes1.png");
        spikesRegion = new TextureRegion(textureSpikes);
    }

    private Stage stage;
    private PlayerActor player;
    private SpikesActor spikes;
    private Texture texturePlayer, textureSpikes;
    private TextureRegion spikesRegion;


    @Override
    public void show() {
        stage = new Stage();
        stage.setDebugAll(true);
        player = new PlayerActor(texturePlayer);
        spikes = new SpikesActor(spikesRegion);
        stage.addActor(player);
        stage.addActor(spikes);

        player.setPosition(20,100);
        spikes.setPosition(500, 100);

    }

    @Override
    public void hide() {
       stage.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        collisionCheck();
        stage.draw();
    }
    private void collisionCheck(){
        if (player.isAlive() && (player.getX() + player.getWidth() > spikes.getX()))
        {
            System.out.println("Collision");
            player.setAlive(false);
        }
    }

    @Override
    public void dispose() {
        texturePlayer.dispose();
    }
}
