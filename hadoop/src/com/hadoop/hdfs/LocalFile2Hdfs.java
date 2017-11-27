package com.hadoop.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class LocalFile2Hdfs {
    public static void main(String[] args) throws Exception {

        // ��ȡ��ȡԴ�ļ���Ŀ���ļ�λ�ò���
        String local = args[0];
        String uri = args[1];

        FileInputStream in = null;
        OutputStream out = null;
        Configuration conf = new Configuration();
        try {
            // ��ȡ�����ļ�����
            in = new FileInputStream(new File(local));

            // ��ȡĿ���ļ���Ϣ
            FileSystem fs = FileSystem.get(URI.create(uri), conf);
            out = fs.create(new Path(uri), new Progressable() {
                @Override
                public void progress() {
                    System.out.println("*");
                }
            });

            // ����ǰ100���ַ�
            in.skip(100);
            byte[] buffer = new byte[20];

            // ��101��λ�ö�ȡ20���ַ���buffer��
            int bytesRead = in.read(buffer);
            if (bytesRead >= 0) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }        
    }
}
