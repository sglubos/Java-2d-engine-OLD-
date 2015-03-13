package svk.sglubos.engine.utils.debug;

import svk.sglubos.engine.utils.Constants;

//TODO document build fancy string formatter
public class DebugStringBuilder implements Constants{
	private StringBuilder builder = new StringBuilder();
	
	
	public void append(String string) {
		builder.append(string);
	}
	
	public void appendTab() {
		builder.append(TABULATOR);
	}
	
	public void appendln(String string) {
		builder.append(string);
		builder.append(LINE_SEPARATOR);
	}
	
	public void append(String... strings) {
		for(String s : strings) {
			builder.append(s);
		}
	}
	
	public void appendln(String... strings) {
		for(String s : strings) {
			builder.append(s);
		}
		builder.append(LINE_SEPARATOR);
	}
	
	public void appendln() {
		builder.append(LINE_SEPARATOR);
	}
	
	public void appendLineSeparator() {
		builder.append(LINE_SEPARATOR);
	}
	
	public void appendTabln(String string) {
		builder.append(TABULATOR);
		builder.append(string);
		builder.append(LINE_SEPARATOR);
	}
	
	public void appendClassData(Class<?> clas, int hashcode) {
		builder.append(clas.getName());
		builder.append("@");
		builder.append(Integer.toHexString(hashcode));
	}
	
	public void appendObjectToString(String string, Object object) {
		builder.append(string);
		try{
			builder.append(object.toString());			
		} catch (NullPointerException e) {
			builder.append("null");
		}
	}
	
	public void appendObjectToStringTab(String string, Object object) {
		builder.append(TABULATOR);
		appendObjectToString(string, object);
	}
	
	public void appendObjectToStringTabln(String string, Object object) {
		builder.append(TABULATOR);
		appendObjectToStringln(string, object);
	}
	
	public void appendObjectToStringln(String string, Object object) {
		builder.append(string);
		try{
			builder.append(object.toString());			
		} catch (NullPointerException e) {
			builder.append("null");
		}
		builder.append(LINE_SEPARATOR);
	}
	
	public void appendObjectArrayln(String string, Object[] object) {
		
	}
	
	public void appendClassDataBracket(Class<?> clas, int hashcode) {
		appendClassData(clas, hashcode);
		builder.append(" {");
		builder.append(LINE_SEPARATOR);
	}
	
	public void appendCloseBracket() {
		builder.append("}");
	}
	
	public String getString() {
		return builder.toString();
	}
}
