package camera;

import com.badlogic.gdx.math.Matrix4;

/**
 * Created by puboe on 4/9/15.
 */
public class OrthographicCamera extends Camera {

    public OrthographicCamera(float viewPortX, float viewPortY, float near, float far) {
        super(viewPortX, viewPortY, near, far);
        setProjectionMatrix(initializeProjectionMatrix());
//        position.set(viewPortX / 2.0f, viewPortY / 2.0f, 1f);
    }

    @Override
    public Matrix4 initializeProjectionMatrix() {
        float[] vector = {
                1f / viewPortX, 0, 0, 0,
                0, 1f / viewPortY, 0, 0,
                0, 0, -2f / (far - near), -(far + near) / (far - near),
                0, 0, 0, 1f};

        // Transpose is needed because OpenGL is Column Major.
        return new Matrix4(vector).tra();
    }

}
