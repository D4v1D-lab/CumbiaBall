package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Box2DScreen extends BaseScreen {
    public Box2DScreen(MainGame game) {
        super(game);
    }

    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    private Body playerBody, groundBody, spikesBody;

    private Fixture playerFixture, groundFixture, spikesFixture;

    private boolean CollisionPush, Collision, playerAlive = true;

    @Override
    public void show() {
        //Preparing the view
        world = new World(new Vector2(0, -10), true);
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16, 9);
        camera.translate(0,1);
        //Collision manager
        world.setContactListener(new ContactListener() {
            //Collision and jump physics
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
                if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor")) ||
                        (fixtureA.getUserData().equals("floor") && fixtureB.getUserData().equals("player"))){
                    if(Gdx.input.isTouched()){
                     CollisionPush = true;
                    }
                        Collision = false;
                }
                if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor")) ||
                        (fixtureA.getUserData().equals("floor") && fixtureB.getUserData().equals("player"))){
                        playerAlive = false;
                    }
                Collision = false;
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
        //Entities
        playerBody = world.createBody(createBodyDef());
        groundBody = world.createBody(createGroundBodyDef());
        spikesBody = world.createBody(createSpikesBodyDef(0.5f));
        //Player constructor fixture
        PolygonShape playerShape = new PolygonShape();
        //Player area and density
        playerShape.setAsBox(0.5f, 0.5f);
        playerFixture = playerBody.createFixture(playerShape, 1);
        playerFixture.setUserData("player");
        playerShape.dispose();

        //Ground constructor fixture
        PolygonShape groundShape = new PolygonShape();
        //Ground area and density
        groundShape.setAsBox(500, 1);
        groundFixture = groundBody.createFixture(groundShape, 1);
        groundFixture.setUserData("floor");
        groundShape.dispose();

        //Spikes fixture
        spikesFixture = createSpikesFixture(spikesBody);

        //Objects linked to a char
        playerFixture.setUserData("player");
        groundFixture.setUserData("floor");
        spikesFixture.setUserData("spikes");
//        //Spikes constructor
//        PolygonShape spikesShape = new PolygonShape();
//        //Spikes area and density
//        spikesShape.setAsBox(0.5f, 0.5f);
//        spikesFixture = spikesBody.createFixture(spikesShape, 1);
//        spikesShape.dispose();
    }
    //Entities positions
    //Spikes Body
    private BodyDef createSpikesBodyDef(float x) {
        BodyDef def = new BodyDef();
        //Spike position
        def.position.set(x, 0.5f);
        return def;
    }

    private BodyDef createGroundBodyDef() {
        BodyDef def = new BodyDef();
        //Floor position
        def.position.set(0, -1);
        return def;
    }

    private BodyDef createBodyDef() {
        BodyDef def = new BodyDef();
        //Player position
        def.position.set(-5, 0.5f);
        //Dynamic bodies are not set by default
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    private Fixture createSpikesFixture(Body spikesBody) {
        //Drawing the spikes
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(0.5f,-0.5f);
        vertices[2] = new Vector2(0, 0.5f);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        Fixture fix = spikesBody.createFixture(shape, 1);
        shape.dispose();
        return fix;
    }

    @Override
    public void dispose() {
        //Disposing memory space to avoid future errors
        spikesBody.destroyFixture(spikesFixture);
        playerBody.destroyFixture(playerFixture);
        groundBody.destroyFixture(groundFixture);

        world.destroyBody(playerBody);
        world.dispose();
        world.destroyBody(spikesBody);

        renderer.dispose();
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //jump input
        if(CollisionPush){
            // Jump once in collisions
            CollisionPush = false;
            jump();
        }
        if (Gdx.input.justTouched() && !Collision){
            CollisionPush = true;
        }
        if (playerAlive){
        //Player speed and motion
            float speedY = playerBody.getLinearVelocity().y;
            playerBody.setLinearVelocity(3, speedY);
        }
       world.step(delta, 6, 2);
        // What player sees
        camera.update();
        renderer.render(world, camera.combined);
    }
    //Linear impulse
    public void jump() {
        Vector2 position = playerBody.getPosition();
        playerBody.applyLinearImpulse(0, 5, position.x, position.y, true);
    }
}
