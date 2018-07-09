package entityStem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class stemmer {
    public  HashMap<String,ArrayList<String>> query_entities;
    public  HashMap<String,ArrayList<String>> document_entities;
    public  HashMap<String,ArrayList<String>> query_related_document;
    public  String query_entities_directory;
    public  String document_entities_directory;
    public  String qrell_directory;

    public stemmer(String qed, String ded, String qrelld){
        query_entities = new HashMap<>();
        document_entities = new HashMap<>();
        query_related_document = new HashMap<>();
        query_entities_directory = qed;
        document_entities_directory = ded;
        qrell_directory = qrelld;
    }
    public void createQueryEntities() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(new File(query_entities_directory)));
        int i =0;
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] lineSplit = line.split(": ");
            if(lineSplit.length>0){
                String key = lineSplit[0];
                String[] entities = lineSplit[1].split(",");
                ArrayList<String> values = new ArrayList<String>();
                for(String value : entities)
                    values.add(value);
                if (!query_entities.containsKey(key))
                    query_entities.put(key,values);

            }
        }
        br.close();
    }

    public void createDocumentEntities() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(new File(document_entities_directory)));
        String line = null;
        while ((line = br.readLine()) != null) {
            String docId = line.substring(0, line.indexOf("\t"));
            String entitiesWithConf = line.substring(line.indexOf("\t", line.indexOf("\t") + 1) + 1);
            String[] ent_conf = entitiesWithConf.split(" ");
            ArrayList<String> entList;
            if (document_entities.containsKey(docId)) {
                entList = document_entities.get(docId);
            } else {
                entList = new ArrayList<>();
            }
            for (String temp : ent_conf) {
                entList.add(temp.split("\\|")[0]);
            }
            document_entities.put(docId, entList);
        }

        br.close();
    }
    
    public void createQueryRelatedDoc() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(new File(qrell_directory)));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] splits = line.split(" ");
            String key = splits[0];
            String value  = splits[2];
            ArrayList<String> entList;
            if (query_related_document.containsKey(key)) {
                entList = query_related_document.get(key);
            } else {
                entList = new ArrayList<>();
            }
            
            entList.add(value);
            
            query_related_document.put(key, entList);
//            System.out.println(docId);
        }

        br.close();
    }

}
