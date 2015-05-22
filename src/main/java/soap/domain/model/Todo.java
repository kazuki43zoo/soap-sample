package soap.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Todo implements Serializable {

    private String todoId;
    private String title;
    private String description;
    private boolean finished;
    private Date createdAt;

}
