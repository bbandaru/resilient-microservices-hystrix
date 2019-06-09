package microservices.enterprise;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class RemoteServiceInvocationCommand extends HystrixCommand<String> {

    private RemoteService service;

    public RemoteServiceInvocationCommand(Setter setter, RemoteService service) {
        super(setter);
        this.service = service;
    }

    @Override
    protected String run() throws Exception {
        return service.invoke();
    }


//    @Override
//    protected String getFallback() {
//        return "Fallback Operation for Timeout Cmd ";
//    }
}
