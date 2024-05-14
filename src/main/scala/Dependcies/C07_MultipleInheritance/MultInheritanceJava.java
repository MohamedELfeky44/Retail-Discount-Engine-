package C07_MultipleInheritance;

// slide 14
public class MultInheritanceJava {
    class Printer {
        public void print(String msg) {System.out.println(msg);}
    }

    class PrintHyphen extends Printer {
        public void print(String msg) {
            System.out.println("------------");
            super.print(msg);
        }
    }

    class PrintStar extends Printer  {
        public void print(String msg) {
            System.out.println("*********");
            super.print(msg);
        }
    }
    /*
    class Printing extends Printer, PrintHyphen, PrintStar{
        public void main(String[] args) {
            new Printing().print("Diamond Problem");
        }
    }
     */
}
