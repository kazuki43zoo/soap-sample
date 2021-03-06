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
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.util.List;

@Validated
@Component
@WebService
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file = "/META-INF/ws/handlers.xml")
public class TodoWeb {

    @Inject
    TodoService todoService;

    @WebMethod
    public List<Todo> getTodos() {
        return todoService.getTodos();
    }

    @WebMethod
    public Todo getTodo(@NotNull String todoId) throws WsValidationException, WsResourceNotFoundException {
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
    public void deleteTodo(@NotNull String todoId) throws WsValidationException {
        todoService.deleteTodo(todoId);
    }

    @WebMethod
    public void deleteTodos() {
        todoService.deleteTodos();
    }

}
