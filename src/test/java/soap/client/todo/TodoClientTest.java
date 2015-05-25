package soap.client.todo;

import com.sun.net.httpserver.Headers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;
import org.springframework.remoting.jaxws.JaxWsSoapFaultException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import soap.client.WsBusinessException;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TodoClientTest {

    @Inject
    TodoService todoService;

    @Test
    public void createAndGetTodo() throws WsValidationException, WsBusinessException, WsResourceNotFoundException {
        Todo todo = new Todo();
        todo.setTitle("テスト");
        todo.setDescription("test description");
        Todo createdTodo = todoService.createTodo(todo);
        System.out.println(createdTodo.getTodoId());
        System.out.println(createdTodo.getTitle());
        System.out.println(createdTodo.getDescription());
        System.out.println(createdTodo.getCreatedAt());
        System.out.println(createdTodo.isFinished());
        System.out.println("-----");
        Todo loadedTodo = todoService.getTodo(createdTodo.getTodoId());
        System.out.println(loadedTodo.getTodoId());
        System.out.println(createdTodo.getTitle());
        System.out.println(loadedTodo.getDescription());
        System.out.println(loadedTodo.getCreatedAt());
        System.out.println(loadedTodo.isFinished());
    }

    @Test
    public void validationErrorOnCreateTodo() {
        Todo todo = new Todo();
        todo.setTodoId("sfasdf");
        todo.setTitle("test title");
        todo.setDescription("test description");
        try {
            System.out.println("--------------1------------");
            todoService.createTodo(todo);
            System.out.println("--------------2------------");
        } catch (WsValidationException e) {
            System.out.println("------------3--------------");
            System.out.println(e.getFaultInfo().getErrors());
            e.printStackTrace();
        } catch (WsBusinessException e) {
            System.out.println("------------4--------------");
            e.printStackTrace();
        }
        System.out.println("------------5--------------");
    }


    @Test
    public void updateAndGetTodo() throws WsValidationException, WsBusinessException, WsResourceNotFoundException {
        Todo todo = new Todo();
        todo.setTitle("test title");
        todo.setDescription("test description");
        Todo createdTodo = todoService.createTodo(todo);
        createdTodo.setTitle("mod title");
        createdTodo.setDescription("mod description");
        createdTodo.setFinished(true);
        System.out.println(createdTodo);
        System.out.println(todoService.updateTodo(createdTodo));
        ;
        System.out.println("-----");
        Todo loadedTodo = todoService.getTodo(createdTodo.getTodoId());
        System.out.println(loadedTodo.getTodoId());
        System.out.println(loadedTodo.getDescription());
        System.out.println(loadedTodo.getCreatedAt());
        System.out.println(loadedTodo.isFinished());
    }

    @Test
    public void getTodoSystemError() throws WsValidationException, WsResourceNotFoundException {
        try {
            todoService.getTodo("systemError");
        } catch (JaxWsSoapFaultException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTodoNotFound() {
        try {
            todoService.getTodo("aaaa");
        } catch (WsResourceNotFoundException e) {
            System.out.println(e.getFaultInfo().getErrors());
            e.printStackTrace();
        }
    }

    @Configuration
    static class TestConfig {
        @Bean
        public JaxWsPortProxyFactoryBean todoService() throws IOException {
            JaxWsPortProxyFactoryBean factoryBean = new JaxWsPortProxyFactoryBean();
            factoryBean.setServiceInterface(TodoService.class);
            factoryBean.setWsdlDocumentResource(new UrlResource("http://localhost:4444/TodoWebService?WSDL"));
            factoryBean.setNamespaceUri("http://todo.jaxws.soap/");
            factoryBean.setServiceName("TodoWebService");
            factoryBean.setPortName("TodoWebPort");
            factoryBean.setHandlerResolver(new MyHandlerResolver());
            factoryBean.setUsername("testuser");
            factoryBean.setPassword("password");
            return factoryBean;
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
