import java.util.*;

class AlignmentStrategy {
    String alignment = "";

    String adjustAlignment() {
        return alignment;
    }
}

class LeftAlignmentStrategy extends AlignmentStrategy {
    String alignment = "";

    @Override
    String adjustAlignment() {
        return alignment;
    }
}

class CenterAlignmentStrategy extends AlignmentStrategy {
    String alignment = "***";

    @Override
    String adjustAlignment() {
        return alignment;
    }
}

class RightAlignmentStrategy extends AlignmentStrategy {
    String alignment = "#####";

    @Override
    String adjustAlignment() {
        return alignment;
    }
}

class TextComposite {
    ArrayList<TextComposite> group;
    char character;
    String colour;
    private AlignmentStrategy alignment;

    // public TextComposite() {
    //     group = new ArrayList<TextComposite>();
    //     colour = "BLACK";
    // }

    public TextComposite(AlignmentStrategy alignment) {
        group = new ArrayList<TextComposite>();
        colour = "BLACK";
        this.alignment = alignment;
    }

    public void setCharacter(char aChar) {
        character = aChar;
    }

    public void setColour(String newColour) {
        colour = newColour;
        for (TextComposite child : group) {
            child.setColour(newColour);
        }
    }

    public char getCharacter() {
        return character;
    }

    public void add(TextComposite child) {
        group.add(child);
    }

    // public void print(int depth) {
    // System.out.println("[" + depth + "] " + character + ": colour is " + colour);
    // for (TextComposite child : group) {
    // child.print(depth + 1);
    // }
    // }

    public void print(int depth) {

        System.out.println(alignment.adjustAlignment() + "[" + depth + "] " +
                character + ": colour is " + colour);

        for (TextComposite child : group) {
            child.print(depth + 1);
        }
    }

    public TextComposite getChild(int index) {
        return group.get(index);
    }

}

public class COMPOSITE_STRATEGY {
    public static void main(String[] args) {
        // COMPOSITE
        // TextComposite aPage = new TextComposite();
        // aPage.setCharacter('A');
        // TextComposite lineOne = new TextComposite();
        // TextComposite charOne = new TextComposite();
        // charOne.setCharacter('B');
        // TextComposite charTwo = new TextComposite();
        // charTwo.setCharacter('C');
        // TextComposite charThree = new TextComposite();
        // charThree.setCharacter('D');
        // TextComposite groupOne = new TextComposite();
        // TextComposite charFour = new TextComposite();
        // charFour.setCharacter('E');
        // TextComposite charFive = new TextComposite();
        // charFive.setCharacter('F');

        // STRATEGY
        TextComposite aPage = new TextComposite(new LeftAlignmentStrategy());
        aPage.setCharacter('A');
        TextComposite lineOne = new TextComposite(new LeftAlignmentStrategy());
        TextComposite charOne = new TextComposite(new LeftAlignmentStrategy());
        charOne.setCharacter('B');
        TextComposite charTwo = new TextComposite(new RightAlignmentStrategy());
        charTwo.setCharacter('C');
        TextComposite charThree = new TextComposite(new RightAlignmentStrategy());
        charThree.setCharacter('D');
        TextComposite groupOne = new TextComposite(new CenterAlignmentStrategy());
        TextComposite charFour = new TextComposite(new CenterAlignmentStrategy());
        charFour.setCharacter('E');
        TextComposite charFive = new TextComposite(new CenterAlignmentStrategy());
        charFive.setCharacter('F');

        groupOne.add(charFour);
        groupOne.add(charFive);

        lineOne.add(charOne);
        lineOne.add(charTwo);
        lineOne.add(charThree);
        lineOne.add(groupOne);

        aPage.add(lineOne);
        aPage.print(0);
        System.out.println();

        groupOne.setColour("RED");
        aPage.print(0);
        System.out.println();

        aPage.setColour("GREEN");
        aPage.print(0);
        System.out.println();
    }
}
