package day0629.PeopleInfo

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object PeopleInfoCalc {
  def main(args:Array[String]): Unit={
    //����SparkContext
    val conf=new SparkConf.setAppName("PeopleInfoCalc").setMaster("local")
    val sc=new SparkContext(conf)
    
    //��ȡ����
    val dataFile=sc.textFile("")
    //��֣��С�Ů
    val maleData=dataFile.filter(f)
    val femaleData=dataFile.filter(f)
    
    val maleHeightData=
    val femaleHeightData=
    
    val lowestMale=maleHeightData
    val lowestFemale=femaleHeightData.so
    
    
  }
}