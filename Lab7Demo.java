import java.util.Stack;

interface RendererBridge {
    void renderCircle(float radiusX, float radiusY);

    void renderRect(float sideX, float sideY);
}

class VectorRendererBridge implements RendererBridge {
    @Override
    public void renderCircle(float radiusX, float radiusY) {

        System.out.println("Drawing a *vector* circle of radius X " + radiusX + " and radius Y " + radiusY);

    }

    @Override
    public void renderRect(float sideX, float sideY) {

        System.out.println("Drawing a *vector* rectangle of side X " + sideX + " and side Y " + sideY);

    }
}

class RasterRendererBridge implements RendererBridge {
    @Override
    public void renderCircle(float radiusX, float radiusY) {

        System.out.println("Drawing a *raster* circle of radius X " + radiusX + " and radius Y " + radiusY);

    }

    @Override
    public void renderRect(float sideX, float sideY) {

        System.out.println("Drawing a *raster* rectangle of side X " + sideX + " and side Y " + sideY);

    }
}

abstract class Shape {
    protected RendererBridge renderer;

    public Shape(RendererBridge renderer) {
        this.renderer = renderer;
    }

    public abstract void draw();

    public abstract void resize(float scaleX, float scaleY);
}

interface Prototype {
    Prototype clonePrototype();
}

class Circle extends Shape implements Prototype {
    public float radiusX, radiusY;

    public Circle(RendererBridge renderer) {
        super(renderer);
    }

    @Override
    public Prototype clonePrototype() {
        Circle newCircle = new Circle(renderer);
        newCircle.radiusX = radiusX;
        newCircle.radiusY = radiusY;
        return newCircle;
    }

    public Circle(RendererBridge renderer, float radius) {
        super(renderer);
        this.radiusX = radius;
        this.radiusY = radius;
    }

    public CircleMemento resizeMemento(float scaleX, float scaleY) {
        CircleMemento old = new CircleMemento(radiusX, radiusY);
        resize(scaleX, scaleY);
        return old;
    }

    public void restoreMemento(CircleMemento c) {
        this.radiusX = c.radiusX;
        this.radiusY = c.radiusY;
    }

    @Override
    public void draw() {
        renderer.renderCircle(radiusX, radiusY);
    }

    @Override
    public void resize(float scaleX, float scaleY) {
        radiusX *= scaleX;
        radiusY *= scaleY;
    }
}

class CircleMemento {
    public float radiusX, radiusY;

    public CircleMemento(float radiusX, float radiusY) {
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }
}

class MementoSingleton {
    private static MementoSingleton INSTANCE;

    private Stack<CircleMemento> mementos;

    protected MementoSingleton() {
        System.out.println("Singleton is initialising");
        mementos = new Stack<>();
    }

    public void setMemento(CircleMemento c) {
        mementos.push(c);
    }

    public CircleMemento getLastMemento() {
        return mementos.pop();
    }

    public static MementoSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MementoSingleton();
        }
        return INSTANCE;
    }
}

public class Lab7Demo {
    public static void main(String[] args) {
        Circle circle = new Circle(new RasterRendererBridge(), 5);

        System.out.println("==MEMENTO==");
        CircleMemento c1 = circle.resizeMemento(10, 20);
        CircleMemento c2 = circle.resizeMemento(20, 20);
        Stack<CircleMemento> mementos = new Stack<>();

        mementos.push(circle.resizeMemento(10, 10));
        mementos.push(circle.resizeMemento(20, 20));
        mementos.push(circle.resizeMemento(30, 30));

        circle.restoreMemento(mementos.pop());
        circle.draw();

        circle.restoreMemento(mementos.pop());
        circle.draw();

        System.out.println("==PROTOTYPE==");
        mementos.push(circle.resizeMemento(30, 30));
        Circle newCircle1 = (Circle) circle.clonePrototype();
        newCircle1.draw();
        circle.restoreMemento(mementos.pop());

        Circle newCircle2 = (Circle) circle.clonePrototype();
        newCircle2.draw();

        System.out.println("==SINGLETON==");
        MementoSingleton.getInstance().setMemento(circle.resizeMemento(10, 20));
        MementoSingleton.getInstance().setMemento(circle.resizeMemento(20, 20));
        circle.restoreMemento(MementoSingleton.getInstance().getLastMemento());
    }
}
