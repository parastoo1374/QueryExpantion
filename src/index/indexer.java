package index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import java.io.BufferedReader;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class indexer {
    private final IndexWriter writer;

    public indexer(String dir , Similarity similarity) throws IOException {
        Directory indexDir = FSDirectory.open(Paths.get(dir));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        cfg.setSimilarity(similarity);
        cfg.setOpenMode(OpenMode.CREATE);
        writer = new IndexWriter(indexDir, cfg);
    }

    protected Document getDocumentArrayList(String docId ,ArrayList<String> documentEntites) throws Exception {
        Document doc = new Document();

        String result = StringUtils.join(documentEntites, ", ");
        doc.add(new TextField("contents", result, Field.Store.YES));
        doc.add(new StringField("DocId", docId,Field.Store.YES));

        return doc;
    }

    protected Document getDocumentString(String dEntity ,String document) throws Exception {
        Document doc = new Document();
        if(document != null){
            doc.add(new TextField("contents", document, Field.Store.YES));
            doc.add(new StringField("DocId", dEntity,Field.Store.YES));
        }
        System.out.println(dEntity);
        return doc;

    }

    private void indexFileArrayList(String docId ,ArrayList<String> documentEntites) throws Exception {
        Document doc = getDocumentArrayList(docId , documentEntites);
        writer.addDocument(doc);
    }



    public int indexHashMapStringArrayList(HashMap<String, ArrayList<String>> document) throws Exception {
        for (String key : document.keySet()) {
            indexFileArrayList(key, document.get(key));
        }
        return writer.numDocs();
    }



    public void close() throws IOException {
        writer.close();
    }

    void indexFile(String documentDirectory) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File(documentDirectory)));
        String line = null;
        String lastDocId="";
        int docCount = 0;
        line = br.readLine();
        lastDocId = line.substring(0, line.indexOf("\t"));
        ArrayList<String>entList = new ArrayList<>();
        do{
            String docId = line.substring(0, line.indexOf("\t"));
            if(lastDocId.equals(docId)) {
                String entitiesWithConf = line.substring(line.indexOf("\t", line.indexOf("\t") + 1) + 1);
                String[] ent_conf = entitiesWithConf.split(" ");
                

                for (String temp : ent_conf) {
                    entList.add(temp.split("\\|")[0]);
                }
                
                line = br.readLine();
            }
            else{
                indexFileArrayList(docId,entList);
                docCount++;
                System.out.println(docId+" is indexed!");
                lastDocId = docId;
                entList = new ArrayList<>();
            }

        }while (line != null);

        System.out.println("number of docs: "+docCount);
        br.close();
    
    }
}
