package com.mygdx.game;

import camera.Camera;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import commons.Constants;
import commons.GameObject;

public class MyGdxGame extends ApplicationAdapter {

    ShaderProgram shaderProgram;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;
    Array<GameObject> objects = new Array<GameObject>();

    @Override
    public void create() {
        Texture texture = new Texture("ship.png");
        String vs = Gdx.files.internal("defaultVS.glsl").readString();
        String fs = Gdx.files.internal("defaultFS.glsl").readString();
        shaderProgram = new ShaderProgram(vs, fs);
        System.out.println(shaderProgram.getLog());
        ModelLoader<?> loader = new ObjLoader();
        ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));

        Mesh spaceshipMesh = new Mesh(true,
                data.meshes.get(0).vertices.length,
                data.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        spaceshipMesh.setVertices(data.meshes.get(0).vertices);
        spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);

        GameObject ship = new GameObject(new Vector3(0, 0, 0), spaceshipMesh, texture);
        objects.add(ship);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);

//        cam = new PerspectiveCamera(20, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam = new OrthographicCamera(3f, 3f);
//        cam.position.set(0f, 0f, 1f);
//        cam.lookAt(0, 0, 0);
//        cam.near = 0f;
//        cam.far = 10f;
//        cam.update();
//        camController = new CameraInputController(cam);
//        Gdx.input.setInputProcessor(camController);

        camera = new camera.OrthographicCamera(3f, 3f, 0f, 15f);
//        camera = new camera.PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0f, 1000f);
        camera.setPosition(0f, 0f, 3f);
        camera.lookAt(0, 0, 0);

    }

    @Override
    public void render() {

        camera.update();

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        shaderProgram.begin();

        for (GameObject obj : objects) {
            obj.getTexture().bind();
            shaderProgram.setUniformMatrix(Constants.U_MVP, camera.getCombined().cpy().mul(obj.getTRS()));
//            shaderProgram.setUniformMatrix(Constants.U_MODEL, obj.getTRS());
            shaderProgram.setUniformi(Constants.U_TEXTURE, 0);
            obj.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);
        }

        shaderProgram.end();
    }
}