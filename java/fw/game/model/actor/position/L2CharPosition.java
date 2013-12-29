/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package fw.game.model.actor.position;

import fw.game.model.L2Character;

/**
 * This class permit to pass (x, y, z, heading) position data to method.
 */
public final class L2CharPosition
{
	/** The heading. */
	public final int x, y, z, heading;

	/**
	 * Constructor of L2CharPosition.<BR>
	 * <BR>
	 *
	 * @param pX the p x
	 * @param pY the p y
	 * @param pZ the p z
	 * @param pHeading the heading
	 */
	public L2CharPosition(int pX, int pY, int pZ, int pHeading)
	{
		x = pX;
		y = pY;
		z = pZ;
		heading = pHeading;
	}
	
	/**
	 * Instantiates a new l2 char position.
	 *
	 * @param _actor the _actor
	 */
	public L2CharPosition(L2Character _actor)
	{
		x = _actor.getX();
		y = _actor.getY();
		z = _actor.getZ();
		heading = _actor.getHeading();
	}
	

}
