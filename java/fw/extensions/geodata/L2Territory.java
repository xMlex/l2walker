package fw.extensions.geodata;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.logging.Logger;

import fw.extensions.geodata.L2Zone.ZoneType;
import fw.extensions.util.GArray;
import fw.extensions.util.Location;
import fw.extensions.util.Rnd;
import fw.game.model.L2Object;

/*import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.instancemanager.ZoneManager;
import l2p.gameserver.model.L2Zone.ZoneType;
import l2p.util.GArray;
import l2p.util.Location;
import l2p.util.Rnd;*/


public class L2Territory
{
	private static Logger _log = Logger.getLogger(L2Territory.class.getName());

	static class Point
	{
		protected int x, y, zmin, zmax;

		Point(int _x, int _y, int _zmin, int _zmax)
		{
			x = _x;
			y = _y;
			zmin = _zmin;
			zmax = _zmax;
		}
	}

	private L2Zone _zone;
	private Polygon poly;
	private Point[] _points;
	protected int _x_min;
	protected int _x_max;
	protected int _y_min;
	protected int _y_max;
	protected int _z_min;
	protected int _z_max;

	private int _id;

	public L2Territory(int id)
	{
		poly = new Polygon();
		_points = new Point[0];
		_x_min = 999999;
		_x_max = -999999;
		_y_min = 999999;
		_y_max = -999999;
		_z_min = 999999;
		_z_max = -999999;
		_id = id;
	}

	public void add(Location loc)
	{
		add(loc.x, loc.y, loc.z, loc.h);
	}

	public void add(int x, int y, int zmin, int zmax)
	{
		if(zmax == -1)
		{
			zmin = zmin - 50;
			zmax = zmin + 100;
		}
		Point[] newPoints = new Point[_points.length + 1];
		System.arraycopy(_points, 0, newPoints, 0, _points.length);
		newPoints[_points.length] = new Point(x, y, zmin, zmax);
		_points = newPoints;

		poly.addPoint(x, y);

		if(x < _x_min)
			_x_min = x;
		if(y < _y_min)
			_y_min = y;
		if(x > _x_max)
			_x_max = x;
		if(y > _y_max)
			_y_max = y;
		if(zmin < _z_min)
			_z_min = zmin;
		if(zmax > _z_max)
			_z_max = zmax;
	}

	/**
	 * Проверяет территорию на самопересечение.
	 */
	public void validate()
	{
		// треугольник не может быть самопересекающимся
		if(_points.length > 3)
			// внешний цикл - перебираем все грани многоугольника
			for(int i = 1; i < _points.length; i++)
			{
				int ii = i + 1 < _points.length ? i + 1 : 0; // вторая точка первой линии
				// внутренний цикл - перебираем все грани многоугольниках кроме той, что во внешнем цикле и соседних 
				for(int n = i; n < _points.length; n++)
					if(Math.abs(n - i) > 1)
					{
						int nn = n + 1 < _points.length ? n + 1 : 0; // вторая точка второй линии
						if(Line2D.linesIntersect(_points[i].x, _points[i].y, _points[ii].x, _points[ii].y, _points[n].x, _points[n].y, _points[nn].x, _points[nn].y))
							_log.warning(this + " is self-intersecting in lines " + i + "-" + ii + " and " + n + "-" + nn);
					}
			}
	}

	public void print()
	{
		for(Point p : _points)
			System.out.println("(" + p.x + "," + p.y + ")");
	}

	public boolean isInside(int x, int y)
	{
		return poly.contains(x, y);
	}

	public boolean isInside(int x, int y, int z)
	{
		return z >= _z_min && z <= _z_max && poly.contains(x, y);
	}

	public boolean isInside(Location loc)
	{
		return isInside(loc.x, loc.y, loc.z);
	}

	public boolean isInside(L2Object obj)
	{
		return isInside(obj.getLoc());
	}

	public int[] getRandomPoint()
	{
		int i;
		int[] p = new int[3];

		mainloop: for(i = 0; i < 100; i++)
		{
			p[0] = Rnd.get(_x_min, _x_max);
			p[1] = Rnd.get(_y_min, _y_max);
			p[2] = _z_min + (_z_max - _z_min) / 2;

			// Для отлова проблемных территорий, вызывающих сильную нагрузку
			if(i == 40)
				_log.warning("Heavy territory: " + this + ", need manual correction");

			if(poly.contains(p[0], p[1]))
			{
				// Не спаунить в зоны, запрещенные для спауна
				if(ZoneManager.getInstance().checkIfInZone(ZoneType.no_spawn, p[0], p[1]))
					continue;

				// Не спаунить в колонны, стены и прочее.
				int tempz = GeoEngine.getHeight(p[0], p[1], p[2], 0);
				if(_z_min != _z_max)
				{
					if(tempz < _z_min || tempz > _z_max || _z_min > _z_max)
						continue;
				}
				else if(tempz < _z_min - 200 || tempz > _z_min + 200)
					continue;

				p[2] = tempz;

				int geoX = p[0] - L2World.MAP_MIN_X >> 4;
				int geoY = p[1] - L2World.MAP_MIN_Y >> 4;

				// Если местность подозрительная - пропускаем
				for(int x = geoX - 1; x <= geoX + 1; x++)
					for(int y = geoY - 1; y <= geoY + 1; y++)
						if(GeoEngine.NgetNSWE(x, y, p[2], 0) != GeoEngine.NSWE_ALL)
							continue mainloop;

				return p;
			}
		}
		_log.warning("Can't make point for " + this);
		return p;
	}

	public void doEnter(L2Object object)
	{
		if(_zone != null)
			if(object.isPlayable())
				_zone.doEnter(object);
			else if(_zone.getZoneTarget() == L2Zone.ZoneTarget.npc && object.isNpc())
				_zone.doEnter(object);
	}

	public void doLeave(L2Object object, boolean notify)
	{
		if(_zone != null)
			if(object.isPlayable())
				_zone.doLeave(object, notify);
			else if( _zone.getZoneTarget() == L2Zone.ZoneTarget.npc && object.isNpc())
				_zone.doLeave(object, notify);
	}

	public final int getId()
	{
		return _id;
	}

	@Override
	public final String toString()
	{
		return "territory '" + _id + "'";
	}

	public int getZmin()
	{
		return _z_min;
	}

	public int getZmax()
	{
		return _z_max;
	}

	public int getXmax()
	{
		return _x_max;
	}

	public int getXmin()
	{
		return _x_min;
	}

	public int getYmax()
	{
		return _y_max;
	}

	public int getYmin()
	{
		return _y_min;
	}

	public void setZone(L2Zone zone)
	{
		_zone = zone;
	}

	public L2Zone getZone()
	{
		return _zone;
	}

	public GArray<int[]> getCoords()
	{
		GArray<int[]> result = new GArray<int[]>();
		for(Point point : _points)
			result.add(new int[] { point.x, point.y, point.zmin, point.zmax });
		return result;
	}

	public Location getCenter()
	{
		return new Location(_x_min + (_x_max - _x_min) / 2, _y_min + (_y_max - _y_min) / 2, _z_min + (_z_max - _z_min) / 2);
	}

	/**
	 * Проверяет валидность территории.
	 * Если у нее нет зоны, или она является спавном - мы ее в L2World не добавляем.
	 * @return нужно ли добавить территорию в L2World
	 */
	public boolean isWorldTerritory()
	{
		return getZone() != null && getZone().getLoc() == this;
	}
}