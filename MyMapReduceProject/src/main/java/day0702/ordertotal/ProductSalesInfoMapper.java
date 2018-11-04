package day0702.ordertotal;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ProductSalesInfoMapper  extends Mapper<LongWritable, Text, IntWritable, Text>{
@Override
protected void map(LongWritable key1, Text value1,Context context)
		throws IOException, InterruptedException {
	// ��������ݣ���������Ʒ
	//ʹ���ж��ļ����ķ�ʽ
	//�õ������HDFS��·����--------> /input/sh/sales
	String path=((FileSplit)context.getInputSplit()).getPath().getName();
			String fileName=path.substring(beginIndex, endIndex);
			String data=value1.toString();
			String[] words=data.split(",");
			
			if(fileName.equals("products")) {
				context.write(new IntWritable(value), new Text(words[1]));
			}else {
				context.write(new IntWritable(Integer.parseInt(words[0])), arg1);
			}
	
}
}
