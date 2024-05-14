package C06_AbstractClass

object C06_AbstractClass extends App {
    abstract class Vehicle(wheels: Int){
        // abstract method
        def hasWindows: Boolean

        // concrete method
        def getNumWheels: Unit = println(s"I have $wheels wheels")
    }

    abstract class Accommodation(area: Int) {
        // Make the wheels accessible to subclasses by providing a protected getter method
        protected def getArea: Int = area
        def calculatePrice: Double
    }

    class Sedan extends Vehicle(4) {
        override def hasWindows: Boolean = true
    }

    class Motorbike extends Vehicle(2) {
        override def hasWindows: Boolean = false
    }

    // If you uncomment the below the program wont compile
//    class RV extends Vehicle(4) with Accommodation(20) {
//        override def hasWindows: Boolean = true
//        override def calculatePrice: Double = getArea * 2.25
//    }
}
