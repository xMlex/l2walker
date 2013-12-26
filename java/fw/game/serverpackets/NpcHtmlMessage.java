package fw.game.serverpackets;

import fw.game.GameEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * the HTML parser in the client knowns these standard and non-standard tags and attributes
 * VOLUMN
 * UNKNOWN
 * UL
 * U
 * TT
 * TR
 * TITLE
 * TEXTCODE
 * TEXTAREA
 * TD
 * TABLE
 * SUP
 * SUB
 * STRIKE
 * SPIN
 * SELECT
 * RIGHT
 * PRE
 * P
 * OPTION
 * OL
 * MULTIEDIT
 * LI
 * LEFT
 * INPUT
 * IMG
 * I
 * HTML
 * H7
 * H6
 * H5
 * H4
 * H3
 * H2
 * H1
 * FONT
 * EXTEND
 * EDIT
 * COMMENT
 * COMBOBOX
 * CENTER
 * BUTTON
 * BR
 * BODY
 * BAR
 * ADDRESS
 * A
 * SEL
 * LIST
 * VAR
 * FORE
 * READONL
 * ROWS
 * VALIGN
 * FIXWIDTH
 * BORDERCOLORLI
 * BORDERCOLORDA
 * BORDERCOLOR
 * BORDER
 * BGCOLOR
 * BACKGROUND
 * ALIGN
 * VALU
 * READONLY
 * MULTIPLE
 * SELECTED
 * TYP
 * TYPE
 * MAXLENGTH
 * CHECKED
 * SRC
 * Y
 * X
 * QUERYDELAY
 * NOSCROLLBAR
 * IMGSRC
 * B
 * FG
 * SIZE
 * FACE
 * COLOR
 * DEFFON
 * DEFFIXEDFONT
 * WIDTH
 * VALUE
 * TOOLTIP
 * NAME
 * MIN
 * MAX
 * HEIGHT
 * DISABLED
 * ALIGN
 * MSG
 * LINK
 * HREF
 * ACTION
 */
public class NpcHtmlMessage extends ServerBasePacket
{
	// d S
	// d is usually 0, S is the html text starting with <html> and ending with </html>

	private GameEngine _clientThread;
	private int _messageId;
	private String _html;

	public NpcHtmlMessage(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);

//		readH();	// msg len
		readC();	// msg type
		_messageId = readD();
		_html = readS();
	}

	public void runImpl()
	{
		System.out.println(_html);
		System.out.println(replace(_html,"<a action=","<a href="));
		_html = replace(_html,"<a action=","<a href=");
		_html = replace(_html,"<br1>","<br>");
		_clientThread.getVisualInterface().requestHtmlDialog(_html);
	}

	final void writeImpl()
	{
		writeC(0x0f);
		writeD(_messageId);
		writeS(_html);
	}

	static String replace(String str, String pattern, String replace) {
		int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	            result.append(str.substring(s, e));
	            result.append(replace);
	            s = e+pattern.length();
	    }

	    result.append(str.substring(s));
	    return result.toString();
	}

}
