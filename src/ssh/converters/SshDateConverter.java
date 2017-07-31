package ssh.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * ʹ��ת������ʱ����Ҫ����Ŀ�н�һ�������ļ�����������ļ��������ǹ̶��ģ�Ϊ��xwork-conversion.properties��.��Ҫע�����
 * �����������ѡ�������ѡ��Ϊ�����ļ��塱����������ύ����������LocalΪzh-CN��Struts2Ĭ�ϻᰴ��Local�������ַ������н��������Դ˴�
 * ���ַ���תΪDate���Ϳ���˳����ɡ����ڱ���ʾʱȴ��������⡣
 * @author Brisk
 *
 */
public class SshDateConverter extends StrutsTypeConverter{
	private DateFormat dateFormat;
	{
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if(toClass == Date.class){
			try{
				return dateFormat.parse(values[0]);
			}
			catch(ParseException e){
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String convertToString(Map context, Object o) {
		if(o instanceof Date){
			return dateFormat.format((Date)o);
		}
		
		return null;
	}

}


