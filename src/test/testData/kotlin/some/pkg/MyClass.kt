class MyClass {
    // #1
    fun greeting(): String {
        return "Hello, world!"
    }

    // #2
    fun farewell(fakeChild: myInnerClass?, names: List<String>): String {
        return "Goodbye, cruel world!"
    }

    internal inner class MyInnerClass {
        // #3
        fun innerGreeting(): String {
            return "Hello, inner!"
        }

        internal inner class MyInnerInnerClass {
            // #4
            fun innerInnerGreeting(): String {
                return "Hello, inner inner!"
            }

            // #5
            fun innerInnerFarewell(): String {
                return "Goodbye, cruel inner inner!"
            }
        }

        // #6
        fun innerFarewell(): String {
            return "Goodbye, cruel inner!"
        }
    }

    internal class MyStaticInnerClass {
        // #7
        fun innerGreeting(): String {
            return "Hello, static inner!"
        }

        // #8
        fun innerFarewell(): String {
            return "Goodbye, cruel static inner!"
        }
    }

    companion object {
        // #9
        fun generateStatic(): String {
            return "&)*(<$&Y*()$&*()<"
        }
    }
}

private class PrivateTopLevelClass {
    // #10
    fun privateTopLevelGreeting(): String {
        return "Hello, private top level!"
    }

    // #11
    fun privateTopLevelFarewell(): String {
        return "Goodbye, cruel private top level!"
    }
}

// # 12
fun topLevelFunction(): String = "Hello. top level!"

