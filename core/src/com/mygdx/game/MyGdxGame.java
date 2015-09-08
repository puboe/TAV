package com.mygdx.game;

import camera.Camera;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class MyGdxGame extends ApplicationAdapter {
    Texture img;
    Mesh spaceshipMesh;
    ShaderProgram shaderProgram;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;

    @Override
    public void create() {
        img = new Texture("ship.png");
        String vs = Gdx.files.internal("defaultVS.glsl").readString();
        String fs = Gdx.files.internal("defaultFS.glsl").readString();
        shaderProgram = new ShaderProgram(vs, fs);
        System.out.println(shaderProgram.getLog());
        ModelLoader<?> loader = new ObjLoader();
        ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));

        spaceshipMesh = new Mesh(true,
                data.meshes.get(0).vertices.length,
                data.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        spaceshipMesh.setVertices(data.meshes.get(0).vertices);
        spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);

        cam = new PerspectiveCamera(20, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam = new OrthographicCamera(3f, 3f);
        cam.position.set(0f, 0f, 1f);
        cam.lookAt(0, 0, 0);
        cam.near = 0f;
        cam.far = 10f;
        cam.update();
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

//        camera = new camera.OrthographicCamera(3f, 3f, 0f, 10f);
        camera = new camera.PerspectiveCamera(20, 5f, 10f, 0f, 10f);
        camera.setPosition(1f, 1f, 1f);
        camera.lookAt(0, 0, 0);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        img.bind();
        shaderProgram.begin();
//        shaderProgram.setUniformMatrix("u_worldView", cam.combined);
        shaderProgram.setUniformMatrix("u_worldView", camera.getCombined());
        shaderProgram.setUniformi("u_texture", 0);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
