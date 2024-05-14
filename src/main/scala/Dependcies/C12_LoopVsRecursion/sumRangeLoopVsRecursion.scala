package C12_LoopVsRecursion

import scala.annotation.tailrec

object sumRangeLoopVsRecursion extends App {
    def greet(name: String): String = s"hello $name"



    // Imperative
    def sumRange(n: Int): Int = {
        var sum = 0
        for (i <- Range.inclusive(1, n)) {
            sum += i
        }
        sum
    }

    // Recursive
//    def sumRangeRec(n: Int): Int = {
//        if (n <= 0) 0
//        else n + sumRangeRec(n - 1)
//    }

    // Tail Recursive
    def sumRangeTailRec(n: Int): Int = {
        @tailrec
        def loop(n: Int, acc: Int): Int = {
            if (n <= 0) acc
            else {
                loop(n - 1, acc + n)
            }
        }
        loop(n, 0)
    }

    println("Starting")



    val startTime1 = System.nanoTime()
    println("Imperative")
    println("result: " + sumRange(100000) + " took: " + (System.nanoTime() - startTime1) / 1000 + " microseconds")

    val startTime2 = System.nanoTime()
    println("Recursive")
    println("result: " + sumRangeTailRec(100000) + " took: " + (System.nanoTime() - startTime2) / 1000 + " microseconds")






//def sumRange(n: Int): Int = {
//    @tailrec
//    def loop(n: Int, acc: Int): Int = {
//        if (n <= 0) acc
//        else loop(n - 1, acc + n)
//    }
//    loop(n, 0)
//}
//val startTime1 = System.nanoTime()
//sumRange(100000000)
//println("recursion took: " + (System.nanoTime()-startTime1)/1000000 + " milliseconds")
}