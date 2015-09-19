package camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    Matrix4 projectionMatrix = new Matrix4();
    Matrix4 viewMatrix = new Matrix4();
    Vector3 position = new Vector3();
    Vector3 rotation = new Vector3();
    Vector3 forward = new Vector3(0, 0, -1);
    Vector3 up = Vector3.Y;
    Vector3 right = Vector3.X;
    Vector3 lookingAt = new Vector3();
    boolean dirtyView = true;

    public Camera(float viewPortX, float viewPortY, float near, float far) {
        this.viewPortX = viewPortX;
        this.viewPortY = viewPortY;
        this.near = near;
        this.far = far;
    }

    public abstract Matrix4 initializeProjectionMatrix();

    public Matrix4 lookAt(float x, float y, float z) {
        lookingAt.set(x, y, z);

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

        Matrix4 orientation = getOrientationMatrix();
//        System.out.println("Orientation matrix: \n" + orientation.toString());

        Matrix4 translation = TransformUtils.getTranslateMatrix(position);
//        System.out.println("Translation matrix: \n" + translation.toString());

        viewMatrix = orientation.mul(translation).inv();
//        System.out.println("Look at matrix: \n" + viewMatrix.toString());

        return viewMatrix;
    }

    public void setProjectionMatrix(Matrix4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public void setPosition(float x, float y, float z) {
        position = new Vector3(x, y, z);
        dirtyView = true;
    }

    public Matrix4 getCombined() {
        Matrix4 tmp = projectionMatrix.cpy();
        return tmp.mul(viewMatrix);
    }

    public Matrix4 getViewMatrix() {

        Matrix4 translation = TransformUtils.getTranslateMatrix(position);
        Matrix4 orientation = getOrientationMatrix();
        orientation.mul(translation);
        viewMatrix = orientation.inv();
//        System.out.println(viewMatrix);
        return viewMatrix;
    }

    private Matrix4 getOrientationMatrix() {
        float[] aux = {
                right.x, up.x, forward.x, 0,
                right.y, up.y, forward.y, 0,
                right.z, up.z, forward.z, 0,
                0, 0, 0, 1};

        // Transpose is needed because OpenGL is Column Major.
        return new Matrix4(aux).tra();
    }

    public void update() {
        float deltaX = 0, deltaY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
            deltaX -= Gdx.graphics.getDeltaTime() * 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
            deltaX += Gdx.graphics.getDeltaTime() * 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
            deltaY += Gdx.graphics.getDeltaTime() * 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
            deltaY -= Gdx.graphics.getDeltaTime() * 1f;

        if (Gdx.input.isTouched()) {
            // TODO Rotate.
        }

        setPosition(position.x + deltaX, position.y + deltaY, position.z);

        getViewMatrix();

    }
}
