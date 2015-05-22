package soap.domain.service.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soap.domain.model.Todo;

import java.util.Date;

@Transactional
@Service
public class TodoServiceImpl implements TodoService {

    public Todo getTodo(String todoId) {
        if("systemError".equals(todoId)){
            throw new NullPointerException();
        }
        Todo todo = new Todo();
        todo.setTodoId(todoId);
        todo.setTitle("title");
        todo.setDescription("description");
        todo.setFinished(true);
        todo.setCreatedAt(new Date());
        return todo;
    }

}
