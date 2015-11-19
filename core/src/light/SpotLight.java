package light;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by riveign on 11/19/15.
 */
public class SpotLight extends Light {

    private float angle;
    private float[] coneDirection;
    private Matrix4 projectionMatrix;
    float far;
    float near;

    public SpotLight(Vector3 position, Vector3 color, float intensity, float angle, float[] coneDirection) {
        super(position, color, intensity);
        this.angle = angle;
        this.coneDirection = coneDirection;
    }

    public SpotLight(Vector3 position, Vector3 color, float intensity, float angle, float[] coneDirection, float far, float near) {
        super(position, color, intensity);
        this.angle = angle;
        this.coneDirection = coneDirection;
        this.far = far;
        this.near = near;
        this.projectionMatrix = initializeProjectionMatrix();
    }

    public float[] getConeDirection() {
        return coneDirection;
    }

    public void setConeDirection(float[] coneDirection) {
        this.coneDirection = coneDirection;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngel(float angle) {
        this.angle = angle;
    }

    public Matrix4 initializeProjectionMatrix() {

        float[] vector = {
                (float) Math.atan(Math.toRadians(angle) / 2.0), 0, 0, 0,
                0, (float) Math.atan(Math.toRadians(angle) / 2.0), 0, 0,
                0, 0, -(far + near) / (near - far), 2 * (far * near) / (near - far),
                0, 0, -1f, 0};

//        System.out.println("PerspectiveCamera Projection Matrix: \n" + new Matrix4(vector).toString());

        // Transpose is needed because OpenGL is Column Major.
        return new Matrix4(vector).tra();
    }
}
