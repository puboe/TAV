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
import commons.Constants;
import light.PointLight;

public class PointLightScene extends ApplicationAdapter {
    Texture texture;
    Mesh spaceshipMesh;
    ShaderProgram shaderProgram;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;
    PointLight pointLight;

    @Override
    public void create() {

        texture = new Texture("ship.png");
        String vs = Gdx.files.internal("point-light-VS.glsl").readString();
        String fs = Gdx.files.internal("point-light-FS.glsl").readString();
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
        pointLight = new PointLight(new Vector3(0f, 1f, 0f), new Vector3(1f, 0f, 1f), 1f);
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
        shaderProgram.setUniformMatrix(Constants.U_MVP, camera.getCombined());
        shaderProgram.setUniformMatrix(Constants.U_MODEL, new Matrix4());
        shaderProgram.setUniformi(Constants.U_TEXTURE, 0);
        shaderProgram.setUniformf("light_intensity", pointLight.getIntensity());
        shaderProgram.setUniformf("u_shininess", 1f);
        shaderProgram.setUniform4fv("light_color", pointLight.getColorArray(), 0, 4);
        shaderProgram.setUniform4fv("light_position", pointLight.getPositionArray(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
