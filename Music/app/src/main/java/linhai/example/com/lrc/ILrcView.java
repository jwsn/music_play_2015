package linhai.example.com.lrc;

import java.util.List;


public interface ILrcView{
	/**
	 * ��ʼ�����ʣ���ɫ�������С������
	 */
	void init();
	
	/***
	 * ��������Դ
	 * @param lrcRows
	 */
	void setLrcContents(List<LrcContent> lrcContents);
	
	/**
	 * ָ��ʱ��
	 * @param progress  ʱ�����
	 * @param fromSeekBarByUser �Ƿ����û�����Seekbar����
	 */
	void seekTo(int progress, boolean fromSeekBar, boolean fromSeekBarByUser);
	
	/***
	 * ���ø�����ֵ����ű���
	 * @param scalingFactor
	 */
	void setLrcScalingFactor(float scalingFactor);
	
	/**
	 * ����
	 */
	void reset();
	
}