package light;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by puboe on 25/9/15.
 */
public class PointLight extends Light {

    public PointLight(Vector3 position, Vector3 color, float intensity) {
        super(position, color, intensity);
    }

    @Override
    public Matrix4 initializeProjectionMatrix() {
        return null;
    }
}


