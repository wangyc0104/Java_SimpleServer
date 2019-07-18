package wyc.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML流式解析器<br>
 * @author Yicheng Wang
 */
@SuppressWarnings("all")
public class WebHandler extends DefaultHandler {
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Mapping> mappings = new ArrayList<Mapping>();
	private Entity entity;
	private Mapping mapping;
	private String tag; // 存储操作的标签
	private boolean isMapping = true; // 标记该标签是否是mapping

	@Override
	public void startElement(String uri, String name, String qName, Attributes atts) {
		if (null != qName) {
			tag = qName; // 存储标签的全名
			if (tag.equals("servlet")) {
				entity = new Entity();
				isMapping = false;
			} else if (tag.equals("servlet-mapping")) {
				mapping = new Mapping();
				isMapping = true;
			}
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String contents = new String(ch, start, length).trim();
		if (null != tag) { // 若标签为空，则不处理
			if (isMapping) { // 操作servlet-mapping
				if (tag.equals("servlet-name")) {
					mapping.setName(contents);
				} else if (tag.equals("url-pattern")) {
					mapping.addPattern(contents); // 一个servlet-name可能有多个pattern
				}
			} else {
				if (tag.equals("servlet-name")) {
					entity.setName(contents);
				} else if (tag.equals("servlet-class")) {
					entity.setClz(contents);
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String name, String qName) {
		if (null != qName) {
			if (qName.equals("servlet")) { // 注意：此处结束标签的"/"会自动忽略
				entities.add(entity); // 将entity添加到entities列表中
			} else if (qName.equals("servlet-mapping")) {
				mappings.add(mapping); // 将mapping添加到mappings列表中
			}
		}
		tag = null; // 每次处理完后，都将tag值丢弃以免下一次解析误用上一次解析的数据。
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

}
