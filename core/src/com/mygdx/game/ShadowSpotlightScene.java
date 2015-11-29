package com.mygdx.game;

import camera.Camera;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import commons.Box;
import light.Light;
import light.SpotLight;

/**
 * Created by riveign on 11/28/15.
 */
public class ShadowSpotlightScene  extends ApplicationAdapter{

    Texture texture;
    Mesh spaceshipMesh;
    ShaderProgram shaderProgram;
    com.badlogic.gdx.graphics.Camera cam;
    Camera camera;
    CameraInputController camController;
    SpotLight spotlight;
    Texture boxTexture;
    Box box;
    Mesh boxMesh;
    Matrix4 depthView;
    FrameBuffer spotlightShadowBuffer;
    ShaderProgram depthShader;

    @Override
    public void create() {
        texture = new Texture("ship.png");
        boxTexture = new Texture("texture.png");

        spotlightShadowBuffer = new FrameBuffer(Pixmap.Format.RGB888, 4096, 4096, true);

        box = new Box(new Vector3(0, 2f, 0f), new Vector3(0,0,0), new Vector3(0.5f,0.5f,0.5f));

        String vs = Gdx.files.internal("spot-light-shadow-VS.glsl").readString();
        String fs = Gdx.files.internal("spot-light-shadow-FS.glsl").readString();

        String dvs = Gdx.files.internal("SS-VS.glsl").readString();
        String dfs = Gdx.files.internal("SS-FS.glsl").readString();

        depthShader = new ShaderProgram(dvs, dfs);
        shaderProgram = new ShaderProgram(vs, fs);
        System.out.println(shaderProgram.getLog());
        System.out.println(depthShader.getLog());

        ModelLoader<?> loader = new ObjLoader();
        ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
        spaceshipMesh = new Mesh(true,
                data.meshes.get(0).vertices.length,
                data.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        spaceshipMesh.setVertices(data.meshes.get(0).vertices);
        spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);

        ModelLoader<?> boxLoader = new ObjLoader();
        ModelData boxData = boxLoader.loadModelData(Gdx.files.internal("box.obj"));
        boxMesh = new Mesh(true,
                boxData.meshes.get(0).vertices.length,
                boxData.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));

        boxMesh.setVertices(boxData.meshes.get(0).vertices);
        boxMesh.setIndices(boxData.meshes.get(0).parts[0].indices);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cam = new OrthographicCamera(3f, 3f);
        cam.position.set(0f, 5f, 5f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        //camera = new camera.OrthographicCamera(3f, 3f, 0f, 15f);
        //camera = new camera.PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0f, 1000f);
        //camera.setPosition(0f, 0f, 3f);
        //camera.lookAt(0, 0, 0);

        spotlight = new SpotLight(new Vector3(0f, 1f, 0f), new Vector3(1f, 0f, 1f), 1f, 0.8f, new float[]{0f, 1f, 0f, 1f}, 0f, 100f);
        depthView = spotlight.getCombined();

    }

    @Override
    public void render() {



        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);


        spotlightShadowBuffer.begin();
        renderDepthShader(spotlight);
        spotlightShadowBuffer.end();

        Texture shadowMap = spotlightShadowBuffer.getColorBufferTexture();
        shadowMap.bind(2);

        cam.update();


        texture.bind();
        boxTexture.bind(1);
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_mvp", cam.combined);
        shaderProgram.setUniformMatrix("u_model", new Matrix4());
        shaderProgram.setUniformMatrix("depth_mvp", depthView);
        shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniformf("u_shininess", 1f);
        shaderProgram.setUniformi("shadow_map", 2);
        shaderProgram.setUniformf("light_intensity", spotlight.getIntensity());
        shaderProgram.setUniformf("cone_angel", spotlight.getAngle());
        shaderProgram.setUniform4fv("cone_direction", spotlight.getConeDirection(), 0, 4);
        shaderProgram.setUniform4fv("light_color", spotlight.getColorArray(), 0, 4);
        shaderProgram.setUniform4fv("light_position", spotlight.getPositionArray(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();

        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_mvp", cam.combined.cpy().mul(box.getTRS()));
        shaderProgram.setUniformMatrix("u_model", box.getTRS());
        shaderProgram.setUniformMatrix("depth_mvp", depthView);
        shaderProgram.setUniformi("u_texture", 1);
        shaderProgram.setUniformf("u_shininess", 1f);
        shaderProgram.setUniformi("shadow_map", 2);
        shaderProgram.setUniformf("light_intensity", spotlight.getIntensity());
        shaderProgram.setUniformf("cone_angel", spotlight.getAngle());
        shaderProgram.setUniform4fv("cone_direction", spotlight.getConeDirection(), 0, 4);
        shaderProgram.setUniform4fv("light_color", spotlight.getColorArray(), 0, 4);
        shaderProgram.setUniform4fv("light_position", spotlight.getPositionArray(), 0, 4);
        boxMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();

    }

    private void renderDepthShader(Light light) {

        depthShader.begin();

        texture.bind(0);
        depthShader.setUniformMatrix("u_mvp", light.getCombined().cpy().mul(new Matrix4()));
        spaceshipMesh.render(depthShader, GL20.GL_TRIANGLES);

        boxTexture.bind(0);
        depthShader.setUniformMatrix("u_mvp", light.getCombined().cpy().mul(box.getTRS()));
        boxMesh.render(depthShader, GL20.GL_TRIANGLES);

        depthShader.end();

    }

}
