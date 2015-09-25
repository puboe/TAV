package commons;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by puboe on 25/9/15.
 */
public abstract class GameObject {

    protected Vector3 position = new Vector3();
    protected Vector3 forward = new Vector3(0, 0, -1);
    protected Vector3 up = Vector3.Y;
    protected Vector3 right = Vector3.X;


}
