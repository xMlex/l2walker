package fw.extensions.util;

import org.w3c.dom.Node;

import java.util.logging.Logger;

public class XMLUtil
{
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(XMLUtil.class.getName());

	public static int readSubNodeIntValue(Node n, String name, int dflt)
	{
		if(name == null)
			return dflt;

		for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			if(name.equalsIgnoreCase(d.getNodeName()))
				return Integer.parseInt(d.getNodeValue());

		return dflt;
	}

	public static long readSubNodeLongValue(Node n, String name, long dflt)
	{
		if(name == null)
			return dflt;

		for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			if(name.equalsIgnoreCase(d.getNodeName()))
				return Long.parseLong(d.getNodeValue());

		return dflt;
	}

	public static String readSubNodeValue(Node n, String name, String dflt)
	{
		if(name == null)
			return dflt;

		for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			if(name.equalsIgnoreCase(d.getNodeName()))
				return d.getNodeValue();

		return dflt;
	}

	public static String getAttributeValue(Node n, String item)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if(d == null)
			return "";
		final String val = d.getNodeValue();
		if(val == null)
			return "";
		return val;
	}

	public static boolean getAttributeBooleanValue(Node n, String item, boolean dflt)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if(d == null)
			return dflt;
		final String val = d.getNodeValue();
		if(val == null)
			return dflt;
		return Boolean.parseBoolean(val);
	}

	public static int getAttributeIntValue(Node n, String item, int dflt)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if(d == null)
			return dflt;
		final String val = d.getNodeValue();
		if(val == null)
			return dflt;
		return Integer.parseInt(val);
	}

	public static long getAttributeLongValue(Node n, String item, long dflt)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if(d == null)
			return dflt;
		final String val = d.getNodeValue();
		if(val == null)
			return dflt;
		return Long.parseLong(val);
	}

	public static Node findSubNode(Node n, String name)
	{
		if(name == null)
			return null;

		for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			if(name.equalsIgnoreCase(d.getNodeName()))
				return d;

		return null;
	}
}