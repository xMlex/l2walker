package fw.extensions.util;

import java.util.StringTokenizer;

public class Net
{
	private final int _net;
	private final int _mask;

	public Net(int net, int mask)
	{
		_net = net;
		_mask = mask;
	}

	public Net(String net, int mask)
	{
		_net = AddrToint(net);
		_mask = mask;
	}

	public Net(String net, String mask)
	{
		_net = AddrToint(net);
		_mask = AddrToint(mask);
	}

	public Net(int net, String mask)
	{
		_net = net;
		_mask = AddrToint(mask);
	}

	public Net(String net_mask)
	{
		StringTokenizer st = new StringTokenizer(net_mask.trim(), "/");
		_net = AddrToint(st.nextToken());
		if(st.hasMoreTokens())
			_mask = AddrToint(st.nextToken());
		else
			_mask = 0xFFFFFFFF;
	}

	public boolean isInNet(int _ip)
	{
		return (_ip & _mask) == _net;
	}

	public boolean isInNet(String addr)
	{
		return isInNet(AddrToint(addr));
	}

	public boolean equal(Net _Net)
	{
		return _Net.getNet() == _net && _Net.getMask() == _mask;
	}

	private static int AddrToint(String addr)
	{
		int _ip = 0;
		StringTokenizer st = new StringTokenizer(addr.trim(), ".");

		int _dots = st.countTokens();

		if(_dots == 1)
		{ //BitMask
			_ip = 0xFFFFFFFF;
			try
			{
				int _bitmask = Integer.parseInt(st.nextToken());
				if(_bitmask > 0)
				{
					if(_bitmask < 32)
						_ip = _ip << 32 - _bitmask;
				}
				else
					_ip = 0;
			}
			catch(NumberFormatException e)
			{}
		}
		else
			for(int i = 0; i < _dots; i++)
				try
				{
					_ip += Integer.parseInt(st.nextToken()) << 24 - i * 8;
				}
				catch(NumberFormatException e)
				{}
		return _ip;
	}

	public int getNet()
	{
		return _net;
	}

	public int getMask()
	{
		return _mask;
	}
}
