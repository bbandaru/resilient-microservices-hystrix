package microservices.enterprise;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception{


        testTimeout();
        // testCircuitBreaker();


    }


    public static void testTimeout() {
        RemoteService service = new RemoteService(3_000);

        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest4"));
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(5_000);
        commandProperties.withExecutionTimeoutEnabled(true);
        config.andCommandPropertiesDefaults(commandProperties);

        try {
            RemoteServiceInvocationCommand cmd = new RemoteServiceInvocationCommand(config, service);
            String res = cmd.execute();
            System.out.println(res);
        } catch (HystrixRuntimeException hre) {
            System.out.println("Hystrix Exception " + hre.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testCircuitBreaker() throws Exception {
        RemoteService faultyService = new RemoteService(false);
        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));

        HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
        properties.withCircuitBreakerSleepWindowInMilliseconds(4000);
        properties.withExecutionIsolationStrategy
                (HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        properties.withCircuitBreakerEnabled(true);
        properties.withCircuitBreakerRequestVolumeThreshold(1);

        config.andCommandPropertiesDefaults(properties);

        for (int i = 0; i < 50; i++) {
            try {
                System.out.println("Run : " + i);
                RemoteServiceInvocationCommand cmd = new RemoteServiceInvocationCommand(config, faultyService);
                cmd.execute();
            } catch (HystrixRuntimeException hre) {
                System.out.println("Hystrix Exception " + hre.getMessage());
            }

            Thread.sleep(1000);
        }


    }
}
