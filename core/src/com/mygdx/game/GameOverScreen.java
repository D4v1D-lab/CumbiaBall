package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameOverScreen extends BaseScreen {

    private Image background;
    private Stage stage;
    private Image gameOver;
//    private Image replay;
    private Texture myTexture;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTexRegionDrawable;
    private ImageButton button;

    public GameOverScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FitViewport(640, 360));
        // Make a texture out of the image
        myTexture = new Texture(Gdx.files.internal("plybtn.png"));
        myTextureRegion = new TextureRegion(myTexture);
        myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        button = new ImageButton(myTexRegionDrawable);

        //Preparing actors
        background = new Image(game.getManager().get("bg.png", Texture.class));
        gameOver = new Image(game.getManager().get("gameover.png", Texture.class));
//        replay = new Image(game.getManager().get("plybtn.png", Texture.class));

         //Set the button up

        button.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.loadingScreen);

            }
        });

        //Giving their positions and sizes
        background.setPosition(0,0);
        gameOver.setPosition(320 - gameOver.getWidth()/2, 180 - gameOver.getHeight()/2);
        button.setPosition(320 - button.getWidth()/2, 60 -button.getHeight()/2);
        //Set them in stage
        stage.addActor(background);
        stage.addActor(gameOver);
        stage.addActor(button);

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}

