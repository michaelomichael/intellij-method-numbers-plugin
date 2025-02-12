public class MyClass {
    class MyInnerClass {
        public String innerGreeting() {
            return "Hello, inner!";
        }

        public String innerFarewell() {
            return "Hello, inner farewell!";
        }
    }

    public String greeting() {
        return "Hello, world!";
    }

    static class MyStaticInnerClass {
        public String innerGreeting() {
            return "Hello, inner!";
        }

        public String innerFarewell() {
            return "Hello, inner farewell!";
        }
    }

    public String farewell() {
        return "Farewell, cruel world!";
    }
}