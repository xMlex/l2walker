package fw.extensions.util;

import fw.clientbase.model.L2Object;
//import fw.clientbase.model.L2World;

import fw.extensions.geodata.GeoEngine;
import fw.extensions.geodata.L2World;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Location implements Serializable
{
	public int x, y, z, h, id = 0;

	public Location()
	{
		x = 0;
		y = 0;
		z = 0;
		h = 0;
	}

	/**
	 * Позиция (x, y, z, heading, npcId)
	 */
	public Location(int locX, int locY, int locZ, int heading, int npcId)
	{
		x = locX;
		y = locY;
		z = locZ;
		h = heading;
		id = npcId;
	}

	/**
	 * Позиция (x, y, z, heading)
	 */
	public Location(int locX, int locY, int locZ, int heading)
	{
		x = locX;
		y = locY;
		z = locZ;
		h = heading;
	}

	/**
	 * Позиция (x, y, z)
	 */
	public Location(int locX, int locY, int locZ)
	{
		this(locX, locY, locZ, 0);
	}

	public Location(L2Object obj)
	{
		this(obj.getX(), obj.getY(), obj.getZ(), obj.getHeading());
	}

	public Location(int[] point)
	{
		x = point[0];
		y = point[1];
		z = point[2];
		try
		{
			h = point[3];
		}
		catch(Exception e)
		{
			h = 0;
		}
	}

	/**
	 * Парсит Location из строки, где коордтнаты разделены пробелами или запятыми
	 */
	public Location(String s) throws IllegalArgumentException
	{
		String[] xyzh = s.replaceAll(",", " ").replaceAll(";", " ").replaceAll("  ", " ").trim().split(" ");
		if(xyzh.length < 3)
			throw new IllegalArgumentException("Can't parse location from string: " + s);
		x = Integer.parseInt(xyzh[0]);
		y = Integer.parseInt(xyzh[1]);
		z = Integer.parseInt(xyzh[2]);
		h = xyzh.length < 4 ? 0 : Integer.parseInt(xyzh[3]);
	}

	public boolean equals(Location loc)
	{
		return loc.x == x && loc.y == y && loc.z == z;
	}

	public boolean equals(int _x, int _y, int _z)
	{
		return _x == x && _y == y && _z == z;
	}

	public boolean equals(int _x, int _y, int _z, int _h)
	{
		return _x == x && _y == y && _z == z && h == _h;
	}

	public Location changeZ(int zDiff)
	{
		z += zDiff;
		return this;
	}

	public Location correctGeoZ()
	{
		z = GeoEngine.getHeight(x, y, z, 0);
		return this;
	}

	public Location correctGeoZ(int refIndex)
	{
		z = GeoEngine.getHeight(x, y, z, refIndex);
		return this;
	}

	public Location setX(int _x)
	{
		x = _x;
		return this;
	}

	public Location setY(int _y)
	{
		y = _y;
		return this;
	}

	public Location setZ(int _z)
	{
		z = _z;
		return this;
	}

	public Location setH(int _h)
	{
		h = _h;
		return this;
	}

	public Location setId(int _id)
	{
		id = _id;
		return this;
	}

	public void set(int _x, int _y, int _z)
	{
		x = _x;
		y = _y;
		z = _z;
	}

	public void set(int _x, int _y, int _z, int _h)
	{
		x = _x;
		y = _y;
		z = _z;
		h = _h;
	}

	public void set(Location loc)
	{
		x = loc.x;
		y = loc.y;
		z = loc.z;
		h = loc.h;
	}

	public Location rnd(int min, int max, boolean change)
	{
		Location loc = Rnd.coordsRandomize(this, min, max);
		//loc = GeoEngine.moveCheck(x, y, z, loc.x, loc.y, 0);
		if(change)
		{
			x = loc.x;
			y = loc.y;
			z = loc.z;
			return this;
		}
		return loc;
	}

	public static Location getAroundPosition(L2Object obj, L2Object obj2, int radius_min, int radius_max, int max_geo_checks)
	{
		Location pos = new Location(obj);
		if(radius_min < 0)
			radius_min = 0;
		if(radius_max < 0)
			radius_max = 0;

		float col_radius = obj.getColRadius() + obj2.getColRadius();
		int randomRadius, randomAngle, x, y, z;
		int min_angle = 0;
		int max_angle = 360;
		if(!obj.equals(obj2))
		{
			double perfect_angle = Util.calculateAngleFrom(obj, obj2);
			min_angle = (int) perfect_angle - 225;
			min_angle = (int) perfect_angle + 135;
		}

		while(true)
		{
			randomRadius = Rnd.get(radius_min, radius_max);
			randomAngle = Rnd.get(min_angle, max_angle);
			x = pos.x + (int) ((col_radius + randomRadius) * Math.cos(randomAngle));
			y = pos.y + (int) ((col_radius + randomRadius) * Math.sin(randomAngle));
			z = pos.z;
			if(max_geo_checks <= 0)
				break;
			z = GeoEngine.getHeight(x, y, z, obj.getReflection());
			if(Math.abs(pos.z - z) < 256 && GeoEngine.getNSWE(x, y, z, obj.getReflection()) == 15)
				break;
			max_geo_checks--;
		}

		pos.x = x;
		pos.y = y;
		pos.z = z;
		return pos;
	}

	public Location world2geo()
	{
		x = x - L2World.MAP_MIN_X >> 4;
		y = y - L2World.MAP_MIN_Y >> 4;
		return this;
	}

	public Location geo2world()
	{
		// размер одного блока 16*16 точек, +8*+8 это его средина
		x = (x << 4) + L2World.MAP_MIN_X + 8;
		y = (y << 4) + L2World.MAP_MIN_Y + 8;
		return this;
	}

	public double distance(Location loc)
	{
		return distance(loc.x, loc.y);
	}

	public double distance(int _x, int _y)
	{
		long dx = x - _x;
		long dy = y - _y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public double distance3D(Location loc)
	{
		return distance3D(loc.x, loc.y, loc.z);
	}

	public double distance3D(int _x, int _y, int _z)
	{
		long dx = x - _x;
		long dy = y - _y;
		long dz = z - _z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	@Override
	public Location clone()
	{
		return new Location(x, y, z, h, id);
	}

	@Override
	public final String toString()
	{
		return x + "," + y + "," + z + "," + h;
	}

	public boolean isNull()
	{
		return x == 0 || y == 0 || z == 0;
	}

	public final String toXYZString()
	{
		return x + "," + y + "," + z;
	}
}