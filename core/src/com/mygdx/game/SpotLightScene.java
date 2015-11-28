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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import light.SpotLight;

public class SpotLightScene extends ApplicationAdapter {
    Texture texture;
    Mesh spaceshipMesh;
    ShaderProgram shaderProgram;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;
    SpotLight spotlight;

    @Override
    public void create() {
        texture = new Texture("ship.png");
        String vs = Gdx.files.internal("spot-light-VS.glsl").readString();
        String fs = Gdx.files.internal("spot-light-FS.glsl").readString();
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

        spotlight = new SpotLight(new Vector3(0f, 6f, 1f), new Vector3(1f, 0f, 1f), 1f, 0.9f, new float[]{0f, 1f, 0f, 1f}, 0f, 100f);
    }

    @Override
    public void render() {

        camera.update();

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        texture.bind();
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_mvp", camera.getCombined());
        shaderProgram.setUniformMatrix("u_model", new Matrix4());
        shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniformf("light_intensity", spotlight.getIntensity());
        shaderProgram.setUniformf("u_shininess", 1f);
        shaderProgram.setUniformf("cone_angel", spotlight.getAngle());
        shaderProgram.setUniform4fv("cone_direction", spotlight.getConeDirection(), 0, 4);
        shaderProgram.setUniform4fv("light_color", spotlight.getColorArray(), 0, 4);
        shaderProgram.setUniform4fv("light_position", spotlight.getPositionArray(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
