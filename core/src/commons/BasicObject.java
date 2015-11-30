package commons;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by puboe on 28/11/15.
 */
public abstract class BasicObject {

    protected Vector3 position = new Vector3();
    protected Vector3 rotation = new Vector3(0, 0, 0);
    protected Vector3 scale = new Vector3(1, 1, 1);

    protected Vector3 forward = new Vector3(0, 0, -1);
    protected Vector3 up = Vector3.Y;
    protected Vector3 right = Vector3.X;

    public BasicObject() {

    }

    public BasicObject(Vector3 position) {
        this.position = position;
    }

    public BasicObject(Vector3 position, Vector3 rotation, Vector3 scale) {
        this(position);
        this.rotation = rotation;
        this.scale = scale;
    }

    protected Matrix4 getXRotationMatrix() {
        return new Matrix4(new float[]{
                1, 0, 0, 0,
                0, (float) Math.cos(rotation.x), (float) Math.sin(rotation.x), 0,
                0, -(float) Math.sin(rotation.x), (float) Math.cos(rotation.x), 0,
                0, 0, 0, 1});
    }

    protected Matrix4 getYRotationMatrix() {
        return new Matrix4(new float[]{
                (float) Math.cos(rotation.y), 0, -(float) Math.sin(rotation.y), 0,
                0, 1, 0, 0,
                (float) Math.sin(rotation.y), 0, (float) Math.cos(rotation.y), 0,
                0, 0, 0, 1});
    }

    protected Matrix4 getZRotationMatrix() {
        return new Matrix4(new float[]{
                (float) Math.cos(rotation.z), (float) Math.sin(rotation.z), 0, 0,
                -(float) Math.sin(rotation.z), (float) Math.cos(rotation.z), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1});
    }

    public Matrix4 getTranslationMatrix() {
        return new Matrix4(
                new float[]{1, 0, 0, 0,
                        0, 1, 0, 0,
                        0, 0, 1, 0,
                        position.x, position.y, position.z, 1});
    }

    public Matrix4 getScaleMatrix() {
        return new Matrix4(
                new float[]{scale.x, 0, 0, 0,
                        0, scale.y, 0, 0,
                        0, 0, scale.z, 0,
                        0, 0, 0, 1});
    }

    public Matrix4 getRotationMatrix() {
        return (getXRotationMatrix().mul(getYRotationMatrix()).mul(getZRotationMatrix()));
    }

    public Matrix4 getTRS() {
        Matrix4 translation = getTranslationMatrix();
        Matrix4 rot = getRotationMatrix();
        return translation.mul(rot).mul(getScaleMatrix());
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }
}
