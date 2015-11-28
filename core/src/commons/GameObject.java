package commons;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by puboe on 25/9/15.
 */
public class GameObject extends BasicObject {

    private Mesh mesh;
    private Texture texture;

    public GameObject(Vector3 position, Mesh mesh, Texture texture) {
        this.position = position;
        this.mesh = mesh;
        this.texture = texture;
    }

    public GameObject(Vector3 position, Vector3 rotation, Vector3 scale, Mesh mesh, Texture texture) {
        this(position, mesh, texture);
        this.rotation = rotation;
        this.scale = scale;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
