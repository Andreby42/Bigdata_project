package day0706.hotip;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.shade.org.apache.http.conn.scheme.Scheme;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import kafka.utils.Log4jController$;
import scala.Function0;
import scala.actors.threadpool.Arrays;
import scala.runtime.BoxedUnit;

public class HotIPMain {
	public static void main(String[] args) {
		//����һ������Topology=spout+bolt
		//Spout ��Kafka�н�������
		TopologyBuilder builder=new TopologyBuilder();
		
		//Spout ��Kafka�н�������
		
		//ָ�������spout�����������kafaka������
		String zks="192.168.234.21:2181";
		//topic������
		String topic="mytopic";
		
		String zkRoot="storm";
		
		String id="mytopic";
		
		//ָ��Broker��ַ��Ϣ		
		BrokerHosts hosts=new ZkHosts(zks);
		SpoutConfig spoutConf=new SpoutConfig(hosts, topic, zkRoot, id);
		//ָ����kafka���յ����ַ���
		spoutConf.scheme=new SchemeAsMultiScheme(new StringScheme());
		spoutConf.zkServers=Arrays.asList(new String[] {"192.168.234.21"});
		spoutConf.zkPort=2181;
		
		
		//ָ�������Spout������ִ�
		builder.setSpout("kafka_reader", new KafkaSpout(spoutConf));
		//ָ������ĵ�һ��bolt������ִ�
		builder.setBolt("split_bolt", new HotIPSplitBolt()).shuffleGrouping("kafka_reader");
		
		//ָ������ĵڶ���bolt������ü���
		builder.setBolt("hotip_bolt",new HotIPTotalBolt()).fieldsGrouping("split_bolt", new Fields("ip"));
		
		Config Conf=new Config();
			LocalCluster cluster=new LocalCluster();
		
		
		//ָ������ĵڶ���bolt���������
	}

}
