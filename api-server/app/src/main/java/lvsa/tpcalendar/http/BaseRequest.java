package lvsa.tpcalendar.http;

import java.util.function.Consumer;

import lvsa.tpcalendar.http.HTTPStatusCode;

public interface BaseRequest {
    public HTTPStatusCode switchMethod(Consumer<HTTPStatusCode> lambda);
}
