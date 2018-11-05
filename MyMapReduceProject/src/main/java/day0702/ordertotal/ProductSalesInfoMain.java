package day0702.ordertotal;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ProductSalesInfoMain {

	public static void main(String[] args) throws Exception {
		//1������Job
		Job job = Job.getInstance(new Configuration());
		job.setJarByClass(ProductSalesInfoMain.class);
		
		//2��ָ�������Mapper�����������
		job.setMapperClass(ProductSalesInfoMapper.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		//3��ָ�������Reducer�����������
		job.setReducerClass(ProductSalesInfoReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		//4���������������
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		//5��ִ��
		job.waitForCompletion(true);

	}

}
