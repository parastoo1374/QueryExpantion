package index;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

public class indexSearcher {
    public  org.apache.lucene.search.IndexSearcher IndexSearcher =null;

    public indexSearcher(String indexDir, Similarity similarity) throws IOException{
        IndexReader rdr = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
        IndexSearcher = new org.apache.lucene.search.IndexSearcher(rdr);
        IndexSearcher.setSimilarity(similarity);
    }

    public  TopDocs search(String q) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {

        TotalHitCountCollector collector = new TotalHitCountCollector();

        QueryParser parser = new QueryParser("contents" ,new StandardAnalyzer());
        Query query = parser.parse(q);
        TopDocs hits = IndexSearcher.search(query, 244886);

        return hits;


    }


    public void close() throws IOException{
        IndexSearcher.getIndexReader().close();
    }


}
