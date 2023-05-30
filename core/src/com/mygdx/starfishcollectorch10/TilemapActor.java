package com.mygdx.starfishcollectorch10;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TilemapActor extends Actor {
    // window dimensions
    public static int windowWidth = 800;
    public static int windowHeight = 600;

    private TiledMap tiledMap;
    private OrthographicCamera tiledCamera;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;

    public TilemapActor(String filename, Stage theStage) {
        // set up tile map, renderer, and camera
        tiledMap = new TmxMapLoader().load(filename);

        int tileWidth = (int) tiledMap.getProperties().get("tilewidth");
        int tileHeight = (int) tiledMap.getProperties().get("tileheight");
        int numTilesHorizontal = (int) tiledMap.getProperties().get("width");
        int numTilesVertical = (int) tiledMap.getProperties().get("height");
        int mapWidth = tileWidth * numTilesHorizontal;
        int mapHeight = tileHeight * numTilesVertical;

        BaseActor.setWorldBounds(mapWidth, mapHeight);

        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
        tiledMapRenderer.setBlending(true);
        tiledCamera = new OrthographicCamera();
        tiledCamera.setToOrtho(false, windowWidth, windowHeight);
        tiledCamera.update();

        theStage.addActor(this);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // adjust tilemap camera to stay in sync with main camera
        Camera mainCamera = getStage().getCamera();
        tiledCamera.position.x = mainCamera.position.x;
        tiledCamera.position.y = mainCamera.position.y;
        tiledCamera.update();
        tiledMapRenderer.setView(tiledCamera);

        // need the following code to force batch order,
        // otherwise it is batched and rendered last
        batch.end();
        tiledMapRenderer.render();
        batch.begin();
    }
}
