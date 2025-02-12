class MyClass {
    internal inner class MyInnerClass {
        fun innerGreeting(): String {
            return "Hello, inner!"
        }

        fun innerFarewell(): String {
            return "Hello, inner farewell!"
        }
    }

    fun greeting(): String {
        return "Hello, world!"
    }

    internal class MyStaticInnerClass {
        fun innerGreeting(): String {
            return "Hello, inner!"
        }

        fun innerFarewell(): String {
            return "Hello, inner farewell!"
        }
    }

    fun farewell(): String {
        return "Farewell, cruel world!"
    }
}