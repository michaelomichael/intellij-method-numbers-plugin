package aaaa.bbbb

import io.kotest.core.spec.style.FunSpec

class KotestFunSpecExample : FunSpec({
    // 1
    test("Top-level test") {}
    // 2
    xtest("Top-level disabled test") {}

    context("Top-level context") {
        // 3
        test("Nested test") {}
        // 4
        xtest("Nested disabled test") {}

        context("Nested context") {
            // 5
            test("Nested nested context test") {}
            // 6
            xtest("Nested nested context disabled test") {}
        }

        listOf(1, 2).forEach {
            // 7
            test("Test in loop iteration $it") {}
            // 8
            xtest("Disabled test in loop iteration $it") {}
        }

        listOf(1, 2).forEach {
            context("Context in loop iteration $it") {
                // 9
                test("Test in looped context") {}
                // 10
                xtest("Disabled test in looped context") {}
            }
            xcontext("Disabled context in loop iteration $it") {
                // 11
                test("Test in disabled looped context") {}
                // 12
                xtest("Disabled test in disabled looped context") {}
            }
        }
    }

    xcontext("Top-level disabled context") {
        // 13
        test("Test in top-level disabled context") {}
        // 14
        xtest("Disabled test in top-level disabled context") {}
    }
})