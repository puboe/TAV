package com.mygdx.game;

import camera.Camera;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
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
import commons.Constants;

public class AnimationsScene extends ApplicationAdapter {

    private final static int BONES = 4;

    Texture texture;
    Mesh objMesh;
    ShaderProgram charShader;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;

    AssetManager assets;
    AnimationController animationController;
    Array<ModelInstance> instances = new Array<ModelInstance>();

    @Override
    public void create() {

        texture = new Texture("Dave.png");
        String vs = Gdx.files.internal("animationsVS.glsl").readString();
        String fs = Gdx.files.internal("animationsFS.glsl").readString();
        charShader = new ShaderProgram(vs, fs);
        System.out.println(charShader.getLog());

//        ModelLoader<?> loader = new ObjLoader();
//        ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
//
//        objMesh = new Mesh(true,
//                data.meshes.get(0).vertices.length,
//                data.meshes.get(0).parts[0].indices.length,
//                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
//        objMesh.setVertices(data.meshes.get(0).vertices);
//        objMesh.setIndices(data.meshes.get(0).parts[0].indices);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam = new OrthographicCamera(3f, 3f);
        cam.position.set(1f, 1f, 1f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        assets.load("Dave.g3db", Model.class);
        assets.load("Dave.png", Texture.class);
        assets.finishLoading();

        Model characterModel = assets.get("Dave.g3db", Model.class);
        ModelInstance charInstance = new ModelInstance(characterModel);
        instances.add(charInstance);

        animationController = new AnimationController(charInstance);
        animationController.animate(charInstance.animations.get(0).id, -1, 1f, null, 0.2f); // Starts the animaton

    }


    @Override
    public void render() {

        camController.update();

        animationController.update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        Matrix4 mvpMatrix = new Matrix4();
        Matrix4 nMatrix = new Matrix4();

        texture.bind();
        charShader.begin();
        charShader.setUniformi(Constants.U_TEXTURE, 0);
        // Bind whatever uniforms / textures you need
        for (ModelInstance charInstance : instances) {
            Array<Renderable> renderables = new Array<Renderable>();
            final Pool<Renderable> pool = new Pool<Renderable>() {
                @Override
                protected Renderable newObject() {
                    return new Renderable();
                }

                @Override
                public Renderable obtain() {
                    Renderable renderable = super.obtain();
//                    renderable.lights = null;
                    renderable.material = null;
                    renderable.mesh = null;
                    renderable.shader = null;
                    return renderable;
                }
            };
            charInstance.getRenderables(renderables, pool);
            Matrix4 idtMatrix = new Matrix4().idt();
            float[] bones = new float[BONES * 16];
            for (int i = 0; i < bones.length; i++)
                bones[i] = idtMatrix.val[i % 16];
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


//        shaderProgram.begin();
//        shaderProgram.setUniformMatrix(Constants.U_MVP, cam.combined);
//        shaderProgram.setUniformi(Constants.U_TEXTURE, 0);
//        objMesh.render(shaderProgram, GL20.GL_TRIANGLES);
//        shaderProgram.end();
    }

    @Override
    public void dispose() {
//        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }
}