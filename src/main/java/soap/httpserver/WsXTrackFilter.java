package soap.httpserver;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class WsXTrackFilter extends Filter {

    private String xTrackAttributeName = "X-Track";

    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        Headers requestHeader = httpExchange.getRequestHeaders();
        String xTrack = requestHeader.getFirst(xTrackAttributeName);
        if (xTrack == null) {
            xTrack = UUID.randomUUID().toString();
        }
        Headers responseHeader = httpExchange.getResponseHeaders();
        responseHeader.add(xTrackAttributeName, xTrack);
        httpExchange.setAttribute(xTrackAttributeName, xTrack);
        MDC.put(xTrackAttributeName, xTrack);
        try {
            chain.doFilter(httpExchange);
        } finally {
            MDC.remove(xTrackAttributeName);
        }
    }

    @Override
    public String description() {
        return "X-Track management filter for web service";
    }

}
