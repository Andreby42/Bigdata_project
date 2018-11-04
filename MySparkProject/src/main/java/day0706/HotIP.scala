package day0706

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.kafka.KafkaUtils

case class LogInfo(user_id:String,user_ip:String,url:String,click_time:String,action_type:String,area_id:String) {
  
  
  
  object HotIP{
    def main(args :Array[String]):Unit={
      Logger.getLogger
      
      
      val conf=new SparkConf().setAppName("HotIP").setMaster("local[2]")
        val ssc=new StreamingContext(conf,Seconds(10))
      
      //
      val sqlContext=new SQLContext(ssc.sparkContext)
      import sqlContext.implicits._
      
      //ָ��Kafka��Topic,��ʾ�����topic�У�ÿ�ν���һ������
      val topic=Map("mytopic"->1)
      
      //����Dstream��������
      val kafkaStream=KafkaUtils.createStream(ssc,"192.168.234.21:2181","mygroup",topic)
      
      //��kAFKA�У����յ���������<key value> key ----�� null��ֵ
      val logRDD=kafkaStream.map(_._2)
       
      
      logRDD.foreachRDD((rdd:RDD[String])=>{
        
        
        val result=rdd.map(_.split(",")).map(x=>new LogInfo(x(0),x(1),x(2),x(3),x(4),x(5))).toDF
        
        //
        result.create
        
        //
        sqlContext.sql("select user_ip as IP,count(user_ip) as PV from clicklog group by user_ip").show
        
        
      })
      //��־
      ssc.start()
      ssc.awaitTermination()
    }
  }
}