package chapter4.checkpoint;

public class Who {
    public void foo(A1 a1) {
        a1.doA1();
    }
}

class A1 {
    public void doA1() {
        A2 a2 = new A2();
        a2.doA2(this);
    }

    public void doIt(final A3 a3) {
        a3.doIt();
    }
}

class A2 {
    public void doA2(final A1 a1) {
        A3 a3 = new A3();
        a1.doIt(a3);
    }
}

class A3 {
    public void doIt() {
    }
}