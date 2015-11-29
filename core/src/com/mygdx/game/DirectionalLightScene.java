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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import commons.Box;
import commons.GameObject;
import light.DirectionalLight;

public class DirectionalLightScene extends ApplicationAdapter {

    ShaderProgram shaderProgram;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;
    DirectionalLight directionalLight;
    Array<GameObject> objects = new Array<GameObject>();

    @Override
    public void create() {
        Texture shipTexture = new Texture("ship.png");
        Texture boxTexture = new Texture("texture.png");

        String vs = Gdx.files.internal("directional-light-VS.glsl").readString();
        String fs = Gdx.files.internal("directional-light-FS.glsl").readString();

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

        ModelLoader<?> boxLoader = new ObjLoader();
        ModelData boxData = boxLoader.loadModelData(Gdx.files.internal("box.obj"));
        Mesh boxMesh = new Mesh(true,
                boxData.meshes.get(0).vertices.length,
                boxData.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));

        boxMesh.setVertices(boxData.meshes.get(0).vertices);
        boxMesh.setIndices(boxData.meshes.get(0).parts[0].indices);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);

        GameObject ship = new GameObject(new Vector3(0, 0, 0), spaceshipMesh, shipTexture);
        Box box = new Box(new Vector3(0, 3f, 0), boxMesh, boxTexture);
        objects.add(ship);
        objects.add(box);

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam = new OrthographicCamera(3f, 3f);
        cam.position.set(0f, 5f, 5f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        directionalLight = new DirectionalLight(new Vector3(3f, 3f, 0f), new Vector3(1f, 0f, 1f), 1f, new float[]{1f, 0f, 1f, 1f}, 0f, 100f);
    }

    @Override
    public void render() {

        cam.update();

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        shaderProgram.begin();

        for (GameObject obj : objects) {
            obj.getTexture().bind();
            shaderProgram.setUniformMatrix("u_mvp", cam.combined.cpy().mul(obj.getTRS()));
            shaderProgram.setUniformMatrix("u_model", obj.getTRS());
            shaderProgram.setUniformi("u_texture", 0);
            obj.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);

        }

        shaderProgram.setUniformf("u_shininess", 1f);
        shaderProgram.setUniformf("light_intensity", directionalLight.getIntensity());
        shaderProgram.setUniform4fv("cone_direction", directionalLight.getConeDirection(), 0, 4);
        shaderProgram.setUniform4fv("light_color", directionalLight.getColorArray(), 0, 4);
        shaderProgram.setUniform4fv("light_position", directionalLight.getPositionArray(), 0, 4);
        shaderProgram.end();
    }
}
