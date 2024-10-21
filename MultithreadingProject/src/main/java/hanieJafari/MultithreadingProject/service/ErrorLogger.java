package hanieJafari.MultithreadingProject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanieJafari.MultithreadingProject.models.ErrorRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ErrorLogger {

    private final ObjectMapper mapper = new ObjectMapper();

    public void logErrors(List<ErrorRecord> errors) {
        try {
            mapper.writeValue(new File("errors.json"), errors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
