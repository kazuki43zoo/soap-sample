package soap.httpserver;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WsLoggingFilter extends Filter {

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        if (log.isDebugEnabled()) {
            URI uri = httpExchange.getRequestURI();
            String method = httpExchange.getRequestMethod();
            InetSocketAddress remoteAddress = httpExchange.getRemoteAddress();
            log.debug("URI:{} METHOD:{} REMOTE-ADDRESS:{}", uri, method, remoteAddress);
            Headers headers = httpExchange.getRequestHeaders();
            log.debug("REQUEST-HEADER:");
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                log.debug("NAME:{} VALUES:{}", entry.getKey(), entry.getValue());
            }
        }
        chain.doFilter(httpExchange);
        if (log.isDebugEnabled()) {
            log.debug("RESPONSE-CODE:{}", httpExchange.getResponseCode());
            Headers headers = httpExchange.getResponseHeaders();
            log.debug("RESPONSE-HEADER:");
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                log.debug("NAME:{} VALUES:{}", entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String description() {
        return "Logging filter for web service";
    }

}
