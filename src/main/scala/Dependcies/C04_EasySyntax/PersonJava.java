package C04_EasySyntax;

public class PersonJava {
    public static class Person {
        public final String name;
        public final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    public static void main(String[] args){
        Person person = new Person("Foo", 20);
        System.out.println("Java: name is ".concat(person.name));
    }
}
