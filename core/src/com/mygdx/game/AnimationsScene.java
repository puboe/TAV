package com.mygdx.game;

import camera.Camera;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class AnimationsScene extends ApplicationAdapter {

    private final static int BONES = 12;
    private final static String FILENAME = "Dave.g3db";

    Texture texture;
    ShaderProgram charShader;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;

    private AssetManager assets = new AssetManager();
    private AnimationController animationController;
    private Array<ModelInstance> instances = new Array<ModelInstance>();
    private Pool<Renderable> pool;

    @Override
    public void create() {

        String vs = Gdx.files.internal("animationsVS.glsl").readString();
        String fs = Gdx.files.internal("defaultFS.glsl").readString();
        charShader = new ShaderProgram(vs, fs);
        System.out.println(charShader.getLog());

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam = new OrthographicCamera(3f, 3f);
        cam.position.set(10f, 15f, 10f);
        cam.lookAt(0, 7f, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assets.load(FILENAME, Model.class);
        assets.load("Dave.png", Texture.class);
        assets.finishLoading();

        Model characterModel = assets.get(FILENAME, Model.class);
        texture = assets.get("Dave.png", Texture.class);
        ModelInstance charInstance = new ModelInstance(characterModel);
        instances.add(charInstance);

        animationController = new AnimationController(charInstance);
        animationController.animate(charInstance.animations.get(0).id, -1, 1f, null, 0.2f); // Starts the animaton

        pool = new Pool<Renderable>() {
            @Override
            protected Renderable newObject() {
                return new Renderable();
            }

            @Override
            public Renderable obtain() {
                Renderable renderable = super.obtain();
                renderable.material = null;
                renderable.mesh = null;
                renderable.shader = null;
                return renderable;
            }
        };
    }


    @Override
    public void render() {

        camController.update();

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        Matrix4 mvpMatrix = new Matrix4();
        Matrix4 nMatrix = new Matrix4();

        charShader.begin();
//        texture.bind();
        animationController.update(Gdx.graphics.getDeltaTime());
//        charShader.setUniformi(Constants.U_TEXTURE, 0);
        // Bind whatever uniforms / textures you need
        for (ModelInstance charInstance : instances) {
            Array<Renderable> renderables = new Array<Renderable>();
            charInstance.getRenderables(renderables, pool);
            Matrix4 idtMatrix = new Matrix4().idt();
            float[] bones = new float[BONES * 16];
            for (int i = 0; i < bones.length; i++) {
                bones[i] = idtMatrix.val[i % 16];
            }
            for (Renderable render : renderables) {
                mvpMatrix.set(cam.combined);
                mvpMatrix.mul(render.worldTransform);
                charShader.setUniformMatrix("u_mvpMatrix", mvpMatrix);
                nMatrix.set(cam.view);
                nMatrix.mul(render.worldTransform);
                charShader.setUniformMatrix("u_modelViewMatrix", nMatrix);
                nMatrix.inv();
                nMatrix.tra();
                charShader.setUniformMatrix("u_normalMatrix", nMatrix);
//                StaticVariables.tempMatrix.idt();
                for (int i = 0; i < bones.length; i++) {
                    final int idx = i / 16;
                    bones[i] = (render.bones == null || idx >= render.bones.length || render.bones[idx] == null) ?
                            idtMatrix.val[i % 16] : render.bones[idx].val[i % 16];
                }
                charShader.setUniformMatrix4fv("u_bones", bones, 0, bones.length);
                render.mesh.render(charShader, render.primitiveType, render.meshPartOffset, render.meshPartSize);
            }
        }
        charShader.end();

    }

    @Override
    public void dispose() {
        instances.clear();
        assets.dispose();
        pool.clear();
    }
}