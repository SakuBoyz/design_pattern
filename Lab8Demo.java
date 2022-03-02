import java.util.ArrayList;
import java.util.Collections;

class GraphicComposite {
    public String name;
    public String color;
    public int width;
    public int height;
    // public ArrayList<GraphicComposite> children = new ArrayList<>();
    public GraphicGroup children = new GraphicGroup();

    private void print(StringBuilder stringBuilder, int depth) {
        stringBuilder.append(String.join("", Collections.nCopies(depth, "*")))
                .append(depth > 0 ? " " : "")
                .append((color == null || color.isEmpty()) ? "" : color + " ")
                .append((name == null) ? "==GROUP==" : name + " ")
                .append((name == null) ? "" : "Width = " + width + " ")
                .append((name == null) ? "" : "Height = " + height + " ")
                .append(System.lineSeparator());
        for (GraphicComposite child : children)
            child.print(stringBuilder, depth + 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        print(sb, 0);
        return sb.toString();
    }

    // @Override
    // public String toString() {
    // String returnString = color + name + width + height + System.lineSeparator();
    // for (GraphicComposite child : children)
    // returnString = returnString + child.toString() + System.lineSeparator();
    // return returnString;
    // }

    public GraphicComposite() {
    }

    public void resize(int scale) {
        this.width *= scale;
        this.height *= scale;
        children.resizeAll(scale);
    }
}

class GraphicGroup extends ArrayList<GraphicComposite> {
    public GraphicGroup() {
    }

    public void resizeAll(int scale) {
        for (GraphicComposite g : this) {
            g.resize(scale);
        }
    }
}

class Oval extends GraphicComposite {
    public Oval(String color, int width, int height) {
        name = "Oval";
        this.color = color;
        this.width = width;
        this.height = height;
    }
}

class Rectangle extends GraphicComposite {
    public Rectangle(String color, int width, int height) {
        name = "Rectangle";
        this.color = color;
        this.width = width;
        this.height = height;
    }
}

public class Lab8Demo {
    public static void main(String[] args) {
        GraphicComposite drawing = new GraphicComposite();
        drawing.children.add(new Rectangle("Red", 100, 100));
        drawing.children.add(new Oval("Yellow", 200, 200));
        GraphicComposite group = new GraphicComposite();
        group.children.add(new Oval("Blue", 300, 300));
        group.children.add(new Rectangle("Blue", 400, 400));
        drawing.children.add(group);
        System.out.println(drawing);

        System.out.println("RESIZE GROUP 4x");
        group.resize(4);
        System.out.println(drawing);
        System.out.println("RESIZE DRAWING 2x");
        drawing.resize(2);
        System.out.println(drawing);
    }
}
