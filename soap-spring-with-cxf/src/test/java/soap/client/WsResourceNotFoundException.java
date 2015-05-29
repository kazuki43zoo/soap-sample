package soap.client;

import javax.xml.ws.WebFault;

@WebFault
public class WsResourceNotFoundException extends WsException {

    public WsResourceNotFoundException(String message) {
        super(message);
    }

    public WsResourceNotFoundException(String message, FaultBean faultBean) {
        super(message, faultBean);
    }

    public WsResourceNotFoundException(String message, FaultBean faultBean, Throwable e) {
        super(message, faultBean, e);
    }

}
