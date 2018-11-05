package day0706.blacklist;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;

import scala.actors.threadpool.Arrays;

public class BlackListTopology {

	public static void main(String[] args) {
		// ����һ������Topology = spout + bolt
		//Spout ��Kafka�н�������
		TopologyBuilder builder = new TopologyBuilder();
				
		//ָ�������spout�����������kafka������
		//ָ��ZK�ĵ�ַ
		String zks = "192.168.157.21:2181";
		//topic������
		String topic = "mytopic";
		//Storm��ZK�ĸ�Ŀ¼
		String zkRoot = "/storm";
		String id = "mytopic";
		//ָ��Broker��ַ��Ϣ
		BrokerHosts hosts = new ZkHosts(zks);		
		
		SpoutConfig spoutConf = new SpoutConfig(hosts, topic, zkRoot, id);
		spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());  //ָ����Kafka�н��յ����ַ���
		spoutConf.zkServers = Arrays.asList(new String[]{"192.168.157.21"});
		spoutConf.zkPort = 2181;
		builder.setSpout("kafka_reader", new KafkaSpout(spoutConf));
		
		//ָ������ĵ�һ��bolt��������зִ�
		builder.setBolt("split_bolt", new BlackListSplitBolt()).shuffleGrouping("kafka_reader");
		
		//ָ������ĵڶ���bolt��������ڴ��ڼ���
		builder.setBolt("blacklist_countbolt", new BlackListTotalByWindowBolt()
				                               .withWindow(BaseWindowedBolt.Duration.seconds(30),  //���ڵĳ��� 
				                            		       BaseWindowedBolt.Duration.seconds(10))  //�����ľ���
				       )
		       .fieldsGrouping("split_bolt", new Fields("userID"));
		
		//ָ������ĵ�����bolt��������������û���Ϣд��MySQL
		builder.setBolt("blacklist_mysql_bolt", new BlackListMySQLBolt()).shuffleGrouping("blacklist_countbolt");
		
		//�ڱ�����������
		Config conf = new Config();
        //Ҫ��֤��ʱʱ����ڵ��ڴ��ڳ���+�����������  
        conf.put("topology.message.timeout.secs", 40000);		
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("mydemoByWindow", conf, builder.createTopology());
	}
}
















