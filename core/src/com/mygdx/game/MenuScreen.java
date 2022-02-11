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

public class MenuScreen extends BaseScreen{

    private Image background;
    private Image intro;

    private Stage stage;
    private Texture myTexture;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTexRegionDrawable;
    private ImageButton pbutton;

    public MenuScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FitViewport(640, 360));
        // Make a texture out of the image
        myTexture = new Texture(Gdx.files.internal("plybtn.png"));
        myTextureRegion = new TextureRegion(myTexture);
        myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        pbutton = new ImageButton(myTexRegionDrawable);
        //Set Images on screen
        background = new Image(game.getManager().get("bg.png", Texture.class));
        intro = new Image(game.getManager().get("intro.png", Texture.class));

        //Set the button up

        pbutton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.gameScreen);

            }
        });
        // Set Positions
        background.setPosition(0,0);
        pbutton.setPosition(320 - pbutton.getWidth()/2, 160 - pbutton.getHeight()/2);
        intro.setPosition(320 - intro.getWidth()/2, 250 - intro.getHeight()/2);

        // Show them in screen
        stage.addActor(background);
        stage.addActor(intro);
        stage.addActor(pbutton);

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

