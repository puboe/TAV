package camera;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Abstract class representing the Camera.
 */
public abstract class Camera {

    float near;
    float far;
    float viewPortY;
    float viewPortX;
    Matrix4 projectionMatrix;
    Matrix4 viewMatrix = new Matrix4();
    Vector3 position = new Vector3();
    Vector3 forward = new Vector3(0, 0, -1);
    Vector3 up = Vector3.Y;
    Vector3 right = Vector3.X;
    boolean dirtyView = true;

    public Camera(float viewPortX, float viewPortY, float near, float far) {
        this.viewPortX = viewPortX;
        this.viewPortY = viewPortY;
        this.near = near;
        this.far = far;
        this.projectionMatrix = initializeProjectionMatrix();
//        this.worldMatrix = initializeWorldMatrix();
    }

    public abstract Matrix4 initializeProjectionMatrix();

    public Matrix4 initializeWorldMatrix() {
        return new Matrix4();
    }

    public Matrix4 lookAt(int x, int y, int z) {

        Vector3 newForward = position.cpy();
        newForward.sub(x, y, z).nor();
//        Vector3 newForward = new Vector3(x, y, z);
//        newForward.sub(position).nor();
        System.out.println("Forward: " + forward.toString() + ", New Forward: " + newForward.toString());
        forward.set(newForward);

        Vector3 newRight = up.cpy();
        newRight.crs(forward).nor();
        System.out.println("Right: " + right.toString() + ", New Right: " + newRight.toString());
        right.set(newRight);

        Vector3 newUp = newForward.crs(newRight).nor();
        System.out.println("Up: " + up.toString() + ", New Up: " + newUp.toString());
        up.set(newUp);

        float[] orientationVector = {
                right.x, right.y, right.z, 0,
                up.x, up.y, up.z, 0,
                forward.x, forward.y, forward.z, 0,
                0, 0, 0, 1};

        Matrix4 orientation = new Matrix4(orientationVector);
//        System.out.println("Orientation matrix: \n" + orientation.toString());

        float[] translationVector = {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                -position.x, -position.y, -position.z, 1};

        Matrix4 translation = new Matrix4(translationVector);
//        System.out.println("Translation matrix: \n" + translation.toString());

        // Si quisieramos guardar la matriz Orientation habría que hacerlo
        // antes de este paso, porque la sobreescribe.
        viewMatrix = orientation.mul(translation);
        System.out.println("Look at matrix: \n" + viewMatrix.toString());

        return viewMatrix;
    }

    public void setPosition(float x, float y, float z) {
        position = new Vector3(x, y, z);
        dirtyView = true;
    }

    public Matrix4 getCombined() {
        Matrix4 tmp = projectionMatrix.cpy();
        return tmp.mul(viewMatrix);
    }
}
