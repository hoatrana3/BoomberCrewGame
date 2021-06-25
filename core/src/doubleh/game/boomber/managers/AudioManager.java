package doubleh.game.boomber.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import doubleh.game.boomber.BoomberGame;
import doubleh.game.boomber.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioManager {
    private HashMap<String, String> musicHandler;
    private HashMap<String, String> soundHandler;
    private HashMap<Long, Sound> soundsPlayed;

    public AudioManager() {
        soundHandler = new HashMap<>();
        musicHandler = new HashMap<>();
        soundsPlayed = new HashMap<>();
    }

    public void load() {
        BoomberGame.getInstance().assets.load("audio/intro.wav", Sound.class);
        addInfomation("s_INTRO", "audio/intro.wav", Sound.class);

        BoomberGame.getInstance().assets.load("audio/healthup.mp3", Sound.class);
        addInfomation("s_HEALTH", "audio/healthup.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/speedup.mp3", Sound.class);
        addInfomation("s_SPEED", "audio/speedup.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/boomcapacity.mp3", Sound.class);
        addInfomation("s_BOOMCAPACITY", "audio/boomcapacity.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/boomlength.mp3", Sound.class);
        addInfomation("s_BOOMLENGTH", "audio/boomlength.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/boom.mp3", Sound.class);
        addInfomation("s_BOOM", "audio/boom.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/placeboom.mp3", Sound.class);
        addInfomation("s_PLACE_BOOM", "audio/placeboom.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/fire.wav", Sound.class);
        addInfomation("s_FIRE", "audio/fire.wav", Sound.class);

        BoomberGame.getInstance().assets.load("audio/winlevel.mp3", Sound.class);
        addInfomation("s_WIN_LEVEL", "audio/winlevel.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/light.mp3", Sound.class);
        addInfomation("s_LIGHT", "audio/light.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/buttonclick.mp3", Sound.class);
        addInfomation("s_BUTTON_CLICK", "audio/buttonclick.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/bossredRadiance.mp3", Sound.class);
        addInfomation("s_BRED_RAD", "audio/bossredRadiance.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/bossredInvi.mp3", Sound.class);
        addInfomation("s_BRED_INVI", "audio/bossredInvi.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/bossredFireWalking.mp3", Sound.class);
        addInfomation("s_BRED_WALK", "audio/bossredFireWalking.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/bossvioletDarkChange.mp3", Sound.class);
        addInfomation("s_BVIOLET_DARK", "audio/bossvioletDarkChange.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/bossvioletDoubleFire.mp3", Sound.class);
        addInfomation("s_BVIOLET_DOUBLE", "audio/bossvioletDoubleFire.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/bossvioletSquareFire.mp3", Sound.class);
        addInfomation("s_BVIOLET_SQUARE", "audio/bossvioletSquareFire.mp3", Sound.class);

        BoomberGame.getInstance().assets.load("audio/dota2pack.mp3", Music.class);
        addInfomation("m_INGAME", "audio/dota2pack.mp3", Music.class);

        BoomberGame.getInstance().assets.load("audio/mainmenu.mp3", Music.class);
        addInfomation("m_MAINMENU", "audio/mainmenu.mp3", Music.class);

        BoomberGame.getInstance().assets.load("audio/losemode.mp3", Music.class);
        addInfomation("m_LOSE_MODE", "audio/losemode.mp3", Music.class);

        BoomberGame.getInstance().assets.load("audio/winmode.mp3", Music.class);
        addInfomation("m_WIN_MODE", "audio/winmode.mp3", Music.class);
    }

    public <T> void addInfomation(String name, String path, Class<T> type) {
        if (type == Sound.class)
            if (!soundHandler.containsKey(name)) soundHandler.put(name, path);

        if (type == Music.class)
            if (!musicHandler.containsKey(name)) musicHandler.put(name, path);
    }

    public synchronized <T> T get(String name) {
        if (soundHandler.containsKey(name))
            return BoomberGame.getInstance().assets.get(soundHandler.get(name));
        if (musicHandler.containsKey(name))
            return BoomberGame.getInstance().assets.get(musicHandler.get(name));
        return null;
    }

    public synchronized <T> T get(String name, Class<T> type) {
        if (type == Sound.class)
            if (soundHandler.containsKey(name))
                return BoomberGame.getInstance().assets.get(soundHandler.get(name), type);

        if (type == Music.class)
            if (musicHandler.containsKey(name))
                return BoomberGame.getInstance().assets.get(musicHandler.get(name), type);

        return null;
    }

    public long playSound(String name, float volume, float pitch, float pan) {
        if (Constants.SOUND_ON)
            if (soundHandler.containsKey(name)) {
                Sound sound = get(name, Sound.class);

                long id = sound.play(volume * Constants.SOUND_VOLUME_RATIO, pitch, pan);
                soundsPlayed.putIfAbsent(id, sound);

                return id;
            }

        return Long.MIN_VALUE;
    }

    public void playMusic(String name) {
        if (Constants.MUSIC_ON)
            if (musicHandler.containsKey(name)) {
                Music music = get(name, Music.class);

                music.setVolume(Constants.MUSIC_VOLUME_RATIO);
                music.play();
            }
    }

    public long loopSound(String name, float volume, float pitch, float pan) {
        if (Constants.SOUND_ON)
            if (soundHandler.containsKey(name)) {
                Sound sound = get(name, Sound.class);

                long id = sound.loop(volume * Constants.SOUND_VOLUME_RATIO, pitch, pan);
                soundsPlayed.putIfAbsent(id, sound);

                return id;
            }

        return Long.MIN_VALUE;
    }

    public void loopMusic(String name) {
        if (Constants.MUSIC_ON)
            if (musicHandler.containsKey(name)) {
                Music music = get(name, Music.class);

                music.setVolume(Constants.MUSIC_VOLUME_RATIO);
                music.play();
                music.setLooping(true);
            }
    }

    public void pauseSound(String name, boolean useSpecificID, long id) {
        if (soundHandler.containsKey(name)) {
            Sound sound = get(name, Sound.class);

            if (!useSpecificID) sound.pause();
            else sound.pause(id);
        }
    }

    public void pauseMusic(String name) {
        if (musicHandler.containsKey(name)) {
            Music music = get(name, Music.class);

            music.pause();
        }
    }

    public void stopSound(String name, boolean useSpecificID, long id) {
        if (soundHandler.containsKey(name)) {
            Sound sound = get(name, Sound.class);

            if (!useSpecificID) sound.stop();
            else sound.stop(id);
        }
    }

    public void stopMusic(String name) {
        if (musicHandler.containsKey(name)) {
            Music music = get(name, Music.class);

            music.stop();
        }
    }

    public void updateVolume() {
        for (long id : soundsPlayed.keySet()) {
            Sound sound = soundsPlayed.get(id);

            sound.setVolume(id, Constants.SOUND_VOLUME_RATIO);
        }

        for (String nameMusic : musicHandler.keySet()) {
            get(nameMusic, Music.class).setVolume(Constants.MUSIC_VOLUME_RATIO);
        }
    }
}
