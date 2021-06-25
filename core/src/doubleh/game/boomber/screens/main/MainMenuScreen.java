package doubleh.game.boomber.screens.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.actors.others.AnimationActor;
import doubleh.game.boomber.screens.BaseScreen;
import doubleh.game.boomber.utils.Constants;
import doubleh.game.boomber.utils.GameFactory;
import doubleh.game.boomber.utils.GameUtils;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * This screen is for game menu
 */
public class MainMenuScreen extends BaseScreen {

    private Stage stage;

    private Group
            menuGroup,
            settingsGroup,
            creditGroup,
            tutorialGroup,
            modeGroup,
            characterShow;

    private TextButton
            buttonPlay,
            buttonExit,
            buttonSetting,
            buttonCredit,
            buttonTutorial,
            buttonTutorialBack,
            buttonSettingBack,
            buttonCreditBack,
            buttonModeBack,
            buttonMusicControl,
            buttonSoundControl,
            buttonCameraStyle,
            buttonLevelMode,
            buttonSurvivalMode,
            buttonBossMode,
            buttonCheat,
            buttonChangeCharactor;

    private AnimationActor
            characterNinja,
            characterKnight,
            characterCombatant,
            characterMummydog;

    private String[] camStyleChoice = new String[]{
            "Lerp & Bounding",
            "Lock & Bounding",
            "Lerp on player",
            "Lock on player"
    };

    private enum CharacterChoices {
        NINJA, KNIGHT, COMBATANT, MUMMYDOG;

        public static CharacterChoices fromInteger(int index) {
            switch (index) {
                case 0:
                    return NINJA;
                case 1:
                    return KNIGHT;
                case 2:
                    return COMBATANT;
                case 3:
                    return MUMMYDOG;
                default:
                    return null;
            }
        }

        public int toInteger() {
            switch (this) {
                case NINJA:
                    return 0;
                case KNIGHT:
                    return 1;
                case COMBATANT:
                    return 2;
                case MUMMYDOG:
                    return 3;
                default:
                    return -1;
            }
        }

        public CharacterChoices getNext() {
            return fromInteger((toInteger() + 1) > 3 ? 0 : toInteger() + 1);
        }

        public CharacterChoices getBefore() {
            return fromInteger((toInteger() - 1) < 0 ? 3 : toInteger() - 1);
        }

        public static CharacterChoices fromString(String name) {
            switch (name) {
                case "ninja":
                    return NINJA;
                case "knight":
                    return KNIGHT;
                case "combatant":
                    return COMBATANT;
                case "mummydog":
                    return MUMMYDOG;
                default:
                    return null;
            }
        }

        @Override
        public String toString() {
            switch (this) {
                case NINJA:
                    return "ninja";
                case KNIGHT:
                    return "knight";
                case MUMMYDOG:
                    return "mummydog";
                case COMBATANT:
                    return "combatant";
                default:
                    return null;
            }
        }

        public String toStringInfo() {
            switch (this) {
                case NINJA:
                    return "Ninja";
                case KNIGHT:
                    return "Knight";
                case MUMMYDOG:
                    return "Mummy Dog";
                case COMBATANT:
                    return "Combatant";
                default:
                    return null;
            }
        }
    }

    private CharacterChoices characterChoices;

    private int
            currentSoundLevel,
            currentMusicLevel,
            currentIndexCameraChoice;

