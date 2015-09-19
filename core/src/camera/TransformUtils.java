package camera;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by puboe on 18/9/15.
 */
public class TransformUtils {

    public static Matrix4 getTranslateMatrix(Vector3 pos) {

        return getTranslateMatrix(pos.x, pos.y, pos.z);
    }

    public static Matrix4 getTranslateMatrix(float x, float y, float z) {

        float[] aux = {1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1};

        // Transpose is needed because OpenGL is Column Major.
        return new Matrix4(aux).tra();
    }

}
