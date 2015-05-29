package soap.client;

import lombok.Data;

import java.util.List;

@Data
public class FaultBean {

    private List<WsError> errors;

}
