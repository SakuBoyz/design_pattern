import java.util.ArrayList;
import java.util.List;

class Person extends Observable<Person> {

    public String name;
    public ChatRoomMediator room;
    private List<String> chatLog = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    public void receive(String sender, String message) {
        String s = sender + ": '" + message + "'";
        chatLog.add(s);
        System.out.println("[" + name + "'s chat session] " + s);
    }

    public void say(String message) {
        String s = "me: '" + message + "'";
        chatLog.add(s);
        room.broadcast(name, message);
    }

    public void printChatLog() {
        System.out.println("[" + name + "'s chatlog]");
        for (String aLog : chatLog) {
            System.out.println(aLog);
        }
    }

    public void privateMessage(String destination, String message) {
        String s = "me to " + destination + ": '" + message + "'";
        chatLog.add(s);
        room.message(name, destination, message);
    }

    public void setName(String name) {
        if (this.name.equals(name))
            return;
        propertyChanged(this, "name", name);
        this.name = name;
    }
}

class ChatRoomMediator {

    private List<Person> people = new ArrayList<>();

    public void broadcast(String source, String message) {
        for (Person person : people) {
            if (!person.name.equals(source)) {
                person.receive(source, message);
            }
        }
    }

    public void join(Person p) {
        String joinMsg = p.name + " joins the chat";
        broadcast("room", joinMsg);
        p.room = this;
        people.add(p);
    }

    public void message(String source, String destination, String message) {
        for (Person person : people) {
            if (person.name.equals(destination)) {
                person.receive(source, message);
                return;
            }
        }
    }
}

class PropertyChangedEvent<T> {
    public T source;
    public String propertyName;
    public Object newValue;

    public PropertyChangedEvent(T source, String propertyName, Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.newValue = newValue;
    }
}

interface Observer<T> {
    void handle(PropertyChangedEvent<T> args);
}

class Observable<T> {
    private List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }

    protected void propertyChanged(T source, String propertyName, Object newValue)

    {
        for (Observer<T> o : observers) {
            o.handle(new PropertyChangedEvent(source, propertyName, newValue));
        }
    }
}

public class Lab10Demo implements Observer<Person> {

    static Person john, jane, simon;
    static ChatRoomMediator room;

    public Lab10Demo() {
        john.subscribe(this);
        jane.subscribe(this);
        simon.subscribe(this);
    }

    @Override
    public void handle(PropertyChangedEvent<Person> args) {

        String s = args.source.name + "'s " + args.propertyName + " has been changed to " + args.newValue;

        System.out.println(s);
        room.broadcast("room", s);
    }

    public static void main(String[] args) {
        // Mediator
        System.out.println("==Mediator==");
        john = new Person("John");
        jane = new Person("Jane");

        room = new ChatRoomMediator();
        room.join(john); // no one knows john
        john.say("hi room");

        room.join(jane); // john knows jane is joining
        john.say("hi again");
        jane.say("oh, hey john");

        simon = new Person("Simon");
        room.join(simon);
        simon.say("hi everyone!");
        jane.say("hi!");
        john.say("hello!");
        jane.privateMessage("Simon", "fancy a dinner tonight?");

        System.out.println();
        john.printChatLog();

        System.out.println();
        jane.printChatLog();

        System.out.println();
        simon.printChatLog();

        // OBSERVER
        new Lab10Demo();
        john.setName("FOREVER ALONE");
    }
}