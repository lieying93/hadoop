package com.hadoop.mapreduce;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AvgTemperatureCombiner extends Reducer<Text, Text, Text, Text>{

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        double sumValue = 0;
        long numValue = 0;

        for(Text value : values) {
            sumValue += Double.parseDouble(value.toString());
            numValue ++;
        }

        context.write(key, new Text(String.valueOf(sumValue) + ',' + String.valueOf(numValue)));
    }
}
