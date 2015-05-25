package soap.domain.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@Data
public class Todo implements Serializable {

    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private String todoId;

    @NotNull
    private String title;

    private String description;

    private boolean finished;

    private Date createdAt;

}
