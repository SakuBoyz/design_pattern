import java.util.ArrayList;
import java.util.List;

class Buffer {
    private char[] characters;
    private int lineWidth;

    public Buffer(int lineHeight, int lineWidth) {
        this.lineWidth = lineWidth;
        characters = new char[lineHeight * lineWidth];
        for (int i = 0; i < characters.length; i++) {
            characters[i] = '*';
        }
    }

    public char charAt(int x, int y) {
        return characters[y * lineWidth + x];
    }
}

class Viewport {
    private char[] characters;
    private final Buffer buffer;
    private final int width;
    private final int height;
    private final int offsetX;
    private final int offsetY;

    public Viewport(Buffer buffer, int width, int height, int offsetX, int offsetY)

    {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        characters = new char[width * height];
    }

    public char charAt(int x, int y) {

        if (x >= offsetX && y >= offsetY)
            return buffer.charAt(x - offsetX, y - offsetY);

        else
            return characters[y * width + x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

class Screen {
    private List<Viewport> viewports = new ArrayList<>();
    int width, height;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static Screen newScreenFacade(int width, int height, int offsetX, int offsetY)

    {
        Buffer buffer = new Buffer(width, height);

        Viewport viewport = new Viewport(buffer, width, height, offsetX, offsetY);

        Screen screen = new Screen(width, height);
        screen.addViewPort(viewport);
        return screen;
    }

    public void addViewPort(Viewport viewport) {
        viewports.add(viewport);
    }

    public void render() {
        for (Viewport vp : viewports) {
            for (int y = 0; y < vp.getHeight(); y++) {
                for (int x = 0; x < vp.getWidth(); x++) {
                    System.out.print(vp.charAt(x, y));
                }
                System.out.println();
            }
        }
    }
}

class Alphabet {
    public char character;
    public boolean isCapitalised;
    public boolean bold, italic, underlined;

    public Alphabet(char character) {
        this.character = character;
    }
}

class FormattedText {
    private Alphabet[] alphabets;

    public FormattedText(String text) {
        alphabets = new Alphabet[text.length()];
        for (int i = 0; i < text.length(); i++) {
            alphabets[i] = new Alphabet(text.charAt(i));
        }
    }

    public void capitalised(int start, int end) {
        for (int i = start; i <= end; i++) {
            alphabets[i].isCapitalised = true;
        }
    }

    public int getNumberofObjects() {
        return alphabets.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < alphabets.length; i++) {

            sb.append(alphabets[i].isCapitalised ? Character.toUpperCase(alphabets[i].character)
                    : alphabets[i].character);

        }
        return sb.toString();
    }
}

class TextRange {
    public int start, end;
    public boolean isCapitalised, isBold, isItalic, isUnderlined;

    public TextRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean covers(int position) {
        return position >= start && position <= end;
    }
}

class FlyweightFormattedText {
    private int[] characterIndex;
    private List<Alphabet> alphabetPool = new ArrayList<>();
    private List<TextRange> formatting = new ArrayList<>();

    public int getNumberofObjects() {
        return alphabetPool.size();
    }

    public FlyweightFormattedText(String text) {
        characterIndex = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            boolean foundChar = false;
            for (int j = 0; j < alphabetPool.size(); j++) {
                Alphabet a = alphabetPool.get(j);
                if (a.character == c) {
                    characterIndex[i] = j;
                    foundChar = true;
                    break;
                }
            }
            if (!foundChar) {
                Alphabet alphabet = new Alphabet(c);
                alphabetPool.add(alphabet);
                characterIndex[i] = alphabetPool.indexOf(alphabet);
            }
        }
    }

    public TextRange getRange(int start, int end) {
        TextRange range = new TextRange(start, end);
        formatting.add(range);
        return range;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < characterIndex.length; i++) {
            char c = alphabetPool.get(characterIndex[i]).character;
            for (TextRange range : formatting) {
                if (range.covers(i) && range.isCapitalised) {
                    c = Character.toUpperCase(c);
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
}

abstract class Model {
    public abstract double getBaseArea();

    public abstract double getHeight();

    // template method
    public double getVolume() {
        return getBaseArea() * getHeight();
    }
}

class MyCylinder extends Model {
    private double radius;
    private double height;

    public MyCylinder(double radius, double height) {
        this.radius = radius;
        this.height = height;
    }

    @Override
    public double getBaseArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getHeight() {
        return height;
    }
}

class MyBox extends Model {
    private double width;
    private double length;
    private double height;

    public MyBox(double width, double length, double height) {
        this.width = width;
        this.length = length;
        this.height = height;
    }

    @Override
    public double getBaseArea() {
        return width * length;
    }

    @Override
    public double getHeight() {
        return height;
    }
}

public class Lab11Demo {
    public static void main(String[] args) {
        System.out.println("=====Facade=====");
        // Buffer buffer = new Buffer(30, 20);
        // Viewport viewport = new Viewport(buffer, 30, 20, 10, 10);
        // Screen screen = new Screen(30, 20);
        // screen.addViewPort(viewport);
        // screen.render();

        Screen screenFromFacade = Screen.newScreenFacade(30, 20, 10, 10);
        screenFromFacade.render();

        System.out.println("=====Flyweight=====");
        FormattedText ft = new FormattedText("Make America Great Again");
        System.out.println(ft);
        ft.capitalised(5, 11);
        System.out.println(ft);
        System.out.println(ft.getNumberofObjects());

        FlyweightFormattedText fft = new FlyweightFormattedText("Make America Great Again");

        System.out.println(fft);
        System.out.println(fft.getNumberofObjects());
        fft.getRange(5, 11).isCapitalised = true;
        System.out.println(fft);

        Model cylinder = new MyCylinder(4.0, 10.0);
        System.out.println("Cylinder has volume of " + cylinder.getVolume());
        Model box = new MyBox(3.0, 4.0, 5.0);
        System.out.println("Box has volume of " + box.getVolume());
    }
}
