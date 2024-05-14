package C04_EasySyntax

object PersonScala extends App {
    class Person(val name: String, val age: Int)

    val person = new Person("Foo", 20)
    print("Scala: name is " + person.name)
}
