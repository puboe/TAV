package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import commons.Box;
import commons.Constants;
import commons.GameObject;
import light.Light;
import light.SpotLight;

public class ShadowSpotlightScene extends ApplicationAdapter {

    ShaderProgram shaderProgram;
    Camera cam;
    CameraInputController camController;
    SpotLight spotLight;
    boolean firstTime;
    Array<GameObject> objects = new Array<GameObject>();
    Array<Light> lights = new Array<Light>();

    @Override
    public void create() {

        ModelLoader loader = new ObjLoader();

        // Cube
        Texture boxTexture = new Texture("texture.png");
        ModelData boxData = loader.loadModelData(Gdx.files.internal("box.obj"));
        Mesh boxMesh = new Mesh(true,
                boxData.meshes.get(0).vertices.length,
                boxData.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));

        boxMesh.setVertices(boxData.meshes.get(0).vertices);
        boxMesh.setIndices(boxData.meshes.get(0).parts[0].indices);
        Box box = new Box(new Vector3(-1f, 1f, 0f), boxMesh, boxTexture);
        objects.add(box);

        // Ship
        Texture texture = new Texture("ship.png");
        ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
        Mesh spaceshipMesh = new Mesh(true,
                data.meshes.get(0).vertices.length,
                data.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        spaceshipMesh.setVertices(data.meshes.get(0).vertices);
        spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
        GameObject ship = new GameObject(new Vector3(0, 0, 0), spaceshipMesh, texture);
        objects.add(ship);

        // Lights
        spotLight = new SpotLight(new Vector3(0f, 3f, 0f), new Vector3(1f, 0f, 1f), 1f, 0.54f, new float[]{0f, -1f, 0f, 1f}, 0f, 100f);
        spotLight.setRotation(new Vector3((float) (Math.PI * 1.5f), 0, 0));
        lights.add(spotLight);

        // Config
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);

        // Camera
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam = new OrthographicCamera(3f, 3f);
        cam.position.set(0f, 5f, 5f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        // Shader
        String vs = Gdx.files.internal("shaders/defaultVS.glsl").readString();
        String fs = Gdx.files.internal("shaders/spotLightFS.glsl").readString();
        shaderProgram = new ShaderProgram(vs, fs);
    }

    @Override
    public void render() {
        firstTime = true;

        // Config
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);

        FrameBuffer shadowMap;
        shadowMap = spotLight.generateShadowMap(objects);

        shadowMap.getColorBufferTexture().bind(1);
        for (GameObject object : objects) {
            shaderProgram.begin();
            object.getTexture().bind(0);
            shaderProgram.setUniformMatrix(Constants.U_MVP, cam.combined.cpy().mul(object.getTRS()));
            shaderProgram.setUniformMatrix(Constants.U_MODEL, object.getTRS());
            shaderProgram.setUniformMatrix("u_lightMVP", spotLight.getCombined().mul(object.getTRS())); //ver el bias
            shaderProgram.setUniformi(Constants.U_TEXTURE, 0);
            shaderProgram.setUniformi("u_shadowMap", 1);

            shaderProgram.setUniform4fv("light_color", spotLight.getColorArray(), 0, 4);
            shaderProgram.setUniform4fv("light_position", spotLight.getPositionArray(), 0, 4);
            //Especular
            shaderProgram.setUniform4fv("eye", new float[]{cam.position.x, cam.position.y, cam.position.z, 1}, 0, 4);
            shaderProgram.setUniform4fv("specular_color", new float[]{1, 1, 1, 1}, 0, 4);
            //Ambiente
            shaderProgram.setUniform4fv("ambient_color", new float[]{0, 0, 1, 1}, 0, 4);
            shaderProgram.setUniform4fv("light_direction", spotLight.getConeDirection(), 0, 4);
            // TODO DESHARDCODEAR ESTO.
            shaderProgram.setUniformf("cosine_inner", (float) Math.cos(Math.toRadians(45.4f)));
            shaderProgram.setUniformf("cosine_outter", (float) Math.cos(Math.toRadians(50f)));
//            shaderProgram.setUniformf("cosine_inner", (float) Math.cos(spotLight.getAngle()));
//            shaderProgram.setUniformf("cosine_outter", (float) Math.cos(spotLight.getAngle()+0.05f));
            object.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);

            shaderProgram.end();
        }
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
