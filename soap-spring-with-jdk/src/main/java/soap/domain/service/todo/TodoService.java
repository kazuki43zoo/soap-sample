package soap.domain.service.todo;

import org.springframework.validation.annotation.Validated;
import soap.domain.model.Todo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

@Validated
public interface TodoService {

    List<Todo> getTodos();

    Todo getTodo(@NotNull String todoId);

    @Validated({Default.class, Todo.Create.class})
    Todo createTodo(@Valid Todo todo);

    Todo updateTodo(Todo todo);

    void deleteTodo(String todoId);

    void deleteTodos();

}
