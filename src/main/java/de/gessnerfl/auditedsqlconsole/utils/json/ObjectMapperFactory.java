package de.gessnerfl.auditedsqlconsole.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperFactory {

    public ObjectMapper createNew(){
        return new ObjectMapper();
    }

}
