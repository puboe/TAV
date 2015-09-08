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
        this.fieldOfViewX = fieldOfViewY; //(float) (2.0 * Math.atan(Math.tan(fieldOfViewY / 2.0) * (viewPortX / viewPortY)));
        System.out.println("Field of view Y: " + fieldOfViewY);
        System.out.println("Field of view X: " + fieldOfViewX);
    }

    @Override
    public Matrix4 initializeProjectionMatrix() {

        float[] vector = {
                (float) Math.atan(Math.toRadians(fieldOfViewX) / 2.0), 0, 0, 0,
                0, (float) Math.atan(Math.toRadians(fieldOfViewY) / 2.0), 0, 0,
                0, 0, -(far + near) / (far - near), -1f,
                0, 0, -2 * (far * near) / (far - near), 0};

        System.out.println("PerspectiveCamera Projection Matrix: \n" + new Matrix4(vector).toString());

        return new Matrix4(vector);
    }

    @Override
    public Matrix4 initializeWorldMatrix() {

        // TODO: Implement this!

        return new Matrix4();
    }
}
