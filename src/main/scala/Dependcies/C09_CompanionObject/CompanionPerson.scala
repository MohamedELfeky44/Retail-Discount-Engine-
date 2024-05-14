package C09_CompanionObject

object CompanionPerson extends App {


    class Person(var name: String, var age: Int) {
        // Can access private fields of companion object
        val x = Person.height

        // This is an instance method because it depends on the instance's age
        def incrementAge = this.age + 1

        // This should be a static method as it doesnt depend on the instance. However, there is no static keyword in scala
        def greet(friend: String) = println(s"Hello $friend")
    }

    // This wont compile
    //Person.greet("khaled")

    var p = new Person("ahmed", 19)
    p.greet("khaled")

    object Person{
        private val height = 180

        // Methods here are static methods that can be called directly on class name
        def greet(friend: String) = println(s"Hello $friend")

        // Factory methods (multiple constructors)
        def apply(name: String, age: Int): Person = new Person(name, age)
        def apply(name:String): Person = new Person(name, -1)
        def apply(): Person = new Person("Unknown", -1)

        // De-Constructor (deconstructs the instance to its components)
        def unapply(p: Person): Tuple2[String, Int] = (p.name, p.age)
    }

    // This is now doable
    Person.greet("kholoud")

    // Creating new instances is easier using Factory methods
    var p1 = Person("sayed", 24)
    var p2 = Person("gamal")
    var p3 = Person()

    // De-Constructing a Person
    println(Person.unapply(p3))
}