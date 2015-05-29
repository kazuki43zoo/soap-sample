package soap.domain.service.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;
import soap.domain.model.Todo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Transactional
@Service
public class TodoServiceImpl implements TodoService {

    @Inject
    JodaTimeDateFactory dateFactory;

    private final ConcurrentHashMap<String, Todo> todos = new ConcurrentHashMap();

    public List<Todo> getTodos() {
        return new ArrayList<>(todos.values());
    }

    public Todo getTodo(String todoId) {
        if ("systemError".equals(todoId)) {
            throw new NullPointerException();
        }
        if (!todos.containsKey(todoId)) {
            throw new ResourceNotFoundException(ResultMessages.error().add("e.xx.fw.5001", todoId));
        }
        return todos.get(todoId);
    }


    public Todo createTodo(Todo todo) {
        if (todos.size() == 5) {
            throw new BusinessException(ResultMessages.error().add("e.xx.td.8001"));
        }
        todo.setTodoId(UUID.randomUUID().toString());
        todo.setCreatedAt(dateFactory.newDate());
        todos.put(todo.getTodoId(), todo);
        return todo;
    }

    public Todo updateTodo(Todo todo) {
        getTodo(todo.getTodoId());
        todos.put(todo.getTodoId(), todo);
        return todo;
    }

    @Override
    public void deleteTodo(String todoId) {
        todos.remove(todoId);
    }

    @Override
    public void deleteTodos() {
        todos.clear();
    }

}
