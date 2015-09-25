package light;

import com.badlogic.gdx.math.Vector3;
import commons.GameObject;

/**
 * Created by puboe on 25/9/15.
 */
public abstract class Light extends GameObject {

    float intensity;
    Vector3 color;

    public Light(Vector3 position, Vector3 color, float intensity) {
        super();
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }
}
