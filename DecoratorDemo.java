interface Shape {
    String info();
}

class Circle implements Shape {
    private float radius;

    public Circle() {
    }

    public Circle(float radius) {
        this.radius = radius;
    }

    void resize(float factor) {
        radius *= factor;
    }

    @Override
    public String info() {
        return "A circle of radius " + radius;
    }
}

class Square implements Shape {
    private float side;

    public Square() {
    }

    public Square(float side) {
        this.side = side;
    }

    @Override
    public String info() {
        return "A square with side " + side;
    }
}

class ColorDecorator implements Shape {
    private Shape shape;
    private String color;

    public ColorDecorator(Shape shape, String color) {
        this.shape = shape;
        this.color = color;
    }

    @Override
    public String info() {
        return shape.info() + " has the color " + color;
    }
}

class TransparentDecorator implements Shape {
    private Shape shape;
    private int transparency;

    public TransparentDecorator(Shape shape, int transparency) {
        this.shape = shape;
        this.transparency = transparency;
    }

    @Override
    public String info() {
        return shape.info() + " has " + transparency + "% transparency";
    }
}

public class DecoratorDemo {
    public static void main(String[] args) {
        Circle circle = new Circle(10);
        System.out.println(circle.info());

        circle.resize(2);
        System.out.println(circle.info());

        ColorDecorator blueSquare = new ColorDecorator(new Square(20), "blue");
        System.out.println(blueSquare.info());

        TransparentDecorator newCircle = new TransparentDecorator(new ColorDecorator(new Circle(5), "green"), 50);
        System.out.println(newCircle.info());
        // cannot resize
        // ERROR!!
        // newCircle.resize(2);
    }
}