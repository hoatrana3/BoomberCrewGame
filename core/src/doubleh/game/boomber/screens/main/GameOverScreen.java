package doubleh.game.boomber.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.modes.LevelMode;
import doubleh.game.boomber.screens.BaseScreen;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameFactory;

public class GameOverScreen extends BaseScreen {

    private Stage stage;
    private VerticalGroup gameOverGroup;

    private Label badgesLabel, badgesNameLabel;
    private TextButton buttonBackToMainMenu;

    public GameOverScreen() {
    }

    @Override
    public void show() {
        super.show();

        BoomberGame.getInstance().audios.stopMusic("m_INGAME");
        BoomberGame.getInstance().audios.playMusic((LevelMode.isFinallyWin) ? "m_WIN_MODE" : "m_LOSE_MODE");

        stage = new Stage(new FitViewport(Constants.WIDTH, Constants.HEIGHT));
        gameOverGroup = new VerticalGroup();

        stage.clear();

        Gdx.input.setInputProcessor(stage);

        Texture bgTex = BoomberGame.getInstance().assets.get("img/mainmenubackground.png", Texture.class);
        Image bgImg = new Image(bgTex);

        bgImg.setSize(stage.getWidth(), stage.getHeight());
        bgImg.setZIndex(Constants.Z_SUPER_LOW);
        bgImg.addAction(Actions.alpha(0.75f));

        stage.addActor(bgImg);

        generateBadges();

        stage.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, 1, Interpolation.pow2)
        ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        stage.draw();
    }

    private void generateBadges() {
        float widthTarget = Constants.DEFAULT_BUTTON_SIZE.x * 1.5f;

        AnimationActor badgesActor = new AnimationActor(null,
                new Vector2(Constants.WIDTH / 2f - widthTarget / 2f,
                        Constants.HEIGHT / 2.5f), new Vector2(), widthTarget,
                "img/badges/badges.atlas", String.valueOf(LevelMode.lastLevelIndexGot),
                0f, 0f, Animation.PlayMode.LOOP, "badge", true);

        stage.addActor(badgesActor);

        badgesNameLabel = new Label(generateMessage(
                "LEATHER PHLOEM",
                "PINE WOODENO",
                "RARE METALNIC",
                "AUTHORA'S SILVER",
                "DOOMEIC GOLDEN",
                "DOMINATING URANIUM",
                "DYAMOND FROM GOD",
                "BOMBER BEYOND GODLIKE"
        ), BoomberGame.skin);
        badgesNameLabel.setAlignment(Align.center);
        badgesNameLabel.setFontScale(1.25f);
        badgesNameLabel.setStyle(new Label.LabelStyle(BoomberGame.getInstance().font, new Color(1, 1, 1, 1)));

        badgesLabel = new Label(
                generateMessage(
                        "No pain, no gain\nYou should try more to be a hero!",
                        "You are on the right track\nLet's do it again!",
                        "Player better, become stronger\nThat’s a real improvement!",
                        "A gift from Authora Goddess\nYou impressed her!",
                        "Golden award of Doom for you\nYou are really a hero now",
                        "Uranium from Venus\nBrave hero will become a God",
                        "Dyamond from God of Gods\nCome on, you met him!",
                        "Congratulations!\nYou are now a God Of Bombers!"
                )
                , BoomberGame.skin);
        badgesLabel.setAlignment(Align.center);
        badgesLabel.setFontScale(1f);
        badgesLabel.setStyle(new Label.LabelStyle(BoomberGame.getInstance().font, new Color(1, 1, 1, 1)));

        gameOverGroup.addActor(badgesNameLabel);
        gameOverGroup.addActor(badgesLabel);

        gameOverGroup.setPosition(Constants.WIDTH / 2f, Constants.HEIGHT / 3.5f);
        gameOverGroup.align(Align.center);
        gameOverGroup.space(15f);

        stage.addActor(gameOverGroup);

        buttonBackToMainMenu = GameFactory.createTextButton("Main Menu", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2f), gameOverGroup.getY() - Constants.DEFAULT_BUTTON_SIZE.y * 3f),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);

                        stage.addAction(Actions.sequence(
                                Actions.fadeOut(1, Interpolation.pow2),
                                Actions.run(() -> BoomberGame.getInstance().setScreen(BoomberGame.getInstance().mainMenuScreen))
                        ));
                    }
                }
        );

        stage.addActor(buttonBackToMainMenu);
    }

    private String generateMessage(String... messages) {
        return messages[LevelMode.lastLevelIndexGot - 1];
    }
}
