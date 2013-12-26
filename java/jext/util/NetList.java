package jext.util;

/**
 * Network util by DRiN Support formats: IP1.IP2.IP3.IP4
 * IP1.IP2.IP3.IP4/MASKBITS[0-32] IP1.IP2.IP3.IP4/M1.M2.M3.M4
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public final class NetList {
	private final GArray<Net> _Nets = new GArray<Net>();

	public boolean AddNet(String address) {
		if (address == null)
			return false;
		if (address.length() == 0)
			return false;
		if (address.startsWith("#"))
			return false;
		if (address.startsWith("*"))
			return false;

		Net _net = new Net(address.trim());
		_Nets.add(_net);
		return true;
	}

	public int LoadFromLines(List<String> lines) {
		int added = 0;
		if (lines == null)
			return added;
		for (int i = 0; i < lines.size(); i++)
			if (AddNet(lines.get(i)))
				added++;
		return added;
	}

	public int LoadFromFile(String fn) {
		if (fn == null || fn.length() == 0)
			return 0;
		int added = 0;

		try {
			LineNumberReader lnr = new LineNumberReader(new BufferedReader(
					new FileReader(fn)));
			String line = null;
			while ((line = lnr.readLine()) != null)
				if (AddNet(line))
					added++;
		} catch (Exception e) {
			return 0;
		}
		return added;
	}

	public int LoadFromString(String _s, String _regex) {
		int added = 0;

		if (_s == null || _regex == null || _s.length() == 0
				|| _regex.length() == 0)
			return 0;

		for (String ip : _s.split(_regex))
			if (AddNet(ip))
				added++;

		return added;
	}

	public int ipInNet(String ip) {
		for (int i = 0; i < _Nets.size(); i++) {
			Net _net = _Nets.get(i);
			if (_net.isInNet(ip))
				return i;
		}
		return -1;
	}

	public boolean isIpInNets(String ip) {
		return ipInNet(ip) != -1;
	}

	public int NetsCount() {
		return _Nets.size();
	}

	public void ClearNets() {
		_Nets.clear();
	}

	private byte[] IntToBytes(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) (i >> 24 & 0xFF);
		result[1] = (byte) (i >> 16 & 0xFF);
		result[2] = (byte) (i >> 8 & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	public String NetByIndex(int i) {
		if (_Nets.size() > i) {
			Net _net = _Nets.get(i);
			try {
				InetAddress _ip = InetAddress.getByAddress(IntToBytes(_net
						.getNet()));
				InetAddress _mask = InetAddress.getByAddress(IntToBytes(_net
						.getMask()));
				return _ip.getHostAddress() + "/" + _mask.getHostAddress();
			} catch (UnknownHostException e) {
				return "";
			}
		}
		return "";
	}

	public void PrintOut() {
		for (int i = 0; i < _Nets.size(); i++)
			System.out.println("  Net #" + i + ": " + NetByIndex(i));
	}
}