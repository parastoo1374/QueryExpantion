/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryexpantion;

import entityStem.stemmer;
import index.indexRunner;
import index.scoreCombination;

/**
 *
 * @author Parastoo
 */
public class QueryExpantion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String query_entities_dir = "./data/query_entities_clueweb_12.txt";
        String doc_entities_dir = "./data/document_entities_clueweb_12-partaa.txt";
        String qrell_dir = "./data/sdm_Results.txt";
        String index_dir = "./index";
        String result_dir = "./results";
        String file_dir = "./results/resultsBM25.txt";
        String otherFile_dir = "./results/sdm_Results.txt";
        double alpha = 0.65;
        stemmer stem = new stemmer(query_entities_dir, doc_entities_dir,qrell_dir);
        try {
            stem.createQueryEntities();
//            stem.createDocumentEntities();
//            stem.createQueryRelatedDoc();
//            indexRunner ir = new indexRunner();
////            ir.documentsIndexerRunner( index_dir, stem.document_entities);
//            ir.searchIndex(stem.query_entities, stem.query_related_document, index_dir, result_dir);
            scoreCombination sc = new scoreCombination(stem ,file_dir , otherFile_dir ,alpha,result_dir );
            sc.combineScores();
        }
        catch (Exception ex){
//             System.err.println(ex.getMessage());
        }
        
    }
    
}
