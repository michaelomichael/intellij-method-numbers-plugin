package aaaa.bbbb;

public class MyClass {
    // #0
    public String greeting() {
        return "Hello, world!";
    }

    // #1
    public String farewell(MyInnerClass fakeChild, List<String> names) {
        return "Goodbye, cruel world!";
    }
    
    class MyInnerClass {
        // #2
        public String innerGreeting() {
            return "Hello, inner!";
        }

        class MyInnerInnerClass {
            // #3
            public String innerInnerGreeting() {
                return "Hello, inner inner!"
            }

            // #4
            public String innerInnerFarewell() {
                return "Goodbye, cruel inner inner!";
            }
        }

        // #5
        public String innerFarewell() {
            return "Goodbye, cruel inner!";
        }
    }
    
    static class MyStaticInnerClass {
        // #6
        public String staticInnerGreeting() {
            return "Hello, static inner!";
        }

        // #7
        public String staticInnerFarewell() {
            return "Goodbye, cruel static inner!";
        }
    }

    // #8
    public static String generateStatic() {
        return "&)*(<$&Y*()$&*()<"
    }
}        

private class PrivateTopLevelClass {
    // #9
    @Override public String privateTopLevelGreeting() {
        return "Hello, private top level!";
    }

    // #10
    @Override
    public String privateTopLevelFarewell() {
        return "Goodbye, cruel private top level!";
    }
}
