package day0629.sogou;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SogouLogMain {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		//1������job
		Job job = Job.getInstance(conf);
		job.setJarByClass(SogouLogMain.class);
		
		//2.�ƶ������Mapper�����������
		job.setMapperClass(SogouLogMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
        
		//3.�ƶ������Reducer�����������
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		//4.�������������
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		//5.ִ��
		job.waitForCompletion(true);
	}

}
