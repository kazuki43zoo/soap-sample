package soap.client;

import javax.xml.ws.WebFault;
import java.util.List;


public class WsException extends Exception {

    private FaultBean faultBean;

    public WsException(String message) {
        super(message);
    }

    public WsException(String message, FaultBean faultBean) {
        super(message);
        this.faultBean = faultBean;
    }

    public WsException(String message, FaultBean faultBean, Throwable e) {
        super(message, e);
        this.faultBean = faultBean;
    }

    public FaultBean getFaultInfo() {
        return faultBean;
    }

    public List<WsError> getErrors(){
        return getFaultInfo().getErrors();
    }

}
