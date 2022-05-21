import java.util.*;
import java.util.Scanner;

class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x = " + x +
                ", y = " + y +
                "}";
    }
}

class Line {
    public Point start, end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
}

class VectorObject extends ArrayList<Line> {
}

class VectorRectangle extends VectorObject {
    public VectorRectangle(int x, int y, int width, int height) {
        add(new Line(new Point(x, y), new Point(x + width, y)));
        add(new Line(new Point(x + width, y), new Point(x + width, y + height)));
        add(new Line(new Point(x + width, y + height), new Point(x, y + height)));
        add(new Line(new Point(x, y + height), new Point(x + width, y)));
    }
}

class LineToPointAdapter extends ArrayList<Point> {
    public LineToPointAdapter(Line line) {
        int dx = Math.abs(line.end.x - line.start.x);
        int dy = Math.abs(line.end.y - line.start.y);
        int sx = (line.start.x < line.end.x) ? 1 : -1;
        int sy = (line.start.y < line.end.y) ? 1 : -1;
        boolean isSwap = false;
        if (dy > dx) {
            int temp = dx;
            dx = dy;
            dy = temp;
            isSwap = true;
        }
        int D = 2 * dy - dx;
        int x = line.start.x;
        int y = line.start.y;
        for (int i = 1; i <= dx; i++) {
            add(new Point(x, y));
            if (D >= 0) {
                if (isSwap)
                    x += sx;
                else
                    y += sy;
                D -= 2 * dx;
            }
            if (isSwap)
                y += sy;
            else
                x += sx;
            D += 2 * dy;
        }
    }
}

interface Drivable {
    void drive();
}

class Car implements Drivable {
    Driver driver;

    public Car(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void drive() {
        System.out.println("Car being driven by " + driver.name);

        if (driver.alcoholLevel >= 50)
            System.out.println(driver.name + " is dead!");

    }
}

class Driver {
    public String name;
    public int age;
    public int alcoholLevel;

    public Driver(String name, int age) {
        this.name = name;
        this.age = age;
        alcoholLevel = 0;
    }

    public void drinkBeer() {
        alcoholLevel += 25;
    }
}

class CarProxy extends Car {
    public CarProxy(Driver driver) {
        super(driver);
    }

    @Override
    public void drive() {
        if (driver.age < 16) {
            System.out.println(driver.name + " is too young");
            return;
        }
        if (driver.alcoholLevel < 50)
            super.drive();
        else
            System.out.println(driver.name + " is too drunk!");
    }
}

class Phone {
    public enum State {
        STANDBY,
        CONNECTING,
        CONNECTED,
        ON_HOLD,
        STOP
    }

    public enum Trigger {
        CALL_DIALED,
        HUNG_UP,
        CALL_CONNECTED,
        PLACED_ON_HOLD,
        TAKEN_OFF_HOLD,
        STOP_USING_PHONE
    }

    public State currentState = State.STANDBY;
    public State exitState = State.STOP;
    public Map<State, List<Pair<Trigger, State>>> rules = new HashMap<>();

    public Phone() {
        rules.put(State.STANDBY, List.of(
                new Pair<>(Trigger.CALL_DIALED, State.CONNECTING),
                new Pair<>(Trigger.STOP_USING_PHONE, State.STOP)));
        rules.put(State.CONNECTING, List.of(
                new Pair<>(Trigger.CALL_CONNECTED, State.CONNECTED),
                new Pair<>(Trigger.HUNG_UP, State.STANDBY)));
        rules.put(State.CONNECTED, List.of(
                new Pair<>(Trigger.PLACED_ON_HOLD, State.ON_HOLD),
                new Pair<>(Trigger.HUNG_UP, State.STANDBY)));
        rules.put(State.ON_HOLD, List.of(
                new Pair<>(Trigger.TAKEN_OFF_HOLD, State.CONNECTING),
                new Pair<>(Trigger.HUNG_UP, State.STANDBY)));
    }
}

class Pair<K, V> {
    K value1;
    V value2;

    public Pair(K value1, V value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
}

public class Lab12Demo {

    private final static List<VectorObject> vectorObjects = new ArrayList<>(
            Arrays.asList(
                    new VectorRectangle(1, 1, 10, 10),
                    new VectorRectangle(3, 3, 6, 6)));

    public static void drawPoint(Point p) {
        System.out.println(p);
    }

    public static void main(String[] args) {
        System.out.println("===== ADAPTER =====");

        for (VectorObject vo : vectorObjects) {
            System.out.println("==new vector object==");
            for (Line line : vo) {
                System.out.println("==new line==");
                LineToPointAdapter adapter = new LineToPointAdapter(line);
                for (Point p : adapter) {
                    drawPoint(p);
                }
            }
        }
        System.out.println("===== PROXY =====");

        Driver driver1 = new Driver("A", 30);
        Car car1 = new Car(driver1);
        car1.drive();
        driver1.drinkBeer();
        car1.drive();
        driver1.drinkBeer();
        car1.drive();

        // A is DEAD

        Driver driver2 = new Driver("B", 40);
        Car car2 = new CarProxy(driver2);
        car2.drive();
        driver2.drinkBeer();
        car2.drive();
        driver2.drinkBeer();
        car2.drive();

        // B survives

        Driver driver3 = new Driver("C", 8);
        Car car3 = new CarProxy(driver3);
        car3.drive();

        // C cannot drive

        System.out.println("===== STATE =====");

        Phone phone = new Phone();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("The phone is currently " + phone.currentState);
            System.out.println("Select a trigger:");
            for (int i = 0; i < phone.rules.get(phone.currentState).size(); i++) {

                System.out.println(i + ". " + phone.rules.get(phone.currentState).get(i).value1);

            }
            System.out.print("Please enter your choice: ");
            int choice = in.nextInt();

            phone.currentState = phone.rules.get(phone.currentState).get(choice).value2;

            if (phone.currentState == phone.exitState)
                break;
        }
        System.out.println("DONE");
    }
}
