package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.mygdx.game.Constants.IMPULSE_JUMP;
import static com.mygdx.game.Constants.PIXELS_IN_METERS;
import static com.mygdx.game.Constants.PLAYER_SPEED;

public class PlayerEntity2 extends Actor {

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean alive = true;
    private boolean jumping = false;
    public static final float PLAYER_SPEED2 = 10f;

    public boolean isMustJump() {
        return mustJump;
    }

    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }

    private boolean mustJump = false;


    public PlayerEntity2(World world, Texture texture, Vector2 position) {
        //Saving instances
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        //Player position
        def.position.set(position);
        //Dynamic bodies are not set by default(player is a dynamic one)
        def.type = BodyDef.BodyType.DynamicBody;
        // Body player creation
        body = world.createBody(def);

        //Player constructor fixture
        PolygonShape playerShape = new PolygonShape();
        //Player area and density
        playerShape.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(playerShape, 1);
        fixture.setUserData("player");
        playerShape.dispose();
        //Static import, size transformation
        setSize(PIXELS_IN_METERS, PIXELS_IN_METERS);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x -0.5f) * PIXELS_IN_METERS,
                (body.getPosition().y - 0.5f) * PIXELS_IN_METERS);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        //Jump if the screen is touched
        if (mustJump) {
            mustJump = false;
            jump();
        }

        if (alive) {
            float speedY = body.getLinearVelocity().y;
            body.setLinearVelocity(PLAYER_SPEED2, speedY);
        }

        if (jumping){
            body.applyForceToCenter(0, -IMPULSE_JUMP * 1.25f, true);
        }

    }

    public void jump() {
        if (!jumping && alive) {
            jumping = true;
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);
        }
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
}
