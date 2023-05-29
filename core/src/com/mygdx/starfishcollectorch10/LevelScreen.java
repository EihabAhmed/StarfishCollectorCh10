package com.mygdx.starfishcollectorch10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelScreen extends BaseScreen {
    private Turtle turtle;

    private boolean win;

    private Label starfishLabel;

    private DialogBox dialogBox;

    private float audioVolume;
    private Sound waterDrop;
    private Music instrumental;
    private Music oceanSurf;

    public void initialize() {
        camera.setToOrtho(false, 800, 600);

        BaseActor ocean = new BaseActor(0, 0, mainStage);
        ocean.loadTexture("water-border.jpg");
        ocean.setSize(1200, 900);

        BaseActor.setWorldBounds(ocean);

        new Starfish(400, 400, mainStage);
        new Starfish(500, 100, mainStage);
        new Starfish(100, 450, mainStage);
        new Starfish(200, 250, mainStage);
        new Starfish(500, 500, mainStage);
        new Starfish(700, 700, mainStage);

        new Rock(200, 150, mainStage);
        new Rock(100, 300, mainStage);
        new Rock(300, 350, mainStage);
        new Rock(450, 200, mainStage);
        new Rock(100, 700, mainStage);

        turtle = new Turtle(20, 20, mainStage);

        win = false;

        starfishLabel = new Label("Starfish Left: ", BaseGame.labelStyle);
        starfishLabel.setColor(Color.CYAN);

        ButtonStyle buttonStyle = new ButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("undo.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);

        Button restartButton = new Button(buttonStyle);
        restartButton.setColor(Color.CYAN);

        restartButton.addListener(
                (Event e) -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    instrumental.dispose();
                    oceanSurf.dispose();

                    StarfishCollectorGame.setActiveScreen(new LevelScreen());
                    return true;
                }
        );

        ButtonStyle buttonStyle2 = new ButtonStyle();

        Texture buttonTex2 = new Texture(Gdx.files.internal("audio.png"));
        TextureRegion buttonRegion2 = new TextureRegion(buttonTex2);
        buttonStyle2.up = new TextureRegionDrawable(buttonRegion2);

        Button muteButton = new Button(buttonStyle2);
        muteButton.setColor(Color.CYAN);

        muteButton.addListener(
                (Event e) -> {
                    if (!isTouchDownEvent(e))
                        return false;

                    audioVolume = 1 - audioVolume;
                    instrumental.setVolume(audioVolume);
                    oceanSurf.setVolume(audioVolume);

                    return true;
                }
        );

        uiTable.pad(10);
        uiTable.add(starfishLabel).top();
        uiTable.add().expandX().expandY();
        uiTable.add(muteButton).top();
        uiTable.add(restartButton).top();

        Sign sign1 = new Sign(20, 400, mainStage);
        sign1.setText("West Starfish Bay");

        Sign sign2 = new Sign(600, 300, mainStage);
        sign2.setText("East Starfish Bay");

        dialogBox = new DialogBox(0, 0, uiStage);
        dialogBox.setBackgroundColor(Color.TAN);
        dialogBox.setFontColor(Color.BROWN);
        dialogBox.setDialogSize(600, 100);
        dialogBox.setFontScale(0.80f);
        dialogBox.alignCenter();
        dialogBox.setVisible(false);

        uiTable.row();
        uiTable.add(dialogBox).colspan(4);

        waterDrop = Gdx.audio.newSound(Gdx.files.internal("Water_Drop.ogg"));
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Master_of_the_Feast.ogg"));
        oceanSurf = Gdx.audio.newMusic(Gdx.files.internal("Ocean_Waves.ogg"));

        audioVolume = 1.00f;
        instrumental.setLooping(true);
        instrumental.setVolume(audioVolume);
        instrumental.play();
        oceanSurf.setLooping(true);
        oceanSurf.setVolume(audioVolume);
        oceanSurf.play();
    }

    public void update(float dt) {

        for (BaseActor rockActor : BaseActor.getList(mainStage, "com.mygdx.starfishcollectorch10.Rock"))
            turtle.preventOverlap(rockActor);

        for (BaseActor starfishActor : BaseActor.getList(mainStage, "com.mygdx.starfishcollectorch10.Starfish")) {
            Starfish starfish = (Starfish) starfishActor;
            if (turtle.overlaps(starfish) && !starfish.isCollected()) {
                starfish.collect();

                waterDrop.play(audioVolume);

                Whirlpool whirl = new Whirlpool(0, 0, mainStage);
                whirl.centerAtActor(starfish);
                whirl.setOpacity(0.25f);
            }
        }

        if (BaseActor.getList(mainStage, "com.mygdx.starfishcollectorch10.Starfish").size() == 0 && !win) {
            win = true;
            turtle.stop();

            BaseActor levelCompletedMessage = new BaseActor(0, 0, uiStage);
            levelCompletedMessage.loadTexture("message-continue.png");
            levelCompletedMessage.centerAtPosition(400, 300);
            levelCompletedMessage.setOpacity(0);
            levelCompletedMessage.addAction(Actions.delay(1));
            levelCompletedMessage.addAction(Actions.after(Actions.fadeIn(1)));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.C) && win) {
            instrumental.dispose();
            oceanSurf.dispose();

            StarfishCollectorGame.setActiveScreen(new LevelScreen2());
        }

        starfishLabel.setText("Starfish Left: " + BaseActor.getList(mainStage, "com.mygdx.starfishcollectorch10.Starfish").size());

        for (BaseActor signActor : BaseActor.getList(mainStage, "com.mygdx.starfishcollectorch10.Sign")) {
            Sign sign = (Sign) signActor;

            turtle.preventOverlap(sign);
            boolean nearby = turtle.isWithinDistance(4, sign);

            if (nearby && !sign.isViewing()) {
                dialogBox.setText(sign.getText());
                dialogBox.setVisible(true);
                sign.setViewing(true);
            }

            if (!nearby && sign.isViewing()) {
                dialogBox.setText(" ");
                dialogBox.setVisible(false);
                sign.setViewing(false);
            }
        }
    }
}
