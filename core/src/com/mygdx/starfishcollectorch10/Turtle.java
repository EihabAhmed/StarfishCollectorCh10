package com.mygdx.starfishcollectorch10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Turtle extends BaseActor {

    private boolean dead;
    private boolean win;

    public Turtle(float x, float y, Stage s) {
        super(x, y, s);

        String[] fileNames = { "turtle-1.png", "turtle-2.png", "turtle-3.png",
                        "turtle-4.png", "turtle-5.png", "turtle-6.png" };

        loadAnimationFromFiles(fileNames, 0.1f, true);

        setAcceleration(400);
        setMaxSpeed(100);
        setDeceleration(400);

        setBoundaryPolygon(8);
    }

    public void act(float dt) {
        super.act(dt);

        if (dead || win) {
            return;
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT))
            accelerateAtAngle(180);
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            accelerateAtAngle(0);
        if (Gdx.input.isKeyPressed(Keys.UP))
            accelerateAtAngle(90);
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            accelerateAtAngle(270);

        applyPhysics(dt);

        setAnimationPaused(!isMoving());

        if (getSpeed() > 0)
            setRotation(getMotionAngle());

        boundToWorld();
        alignCamera();
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isWin() {
        return win;
    }

    public void die() {
        dead = true;
        setAnimationPaused(true);
        addAction(Actions.fadeOut(1));
        addAction(Actions.after(Actions.removeActor()));
    }

    public void stop() {
        win = true;
        setAnimationPaused(true);
    }
}
