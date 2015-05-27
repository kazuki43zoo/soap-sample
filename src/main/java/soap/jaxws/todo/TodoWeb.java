package soap.jaxws.todo;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import soap.domain.model.Todo;
import soap.domain.service.todo.TodoService;
import soap.jaxws.WsBusinessException;
import soap.jaxws.WsResourceNotFoundException;
import soap.jaxws.WsValidationException;

import javax.inject.Inject;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.List;

@Validated
@Component
@WebService
@HandlerChain(file = "/META-INF/ws/handlers.xml")
public class TodoWeb {

    @Inject
    TodoService todoService;

    @WebMethod
    public List<Todo> getTodos() {
        return todoService.getTodos();
    }

    @WebMethod
    public Todo getTodo(String todoId) throws WsResourceNotFoundException {
        return todoService.getTodo(todoId);
    }

    @Validated({Default.class, Todo.Create.class})
    @WebMethod
    public Todo createTodo(@Valid Todo todo) throws WsValidationException, WsBusinessException {
        return todoService.createTodo(todo);
    }

    @Validated({Default.class, Todo.Update.class})
    @WebMethod
    public Todo updateTodo(@Valid Todo todo) throws WsValidationException, WsResourceNotFoundException {
        return todoService.updateTodo(todo);
    }

    @WebMethod
    public void deleteTodo(String todoId) {
        todoService.deleteTodo(todoId);
    }

    @WebMethod
    public void deleteTodos() {
        todoService.deleteTodos();
    }

}
