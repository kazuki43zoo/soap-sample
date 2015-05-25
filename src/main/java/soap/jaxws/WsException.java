package soap.jaxws;

import java.util.ArrayList;
import java.util.List;

public class WsException extends Exception {

    private final List<WsError> errors = new ArrayList<>();

    public void addError(String code, String message, String path) {
        errors.add(new WsError(code, message, path));
    }

    public List<WsError> getErrors() {
        return errors;
    }

}
