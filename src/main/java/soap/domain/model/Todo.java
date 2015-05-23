package soap.domain.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

@Data
public class Todo implements Serializable {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = Create.class)
    private String todoId;
    @NotNull
    private String title;
    private String description;
    private boolean finished;
    private Date createdAt;

}
