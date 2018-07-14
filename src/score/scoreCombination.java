/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package score;

import entityStem.stemmer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

/**
 *
 * @author Parastoo
 */
public class scoreCombination {
    
    public double maxValue_result;
    public double minValue_result;
    public double maxValue_otherResult;
    public double minValue_otherResult;
    public double alpha;
    
    public String file_dir;
    public String otherFile_dir;
    public String result_dir;
    public stemmer stem;
    
    public HashMap<String, ArrayList<String>> query_related_doc;
    public HashMap<String, ArrayList<String>> results = new HashMap<>();
    
    public scoreCombination(stemmer stem,String fileDir, String otherFileDir , double alpha, String resultDir) throws IOException{
        this.stem = stem;
        this.file_dir = fileDir;
        this.otherFile_dir = otherFileDir;
        this.query_related_doc = stem.createQueryRelatedDocWithScores();
        this.result_dir = resultDir;
        this.alpha = alpha;
        
        double[] fileMaxMin = stem.computeMaxAndMin(file_dir);
        double[] otherFileMaxMin = stem.computeMaxAndMin(otherFile_dir);
        
        this.minValue_result = fileMaxMin[0];
        this.maxValue_result = fileMaxMin[1];
        this.minValue_otherResult = otherFileMaxMin[0];
        this.maxValue_otherResult = otherFileMaxMin[1];
    }
    
    
    public void combineScores( ) throws IOException{
        try {
            
            Writer writer = new BufferedWriter(new FileWriter((result_dir + "/resultSdmAndBM25.txt"),true), 26384);
            BufferedReader br = new BufferedReader(new FileReader(new File(file_dir)));
            String line = null;
            String temp ;
            int rank;
            while ((line = br.readLine()) != null) {
                
                String[] splits = line.split(" ");
                double score = Double.parseDouble(splits[4]);
                String qid = splits[0];
                String document  = splits[2];
                score = computeScore(qid, document,score);
                
                ArrayList<String> entList;
                if (results.containsKey(qid)) {
                    entList = results.get(qid);
                } else {
                    entList = new ArrayList<>();
                }

                entList.add(document + " "+score);

                results.put(qid, entList);
                
                
            }
            
                
            sortHashMap();
            
            for (int i = 201; i <= 250; i++) {
                try{
                    rank = 1;
                    for (String value :results.get(Integer.toString(i))) {
                        temp = "";
                        String[] spilits = value.split(" ");
                        temp = i + " " + "Q0" + " " + spilits[0] + " " + rank + " " + spilits[1] + " " + "parastoo" + "\n";
                        writer.write(temp);
                        System.out.print(temp);
                        rank++;
                    }
                }
                catch(Exception ex){
                    
                }
            }
            writer.close();
        
        }
        catch(Exception ex){
            
        }
    }

    private double computeScore(String qid,String document, double score) {
        for (String value : query_related_doc.get(qid)) {
            if(value.contains(document)){
                double otherScore = Double.parseDouble( value.split(" ")[1]);
                otherScore = normalizeScore(otherScore,minValue_otherResult,maxValue_otherResult);
                score = normalizeScore(score, minValue_result, maxValue_result);
                return ( alpha * otherScore + (1-alpha) * score);
            }
            
        }
        return 0;
    }

    private double normalizeScore(double score, double min, double max) {
        return (score - min )/ (double) (max - min);
    }

    private void sortHashMap() {
        for (int k = 201; k <= 250; k++) {
            try{
                ArrayList<String> values =(ArrayList<String>) results.get(Integer.toString(k));
                
                boolean swapped = true;
                int j = 0;
                while (swapped) {
                    swapped = false;
                    j++;
                    for (int i = 0; i < values.size() - j; i++) {
                        String valueI = (String)values.get(i);
                        String valueII = (String)values.get(i+1);
                        if (Double.parseDouble(valueI.split(" ")[1]) < Double.parseDouble(valueII.split(" ")[1])) {
                            values.set(i, valueII);
                            values.set(i+1, valueI) ;
                            swapped = true;
                        }
                    }
                }
                results.put(Integer.toString(k), values);
            }
            catch(Exception ex){
                
            }
        }
        
    
    }
}
