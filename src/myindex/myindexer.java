/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myindex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Parastoo
 */
public class myindexer {
    public Map<String,Map<String, Integer> > termFrequencies =  new HashMap<>();
    public Map<String, Integer> invertedDocFrequency = new HashMap<>() ;
    public Map<String, String> docsAbstract = new HashMap<>();
    
    public void insertDocAbstract(String docid,String docEntities){
        docsAbstract.put(docid,docEntities);
    }
    
    public void index() throws Exception{
        for (Map.Entry<String, String> entry : docsAbstract.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            List<String> documentTokens = Arrays.asList(value.split(", "));
            Map<String ,Integer> termFrequency = new HashMap<>();
            for (int i = 0; i < documentTokens.size(); i++) {
                if(termFrequency.containsKey(documentTokens.get(i))){
                    termFrequency.put(documentTokens.get(i),termFrequency.get(documentTokens.get(i))+1);
                }
                else{
                    termFrequency.put(documentTokens.get(i),1);
                }
            }
            termFrequencies.put(key, termFrequency);
        }
        
    }
    
    public void calcIdf() throws Exception{
        
        Iterator it = termFrequencies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String key = (String) pair.getKey();
            Map<String, Integer> termFrequency = (Map<String , Integer>) pair.getValue();
            Iterator it2 = termFrequency.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair2 = (Map.Entry)it2.next();
                String key2 = (String) pair2.getKey();
                if(invertedDocFrequency.containsKey(key2)){
                    invertedDocFrequency.put(key2, invertedDocFrequency.get(key2)+1);
                }
                else
                    invertedDocFrequency.put(key2,1);
            }
            
        }
        
        
    }
    
    public  int search( String queryToken, String docEntity ){
        if(termFrequencies.containsKey(docEntity)){
            if(termFrequencies.get(docEntity).containsKey(queryToken))
                 return termFrequencies.get(docEntity).get(queryToken);
            else 
                return 0;
        }
        else
            return 0;
    }
    
    public float Tf_Idf (String token,String docEntity){
        
        float tf =0;
        float idf = 0;
        if(termFrequencies.containsKey(docEntity)){
            if(termFrequencies.get(docEntity).containsKey(token))
                tf = (float) (1+ Math.log(termFrequencies.get(docEntity).get(token)));
            if(invertedDocFrequency.containsKey(token))
                idf = (float) Math.log(termFrequencies.size()/(float)invertedDocFrequency.get(token));
            return tf*idf;
        }
        else
            return 0;
    }
}
