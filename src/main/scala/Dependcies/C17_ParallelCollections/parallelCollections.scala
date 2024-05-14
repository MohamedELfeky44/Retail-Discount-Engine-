package C17_ParallelCollections

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.immutable.ParIterable

object parallelCollections extends App{
    def sumRange(n: Long): Long = {
        @tailrec
        def loop(n: Long, acc: Long): Long = {
            if (n <= 0) acc
            else {
                loop(n - 1, acc + n)
            }
        }
        loop(n, 0)
    }

    println("starting")
//    val startTime1 = System.nanoTime()
//
//    val array: Seq[Long] = Seq(1000000000L, 2000000000L, 3000000000L, 4000000000L, 5000000000L)
//    val newArray = array.map(sumRange)
//
//    println("Normal collection took: " + (System.nanoTime()-startTime1)/1000000 + " milliseconds")
//
//    val startTime2 = System.nanoTime()
//
//    val forkJoinPool = new java.util.concurrent.ForkJoinPool(5)
//
//    val parArray: ParIterable[Long] = array.par
//
//    parArray.tasksupport = new ForkJoinTaskSupport(forkJoinPool)
//
//    val newParArray = parArray.map(sumRange)
//    println("Parallel collection took: " + (System.nanoTime()-startTime2)/1000000 + " milliseconds")



//
//    //////////////////////////////

    val largeArray = Range(1, 10000000).toList

    val startTime3 = System.nanoTime()
    largeArray.map(x => x+2)

    println("collection took: " + (System.nanoTime()-startTime3)/1000000 + " milliseconds")


    val startTime4 = System.nanoTime()
    val forkJoinPool = new java.util.concurrent.ForkJoinPool(2)
    val parArray: ParIterable[Int] = largeArray.par
    parArray.tasksupport = new ForkJoinTaskSupport(forkJoinPool)
    val newParArray = parArray.map(x => x+2)
    println("Parallel collection took: " + (System.nanoTime()-startTime4)/1000000 + " milliseconds")
}



