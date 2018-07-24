package index;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.document.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;

public class indexRunner {

    public static void documentsIndexerRunner(Similarity sim, boolean indexFile,String documentDirectory, String indexDirectory, HashMap<String, ArrayList<String>> document_hashmap_entities) {
    try {
        if(!indexFile)
            indexDocumentHashMap(sim, document_hashmap_entities, indexDirectory );
        else
            indexDocumentFile(sim, documentDirectory,indexDirectory);

    } catch (Exception ex) {
        Logger.getLogger(indexRunner.class.getName()).log(Level.SEVERE, null, ex);
    }

}

    private static void indexDocumentHashMap(Similarity sim  ,HashMap<String, ArrayList<String>> documentHashMap, String indexDir) throws IOException, Exception {
        indexer indexer = new indexer(indexDir , sim);
        indexer.indexHashMapStringArrayList(documentHashMap);
        indexer.close();

    }

    public static void searchIndex(Similarity sim,int base,int length, HashMap<String, ArrayList<String>> queryHashMap,
            HashMap<String, ArrayList<String>> queryRelatedDoc, String indexDirectory,
            String resultDirectory) 
    {
        try {
            File f =new File(resultDirectory);
            if(f.exists())
                f.delete();
            
            Writer writer = new BufferedWriter(new FileWriter((resultDirectory ),true), 26384);
            indexSearcher is = new indexSearcher(indexDirectory, sim);
            for (int i = base+1 ; i<=base+length ; i++) {
                int rank = 1;
                try{
                    String query = String.join(",", queryHashMap.get(Integer.toString(i)));
                    ArrayList<String> values = queryRelatedDoc.get(Integer.toString(i));
                    ArrayList<String> used = new ArrayList<>();
                    String temp = "";
                    
                    TopDocs hits = is.search(query);
                    for (ScoreDoc scoreDoc : hits.scoreDocs) {
                        temp = "";
                        Document doc = is.IndexSearcher.doc(scoreDoc.doc);
                        
                        if(values.contains(doc.get("DocId")))
                        {
                            used.add(doc.get("DocId"));
                            temp = i + " " + "Q0" + " " + doc.get("DocId") + " " + rank + " " + scoreDoc.score + " " + "parastoo" + "\n";
                            writer.write(temp);
//                            System.out.print(temp);
                            rank++;
                        }
                        
                        if(rank>1000)
                            break;
                    }
                    if(rank<1000){
                        for (String value : values) {
                            if(!used.contains(value)){
                                used.add(value);
                                temp = "";
                                temp = i + " " + "Q0" + " " + value + " " + rank + " " + "0.00" + " " + "parastoo" + "\n";
                                writer.write(temp);
//                                System.out.print(temp);
                                rank++;
                            }
                            if(rank>1000)
                                break;
                        }
                    }
                    
                }
                catch(Exception ex){
//                    System.err.println(ex.getMessage());
                }
            }
            writer.close();
        }
        catch(Exception ex){
            System.err.println(ex.getMessage());
        }

    }

    private static String getIntersection(ArrayList<String> queryEntities, String document) {
        String output = "";
        int count = 0;
        ArrayList<String> documentEntities = new ArrayList<>(Arrays.asList(document.split(",")));
        for(String q : queryEntities){
            count = 0;
            for(String d : documentEntities) {
                count ++;
            }
            output += q +":" +count +" "; 
        }
        return output;
    }

    private static void indexDocumentFile(Similarity sim, String documentDirectory, String indexDirectory) throws Exception {
        indexer indexer = new indexer(indexDirectory , sim);
        indexer.indexFile(documentDirectory);
        indexer.close();
    }
    
    
    

}
