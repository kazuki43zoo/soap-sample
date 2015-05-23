package soap.domain.service.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import soap.domain.model.Todo;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Transactional
@Service
public class TodoServiceImpl implements TodoService {

    @Inject
    JodaTimeDateFactory dateFactory;

    private final ConcurrentHashMap<String, Todo> todos = new ConcurrentHashMap();

    public Todo getTodo(String todoId) {
        if ("systemError".equals(todoId)) {
            throw new NullPointerException();
        }
        return todos.get(todoId);
    }


    public Todo create(@Valid  Todo todo) {
        todo.setTodoId(UUID.randomUUID().toString());
        todo.setCreatedAt(dateFactory.newDate());
        todos.put(todo.getTodoId(), todo);
        return todo;
    }

}
