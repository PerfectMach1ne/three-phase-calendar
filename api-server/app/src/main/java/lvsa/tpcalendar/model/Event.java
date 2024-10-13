package lvsa.tpcalendar.model;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;

import lvsa.tpcalendar.http.HTTPStatusCode;

public interface Event {
    final LocalDateTime createdDate = LocalDateTime.now();
    
    LocalDateTime getDateTime();
    String getName();
    String getDescription();
    String getCreatedDate();
    String getUpdatedDate();

    void setDateTime(LocalDateTime localdatetime);
    void setName(String name);
    void setDescription(String desc);
    
    HTTPStatusCode read(int hashCode);

    HTTPStatusCode create(JsonObject jsonObj);

    HTTPStatusCode update(JsonObject jsonObj);
    HTTPStatusCode update();

    HTTPStatusCode delete(int hashCode);
}
