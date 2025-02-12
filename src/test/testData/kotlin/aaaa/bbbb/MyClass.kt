package aaaa.bbbb

class MyClass {
    // #0
    fun greeting(): String {
        return "Hello, world!"
    }

    // #1
    fun farewell(fakeChild: myInnerClass?, names: List<String>): String {
        return "Goodbye, cruel world!"
    }

    internal inner class MyInnerClass {
        // #2
        fun innerGreeting(): String {
            return "Hello, inner!"
        }

        internal inner class MyInnerInnerClass {
            // #3
            fun innerInnerGreeting(): String {
                return "Hello, inner inner!"
            }

            // #4
            fun innerInnerFarewell(): String {
                return "Goodbye, cruel inner inner!"
            }
        }

        // #5
        fun innerFarewell(): String {
            return "Goodbye, cruel inner!"
        }
    }

    internal class MyStaticInnerClass {
        // #6
        fun innerGreeting(): String {
            return "Hello, static inner!"
        }

        // #7
        fun innerFarewell(): String {
            return "Goodbye, cruel static inner!"
        }
    }

    companion object {
        // #8
        fun generateStatic(): String {
            return "&)*(<$&Y*()$&*()<"
        }
    }
}

private class PrivateTopLevelClass {
    // #9
    fun privateTopLevelGreeting(): String {
        return "Hello, private top level!"
    }

    // #10
    fun privateTopLevelFarewell(): String {
        return "Goodbye, cruel private top level!"
    }
}

// # 11
fun topLevelFunction(): String = "Hello. top level!"

