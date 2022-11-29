package activity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ControllerRequestResponseLogger extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            doFilterWrapped(wrapRequest(httpServletRequest), wrapResponse(httpServletResponse), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper httpServletRequest, ContentCachingResponseWrapper httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            afterRequest(httpServletRequest, httpServletResponse);
            httpServletResponse.copyBodyToResponse();
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) throws IOException {
        if (log.isInfoEnabled()) {
            logRequest(request, request.getContentAsByteArray());
            logResponse(response);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, byte[] body) throws IOException {
        if (log.isDebugEnabled()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n').append("===========================request begin================================================").append('\n');
            stringBuilder.append("URI         : {").append(request.getRequestURI()).append("}\n");
            stringBuilder.append("Method      : {").append(request.getMethod()).append("}\n");
            stringBuilder.append("Headers     : {");
            for (String headerName : Collections.list(request.getHeaderNames())) {
                stringBuilder.append("{");
                stringBuilder.append(headerName);
                stringBuilder.append("=[");
                stringBuilder.append(request.getHeader(headerName));
                stringBuilder.append("]}");
            }
            stringBuilder.append("}");
            stringBuilder.append('\n');
            stringBuilder.append("Request body: {").append(new String(body, "UTF-8")).append("}\n");
            stringBuilder.append("==========================request end================================================");
            log.debug(stringBuilder.toString());
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) throws UnsupportedEncodingException {
        if (log.isDebugEnabled()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n').append("============================response begin==========================================").append('\n');
            stringBuilder.append("Status code  : {").append(response.getStatusCode()).append("}\n");
            stringBuilder.append("Headers     : {");
            for (String headerName : response.getHeaderNames()) {
                stringBuilder.append("{");
                stringBuilder.append(headerName);
                stringBuilder.append("=[");
                stringBuilder.append(response.getHeader(headerName));
                stringBuilder.append("]}");
            }
            stringBuilder.append("}").append('\n');

            if (response.getContentSize() > 0) {
                String contentType=response.getContentType();
                if(MediaType.APPLICATION_JSON_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType) || MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
                    stringBuilder.append("Response body: {").append(new String(response.getContentAsByteArray(), "UTF-8")).append("}\n");
                }
            }
            stringBuilder.append("=======================response end=================================================");
            log.debug(stringBuilder.toString());
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}

