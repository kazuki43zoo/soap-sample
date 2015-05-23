package soap.domain.service.todo;

import org.springframework.validation.annotation.Validated;
import soap.domain.model.Todo;
import soap.ws.todo.ValidationException;

import javax.validation.Valid;
import javax.validation.groups.Default;

@Validated
public interface TodoService {

    Todo getTodo(String todoId);

    @Validated({Default.class, Todo.Create.class})
    Todo create(@Valid Todo todo);

}
