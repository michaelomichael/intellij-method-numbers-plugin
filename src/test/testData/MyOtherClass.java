public class MyClass {
    public String greeting() {
        return "Hello, world!";
    }
    
    public String farewell() {
        return "Farewell, cruel world!";
    }
    
    class MyInnerClass {
        public String innerGreeting() {
            return "Hello, inner!";
        }
        
        public String innerFarewell() {
            return "Hello, inner farewell!";
        }
    }
    
    static class MyStaticInnerClass {
        public String innerGreeting() {
            return "Hello, inner!";
        }
        
        public String innerFarewell() {
            return "Hello, inner farewell!";
        }
    }
}        