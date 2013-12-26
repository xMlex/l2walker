package fw.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class PacketStream {
	private static String _name = "";
    private PacketStream() {
    };

    static public byte[] readPacket(BufferedInputStream in) throws IOException {
	int lengthLo = in.read();
	int lengthHi = in.read();
	int length = (lengthHi * 256 + lengthLo) - 2;
	
	//System.out.println("Read "+_name+" len: "+length);
	
	if (lengthHi < 0) {
	    System.out.println("Terminated the connection. Read packet");
	    return null;
	}

	byte[] incoming = new byte[length];

	int receivedBytes = 0;
	int newBytes = 0;
	while (newBytes != -1 && receivedBytes < length) {
	    newBytes = in.read(incoming, receivedBytes, length - receivedBytes);
	    receivedBytes += newBytes;
	}
	// System.out.print(Printer.printData(incoming,incoming.length,"READ PACKET"));
	// LOG
	return incoming;
    }

    static public void writePacket(BufferedOutputStream out, byte data[]) throws IOException {
	int len = data.length + 2;
	out.write(len & 0xff);
	out.write(len >> 8 & 0xff);
	out.write(data);
	out.flush();
	//System.out.println("Send "+_name+" len: "+len);
	//System.out.print(Printer.printData(data,data.length,_name+" "+"WRITE PACKET"));
    }
    static public void setName(String name){
    	_name = name;
    }
}
