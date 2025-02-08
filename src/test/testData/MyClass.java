public class MyClass {
    // #1
    public String greeting() {
        return "Hello, world!";
    }

    // #2
    public String farewell() {
        return "Goodbye, cruel world!";
    }
    
    class MyInnerClass {
        // #3
        public String innerGreeting() {
            return "Hello, inner!";
        }

        class MyInnerInnerClass {
            // #4
            public String innerInnerGreeting() {
                return "Hello, inner inner!"
            }

            // #5
            public String innerInnerFarewell() {
                return "Goodbye, cruel inner inner!";
            }
        }

        // #6
        public String innerFarewell() {
            return "Goodbye, cruel inner!";
        }
    }
    
    static class MyStaticInnerClass {
        // #7
        public String innerGreeting() {
            return "Hello, static inner!";
        }

        // #8
        public String innerFarewell() {
            return "Goodbye, cruel static inner!";
        }
    }

    // #9
    public static String generateStatic() {
        return "&)*(<$&Y*()$&*()<"
    }

    private class PrivateTopLevelClass {
        // #10
        public String privateGreeting() {
            return "Hello, private!";
        }

        // #11
        public String privateFarewell() {
            return "Goodbye, cruel private!";
        }
    }
}        