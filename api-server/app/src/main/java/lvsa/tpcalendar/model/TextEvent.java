package lvsa.tpcalendar.model;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;

import lvsa.tpcalendar.http.HTTPStatusCode;

public class TextEvent implements Event {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }

    @Override
    public String getCreatedDate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCreatedDate'");
    }

    @Override
    public String getUpdatedDate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUpdatedDate'");
    }

    @Override
    public void setName(String newName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setName'");
    }

    @Override
    public void setDescription(String newDesc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDescription'");
    }

    @Override
    public LocalDateTime getDateTime() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDateTime'");
    }

    @Override
    public void setDateTime(LocalDateTime localdatetime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDateTime'");
    }

    @Override
    public HTTPStatusCode read(int hashCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public HTTPStatusCode create(JsonObject jsonObj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public HTTPStatusCode update(JsonObject jsonObj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public HTTPStatusCode update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public HTTPStatusCode delete(int hashCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
