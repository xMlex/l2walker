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
package fw.game.model;

import java.util.List;
import java.util.logging.Logger;

import fw.connection.game.clientpackets.L2GameClientPacket;
import fw.extensions.util.Location;

/**
 * Mother class of all character objects of the world (PC, NPC...)<BR>
 * <BR>
 * L2Character :<BR>
 * <BR>
 * <li>L2CastleGuardInstance</li> <li>L2DoorInstance</li> <li>L2NpcInstance</li> <li>L2PlayableInstance</li><BR>
 * <BR>
 * <B><U> Concept of L2CharTemplate</U> :</B><BR>
 * <BR>
 * Each L2Character owns generic and static properties (ex : all Keltir have the same number of HP...). All of those
 * properties are stored in a different template for each type of L2Character. Each template is loaded once in the
 * server cache memory (reduce memory use). When a new instance of L2Character is spawned, server just create a link
 * between the instance and the template. This link is stored in <B>_template</B><BR>
 * <BR>
 * 
 * @version $Revision: 1.5.5 $ $Date: 2009/05/12 19:45:27 $
 * @authors eX1steam, programmos, L2Scoria dev&sword dev
 */
public abstract class L2Character extends L2Object
{
	/** The Constant _log. */
	protected static final Logger _log = Logger.getLogger(L2Character.class.getName());
	
	public static final double HEADINGS_IN_PI = 10430.378350470452724949566316381;
	public static final int INTERACTION_DISTANCE = 200;
	
	protected double _currentCp = 0;
	protected double _currentHp = 1;
	protected double _currentMp = 1;
	
	private int _heading;
	protected String _name;
	protected String _title;

	private int _mAtkSpd;

	private int _max_cp;

	private int _max_hp;

	private int _max_mp;

	private int _cur_mdef;

	private int _cur_patk;

	private int _cur_pdef;

	private int _cur_runspd;

	private L2Object _target;

	private boolean _running;

	private boolean _isTeleporting;
	
	public L2Character(Integer objectId) {
		super(objectId);
	}
	
	public int calcHeading(Location dest)
	{
		if(dest == null)
			return 0;
		if(Math.abs(getX() - dest.x) == 0 && Math.abs(getY() - dest.y) == 0)
			return _heading;
		return calcHeading(dest.x, dest.y);
	}

	public int calcHeading(int x_dest, int y_dest)
	{
		return (int) (Math.atan2(getY() - y_dest, getX() - x_dest) * HEADINGS_IN_PI) + 32768;
	}
	
	public final L2Character getCharTarget()
	{
		L2Object target = getTarget();
		if(target == null || !target.isCharacter())
			return null;
		return (L2Character) target;
	}
	
	public final double getCurrentCp()
	{
		return _currentCp;
	}

	public final double getCurrentCpRatio()
	{
		return getCurrentCp() / getMaxCp();
	}

	public final double getCurrentCpPercents()
	{
		return getCurrentCpRatio() * 100f;
	}

	public final boolean isCurrentCpFull()
	{
		return getCurrentCp() >= getMaxCp();
	}

	public final boolean isCurrentCpZero()
	{
		return getCurrentCp() < 1;
	}

	public final double getCurrentHp()
	{
		return _currentHp;
	}

	public final double getCurrentHpRatio()
	{
		return getCurrentHp() / getMaxHp();
	}

	public final double getCurrentHpPercents()
	{
		return getCurrentHpRatio() * 100f;
	}

	public final boolean isCurrentHpFull()
	{
		return getCurrentHp() >= getMaxHp();
	}

	public final boolean isCurrentHpZero()
	{
		return getCurrentHp() < 1;
	}

	public final double getCurrentMp()
	{
		return _currentMp;
	}

	public final double getCurrentMpRatio()
	{
		return getCurrentMp() / getMaxMp();
	}

	public final double getCurrentMpPercents()
	{
		return getCurrentMpRatio() * 100f;
	}

	public final boolean isCurrentMpFull()
	{
		return getCurrentMp() >= getMaxMp();
	}

	public final boolean isCurrentMpZero()
	{
		return getCurrentMp() < 1;
	}
	
	/**
	 * If <b>boolean toChar is true heading calcs this->target, else target->this.
	 */
	public int getHeadingTo(L2Object target, boolean toChar)
	{
		if(target == null || target == this)
			return -1;

		int dx = target.getX() - getX();
		int dy = target.getY() - getY();
		int heading = (int) (Math.atan2(-dy, -dx) * HEADINGS_IN_PI + 32768);

		heading = toChar ? target.getHeading() - heading : getHeading() - heading;

		if(heading < 0)
			heading = heading + 1 + Integer.MAX_VALUE & 0xFFFF;
		else if(heading > 0xFFFF)
			heading &= 0xFFFF;

		return heading;
	}
	
	public int getMAtkSpd()
	{
		return _mAtkSpd;
	}

	public final int getMaxCp()
	{
		return _max_cp;
	}

	public int getMaxHp()
	{
		return _max_hp;
	}

	public int getMaxMp()
	{
		return _max_mp;
	}

	public int getMDef()
	{
		return _cur_mdef;
	}
	
	@Override
	public String getName()
	{
		return _name;
	}
	
	public int getPAtk()
	{
		return _cur_patk;
	}

	public int getPAtkSpd()
	{
		return _cur_patk;
	}

	public int getPDef()
	{
		return _cur_pdef;
	}

	public int getRunSpeed()
	{
		return _cur_runspd;
	}
	public L2Object getTarget()
	{
		return _target;
	}
	public boolean isDead()
	{
		return _currentHp <= 0;
	}
	public final boolean isRunning()
	{
		return _running;
	}
	
	public final void setCurrentHp(double newHp)
	{
		_currentHp=newHp;
	}
	public final void setCurrentMp(double newMp)
	{
		_currentMp=newMp;
	}
	public final void setCurrentCp(double newCp)
	{
		if(!isPlayer())
			return;
		_currentCp = newCp;
	}
	@Override
	public final int getHeading()
	{
		return _heading;
	}
	@Override
	public void setHeading(int heading)
	{
		_heading = heading;
	}
	public final void setIsTeleporting(boolean value)
	{
		_isTeleporting = value;
	}
	public final void setName(String name)
	{
		_name = name;
	}
	public void setTarget(L2Object object)
	{
		_target=object;
	}
	public void setTitle(String title)
	{
		_title = title;
	}
	//implements
	public int getKarma()
	{
		return 0;
	}
	public int getNpcId()
	{
		return 0;
	}
	public int getTeam()
	{
		return 0;
	}
	public boolean isUndead()
	{
		return false;
	}
	public void sendPacket(L2GameClientPacket... mov)
	{}
	
	public void sitDown()
	{}

	public void standUp()
	{}
	public boolean hasMinions()
	{
		return false;
	}
	public boolean isCursedWeaponEquipped()
	{
		return false;
	}
	public boolean isHero()
	{
		return false;
	}
	public int getClanCrestId()
	{
		return 0;
	}

	public int getClanCrestLargeId()
	{
		return 0;
	}

	public int getAllyCrestId()
	{
		return 0;
	}
	public abstract byte getLevel();
	
}
