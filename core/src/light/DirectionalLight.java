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
    private ShaderProgram shadowMapShader;
    FrameBuffer shadowMapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 2048, 2048, true);

    public DirectionalLight(Vector3 position, Vector3 color, float intensity, float[] direction) {
        super(position, color, intensity);
        this.direction = direction;
        initShader();
    }

    public DirectionalLight(Vector3 position, Vector3 color, float intensity, float[] direction, float far, float near) {
        super(position, color, intensity);
        this.direction = direction;
        this.far = far;
        this.near = near;
        this.height = 50;
        this.width = 50;
        this.projectionMatrix = initializeProjectionMatrix();
        initShader();
    }

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

    public float[] getDirection() {
        return direction;
    }

    public void setDirection(float[] direction) {
        this.direction = direction;
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

    public Matrix4 getCombined() {
        return getProjectionMatrix().cpy().mul(getViewMatrix());
    }
}
