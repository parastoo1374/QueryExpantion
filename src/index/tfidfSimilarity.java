/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package index;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;
import java.lang.Math;

/**
 *
 * @author Parastoo
 */
public class tfidfSimilarity extends TFIDFSimilarity{

    @Override
    public float coord(int i, int i1) {
        return 1;
    }

    @Override
    public float queryNorm(float f) {
        return 1;
    }

    @Override
    public float tf(float f) {
        return (float) Math.sqrt(f);
    }

    @Override
    public float idf(long l, long l1) {
        return (float) (Math.log( l1/(double) (l+1)) +1.0);
    }

    @Override
    public float lengthNorm(FieldInvertState fis) {
        return 1;
    }

    @Override
    public float decodeNormValue(long l) {
        return 1;
    }

    @Override
    public long encodeNormValue(float f) {
        return 1;
    }

    @Override
    public float sloppyFreq(int i) {
        return 1;
    }

    @Override
    public float scorePayload(int i, int i1, int i2, BytesRef br) {
        return 1;
    }
    
}