    public MainMenuScreen() {
        currentMusicLevel = (int) (Constants.MUSIC_VOLUME_RATIO * 5);
        currentSoundLevel = (int) (Constants.SOUND_VOLUME_RATIO * 5);
        currentIndexCameraChoice = 0;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Constants.WIDTH, Constants.HEIGHT));

        menuGroup = new Group();
        settingsGroup = new Group();
        creditGroup = new Group();
        modeGroup = new Group();
        characterShow = new Group();
        tutorialGroup = new Group();

        BoomberGame.getInstance().audios.stopMusic("m_INGAME");
        BoomberGame.getInstance().audios.loopMusic("m_MAINMENU");
        BoomberGame.getInstance().audios.get("m_MAINMENU", Music.class).setVolume(1f);

        characterChoices = CharacterChoices.fromString(Constants.CHARACTOR_NAME);

        Gdx.input.setInputProcessor(stage);

        initEverything();

        stage.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1f, 1f, Interpolation.pow2)
        ));
    }

    private void update(float delta) {
        stage.act(delta);
        formatCharactersShow();
        characterChoices = CharacterChoices.fromString(Constants.CHARACTOR_NAME);

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) changeCharacter(characterChoices.getBefore());
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) changeCharacter(characterChoices.getNext());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
    }

    private void initMenu() {
        Texture gameLogo = BoomberGame.getInstance().assets.get("img/namegame.png", Texture.class);
        Image gameImg;
        gameImg = new Image(gameLogo);

        if (!Constants.IS_FULL_SCREEN)
            gameImg.setScale(Constants.WIDTH / 1600f * 0.75f, Constants.WIDTH / 1600f * 0.75f);
        else gameImg.setScale(Constants.WIDTH / 1600f, Constants.WIDTH / 1600f);

        gameImg.setOrigin(gameImg.getWidth() / 2, gameImg.getHeight() / 2);

        if (Constants.IS_FULL_SCREEN)
            gameImg.setPosition(stage.getWidth() / 2 - gameImg.getWidth() / 2f, stage.getHeight() - gameImg.getHeight());
        else
            gameImg.setPosition(stage.getWidth() / 2 - gameImg.getWidth() / 2f, stage.getHeight() - gameImg.getHeight() / 1.35f);

        gameImg.setZIndex(Constants.Z_HIGH);

        buttonPlay = GameFactory.createTextButton("Play", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2(Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2, Constants.HEIGHT / 2f + Constants.DEFAULT_BUTTON_SIZE.y / 2),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(modeGroup, menuGroup, true);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonSetting = GameFactory.createTextButton("Settings", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2(Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2, buttonPlay.getY() - Constants.DEFAULT_BUTTON_SIZE.y * (3f / 2f)),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(settingsGroup, menuGroup, true);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonTutorial = GameFactory.createTextButton("Tutorial", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2(Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2, buttonSetting.getY() - Constants.DEFAULT_BUTTON_SIZE.y * (3f / 2f)),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(tutorialGroup, menuGroup, true);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonCredit = GameFactory.createTextButton("Credits", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2(Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2, buttonTutorial.getY() - Constants.DEFAULT_BUTTON_SIZE.y * (3f / 2f)),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(creditGroup, menuGroup, true);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonExit = GameFactory.createTextButton("Exit", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2(Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2, buttonCredit.getY() - Constants.DEFAULT_BUTTON_SIZE.y * (3f / 2f)),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                        stage.addAction(Actions.sequence(
                                fadeOut(1f, Interpolation.pow2),
                                run(() -> Gdx.app.exit())
                        ));
                    }
                }
        );

        menuGroup.addActor(gameImg);
        menuGroup.addActor(buttonPlay);
        menuGroup.addActor(buttonSetting);
        menuGroup.addActor(buttonCredit);
        menuGroup.addActor(buttonTutorial);
        menuGroup.addActor(buttonExit);

        stage.addActor(menuGroup);
    }

    private void initSettings() {
        buttonSettingBack = GameFactory.createTextButton("Back", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2) + Constants.WIDTH, buttonExit.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(menuGroup, settingsGroup, false);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonMusicControl = GameFactory.createTextButton("Music : " + currentMusicLevel, "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2) + Constants.WIDTH, buttonPlay.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        currentMusicLevel++;
                        currentMusicLevel = (currentMusicLevel > 5) ? 0 : currentMusicLevel;

                        Constants.MUSIC_VOLUME_RATIO = currentMusicLevel / 5f;

                        buttonMusicControl.setText("Music : " + currentMusicLevel);

                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonSoundControl = GameFactory.createTextButton("Sound : " + currentSoundLevel, "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2(buttonMusicControl.getX() - Constants.DEFAULT_BUTTON_SIZE.x * 9 / 8, buttonPlay.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        currentSoundLevel++;
                        currentSoundLevel = (currentSoundLevel > 5) ? 0 : currentSoundLevel;

                        Constants.SOUND_VOLUME_RATIO = currentSoundLevel / 5f;

                        buttonSoundControl.setText("Sound : " + currentSoundLevel);

                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonCheat = GameFactory.createTextButton("Cheat : Off", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2(buttonMusicControl.getX() + Constants.DEFAULT_BUTTON_SIZE.x * 9 / 8, buttonPlay.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Constants.USE_CHEAT = !Constants.USE_CHEAT;

                        buttonCheat.setText("Cheat : " + ((Constants.USE_CHEAT) ? "On" : "Off"));

                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonCameraStyle = GameFactory.createTextButton("Camera : " + camStyleChoice[currentIndexCameraChoice], "default", Color.BLACK,
                new Vector2((buttonCheat.getX() + Constants.DEFAULT_BUTTON_SIZE.x - buttonSoundControl.getX()), Constants.DEFAULT_BUTTON_SIZE.y),
                new Vector2(buttonSoundControl.getX(), buttonSetting.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        currentIndexCameraChoice++;
                        currentIndexCameraChoice = (currentIndexCameraChoice > camStyleChoice.length - 1) ? 0 : currentIndexCameraChoice;

                        buttonCameraStyle.setText("Camera : " + camStyleChoice[currentIndexCameraChoice]);

                        switch (camStyleChoice[currentIndexCameraChoice]) {
                            case "Lerp & Bounding":
                                Constants.USE_LEARP = true;
                                Constants.USE_BOUND = true;
                                break;
                            case "Lock & Bounding":
                                Constants.USE_LEARP = false;
                                Constants.USE_BOUND = true;
                                break;
                            case "Lerp on player":
                                Constants.USE_LEARP = true;
                                Constants.USE_BOUND = false;
                                break;
                            case "Lock on player":
                                Constants.USE_LEARP = false;
                                Constants.USE_BOUND = false;
                                break;
                        }

                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        settingsGroup.addActor(buttonMusicControl);
        settingsGroup.addActor(buttonSoundControl);
        settingsGroup.addActor(buttonCameraStyle);
        settingsGroup.addActor(buttonCheat);
        settingsGroup.addActor(buttonSettingBack);

        stage.addActor(settingsGroup);
    }

    private void initTutorial() {
        buttonTutorialBack = GameFactory.createTextButton("Back", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2) + Constants.WIDTH, buttonExit.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(menuGroup, tutorialGroup, false);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        Image imgTutor = new Image(BoomberGame.getInstance().assets.get("img/tutorial.png", Texture.class));
        imgTutor.setScale((Constants.WIDTH) / (imgTutor.getPrefWidth() * 1.5f), (Constants.WIDTH) / (imgTutor.getPrefWidth() * 1.5f));
        imgTutor.setPosition((buttonTutorialBack.getX() - ((imgTutor.getPrefWidth() / 2.5f) * imgTutor.getScaleX())), (Constants.HEIGHT / 2f) - ((imgTutor.getPrefHeight() / 3) * imgTutor.getScaleX()));

        tutorialGroup.addActor(imgTutor);
        tutorialGroup.addActor(buttonTutorialBack);

        stage.addActor(tutorialGroup);
    }

    private void initCredit() {
        buttonCreditBack = GameFactory.createTextButton("Back", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x / 2) + Constants.WIDTH, buttonExit.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(menuGroup, creditGroup, false);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        Image imgCredits = new Image(BoomberGame.getInstance().assets.get("img/credits.png", Texture.class));
        imgCredits.setScale((Constants.WIDTH) / (imgCredits.getPrefWidth() * 3f), (Constants.WIDTH) / (imgCredits.getPrefWidth() * 3f));
        imgCredits.setPosition((buttonCreditBack.getX() - ((imgCredits.getPrefWidth() / 4.5f) * imgCredits.getScaleX())), (Constants.HEIGHT / 2f) - ((imgCredits.getPrefHeight() / 3) * imgCredits.getScaleX()));

        creditGroup.addActor(imgCredits);
        creditGroup.addActor(buttonCreditBack);

        stage.addActor(creditGroup);
    }

    private void initPlayMode() {
        buttonLevelMode = GameFactory.createTextButton("Original", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x * 2.25f) + Constants.WIDTH, buttonPlay.getY() + Constants.DEFAULT_BUTTON_SIZE.y * 3),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BoomberGame.imgFadeTransition = new Image(BoomberGame.getInstance().assets.get("img/black.jpg", Texture.class));

                        GameUtils.lazySetupFadeImageTransition(new Vector2(0, 0), new Vector2(Constants.WIDTH, Constants.HEIGHT),
                                0, 1, 1,
                                () -> BoomberGame.getInstance().setScreen(BoomberGame.getInstance().gameScreen));

                        stage.addActor(BoomberGame.imgFadeTransition);
                        stage.addAction(Actions.fadeOut(1, Interpolation.pow2));

                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonSurvivalMode = GameFactory.createTextButton("Survival", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x * 2.25f) + Constants.WIDTH, buttonPlay.getY() + Constants.DEFAULT_BUTTON_SIZE.y * 1.5f),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonBossMode = GameFactory.createTextButton("Boss Fight", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x * 2.25f) + Constants.WIDTH, buttonPlay.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonChangeCharactor = GameFactory.createTextButton(characterChoices.toStringInfo(), "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x * 2.25f) + Constants.WIDTH, buttonExit.getY() + Constants.DEFAULT_BUTTON_SIZE.y * 1.5f),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        changeCharacter(characterChoices.getNext());
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        buttonModeBack = GameFactory.createTextButton("Back", "default", Color.BLACK,
                Constants.DEFAULT_BUTTON_SIZE,
                new Vector2((Constants.WIDTH / 2f - Constants.DEFAULT_BUTTON_SIZE.x * 2.25f) + Constants.WIDTH, buttonExit.getY()),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        makeGroupComeAndIn(menuGroup, modeGroup, false);
                        BoomberGame.getInstance().audios.playSound("s_BUTTON_CLICK", 1, 1, 0);
                    }
                }
        );

        characterNinja = new AnimationActor(null, new Vector2(buttonModeBack.getX() + Constants.DEFAULT_BUTTON_SIZE.x * 1.5f, buttonModeBack.getY()), new Vector2(), Constants.DEFAULT_BUTTON_SIZE.x / 1.5f, "img/character/ninja/ninja.atlas", "idle", 5f, 0f, Animation.PlayMode.LOOP, "ninja", true);
        characterNinja.callFlip(true, false);

        characterKnight = new AnimationActor(null, new Vector2(characterNinja.getX() + Constants.DEFAULT_BUTTON_SIZE.x / 1.25f, buttonModeBack.getY() + Constants.DEFAULT_BUTTON_SIZE.y / 2), new Vector2(), Constants.DEFAULT_BUTTON_SIZE.x / 1.5f, "img/character/knight/knight.atlas", "idle", 5f, 0f, Animation.PlayMode.LOOP, "knight", true);
        characterKnight.callFlip(true, false);

        characterCombatant = new AnimationActor(null, new Vector2(characterKnight.getX() + Constants.DEFAULT_BUTTON_SIZE.x / 1.25f, buttonModeBack.getY()), new Vector2(), Constants.DEFAULT_BUTTON_SIZE.x / 1.5f, "img/character/combatant/combatant.atlas", "idle", 5f, 0f, Animation.PlayMode.LOOP, "combatant", true);
        characterCombatant.callFlip(true, false);

        characterMummydog = new AnimationActor(null, new Vector2(characterCombatant.getX() + Constants.DEFAULT_BUTTON_SIZE.x / 1.25f, buttonModeBack.getY() + Constants.DEFAULT_BUTTON_SIZE.y / 2), new Vector2(), Constants.DEFAULT_BUTTON_SIZE.x / 1.5f, "img/character/mummydog/mummydog.atlas", "idle", 5f, 0f, Animation.PlayMode.LOOP, "mummydog", true);
        characterMummydog.callFlip(true, false);

        characterShow.addActor(characterNinja);
        characterShow.addActor(characterKnight);
        characterShow.addActor(characterCombatant);
        characterShow.addActor(characterMummydog);

        modeGroup.addActor(buttonLevelMode);
        modeGroup.addActor(buttonSurvivalMode);
        modeGroup.addActor(buttonBossMode);
        modeGroup.addActor(buttonModeBack);
        modeGroup.addActor(buttonChangeCharactor);
        modeGroup.addActor(characterShow);

        stage.addActor(modeGroup);
    }

    private void initEverything() {
        Texture bgTex = BoomberGame.getInstance().assets.get("img/mainmenubackground.png", Texture.class);
        Image bgImg = new Image(bgTex);

        bgImg.setSize(stage.getWidth(), stage.getHeight());
        bgImg.setZIndex(Constants.Z_SUPER_LOW);
        bgImg.addAction(alpha(0.75f));

        stage.addActor(bgImg);

        initMenu();
        initSettings();
        initCredit();
        initPlayMode();
        initTutorial();
    }

    private void makeGroupComeAndIn(Group outGroup, Group inGroup, boolean toLeft) {
        outGroup.addAction(moveTo(outGroup.getX() - Constants.WIDTH * ((toLeft) ? 1 : -1), outGroup.getY(), 1f, Interpolation.pow2));
        inGroup.addAction(moveTo(outGroup.getX() - Constants.WIDTH * ((toLeft) ? 1 : -1), outGroup.getY(), 1f, Interpolation.pow2));
    }

    private void formatCharactersShow() {
        for (Actor child : characterShow.getChildren()) {
            Color childColor = child.getColor();

            child.setColor(childColor.r, childColor.g, childColor.b, 0.5f);
        }

        AnimationActor character = characterShow.findActor(characterChoices.toString());
        Color characterColor = character.getColor();

        characterColor.set(characterColor.r, characterColor.g, characterColor.b, 1f);
    }

    private void changeCharacter(CharacterChoices target) {
        characterChoices = target;
        Constants.CHARACTOR_NAME = characterChoices.toString();
        buttonChangeCharactor.setText(characterChoices.toStringInfo());
    }
}
