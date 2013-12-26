package fw.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javolution.text.TextBuilder;


public class Printer {


		public static byte[] toBytes(String value) {
			value = value.replaceAll(" ", "");
			byte[] result = new byte[value.length() / 2];
			for(int i=0;i<result.length;i++) {
				result[i] = (byte)(Integer.parseInt(value.substring(i*2, (i*2)+2), 16) & 0xff);
			}
			return result;
		}

	    public static String printData(byte[] data) {
	    	StringBuilder sb = new StringBuilder();

	    	int i, j;
	    	for (i=0; i<data.length; i++) {
	    		if ((i>0) && (i%16==0)) {
	    			sb.append("               ");
	    			for (j=i-16; j<i; j++) {
	    				if ((data[j]>0x1f) && (data[j]<0x80)) {
	    					sb.append((char) (data[j] % 0xff));
	    				} else {
	    					sb.append('.');
	    				}
	    			}
	    			sb.append('\n');
	    		}

	    		sb.append(' ');
	    		sb.append(String.format("%1$02x", data[i] & 0xff));
	    	}

	    	// last step
	    	j = (data.length % 16);
	    	if (j > 0) {
	    		j = 16 - j;
	        	for (i=data.length-j; i<data.length; i++) {
	        		sb.append(" __");
	        	}
	        	sb.append("               ");
	        	j = 16 - j;
	        	for (i=data.length-j; i<data.length; i++) {
	        		if ((data[i]>0x1f) && (data[i]<0x80)) {
						sb.append((char) (data[i] % 0xff));
					} else {
						sb.append('.');
					}
	        	}
	    	}

	    	sb.append('\n');

	    	return sb.toString();
	    }

	    public static String printData(byte[] data, int len)
		{
	        TextBuilder result = new TextBuilder();

			int counter = 0;

			for (int i=0;i< len;i++)
			{
				if (counter % 16 == 0)
				{
					result.append(fillHex(i,4)+": ");
				}

				result.append(fillHex(data[i] & 0xff, 2) + " ");
				counter++;
				if (counter == 16)
				{
					result.append("   ");

					int charpoint = i-15;
					for (int a=0; a<16;a++)
					{
						int t1 = data[charpoint++];
						if (t1 > 0x1f && t1 < 0x80)
						{
							result.append((char)t1);
						}
						else
						{
							result.append('.');
						}
					}

					result.append("\n");
					counter = 0;
				}
			}

			int rest = data.length % 16;
			if (rest > 0 )
			{
				for (int i=0; i<17-rest;i++ )
				{
					result.append("   ");
				}

				int charpoint = data.length-rest;
				for (int a=0; a<rest;a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char)t1);
					}
					else
					{
						result.append('.');
					}
				}

				result.append("\n");
			}


			return result.toString();
		}

	    public static String printData(byte[] data, int len, String Header)
		{
	        TextBuilder result = new TextBuilder();

			int counter = 0;

			result.append("<<"+Header+">> len:"+len+"\n");
			result.append("BEGIN====================================================================\n");

			for (int i=0;i< len;i++)
			{
				if (counter % 16 == 0)
				{
					result.append(fillHex(i,4)+": ");
				}

				result.append(fillHex(data[i] & 0xff, 2) + " ");
				counter++;
				if (counter == 16)
				{
					result.append("   ");

					int charpoint = i-15;
					for (int a=0; a<16;a++)
					{
						int t1 = data[charpoint++];
						if (t1 > 0x1f && t1 < 0x80)
						{
							result.append((char)t1);
						}
						else
						{
							result.append('.');
						}
					}

					result.append("\n");
					counter = 0;
				}
			}

			int rest = data.length % 16;
			if (rest > 0 )
			{
				for (int i=0; i<17-rest;i++ )
				{
					result.append("   ");
				}

				int charpoint = data.length-rest;
				for (int a=0; a<rest;a++)
				{
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char)t1);
					}
					else
					{
						result.append('.');
					}
				}

				result.append("\n");
			}

			result.append("END======================================================================\n\n");
			return result.toString();
		}

		public static String fillHex(int data, int digits)
		{
			String number = Integer.toHexString(data);

			for (int i=number.length(); i< digits; i++)
			{
				number = "0" + number;
			}

			return number;
		}

		public static String getCurrDate(){
			Date now = new Date();
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			return "["+df.format(now)+"] ";
		}
}
