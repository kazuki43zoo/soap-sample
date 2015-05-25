package soap.domain.service.todo;

import soap.domain.model.Todo;

public interface TodoService {

    Todo getTodo(String todoId);

    Todo createTodo(Todo todo);

    Todo updateTodo(Todo todo);

}
