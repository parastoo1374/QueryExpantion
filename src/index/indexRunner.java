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

    public static void documentsIndexerRunner(String indexDirectory, HashMap<String, ArrayList<String>> document_hashmap_entities) {
    try {

        indexDocumentHashMap(document_hashmap_entities, indexDirectory );

    } catch (Exception ex) {
        Logger.getLogger(indexRunner.class.getName()).log(Level.SEVERE, null, ex);
    }

}

    private static void indexDocumentHashMap(HashMap<String, ArrayList<String>> documentHashMap, String indexDir) throws IOException, Exception {
        indexer indexer = new indexer(indexDir , new tfidfSimilarity());
        indexer.indexHashMapStringArrayList(documentHashMap);
        indexer.close();

    }

    public static void searchIndex(HashMap<String, ArrayList<String>> queryHashMap,
            HashMap<String, ArrayList<String>> queryRelatedDoc, String indexDirectory,
            String resultDirectory) 
    {
        try {
            Writer writer = new BufferedWriter(new FileWriter((resultDirectory + "/resultsBM25.txt"),true), 26384);
            indexSearcher is = new indexSearcher(indexDirectory, new BM25Similarity());
            for (int i = 201 ; i<=250 ; i++) {
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
                            temp = i + "\t" + "Q0" + "\t" + doc.get("DocId") + "\t" + rank + "\t" + scoreDoc.score + "\t" + "parastoo" + "\n";
                            writer.write(temp);
                            System.out.print(temp);
                            rank++;
                        }
                        
                        if(rank>100)
                            break;
                    }
                    if(rank<100){
                        for (String value : values) {
                            if(!used.contains(value)){
                                used.add(value);
                                temp = "";
                                temp = i + "\t" + "Q0" + "\t" + value + "\t" + rank + "\t" + "0.00" + "\t" + "parastoo" + "\n";
                                writer.write(temp);
                                System.out.print(temp);
                                rank++;
                            }
                            if(rank>100)
                                break;
                        }
                    }
                }
                catch(Exception ex){
//                    System.err.println(ex.getMessage());
                }
            }
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

}
