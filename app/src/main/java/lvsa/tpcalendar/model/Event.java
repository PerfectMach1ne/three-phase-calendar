package lvsa.tpcalendar.model;

import java.time.LocalDateTime;

public interface Event {
    final LocalDateTime createdDate = LocalDateTime.now();
    
    String getName();
    String getDescription();
    String getCreatedDate();
    String getUpdatedDate();

    void setName(String name);
    void setDescription(String desc);
    
    void create();

    void update(byte mode);

    void delete();
}
