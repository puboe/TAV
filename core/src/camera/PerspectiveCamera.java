package camera;

import com.badlogic.gdx.math.Matrix4;

/**
 * Perspective camera.
 */
public class PerspectiveCamera extends Camera {

    private float fieldOfViewX;
    private float fieldOfViewY;

    public PerspectiveCamera(float fieldOfViewY, float viewPortX, float viewPortY, float near, float far) {
        super(viewPortX, viewPortY, near, far);
        this.fieldOfViewY = fieldOfViewY;
        this.fieldOfViewX = (float) (2.0 * Math.toDegrees(Math.atan(Math.tan(Math.toRadians(fieldOfViewY) / 2.0))) * (viewPortX / viewPortY));
        System.out.println("Field of view Y: " + fieldOfViewY);
        System.out.println("Field of view X: " + fieldOfViewX);
        setProjectionMatrix(initializeProjectionMatrix());
    }

    @Override
    public Matrix4 initializeProjectionMatrix() {

        float[] vector = {
                (float) Math.atan(Math.toRadians(fieldOfViewX) / 2.0), 0, 0, 0,
                0, (float) Math.atan(Math.toRadians(fieldOfViewY) / 2.0), 0, 0,
                0, 0, -(far + near) / (near - far), 2 * (far * near) / (near - far),
                0, 0, -1f, 0};

//        System.out.println("PerspectiveCamera Projection Matrix: \n" + new Matrix4(vector).toString());

        // Transpose is needed because OpenGL is Column Major.
        return new Matrix4(vector).tra();
    }

}
