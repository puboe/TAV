package light;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by riveign on 11/6/15.
 */
public class DirectionalLight extends Light {

    private float[] direction;
    private Matrix4 projectionMatrix;
    float far;
    float near;
    float height;
    float width;

    public DirectionalLight(Vector3 position, Vector3 color, float intensity, float[] coneDirection) {
        super(position, color, intensity);
        this.direction = coneDirection;
    }

    public DirectionalLight(Vector3 position, Vector3 color, float intensity, float[] coneDirection, float far, float near) {
        super(position, color, intensity);
        this.direction = coneDirection;
        this.far = far;
        this.near = near;
        this.height = 50;
        this.width = 50;
        this.projectionMatrix = initializeProjectionMatrix();
    }

    public float[] getConeDirection() {
        return direction;
    }

    public void setConeDirection(float[] coneDirection) {
        this.direction = coneDirection;
    }

    public Matrix4 initializeProjectionMatrix() {

        float[] vector = {
                1 / width, 0, 0, 0,
                0, 1 / height, 0, 0,
                0, 0, -(far + near) / (near - far), 2 * (far * near) / (near - far),
                0, 0, -1f, 0};

//        System.out.println("PerspectiveCamera Projection Matrix: \n" + new Matrix4(vector).toString());

        // Transpose is needed because OpenGL is Column Major.
        return new Matrix4(vector).tra();
    }
}
