package commons;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by puboe on 25/9/15.
 */
public abstract class GameObject {

    protected Vector3 position = new Vector3();
    protected Vector3 forward = new Vector3(0, 0, -1);
    protected Vector3 up = Vector3.Y;
    protected Vector3 right = Vector3.X;
    protected Vector3 rotation;
    protected Vector3 scale;

    protected Matrix4 getXRotationMatrix() {
        return new Matrix4(new float[] { 1, 0, 0, 0,
                0, (float)Math.cos(rotation.x), (float) Math.sin(rotation.x), 0,
                0, - (float) Math.sin(rotation.x), (float) Math.cos(rotation.x), 0,
                0, 0, 0, 1 });
    }

    protected Matrix4 getYRotationMatrix() {
        return new Matrix4(new float[] { (float)Math.cos(rotation.y), 0, - (float)Math.sin(rotation.y), 0,
                0, 1, 0, 0,
                (float)Math.sin(rotation.y), 0, (float) Math.cos(rotation.y), 0,
                0, 0, 0, 1 });
    }

    protected Matrix4 getZRotationMatrix() {
        return new Matrix4(new float[] { (float)Math.cos(rotation.z), (float)Math.sin(rotation.z), 0, 0,
                - (float)Math.sin(rotation.z), (float)Math.cos(rotation.z), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1 });
    }

    public Matrix4 getTranslationMatrix() {
        return new Matrix4(
                new float[] { 1, 0, 0, 0,
                        0, 1, 0, 0,
                        0, 0, 1, 0,
                        position.x, position.y, position.z, 1 });
    }

    public Matrix4 getScaleMatrix() {
        return new Matrix4(
                new float[] { scale.x, 0, 0, 0,
                        0, scale.y, 0, 0,
                        0, 0, scale.z, 0,
                        0, 0, 0, 1 });
    }

    public Matrix4 getRotationMatrix() {
        return (getXRotationMatrix().mul(getYRotationMatrix()).mul(getZRotationMatrix()));
    }

    public Matrix4 getTRS() {
        Matrix4 translation = getTranslationMatrix();
        Matrix4 rot = getRotationMatrix();
        return translation.mul(rot).mul(getScaleMatrix());
    }




}
