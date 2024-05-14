package C01_NonDeterministicBehaviour;

public class C01_NonDeterministicBehavior {
    private static int x = 0;

    public static void main(String[] args) {
        Thread incrementThread = new Thread(() -> {
            x += 2;
            System.out.println("1-Increment thread intermediate output: x = " + x);
            x += 4;
            System.out.println("2-Increment thread final output: x = " + x);
        });

        Thread multiplyThread = new Thread(() -> {
            x = x * 5;
            System.out.println("3-Multiply thread intermediate output: x = " + x);
            x = x * 2;
            System.out.println("4-Multiply thread final output: x = " + x);
        });

        incrementThread.start();
        multiplyThread.start();


        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
