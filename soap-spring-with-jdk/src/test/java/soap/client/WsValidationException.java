package soap.client;

import javax.xml.ws.WebFault;


@WebFault
public class WsValidationException extends WsException {

    public WsValidationException(String message) {
        super(message);
    }

    public WsValidationException(String message, FaultBean faultBean) {
        super(message, faultBean);
    }

    public WsValidationException(String message, FaultBean faultBean, Throwable e) {
        super(message, faultBean, e);
    }

}
