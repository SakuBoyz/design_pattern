import java.lang.UnsupportedOperationException;

class Document {
}

interface Machine {
    void print(Document d);

    void fax(Document d) throws Exception;

    void scan(Document d) throws Exception;
}

interface Printer {
    void print(Document d);
}

interface Scanner {
    void scan(Document d);
}

interface Fax {
    void fax(Document d);
}

class MultiFunctionPrinter implements Machine {
    @Override
    public void print(Document d) {
    }

    @Override
    public void fax(Document d) {
    }

    @Override
    public void scan(Document d) {
    }
}

class OldFashionPrinter implements Machine {
    @Override
    public void print(Document d) {
    }

    @Override
    public void fax(Document d) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void scan(Document d) {
        throw new UnsupportedOperationException();
    }
}

class JustAPrinter implements Printer {
    @Override
    public void print(Document d) {
    }
}

class Photocopier implements Printer, Scanner {
    @Override
    public void print(Document d) {
    }

    @Override
    public void scan(Document d) {
    }
}

/**
 * SOLID_I
 */
public class SOLID_I {

    public static void main(String[] args) {

    }
}