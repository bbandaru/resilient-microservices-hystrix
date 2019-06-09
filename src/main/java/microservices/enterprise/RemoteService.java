package microservices.enterprise;

import java.util.Random;

public class RemoteService {
    private long wait;
    private boolean isFaulty;

    public RemoteService(long wait) {
        this.wait = wait;
        isFaulty = false;
    }

    public RemoteService(boolean isFaulty) {
        this.isFaulty = isFaulty;
    }

    String invoke() throws InterruptedException {
        System.out.println("Service invoked.... ");

        Random rand = new Random();
        if (rand.nextInt(100) % 2 == 0) {
            isFaulty = true;
        } else {
            isFaulty = false;
        }

        if (!isFaulty) {
            Thread.sleep(wait);
            System.out.println("Service Behavior : Successful");
            return "Service Response : Successful! ";
        }
        System.out.println("Service Behavior : Faulty");
        throw new InterruptedException("IO Exception");
    }



}
