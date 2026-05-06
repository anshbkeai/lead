package com.leaderboard.lead.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import tools.jackson.core.JsonParser;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.MappingIterator;
import tools.jackson.databind.ObjectMapper;

@Component
public class JsonParserService {

    @PostConstruct
    public void readFile() {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        String line ;
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader("/workspaces/codespaces-blank/lead/src/main/java/com/leaderboard/lead/util/data.json"));
                JsonParser parser = mapper.createParser(bufferedReader);
        ) {
            MappingIterator<Test> it = mapper.readerFor(Test.class).readValues(bufferedReader);
            while (it.hasNext()) {
                Test obj = it.next();
                System.out.println(obj);
            }
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
   
    }
   
}
