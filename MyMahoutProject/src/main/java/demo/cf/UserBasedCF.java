package demo.cf;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


/*
 * �����Ĺ���(����)
 * 1.���������û���user������Ʒ��item��������
 * 2.�����û�����Ʒ�����֣����������û�֮������ƶ�(�������ƶȡ�ŷʽ����ȵ�)
 * 3.ѡ���뵱ǰ�û������Ƶ�N���û�
 * 4.����ǰ�û����������Ʒ���Ƽ��������û�����֮Ҳ��
 */
public class UserBasedCF {
    final static int NEIGHBORHOOD_NUM = 2;
    final static int RECOMMENDER_NUM = 3;

    public static void main(String[] args) throws Exception {
    	//��������Դ��������ģ�ͣ�������־���
        String file = "D:\\download\\data\\ratingdata.txt";
        DataModel model = new FileDataModel(new File(file));

        //���ݴ�־���,�����û������ƶ�
        UserSimilarity usersimilarity = new EuclideanDistanceSimilarity(model);
        
        //�ҵ����û�������ھӣ������ҵ�NEIGHBORHOOD_NUM�������û�
        //n:�ҵ����Ƶ�N���û�
        NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, usersimilarity, model);
        
        // ���������û����Ƽ����棬����dataModelΪ����ģ�ͣ�neighborhoodΪ�û�����ģ�ͣ�usersimilarityΪ���ƶ�ģ��
        Recommender r = new GenericUserBasedRecommender(model, neighbor, usersimilarity);
        
        System.out.println("***************����һ����һ���û��Ƽ�****************");
        //���û�1�Ƽ�������Ʒ  ˵����recommender.recommend(userID, howMany)
        List<RecommendedItem> recommendations = r.recommend(1, 2);
		for (RecommendedItem recommendation : recommendations) {
			// ����Ƽ����
			System.out.println("���û�1�Ƽ�����Ʒ�ǣ�" + recommendation.getItemID());
		}
		
		System.out.println("***************���Զ�����ÿ���û����Ƽ�****************");
		//��ÿ���û��Ƽ���Ʒ
        LongPrimitiveIterator iter = model.getUserIDs();
        while (iter.hasNext()) {
            long uid = iter.nextLong();
            System.out.printf("�û�ID:%s  ", uid);            
            
            List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
            for (RecommendedItem ritem : list) {
                System.out.printf("(%s,%f)", ritem.getItemID(), ritem.getValue());
            }
            System.out.println();
        }
    }
}