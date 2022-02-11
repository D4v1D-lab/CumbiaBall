package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.FloorEntity;
import com.mygdx.game.entities.LevelEntity;
import com.mygdx.game.entities.PlayerEntity;
import com.mygdx.game.entities.SpikeEntity;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends BaseScreen {
    private Stage stage;
    private World world;
    private PlayerEntity player;
    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();
    private List<SpikeEntity> spikeList = new ArrayList<SpikeEntity>();
    private List<LevelEntity> levelList = new ArrayList<LevelEntity>();
    private Sound jumpSound, deathSound;
    private Music bgMusic;
    public Image bground;
    SpriteBatch batch;
    Texture img;


    public GameScreen(final MainGame game) {
        super(game);
        //Charge sound effects
        jumpSound = game.getManager().get("jump.mp3");
        deathSound = game.getManager().get("death.mp3");
        bgMusic= game.getManager().get("song1.mp3");
        //Instances
        stage = new Stage(new FitViewport(640, 360));

        world = new World(new Vector2(0, -10), true);

        //bground
//        bground = new Image(game.getManager().get("bground.png", Texture.class));
//        bground.setPosition(0,40);
//        bground.setSize(640, 320);
//        stage.addActor(bground);
        // Background features
        batch = new SpriteBatch();
        img = new Texture("bground.png");


        world.setContactListener(new ContactListener() {

            private boolean areCollided(Contact contact, Object userA, Object userB){
                return contact.getFixtureA().getUserData().equals(userA) && contact.getFixtureB().getUserData().equals(userB) ||
                        contact.getFixtureA().getUserData().equals(userB) && contact.getFixtureB().getUserData().equals(userA);

            }

            @Override
            public void beginContact(Contact contact) {
                if (areCollided(contact, "player", "floor")) {
                    player.setJumping(false);
                    if (Gdx.input.isTouched()) {
                        player.setMustJump(true);
                    }
                }
                // What happens when we fail
                if (areCollided(contact, "player", "spike")) {
                    if (player.isAlive()) {
                        player.setAlive(false);
                        //Sounds effects when the player dies
                        deathSound.play(0.40f);
                        bgMusic.stop();

                                stage.addAction(
                                        Actions.sequence(
                                                Actions.delay(2.85f),
                                                Actions.run(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        game.setScreen(game.gameOverScreen);
                                                    }
                                                })
                                        )
                                );
                    }
                }
                if (areCollided(contact, "player", "finish")) {
                    if (player.isAlive()) {
                        player.setAlive(false);
                        //Sounds effects when the player dies

                        stage.addAction(
                                Actions.sequence(
                                        Actions.delay(1.5f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                game.setScreen(game.gameScreen2);
                                            }
                                        })
                                )
                        );
                    }
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

    }

    @Override
    public void show() {
        Texture playerTexture = game.getManager().get("ballgamer.png");
        Texture floorTexture = game.getManager().get("floor.png");
        Texture overfloorTexture = game.getManager().get("overfloor.png");
        Texture spikesTexture = game.getManager().get("spikes1.png");
        Texture finishTexture = game.getManager().get("finish.png");
        player = new PlayerEntity(world, playerTexture, new Vector2(1.5f, 1.5f));

        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 0, 1000, 1));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 12, 10, 2));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 32, 5, 1.5f));
        floorList.add(new FloorEntity(world, floorTexture, finishTexture, 105, 10, 1.5f));
        levelList.add(new LevelEntity(world, finishTexture, 105, 10, 1.6f));
        spikeList.add(new SpikeEntity(world, spikesTexture, 6, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 18, 2));
        spikeList.add(new SpikeEntity(world, spikesTexture, 26, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 38, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 47, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 56, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 65, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 74, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 83, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 92, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 101, 1));



        stage.addActor(player);
        for (FloorEntity floor : floorList) {
            stage.addActor(floor);
        }
        for (SpikeEntity spike : spikeList) {
            stage.addActor(spike);
        }
        for (LevelEntity finish : levelList) {
            stage.addActor(finish);
        }

        bgMusic.play();
    }

    @Override
    public void hide() {
        jumpSound.stop();
        deathSound.stop();
        bgMusic.stop();
        player.detach();
        player.remove();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 2, 0);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, stage.getWidth(), stage.getHeight());
        batch.end();
        //Camera movement

        if (player.getX() > 150 && player.isAlive()) {
            stage.getCamera().translate(Constants.PLAYER_SPEED * delta * Constants.PIXELS_IN_METERS, 0,0);
        }

        if (Gdx.input.isTouched() && player.isAlive()){
            jumpSound.play();
            player.jump();
        }
        //Gravity laws
        stage.act();
        world.step(delta, 6,2);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();

        for (FloorEntity floor : floorList) {
            floor.detach();
            floor.remove();

        }
        for (SpikeEntity spike : spikeList) {
            spike.detach();
            spike.remove();
        }
    }
}
