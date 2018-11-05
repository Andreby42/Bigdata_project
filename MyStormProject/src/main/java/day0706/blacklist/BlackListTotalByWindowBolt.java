package day0706.blacklist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;

//ÿ10���ӣ�ͳ�ƹ�ȥ30���ڷ���Ƶ�ʳ���4�ε��û���Ϣ��
public class BlackListTotalByWindowBolt extends BaseWindowedBolt {

	private OutputCollector collector;
	
	//����һ�����ϣ�������ô��ڴ���Ľ��
	private Map<Integer, Integer> result = new HashMap<Integer, Integer>();
	
	//������inputWindow�ô����е�����
	public void execute(TupleWindow inputWindow) {
		//�õ��ô����е���������
		List<Tuple> list = inputWindow.get();
		
		//����ô����е���������
		for(Tuple t:list){
			//ȡ������
			int userID = t.getIntegerByField("userID");
			int count = t.getIntegerByField("count");
			
			//���
			if(result.containsKey(userID)){
				//����������ۼ�
				int total = result.get(userID);
				result.put(userID, total+count);
			}else{
				//��һ�γ���
				result.put(userID, count);
			}
			
			//���
			System.out.println("ͳ�ƵĽ��: " + result);
		
			//Ƶ�ʳ���4�ε��û���Ϣ  -----> ���MySQL���ݿ�
			if(result.get(userID) > 4){
				//������û���Ϣ������һ��bolt��� -----> ���MySQL���ݿ�
				this.collector.emit(new Values(userID,result.get(userID)));
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("userid","PV"));
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
}












