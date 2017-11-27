package com.hadoop.lianxi;

 

import java.io.IOException;

 

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.io.LongWritable;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.Mapper;

import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

 

public class Temperature {

    /**

     * �ĸ��������ͷֱ����

     * KeyIn        Mapper���������ݵ�Key��������ÿ�����ֵ���ʼλ�ã�0,11,...��

     * ValueIn      Mapper���������ݵ�Value��������ÿ������

     * KeyOut       Mapper��������ݵ�Key��������ÿ�������еġ���ݡ�

     * ValueOut     Mapper��������ݵ�Value��������ÿ�������еġ����¡�

     */

    static class TempMapper extends

            Mapper<LongWritable, Text, Text, IntWritable> {

        @Override

        public void map(LongWritable key, Text value, Context context)

                throws IOException, InterruptedException {

            // ��ӡ����: Before Mapper: 0, 2000010115

            System.out.print("Before Mapper: " + key + ", " + value);

            String line = value.toString();

            String year = line.substring(0, 4);

            int temperature = Integer.parseInt(line.substring(8));

            context.write(new Text(year), new IntWritable(temperature));

            // ��ӡ����: After Mapper:2000, 15

            System.out.println(

                    "======" +

                    "After Mapper:" + new Text(year) + ", " + new IntWritable(temperature));

        }

    }

 

    /**

     * �ĸ��������ͷֱ����

     * KeyIn        Reducer���������ݵ�Key��������ÿ�������еġ���ݡ�

     * ValueIn      Reducer���������ݵ�Value��������ÿ�������еġ����¡�

     * KeyOut       Reducer��������ݵ�Key�������ǲ��ظ��ġ���ݡ�

     * ValueOut     Reducer��������ݵ�Value����������һ���еġ�������¡�

     */

    static class TempReducer extends

            Reducer<Text, IntWritable, Text, IntWritable> {

        @Override

        public void reduce(Text key, Iterable<IntWritable> values,

                Context context) throws IOException, InterruptedException {

            int maxValue = Integer.MIN_VALUE;

            StringBuffer sb = new StringBuffer();

            //ȡvalues�����ֵ

            for (IntWritable value : values) {

                maxValue = Math.max(maxValue, value.get());

                sb.append(value).append(", ");

            }

            // ��ӡ������ Before Reduce: 2000, 15, 23, 99, 12, 22, 

            System.out.print("Before Reduce: " + key + ", " + sb.toString());

            context.write(key, new IntWritable(maxValue));

            // ��ӡ������ After Reduce: 2000, 99

            System.out.println(

                    "======" +

                    "After Reduce: " + key + ", " + maxValue);

        }

    }

 

    public static void main(String[] args) throws Exception {

        //����·��

        String dst = "hdfs://localhost:9000/intput.txt";

        //���·���������ǲ����ڵģ����ļ���Ҳ���С�

        String dstOut = "hdfs://localhost:9000/output";

        Configuration hadoopConfig = new Configuration();

         

        hadoopConfig.set("fs.hdfs.impl", 

            org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()

        );

        hadoopConfig.set("fs.file.impl",

            org.apache.hadoop.fs.LocalFileSystem.class.getName()

        );

        Job job = new Job(hadoopConfig);

         

        //�����Ҫ���jar���У���Ҫ�������

        //job.setJarByClass(NewMaxTemperature.class);

 

        //jobִ����ҵʱ���������ļ���·��

        FileInputFormat.addInputPath(job, new Path(dst));

        FileOutputFormat.setOutputPath(job, new Path(dstOut));

 

        //ָ���Զ����Mapper��Reducer��Ϊ�����׶ε���������

        job.setMapperClass(TempMapper.class);

        job.setReducerClass(TempReducer.class);

         

        //���������������Key��Value������

        job.setOutputKeyClass(Text.class);

        job.setOutputValueClass(IntWritable.class);

         

        //ִ��job��ֱ�����

        job.waitForCompletion(true);

        System.out.println("Finished");

    }

}