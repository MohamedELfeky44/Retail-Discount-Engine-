package C07_MultipleInheritance

// slide 14
object MultiInheritanceScala extends App {
    trait Printer {
        def print(msg: String): Unit = println(msg)
    }

    trait PrintHyphen extends Printer {
        override def print(msg: String): Unit = {
            println("-------------")
            super.print(msg)
        }
    }

    trait PrintStar extends Printer {
        override def print(msg: String): Unit = {
            println("*************")
            super.print(msg)
        }
    }

    class Printing extends Printer with PrintStar with PrintHyphen

    new Printing().print("Solving Diamond Problem")
}
