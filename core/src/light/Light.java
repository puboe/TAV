package light;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import commons.BasicObject;
import commons.GameObject;

/**
 * Created by puboe on 25/9/15.
 */
public abstract class Light extends BasicObject {

    float intensity;
    Vector3 color;
    Matrix4 projectionMatrix;

    public Light(Vector3 position, Vector3 color, float intensity) {
        super(position);
        this.color = color;
        this.intensity = intensity;
        this.projectionMatrix = initializeProjectionMatrix();
    }

    public Matrix4 getViewMatrix() {
        return getTRS().inv();
    }

    public abstract Matrix4 initializeProjectionMatrix();

    public abstract FrameBuffer generateShadowMap(Array<GameObject> objects);

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

    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
}
