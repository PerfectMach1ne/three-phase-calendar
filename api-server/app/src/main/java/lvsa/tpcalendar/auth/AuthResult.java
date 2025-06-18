package lvsa.tpcalendar.auth;

import lvsa.tpcalendar.http.HTTPStatusCode;

@SuppressWarnings("unused")
public class AuthResult {
    private final int user_id;
    private final boolean success;
    private final String errorMessage;
    private final HTTPStatusCode status;

    /** 
     * On success.
     */
    public AuthResult(int userId, HTTPStatusCode status) {
        this.user_id = userId;
        this.success = true;
        this.errorMessage = null;
        this.status = status;
    }

    /** 
     * On failure.
     */
    public AuthResult(String errorMsg, HTTPStatusCode status) {
        this.user_id = -1;
        this.success = false;
        this.errorMessage = errorMsg;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
    public HTTPStatusCode getStatus() {
        return status;
    }
}
