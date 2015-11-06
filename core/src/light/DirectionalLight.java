package light;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by riveign on 11/6/15.
 */
public class DirectionalLight extends Light {
    public DirectionalLight(Vector3 position, Vector3 color, float intensity) {
        super(position, color, intensity);
    }
}
