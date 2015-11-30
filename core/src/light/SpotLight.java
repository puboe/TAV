package light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import commons.GameObject;

public class SpotLight extends Light {

    private float angle;
    private float[] coneDirection;
    float far;
    float near;
    private ShaderProgram shadowMapShader;
    FrameBuffer shadowMapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 2048, 2048, true);

    private void initShader() {
        String shadowVs = Gdx.files.internal("shaders/shadowMapVS.glsl").readString();
        String shadowFs = Gdx.files.internal("shaders/shadowMapFS.glsl").readString();
        shadowMapShader = new ShaderProgram(shadowVs, shadowFs);
        System.out.println(shadowMapShader.getLog());
    }

    @Override
    public FrameBuffer generateShadowMap(Array<GameObject> objects) {
        shadowMapBuffer.begin();
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl20.glDisable(GL20.GL_BLEND);
        for (GameObject object : objects) {
            object.getTexture().bind(0);
            shadowMapShader.begin();
            shadowMapShader.setUniformMatrix("u_worldView", getCombined().mul(object.getTRS())); //aca trabajar
            object.getMesh().render(shadowMapShader, GL20.GL_TRIANGLES);
            shadowMapShader.end();
        }
        shadowMapBuffer.end();
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        return shadowMapBuffer;
    }

    public SpotLight(Vector3 position, Vector3 color, float intensity, float angle, float[] coneDirection) {
        super(position, color, intensity);
        this.angle = angle;
        this.coneDirection = coneDirection;
        initShader();
    }

    public SpotLight(Vector3 position, Vector3 color, float intensity, float angle, float[] coneDirection, float far, float near) {
        super(position, color, intensity);
        this.angle = angle;
        this.coneDirection = coneDirection;
        this.far = far;
        this.near = near;
        initShader();
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

    @Override
    public Matrix4 initializeProjectionMatrix() {

        float[] vector = {
                (float) Math.atan(Math.toRadians(angle) / 2.0), 0, 0, 0,
                0, (float) Math.atan(Math.toRadians(angle) / 2.0), 0, 0,
                0, 0, -(far + near) / (far - near), -2 * (far * near) / (far - near),
                0, 0, -1f, 0};

        // Transpose is needed because OpenGL is Column Major.
        return new Matrix4(vector).tra();
    }

    @Override
    public Matrix4 getProjectionMatrix() {
        return new Matrix4().setToOrtho(-25, 25, -25, 25, -10, 100);
    }

    public Matrix4 getCombined() {
        return getProjectionMatrix().cpy().mul(getViewMatrix());
    }
}
