package soap.domain.service.todo;

import soap.domain.model.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> getTodos();

    Todo getTodo(String todoId);

    Todo createTodo(Todo todo);

    Todo updateTodo(Todo todo);

    void deleteTodo(String todoId);

    void deleteTodos();

}
