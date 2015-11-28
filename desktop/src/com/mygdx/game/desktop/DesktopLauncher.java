package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.AnimationsScene;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ShadowSpotlightScene;
import com.mygdx.game.SpotLightScene;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 768;
        new LwjglApplication(new ShadowSpotlightScene(), config);

    }
}
