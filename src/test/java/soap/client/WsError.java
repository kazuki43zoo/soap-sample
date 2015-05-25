package soap.client;

import lombok.Data;

@Data
public class WsError {
    private String code;
    private String message;
    private String path;
}
