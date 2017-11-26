import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Level {

    private int width;
    private int height;
    private String title;
    private BufferedImage bgImage;
    private ArrayList<PhysicalObject> collidableObjects = new ArrayList<PhysicalObject>();
    private ArrayList<Platform> drawableObjects = new ArrayList<Platform>();

    public void addPlatform(Vector2 pos, double width, double height, SurfaceType surface) {
        Platform platform = new Platform.Builder(pos, width, height).surface(surface).build();
        for (PhysicalObject component : platform.components()) {
            addCollidableObject(component);
        }
        addDrawableObject(platform);
    }


    public void addCollidableObject(PhysicalObject obj) {

        collidableObjects.add(obj);
    }

    public void addDrawableObject(Platform obj) {

        drawableObjects.add(obj);
    }

    public void addFlies(int[] position, String type) {

    }

    public void setStartPosition() {

    }

    public void setEndPosition() {

    }

    public ArrayList<PhysicalObject> getCollidableObjects() {
        return collidableObjects;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public BufferedImage getBgImage() {
        return bgImage;
    }

    public void setBgImage(BufferedImage bgImage) {
        this.bgImage = bgImage;
    }

}
