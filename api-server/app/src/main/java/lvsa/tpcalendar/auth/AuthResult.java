package lvsa.tpcalendar.auth;

import lvsa.tpcalendar.http.HTTPStatusCode;

/**
 * Helper class for /api/login and /api/register endpoint DBProxy-APIRouter communication.
 */
@SuppressWarnings("unused")
public class AuthResult {
    private final int user_id;
    private final boolean success;
    private final String errorMessage;
    private final HTTPStatusCode status;

    /** 
     * Constructor for results on success.
     */
    public AuthResult(int userId, HTTPStatusCode status) {
        this.user_id = userId;
        this.success = true;
        this.errorMessage = null;
        this.status = status;
    }

    /** 
     * Constructor for results on failure.
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
