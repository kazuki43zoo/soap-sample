package soap.jaxws.todo;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import soap.domain.model.Todo;
import soap.domain.service.todo.TodoService;
import soap.jaxws.*;

import javax.inject.Inject;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.groups.Default;
import java.util.List;

@Component
@WebService
@HandlerChain(file = "/META-INF/ws/handlers.xml")
public class TodoWeb /*extends SpringBeanAutowiringSupport */ {

    @Inject
    TodoService todoService;

    @Inject
    WsValidationExecutor validationExecutor;

    @Inject
    WsExceptionConverter exceptionConverter;

    @WebMethod
    public List<Todo> getTodos() {
        return todoService.getTodos();
    }

    @WebMethod
    public Todo getTodo(String todoId) throws WsResourceNotFoundException {
        try {
            return todoService.getTodo(todoId);
        } catch (ResourceNotFoundException e) {
            throw exceptionConverter.toWsResourceNotFoundException(e);
        }
    }

    @WebMethod
    public Todo createTodo(Todo todo) throws WsValidationException, WsBusinessException {
        try {
            validationExecutor.execute(todo, Default.class, Todo.Create.class);
            return todoService.createTodo(todo);
        } catch (BindException e) {
            throw exceptionConverter.toWsValidationException(e);
        } catch (BusinessException e) {
            throw exceptionConverter.toWsBusinessException(e);
        }
    }

    @WebMethod
    public Todo updateTodo(Todo todo) throws WsValidationException, WsResourceNotFoundException {
        try {
            validationExecutor.execute(todo, Default.class, Todo.Update.class);
            return todoService.updateTodo(todo);
        } catch (BindException e) {
            throw exceptionConverter.toWsValidationException(e);
        } catch (ResourceNotFoundException e) {
            throw exceptionConverter.toWsResourceNotFoundException(e);
        }
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
