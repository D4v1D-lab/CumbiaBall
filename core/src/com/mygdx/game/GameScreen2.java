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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.FloorEntity;
import com.mygdx.game.entities.PlayerEntity2;
import com.mygdx.game.entities.SpikeEntity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.ArrayList;
import java.util.List;

public class GameScreen2 extends BaseScreen {
    private Stage stage;
    private World world;
    private PlayerEntity2 player;
    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();
    private List<SpikeEntity> spikeList = new ArrayList<SpikeEntity>();
    private Sound jumpSound, deathSound;
    private Music bgMusic;
    public Image bground;
    SpriteBatch batch;
    Texture img;


    public GameScreen2(final MainGame game) {
        super(game);
        //Charge sound effects
        jumpSound = game.getManager().get("jump.mp3");
        deathSound = game.getManager().get("death.mp3");
        bgMusic= game.getManager().get("song2.mp3");
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
        img = new Texture("bground2.png");


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
        Texture playerTexture = game.getManager().get("player1.png");
        Texture floorTexture = game.getManager().get("floor.png");
        Texture overfloorTexture = game.getManager().get("overfloor.png");
        Texture spikesTexture = game.getManager().get("spikes1.png");
        player = new PlayerEntity2(world, playerTexture, new Vector2(1.5f, 1.5f));

        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 2, 1000, 1));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 14, 10, 2));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 30, 5, 1.5f));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 113, 4, 1.5f));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 124, 4, 1.5f));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 135, 4, 1.5f));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 148, 4, 1.5f));
        floorList.add(new FloorEntity(world, floorTexture, overfloorTexture, 158, 4, 3));
        spikeList.add(new SpikeEntity(world, spikesTexture, 18, 2));
        spikeList.add(new SpikeEntity(world, spikesTexture, 37, 1));
        spikeList.add(new SpikeEntity(world, spikesTexture, 50, 1));
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
            stage.getCamera().translate(Constants.PLAYER_SPEED2 * delta * Constants.PIXELS_IN_METERS, 0,0);
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
