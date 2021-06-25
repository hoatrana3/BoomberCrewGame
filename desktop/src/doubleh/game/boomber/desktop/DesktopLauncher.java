package doubleh.game.boomber.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.utils.Constants;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = Constants.WIDTH;
        config.height = Constants.HEIGHT;
        config.title = Constants.TITLE;
        config.resizable = false;
        config.foregroundFPS = 60;
        config.backgroundFPS = 60;

        config.addIcon("img/icons/icon_128.png", Files.FileType.Internal);
        config.addIcon("img/icons/icon_32.png", Files.FileType.Internal);
        config.addIcon("img/icons/icon_16.png", Files.FileType.Internal);

        if (Constants.IS_FULL_SCREEN) config.fullscreen = true;

        new LwjglApplication(BoomberGame.getInstance(), config);
    }
}
