package lvsa.tpcalendar.model;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;

public interface Event {
    final LocalDateTime createdDate = LocalDateTime.now();
    
    String getName();
    String getDescription();
    LocalDateTime getDateTime();
    String getCreatedDate();
    String getUpdatedDate();

    void setName(String name);
    void setDescription(String desc);
    
    void create(JsonObject jsonObj);

    void update(JsonObject jsonObj);
    void update();

    void delete(int hashCode);
}
