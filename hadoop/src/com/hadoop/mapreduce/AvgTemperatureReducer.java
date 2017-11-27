package com.hadoop.mapreduce;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AvgTemperatureReducer extends Reducer<Text, Text, Text, IntWritable>{

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        double sumValue = 0;
        long numValue = 0;
        int avgValue = 0;

        for(Text value : values) {
            String[] valueAll = value.toString().split(",");
            sumValue += Double.parseDouble(valueAll[0]);
            numValue += Integer.parseInt(valueAll[1]);
        }

        avgValue  = (int)(sumValue/numValue);
        context.write(key, new IntWritable(avgValue));
    }
}
