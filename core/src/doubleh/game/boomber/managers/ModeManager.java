package doubleh.game.boomber.managers;

import doubleh.game.boomber.modes.BaseMode;
import doubleh.game.boomber.modes.LevelMode;

public class ModeManager {
    private BaseMode currentMode;

    public ModeManager() {
    }

    public void load(int modeIndex) {
        switch (modeIndex) {
            case 0:
                currentMode = new LevelMode();
                break;
            case 1:
                break;
            default:
                break;
        }
    }

    public void update(float delta) {
        currentMode.update(delta);
    }

    public void render(float delta) {
        currentMode.render(delta);
    }

    public void dispose() {
        currentMode.stage.dispose();
        currentMode.dispose();
    }

    public BaseMode getCurrentMode() {
        return currentMode;
    }
}
