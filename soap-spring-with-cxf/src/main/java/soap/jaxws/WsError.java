package soap.jaxws;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WsError {
    private String code;
    private String message;
    private String path;
}
