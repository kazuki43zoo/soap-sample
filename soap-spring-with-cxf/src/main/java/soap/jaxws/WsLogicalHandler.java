package soap.jaxws;

import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;


public class WsLogicalHandler implements LogicalHandler<LogicalMessageContext> {

    @Override
    public boolean handleMessage(LogicalMessageContext context) {
        System.out.println(getClass().getSimpleName() + ": handleMessage ...");
        return true;
    }

    @Override
    public boolean handleFault(LogicalMessageContext context) {
        System.out.println(getClass().getSimpleName() + ": handleFault ...");
        return true;
    }

    @Override
    public void close(MessageContext context) {
        System.out.println(getClass().getSimpleName() + ": close ...");
    }
}
