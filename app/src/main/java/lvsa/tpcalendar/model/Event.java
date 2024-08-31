package lvsa.tpcalendar.model;

import java.time.LocalDateTime;

public interface Event {
    String name = "Unnamed event";
    String description = "";
    final LocalDateTime createdDate = LocalDateTime.now();
    LocalDateTime updatedDate = null;
    
    String getName();
    String getDescription();
    String getCreatedDate();
    String getUpdatedDate();

    String setName();
    String setDescription();
    
    void create();

    void update();

    void delete();
}
