package fw.util;

public class Calc
{
	public static int diferenca(int a, int b)
	{
		return (a > b) ? a - b : b - a;
	}

	public static long diferenca(long a, long b)
	{
		return (a > b) ? a - b : b - a;
	}

	public static float diferenca(float a, float b)
	{
		return (a > b) ? a - b : b - a;
	}

	public static double diferenca(double a, double b)
	{
		return (a > b) ? a - b : b - a;
	}

	public static int distanciaVertexInt(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		int distX = x2 - x1;
		int distY = y1 - y1;
		int distZ = z1 - z1;
		return (int) Math.sqrt((distX * distX) + (distY * distY) + (distZ * distZ));
	}

	public static double distanciaVertexDouble(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		int distX = x2 - x1;
		int distY = y1 - y1;
		int distZ = z1 - z1;
		return   Math.sqrt((distX * distX) + (distY * distY) + (distZ * distZ));
	}

}
