package soap.client;

import javax.xml.ws.WebFault;

@WebFault
public class WsBusinessException extends WsException {

    public WsBusinessException(String message) {
        super(message);
    }

    public WsBusinessException(String message, FaultBean faultBean) {
        super(message, faultBean);
    }

    public WsBusinessException(String message, FaultBean faultBean, Throwable e) {
        super(message, faultBean, e);
    }

}
