package lvsa.tpcalendar.http;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;

public enum HTTPStatusCode {
	HTTP_100_CONTINUE(100), HTTP_101_SWITCHING_PROTOCOLS(101),

	HTTP_200_OK(200), HTTP_201_CREATED(201), HTTP_202_ACCEPTED(202),
	HTTP_203_NONAUTHORITATIVE_INFORMATION(203), HTTP_204_NO_CONTENT(204),
	HTTP_205_RESET_CONTENT(205), HTTP_206_PARTIAL_CONTENT(206),

	HTTP_300_MULTIPLE_CHOICES(300), HTTP_301_MOVED_PERMANENTLY(301),
	HTTP_302_FOUND(302), HTTP_303_SEE_OTHER(303), 
	HTTP_304_NOT_MODIFIED(304), HTTP_305_USE_PROXY(305), HTTP_306_UNUSED(306),
	HTTP_307_TEMPORARY_REDIRECT(307), HTTP_308_PERMANENT_REDIRECT(308),

	HTTP_400_BAD_REQUEST(400), HTTP_401_UNAUTHORIZED(401),
	HTTP_402_PAYMENT_REQUIRED(402), HTTP_403_FORBIDDEN(403),
	HTTP_404_NOT_FOUND(404), HTTP_405_METHOD_NOT_ALLOWED(405),
	HTTP_406_NOT_ACCEPTABLE(406), HTTP_407_PROXY_AUTHENTICATION_REQUIRED(407),
	HTTP_408_REQUEST_TIMEOUT(408), HTTP_409_CONFLICT(409), HTTP_410_GONE(410),
	HTTP_411_LENGTH_REQUIRED(411), HTTP_412_PRECONDITION_FAILED(412),
	HTTP_413_CONTENT_TOO_LARGE(413), HTTP_414_URI_TOO_LONG(414),
	HTTP_415_UNSUPPORTED_MEDIA_TYPE(415), HTTP_416_RANGE_NOT_SATISFIABLE(416), 
	HTTP_417_EXPECTATION_FAILED(417), HTTP_418_IM_A_TEAPOT(418),
	HTTP_421_MISDIRECTED_REQUEST(421), HTTP_422_UNPROCESSABLE_CONTENT(422),
	HTTP_426_UPGRADE_REQUIRED(426),

	HTTP_500_INTERNAL_SERVER_ERROR(500), HTTP_501_NOT_IMPLEMENTED(501),
	HTTP_502_BAD_GATEWAY(502), HTTP_503_SERVICE_UNAVAILABLE(503),
	HTTP_504_GATEWAY_TIMEOUT(504), HTTP_505_HTTP_VERSION_NOT_SUPPORTED(505);

	private int statusCode;

	HTTPStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getint() {
		return statusCode;
	}

	public String wrapAsJsonRes() {
		Gson gson = new Gson();
        Map<String, String> resMap = new LinkedHashMap<>();
		String wrapStr = "500 Internal Server Error";

		switch (this.statusCode) {
			case 100: wrapStr = "100 Continue"; break;
			case 101: wrapStr = "101 Switching Protocols"; break;
			case 200: wrapStr = "200 OK"; break;
			case 201: wrapStr = "201 Created"; break;
			case 202: wrapStr = "202 Accepted"; break;
			case 203: wrapStr = "203 Non-Authoritative Information"; break;
			case 204: wrapStr = "204 No Content"; break;
			case 205: wrapStr = "205 Reset Content"; break;
			case 206: wrapStr = "206 Partial Content"; break;
			case 300: wrapStr = "300 Multiple Choices"; break;
			case 301: wrapStr = "301 Moved Permanently"; break;
			case 302: wrapStr = "302 Found"; break;
			case 303: wrapStr = "303 See Other"; break;
			case 304: wrapStr = "304 Not Modified"; break;
			case 305: wrapStr = "305 Use Proxy"; break;
			case 306: wrapStr = "306 (Unused)"; break;
			case 307: wrapStr = "307 Temporary Redirect"; break;
			case 308: wrapStr = "308 Permanent Redirect"; break;
			case 400: wrapStr = "400 Bad Request"; break;
			case 401: wrapStr = "401 Unauthorized"; break;
			case 402: wrapStr = "402 Payment Required"; break;
			case 403: wrapStr = "403 Forbidden"; break;
			case 404: wrapStr = "404 Not Found"; break;
			case 405: wrapStr = "405 Method Not Allowed"; break;
			case 406: wrapStr = "406 Not Acceptable"; break;
			case 407: wrapStr = "407 Proxy Authentication Required"; break;
			case 408: wrapStr = "408 Request Timeout"; break;
			case 409: wrapStr = "409 Conflict"; break;
			case 410: wrapStr = "410 Gone"; break;
			case 411: wrapStr = "411 Length Required"; break;
			case 412: wrapStr = "412 Precondition Failed"; break;
			case 413: wrapStr = "413 Content Too Large"; break;
			case 414: wrapStr = "414 URI Too Long"; break;
			case 415: wrapStr = "415 Unsupported Media Type"; break;
			case 416: wrapStr = "416 Range Not Satisfiable"; break;
			case 417: wrapStr = "417 Expectation Failed"; break;
			case 418: wrapStr = "418 I'm a Teapot"; break;
			case 421: wrapStr = "421 Misdirected Request"; break;
			case 422: wrapStr = "422 Unprocessable Content"; break;
			case 426: wrapStr = "426 Upgrade Required"; break;
			case 500: wrapStr = "500 Internal Server Error"; break;
			case 501: wrapStr = "501 Not Implemented"; break;
			case 502: wrapStr = "502 Bad Gateway"; break;
			case 503: wrapStr = "503 Service Unavailable"; break;
			case 504: wrapStr = "504 Gateway Timeout"; break;
			case 505: wrapStr = "505 HTTP Version Not Supported"; break;
			default: wrapStr = "500 Internal Server Error";
		}

		resMap.put("result", wrapStr);
		wrapStr = gson.toJson(resMap);
		return wrapStr; 
	}
}
