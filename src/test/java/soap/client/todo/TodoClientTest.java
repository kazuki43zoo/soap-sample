package soap.client.todo;

import com.sun.net.httpserver.Headers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.UrlResource;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;
import org.springframework.remoting.jaxws.JaxWsSoapFaultException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import soap.client.WsBusinessException;
import soap.client.WsError;
import soap.client.WsResourceNotFoundException;
import soap.client.WsValidationException;
import soap.domain.model.Todo;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TodoClientTest {

    @Inject
    TodoService todoService;

    @Before
    public void setup() {
        System.out.println(todoService.getTodos());
        todoService.deleteTodos();
    }

    @Test
    public void create() throws WsValidationException, WsBusinessException, WsResourceNotFoundException {
        //test
        Todo todo = new Todo();
        todo.setTitle("テスト");
        todo.setDescription("test description");
        Todo createdTodo = todoService.createTodo(todo);

        // assert
        Todo loadedTodo = todoService.getTodo(createdTodo.getTodoId());
        assertThat(createdTodo.toString(), is(loadedTodo.toString()));
    }

    @Test
    public void update() throws WsValidationException, WsBusinessException, WsResourceNotFoundException {
        // setup
        Todo todo = new Todo();
        todo.setTitle("test title");
        todo.setDescription("test description");
        Todo createdTodo = todoService.createTodo(todo);

        // test
        createdTodo.setTitle("mod title");
        createdTodo.setDescription("mod description");
        createdTodo.setFinished(true);
        todoService.updateTodo(createdTodo);

        // assert
        Todo loadedTodo = todoService.getTodo(createdTodo.getTodoId());
        assertThat(createdTodo.toString(), is(loadedTodo.toString()));
    }

    @Test(expected = WsResourceNotFoundException.class)
    public void delete() throws WsValidationException, WsBusinessException, WsResourceNotFoundException {

        // setup
        Todo todo = new Todo();
        todo.setTitle("test title");
        todo.setDescription("test description");
        Todo createdTodo = todoService.createTodo(todo);

        // test
        todoService.deleteTodo(createdTodo.getTodoId());

        // assert
        todoService.getTodo(createdTodo.getTodoId());
    }

    @Test(expected = WsBusinessException.class)
    public void businessError() throws WsBusinessException, WsValidationException {
        // test
        Todo todo = new Todo();
        todo.setTitle("test title");
        todo.setDescription("test description");

        todoService.createTodo(todo);
        todoService.createTodo(todo);
        todoService.createTodo(todo);
        todoService.createTodo(todo);
        todoService.createTodo(todo);
        todoService.createTodo(todo);

    }


    @Test
    public void validationError() throws WsBusinessException {
        try {
            // test
            Todo todo = new Todo();
            todo.setTodoId("sfasdf");
            todo.setTitle("test title");
            todo.setDescription("test description");

            todoService.createTodo(todo);

            //assert
            fail();
        } catch (WsValidationException e) {
            assertThat(e.getErrors().size(), is(1));
            WsError error = e.getErrors().get(0);
            assertThat(error.getCode(), is("Null"));
            assertThat(error.getMessage(), is("must be null"));
            assertThat(error.getPath(), is("todoId"));
        }
    }

    @Test(expected = JaxWsSoapFaultException.class)
    public void systemError() throws WsValidationException, WsResourceNotFoundException {
        // test
        todoService.getTodo("systemError");
    }

    @Test(expected = WsResourceNotFoundException.class)
    public void notFound() throws WsResourceNotFoundException {
        todoService.getTodo("aaaa");
    }

    @Configuration
    @TestPropertySource(locations = "test.properties")
    static class TestConfig {
        @Bean
        public JaxWsPortProxyFactoryBean todoService(@Value("${jaxws.portNumber:8080}") String portNumber) throws IOException {
            JaxWsPortProxyFactoryBean factoryBean = new JaxWsPortProxyFactoryBean();
            factoryBean.setServiceInterface(TodoService.class);
            factoryBean.setWsdlDocumentResource(new UrlResource("http://localhost:" + portNumber + "/ws/TodoWebService?wsdl"));
            factoryBean.setNamespaceUri("http://todo.jaxws.soap/");
            factoryBean.setServiceName("TodoWebService");
            factoryBean.setPortName("TodoWebPort");
            factoryBean.setHandlerResolver(new MyHandlerResolver());
            return factoryBean;
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
            return new PropertySourcesPlaceholderConfigurer();
        }

    }

    public static class MyHandlerResolver implements HandlerResolver {
        private final List<Handler> chain = new ArrayList<>();

        public MyHandlerResolver() {
            chain.add(new SOAPHttpHeaderHandler());
        }

        public List<Handler> getHandlerChain(PortInfo portInfo) {
            return chain;
        }
    }

    public static class SOAPHttpHeaderHandler implements SOAPHandler<SOAPMessageContext> {

        public boolean handleMessage(SOAPMessageContext context) {
            System.out.println(getClass().getSimpleName() + ": handleMessage ...");
            if (Boolean.class.cast(context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))) {
                System.out.println("-----request------");
                Headers requestHeaders = new Headers();
                String xTrack = UUID.randomUUID().toString();
                System.out.println("X-Track : " + xTrack);
                requestHeaders.add("X-Track", xTrack);
                context.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
            } else {
                System.out.println("-----response------");
                Map<?, ?> responseHeaders = Map.class.cast(context.get(MessageContext.HTTP_RESPONSE_HEADERS));
                for (Map.Entry<?, ?> entry : responseHeaders.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
            }
            return true;
        }

        @Override
        public boolean handleFault(SOAPMessageContext context) {
            System.out.println(getClass().getSimpleName() + ": handleFault ...");
            System.out.println("-----response------");
            Map<?, ?> responseHeaders = Map.class.cast(context.get(MessageContext.HTTP_RESPONSE_HEADERS));
            for (Map.Entry<?, ?> entry : responseHeaders.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            return true;
        }

        @Override
        public void close(MessageContext context) {
            System.out.println(getClass().getSimpleName() + ": close ...");
        }

        @Override
        public Set<QName> getHeaders() {
            System.out.println(getClass().getSimpleName() + ": getHeaders ...");
            return null;
        }
    }

}
