package day0706.hotip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.mysql.fabric.xmlrpc.base.Value;

public class HotIPTotalBolt extends BaseRichBolt {
	
	private OutputCollector collector;
	
	private Map<String, Integer> result=new HashMap<String, Integer>();
	
	
 
	public void execute(Tuple tuple) {
		// ȡ������
		String ip=tuple.getStringByField("ip");
		int count=tuple.getIntegerByField("count");
		
		//���
		if (result.containsKey(ip)) {
			//������ڣ��ۼ�
			int total=result.get(ip);
					result.put(ip, total+count);
		}
		else {
			result.put(ip,count);
		}
		
		System.out.println("Hot IP�Ľ����"+result);
		this.collector.emit((List<Object>) new Values(ip,result.get(ip)));
	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector=collector;
	}

	public void OutputFields(OutputFieldsDeclarer declare) {
		// TODO Auto-generated method stub
		declare.declare(new Fields("ip","total"));
		
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}

}
