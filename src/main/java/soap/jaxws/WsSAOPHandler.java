package soap.jaxws;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;


public class WsSAOPHandler implements SOAPHandler<SOAPMessageContext> {
    @Override
    public Set<QName> getHeaders() {
        System.out.println(getClass().getSimpleName() + ": getHeaders ...");
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        System.out.println(getClass().getSimpleName() + ": handleMessage ...");
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        try {
            System.out.println(        context.getMessage().getSOAPBody().getFault().getDetail().getTextContent());
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        System.out.println(getClass().getSimpleName() + ": handleFault ...");
        return true;
    }

    @Override
    public void close(MessageContext context) {
        System.out.println(getClass().getSimpleName() + ": close ...");
    }

}
