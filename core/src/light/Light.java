package light;

import camera.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by puboe on 25/9/15.
 */
public abstract class Light extends Camera {

    float intensity;
    Vector3 color;

    public Light(Vector3 position, Vector3 color, float intensity) {
        super(0, 0, 0, 0);
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

    public float[] getPositionArray() {
        return new float[]{position.x, position.y, position.z, 1f};
    }

    public float[] getColorArray() {
        return new float[]{color.x, color.y, color.z, 1f};
    }

}
