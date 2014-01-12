package l2p.gameserver.geodata;

/**
 * @Author: Diamond
 * @Date: 23/11/2007
 * @Time: 13:11:59
 */
public class Layer
{
	public short height;
	public byte nswe;

	public Layer(short h, byte n)
	{
		height = h;
		nswe = n;
	}
}
