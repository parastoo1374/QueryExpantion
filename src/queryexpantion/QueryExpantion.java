/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryexpantion;

import entityStem.stemmer;
import index.indexRunner;
import index.tfSimilarity;
import index.tfidfSimilarity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import score.scoreCombination;
import myindex.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;

/**
 *
 * @author Parastoo
 */
public class QueryExpantion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dataSet = "2012";//2009 -2012
        String similarityName = "BM25";//Tf,TfIDF,Default,BM25
        double alpha = 0.9;
        boolean indexFile = false;
        Similarity similarity = new BM25Similarity();//BM25Similarity,tfidfSimilarity,DefaultSimilarity,tfSimilarity
        int base = 200;//0,200
        int length = 50;//50,200

        String query_entities_dir = "./data/"+dataSet+"/query_entities.txt";
        String doc_entities_dir = "./data/"+dataSet+"/document_entities.txt";
        String sdm_dir = "./data/"+dataSet+"/sdm_Results.txt";
        String index_dir = "./index/"+dataSet+"/index"+similarityName;
        String result_dir = "./results/"+dataSet+"/results"+similarityName+".txt";
        String combination_dir = "./results/"+dataSet+"/resultsSdmAnd"+similarityName+Double.toString(alpha)+".txt";
        
//        stemmer stem = new stemmer(query_entities_dir, doc_entities_dir,sdm_dir);
//        try {
//            stem.createQueryEntities();
//            if(!indexFile)
//                stem.createDocumentEntities();
//            stem.createQueryRelatedDoc();
//            indexRunner ir = new indexRunner();
////            ir.documentsIndexerRunner(similarity,indexFile,doc_entities_dir, index_dir, stem.document_entities);
////            ir.searchIndex(similarity ,base, length, stem.query_entities, stem.query_related_document, index_dir, result_dir);
//            scoreCombination sc = new scoreCombination(stem,base ,length ,result_dir ,sdm_dir ,alpha,combination_dir );
//            sc.combineScores();
//        }
//        catch (Exception ex){
//             System.err.println(ex.getMessage());
//        }
        
//        ****************************remove doplicated in qrel***********************************
//        BufferedReader br;
//        
//        try {
//            Writer writer = new BufferedWriter(new FileWriter(("./data/2009/qrell_new.txt" ),true), 46384);
//            br = new BufferedReader(new FileReader(new File("./data/2009/qrell.txt")));
//        
//        String line = null;
//        String lastDocId="";
//        String lastqid ="";
//        line = br.readLine();
//        
//        do{
//            String docId = line.split(" ")[2];
//            String qid = line.split(" ")[0];
//            if(qid.equals("151"))
//                System.out.println("here");
//            if(lastDocId.equals(docId) && qid.equals(lastqid)) {
//                
//            }
//            else{
//                System.out.println(line);
//                writer.write(line+"\n");
//                lastDocId = docId;
//                lastqid = qid;
//            }
//            line= br.readLine();
//        }while (line != null);
//
//        
//        br.close();
//        writer.close();
//        } catch (Exception ex) {
//            Logger.getLogger(QueryExpantion.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        ********************************************************************************
        
    try {
            String year = "2012";
            String fileName = "./results/"+year+"/map/mapTfIDF";
            BufferedReader br1 = new BufferedReader(new FileReader("./results/"+year+"/map/mapSdm"));
            BufferedReader br2 = new BufferedReader(new FileReader(fileName)) ;
            Writer writer = new BufferedWriter(new FileWriter(("./results/"+year+"/map/improvedResultswithoutEquals.txt" ),true), 46384);
            
            writer.write("**********"+fileName+" improved! "+"************\n");
            String line1 = "";
            String line2 = "";
            while((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null){
                if(line1.split("\\s+")[1].equals(line2.split("\\s+")[1])){
                    if(Double.parseDouble(line1.split("\\s+")[2]) < Double.parseDouble(line2.split("\\s+")[2])){
                        writer.write(line2+"\n");
                    }
                }
            }
            
            br1.close();
            br2.close();
            writer.close();
            
    }
    catch(IOException e){
        System.out.println(e);
    }
        
    }
    
    
    
    
}
