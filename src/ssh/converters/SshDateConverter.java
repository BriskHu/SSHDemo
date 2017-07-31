package ssh.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 使用转换器类时，需要在项目中建一个配置文件，这个配置文件的名字是固定的，为“xwork-conversion.properties”.需要注意的是
 * 浏览器语言首选项的语言选择为“中文简体”，即浏览器提交给服务器的Local为zh-CN。Struts2默认会按该Local对日期字符串进行解析，所以此处
 * 从字符串转为Date类型可以顺利完成。但在表单显示时却会出现问题。
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


