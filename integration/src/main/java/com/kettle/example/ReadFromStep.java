package com.kettle.example;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.trans.step.RowListener;
import org.pentaho.di.trans.step.StepInterface;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abelard on 6/15/17.
 */
public class ReadFromStep {

    public static void main(String[] args) throws KettleException {
        String filename = args[0];
        String stepname = args[1];

        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta(filename);
        Trans trans = new Trans(transMeta);
        trans.prepareExecution(null);

        final List<RowMetaAndData> rows = new ArrayList<RowMetaAndData>();
        RowListener rowListener = new RowAdapter() {
            public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row) {
                rows.add(new RowMetaAndData(rowMeta, row));
            }
        };
        StepInterface stepInterface = trans.findRunThread(stepname);
        stepInterface.addRowListener(rowListener);

        trans.startThreads();
        trans.waitUntilFinished();

        if(trans.getErrors() != 0) {
            System.out.println("Error");
        } else {
            System.out.println("We read " + rows.size() + " rows from step " + stepname);
        }
    }
}
