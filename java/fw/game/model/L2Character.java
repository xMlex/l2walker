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

import java.util.logging.Logger;

import fw.connection.game.clientpackets.L2GameClientPacket;
import fw.extensions.util.Location;

/**
 * Mother class of all character objects of the world (PC, NPC...)<BR>
 * <BR>
 * L2Character :<BR>
 * <BR>
 * <li>L2CastleGuardInstance</li> <li>L2DoorInstance</li> <li>L2NpcInstance</li>
 * <li>L2PlayableInstance</li><BR>
 * <BR>
 * <B><U> Concept of L2CharTemplate</U> :</B><BR>
 * <BR>
 * Each L2Character owns generic and static properties (ex : all Keltir have the
 * same number of HP...). All of those properties are stored in a different
 * template for each type of L2Character. Each template is loaded once in the
 * server cache memory (reduce memory use). When a new instance of L2Character
 * is spawned, server just create a link between the instance and the template.
 * This link is stored in <B>_template</B><BR>
 * <BR>
 * 
 * @version $Revision: 1.5.5 $ $Date: 2009/05/12 19:45:27 $
 * @authors eX1steam, programmos, L2Scoria dev&sword dev
 */
public abstract class L2Character extends L2Object {

	/** The Constant _log. */
	protected static final Logger _log = Logger.getLogger(L2Character.class
			.getName());

	public static final double HEADINGS_IN_PI = 10430.378350470452724949566316381;
	public static final int INTERACTION_DISTANCE = 200;

	protected double _currentCp = 0;
	protected double _currentHp = 1;
	protected double _currentMp = 1, _MovementSpeedMultiplier = 1.2;

	private int _heading;
	protected String _name;
	protected String _title;

	private int _mAtkSpd;
	private int _max_cp;
	private int _max_hp;
	private int _max_mp;
	private int _cur_matk;
	private int _cur_mdef;
	private int _cur_patk;
	private int _cur_pdef;
	private int _cur_runspd=10,_cur_walkspd=10;
	private L2Object _target;

	private boolean _running = false, _infight = false, HasHideout, HasCastle,
			flags, Sweepable = false, Access, _isDead = false,
			_isSummoned = false;
	private boolean _isTeleporting = false, _isMove = false;

	/** Movement data of this L2Character. */
	protected MoveData _move;

	public L2Character(Integer objectId) {
		super(objectId);
	}
	
	public Location applyOffset(Location point, int offset)
	{
		if(offset <= 0)
			return point;

		long dx = point.x - getX();
		long dy = point.y - getY();
		long dz = point.z - getZ();

		double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

		if(distance <= offset)
		{
			point.set(getX(), getY(), getZ());
			return point;
		}

		if(distance >= 1)
		{
			double cut = offset / distance;
			point.x -= (int) (dx * cut + 0.5f);
			point.y -= (int) (dy * cut + 0.5f);
			point.z -= (int) (dz * cut + 0.5f);

			//if(!isFlying() && !isInVehicle() && !isSwimming() && !isVehicle())
			//	point.correctGeoZ();
		}

		return point;
	}

	/**
	 * Update the position of the L2Character during a movement and return True
	 * if the movement is finished.<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * At the beginning of the move action, all properties of the movement are
	 * stored in the MoveData object called <B>_move</B> of the L2Character. The
	 * position of the start point and of the destination permit to estimated in
	 * function of the movement speed the time to achieve the destination.<BR>
	 * <BR>
	 * When the movement is started (ex : by MovetoLocation), this method will
	 * be called each 0.1 sec to estimate and update the L2Character position on
	 * the server. Note, that the current server position can differe from the
	 * current client position even if each movement is straight foward. That's
	 * why, client send regularly a Client->Server ValidatePosition packet to
	 * eventually correct the gap on the server. But, it's always the server
	 * position that is used in range calculation.<BR>
	 * <BR>
	 * At the end of the estimated movement time, the L2Character position is
	 * automatically set to the destination position even if the movement is not
	 * finished.<BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : The current Z position is
	 * obtained FROM THE CLIENT by the Client->Server ValidatePosition Packet.
	 * But x and y positions must be calculated to avoid that players try to
	 * modify their movement speed.</B></FONT><BR>
	 * <BR>
	 * 
	 * @param gameTicks
	 *            Nb of ticks since the server start
	 * @return True if the movement is finished
	 */
	public boolean updatePosition(int gameTicks) {
		// Get movement data
		MoveData m = _move;

		if (m == null)
			return true;

		if (!isVisible()) {
			_move = null;
			return true;
		}

		if (m._moveTimestamp == 0) {
			m._moveTimestamp = m._moveStartTime;
			m._xAccurate = getX();
			m._yAccurate = getY();
		}

		// Check if the position has alreday be calculated
		if (m._moveTimestamp == gameTicks)
			return false;

		int xPrev = getX();
		int yPrev = getY();
		int zPrev = getZ();

		double dx, dy, dz, distFraction;
		// if(Config.COORD_SYNCHRONIZE == 1)
		// the only method that can modify x,y while moving (otherwise _move
		// would/should be set null)
		// {
		// dx = m._xDestination - xPrev;
		// dy = m._yDestination - yPrev;
		// }
		// else
		// otherwise we need saved temporary values to avoid rounding errors
		// {
		dx = m._xDestination - m._xAccurate;
		dy = m._yDestination - m._yAccurate;
		// }
		// Z coordinate will follow geodata or client values
		/*
		 * if(Config.GEODATA > 0 && Config.COORD_SYNCHRONIZE == 2 && !isFlying()
		 * && !isInsideZone(L2Character.ZONE_WATER) && !m.disregardingGeodata &&
		 * GameTimeController.getGameTicks() % 10 == 0 // once a second to
		 * reduce possible cpu load && !(this instanceof L2BoatInstance)) {
		 * short geoHeight = GeoData.getInstance().getSpawnHeight(xPrev, yPrev,
		 * zPrev - 30, zPrev + 30, getObjectId()); dz = m._zDestination -
		 * geoHeight; // quite a big difference, compare to validatePosition
		 * packet if(this instanceof L2PcInstance && Math.abs(((L2PcInstance)
		 * this).getClientZ() - geoHeight) > 200 && Math.abs(((L2PcInstance)
		 * this).getClientZ() - geoHeight) < 1500) { dz = m._zDestination -
		 * zPrev; // allow diff } else if(isInCombat() && Math.abs(dz) > 200 &&
		 * dx * dx + dy * dy < 40000) // allow mob to climb up to pcinstance {
		 * dz = m._zDestination - zPrev; // climbing } else { zPrev = geoHeight;
		 * } } else {
		 */
		dz = m._zDestination - zPrev;
		// }

		float speed;
		speed = getMoveSpeed();

		double distPassed = speed * (gameTicks - m._moveTimestamp)
				/ GameTimeController.TICKS_PER_SECOND;
		if (dx * dx + dy * dy < 10000 && dz * dz > 2500) // close enough, allows
															// error between
															// client and server
															// geodata if it
															// cannot be avoided
		{
			distFraction = distPassed / Math.sqrt(dx * dx + dy * dy);
		} else {
			distFraction = distPassed / Math.sqrt(dx * dx + dy * dy + dz * dz);
		}

		// if (Config.DEVELOPER) _log.warning("Move Ticks:" + (gameTicks -
		// m._moveTimestamp) + ", distPassed:" + distPassed + ", distFraction:"
		// + distFraction);

		if (distFraction > 1) // already there
		{
			// Set the position of the L2Character to the destination
			super.setXYZ(m._xDestination, m._yDestination, m._zDestination);
		} else {
			m._xAccurate += dx * distFraction;
			m._yAccurate += dy * distFraction;

			// Set the position of the L2Character to estimated after parcial
			// move
			super.setXYZ((int) m._xAccurate, (int) m._yAccurate, zPrev
					+ (int) (dz * distFraction + 0.5));
		}

		// Set the timer of last position update to now
		m._moveTimestamp = gameTicks;

		return distFraction > 1;
	}

	// called from AIAccessor only
	public void moveToLocation(int x, int y, int z) {
		moveToLocation(x,y,z,20);
	}
	/**
	 * Calculate movement data for a move to location action and add the
	 * L2Character to movingObjects of GameTimeController (only called by AI
	 * Accessor).<BR>
	 * <BR>
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * At the beginning of the move action, all properties of the movement are
	 * stored in the MoveData object called <B>_move</B> of the L2Character. The
	 * position of the start point and of the destination permit to estimated in
	 * function of the movement speed the time to achieve the destination.<BR>
	 * <BR>
	 * All L2Character in movement are identified in <B>movingObjects</B> of
	 * GameTimeController that will call the updatePosition method of those
	 * L2Character each 0.1s.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Get current position of the L2Character</li> <li>Calculate distance
	 * (dx,dy) between current position and destination including offset</li>
	 * <li>Create and Init a MoveData object</li> <li>Set the L2Character _move
	 * object to MoveData object</li> <li>Add the L2Character to movingObjects
	 * of the GameTimeController</li> <li>Create a task to notify the AI that
	 * L2Character arrives at a check point of the movement</li><BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T send
	 * Server->Client packet MoveToPawn/CharMoveToLocation </B></FONT><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>AI : onIntentionMoveTo(L2CharPosition), onIntentionPickUp(L2Object),
	 * onIntentionInteract(L2Object)</li> <li>
	 * FollowTask</li><BR>
	 * <BR>
	 * 
	 * @param x
	 *            The X position of the destination
	 * @param y
	 *            The Y position of the destination
	 * @param z
	 *            The Y position of the destination
	 * @param offset
	 *            The size of the interaction area of the L2Character targeted
	 */
	public void moveToLocation(int x, int y, int z, int offset) {
		/*
		 * //when start to move again, it has to stop sitdown task if(this
		 * instanceof L2PcInstance)
		 * ((L2PcInstance)this).setPosticipateSit(false);
		 * 
		 * // Fix archer bug with movment/hittask if (this instanceof
		 * L2PcInstance && this.isAttackingNow()) { L2ItemInstance rhand =
		 * ((L2PcInstance)
		 * this).getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND); if
		 * ((rhand != null && rhand.getItemType() == L2WeaponType.BOW)) return;
		 * }
		 */
		if (!isLive())
			return;

		// Get the Move Speed of the L2Charcater
		float speed = getMoveSpeed();

		if (speed <= 0 || isMovementDisabled())
			return;

		// Get current position of the L2Character
		final int curX = super.getX();
		final int curY = super.getY();
		final int curZ = super.getZ();

		// Calculate distance (dx,dy) between current position and destination
		//
		double dx = x - curX;
		double dy = y - curY;
		double dz = z - curZ;
		double distance = Math.sqrt(dx * dx + dy * dy);

		/*
		 * if(Config.GEODATA > 0 && isInsideZone(ZONE_WATER) && distance > 700)
		 * { double divider = 700 / distance; x = curX + (int) (divider * dx); y
		 * = curY + (int) (divider * dy); z = curZ + (int) (divider * dz); dx =
		 * x - curX; dy = y - curY; dz = z - curZ; distance = Math.sqrt(dx * dx
		 * + dy * dy); }
		 */

		/*
		 * if(Config.DEBUG) { _log.fine("distance to target:" + distance); }
		 */

		// Define movement angles needed
		// ^
		// | X (x,y)
		// | /
		// | /distance
		// | /
		// |/ angle
		// X ---------->
		// (curx,cury)

		double cos;
		double sin;

		// Check if a movement offset is defined or no distance to go through
		if (offset > 0 || distance < 1) {
			// approximation for moving closer when z coordinates are different
			//
			offset -= Math.abs(dz);

			if (offset < 5) {
				offset = 5;
			}

			// If no distance to go through, the movement is canceled
			if (distance < 1 || distance - offset <= 0) {
				sin = 0;
				cos = 1;
				distance = 0;
				x = curX;
				y = curY;

				/*
				 * if(Config.DEBUG) {
				 * _log.fine("already in range, no movement needed."); }
				 */

				// Notify the AI that the L2Character is arrived at destination
				// getAI().notifyEvent(CtrlEvent.EVT_ARRIVED, null);

				return;
			}
			// Calculate movement angles needed
			sin = dy / distance;
			cos = dx / distance;

			distance -= offset - 5; // due to rounding error, we have to move a
									// bit closer to be in range

			// Calculate the new destination with offset included
			x = curX + (int) (distance * cos);
			y = curY + (int) (distance * sin);

		} else {
			// Calculate movement angles needed
			sin = dy / distance;
			cos = dx / distance;
		}

		// Create and Init a MoveData object
		MoveData m = new MoveData();

		// GEODATA MOVEMENT CHECKS AND PATHFINDING

		m.onGeodataPathIndex = -1; // Initialize not on geodata path
		m.disregardingGeodata = false;

		// Caclulate the Nb of ticks between the current position and the
		// destination
		// One tick added for rounding reasons
		// int ticksToMove = 1 + (int) (GameTimeController.TICKS_PER_SECOND *
		// distance / speed);

		// Calculate and set the heading of the L2Character
		setHeading((int) (Math.atan2(-sin, -cos) * 10430.37835) + 32768);

		/*
		 * if(Config.DEBUG) { _log.fine("dist:" + distance + "speed:" + speed +
		 * " ttt:" + ticksToMove + " heading:" + getHeading()); }
		 */

		m._xDestination = x;
		m._yDestination = y;
		m._zDestination = z; // this is what was requested from client
		m._heading = 0;

		m._moveStartTime = GameTimeController.getGameTicks();

		/*
		 * if(Config.DEBUG) { _log.fine("time to target:" + ticksToMove); }
		 */

		// Set the L2Character _move object to MoveData object
		_move = m;

		// Add the L2Character to movingObjects of the GameTimeController
		// The GameTimeController manage objects movement
		GameTimeController.getInstance().registerMovingObject(this);

		// Create a task to notify the AI that L2Character arrives at a check
		// point of the movement
		/*
		 * if(ticksToMove * GameTimeController.MILLIS_IN_TICK > 3000) {
		 * ThreadPoolManager.getInstance().scheduleAi(new
		 * NotifyAITask(CtrlEvent.EVT_ARRIVED_REVALIDATE), 2000); }
		 */

		// the CtrlEvent.EVT_ARRIVED will be sent when the character will
		// actually arrive
		// to destination by GameTimeController

		m = null;
	}

	private boolean isMovementDisabled() {
		return false;
	}

	/**
	 * This class group all mouvement data.<BR>
	 * <BR>
	 * <B><U> Data</U> :</B><BR>
	 * <BR>
	 * <li>_moveTimestamp : Last time position update</li> <li>_xDestination,
	 * _yDestination, _zDestination : Position of the destination</li> <li>
	 * _xMoveFrom, _yMoveFrom, _zMoveFrom : Position of the origin</li> <li>
	 * _moveStartTime : Start time of the movement</li> <li>_ticksToMove : Nb of
	 * ticks between the start and the destination</li> <li>
	 * _xSpeedTicks, _ySpeedTicks : Speed in unit/ticks</li><BR>
	 * <BR>
	 */
	public static class MoveData {
		// when we retrieve x/y/z we use GameTimeControl.getGameTicks()
		// if we are moving, but move timestamp==gameticks, we don't need
		// to recalculate position
		/** The _move start time. */
		public int _moveStartTime;

		/** The _move timestamp. */
		public int _moveTimestamp;

		/** The _x destination. */
		public int _xDestination;

		/** The _y destination. */
		public int _yDestination;

		/** The _z destination. */
		public int _zDestination;

		/** The _x accurate. */
		public double _xAccurate;

		/** The _y accurate. */
		public double _yAccurate;

		/** The _z accurate. */
		public double _zAccurate;

		/** The _heading. */
		public int _heading;

		/** The disregarding geodata. */
		public boolean disregardingGeodata;

		/** The on geodata path index. */
		public int onGeodataPathIndex;

		/** The geo path. */
		// public Node[] geoPath;

		/** The geo path accurate tx. */
		public int geoPathAccurateTx;

		/** The geo path accurate ty. */
		public int geoPathAccurateTy;

		/** The geo path gtx. */
		public int geoPathGtx;

		/** The geo path gty. */
		public int geoPathGty;
	}

	public int calcHeading(Location dest) {
		if (dest == null)
			return 0;
		if (Math.abs(getX() - dest.x) == 0 && Math.abs(getY() - dest.y) == 0)
			return _heading;
		return calcHeading(dest.x, dest.y);
	}

	public int calcHeading(int x_dest, int y_dest) {
		return (int) (Math.atan2(getY() - y_dest, getX() - x_dest) * HEADINGS_IN_PI) + 32768;
	}

	public final L2Character getCharTarget() {
		L2Object target = getTarget();
		if (target == null || !target.isCharacter())
			return null;
		return (L2Character) target;
	}

	public final double getCurrentCp() {
		return _currentCp;
	}

	public final double getCurrentCpRatio() {
		return getCurrentCp() / getMaxCp();
	}

	public final double getCurrentCpPercents() {
		return getCurrentCpRatio() * 100f;
	}

	public final boolean isCurrentCpFull() {
		return getCurrentCp() >= getMaxCp();
	}

	public final boolean isCurrentCpZero() {
		return getCurrentCp() < 1;
	}

	public final double getCurrentHp() {
		return _currentHp;
	}

	public final double getCurrentHpRatio() {
		return getCurrentHp() / getMaxHp();
	}

	public final double getCurrentHpPercents() {
		return getCurrentHpRatio() * 100f;
	}

	public final boolean isCurrentHpFull() {
		return getCurrentHp() >= getMaxHp();
	}

	public final boolean isCurrentHpZero() {
		return getCurrentHp() < 1;
	}

	public final double getCurrentMp() {
		return _currentMp;
	}

	public final double getCurrentMpRatio() {
		return getCurrentMp() / getMaxMp();
	}

	public final double getCurrentMpPercents() {
		return getCurrentMpRatio() * 100f;
	}

	public final boolean isCurrentMpFull() {
		return getCurrentMp() >= getMaxMp();
	}

	public final boolean isCurrentMpZero() {
		return getCurrentMp() < 1;
	}

	/**
	 * If <b>boolean toChar is true heading calcs this->target, else
	 * target->this.
	 */
	public int getHeadingTo(L2Object target, boolean toChar) {
		if (target == null || target == this)
			return -1;

		int dx = target.getX() - getX();
		int dy = target.getY() - getY();
		int heading = (int) (Math.atan2(-dy, -dx) * HEADINGS_IN_PI + 32768);

		heading = toChar ? target.getHeading() - heading : getHeading()
				- heading;

		if (heading < 0)
			heading = heading + 1 + Integer.MAX_VALUE & 0xFFFF;
		else if (heading > 0xFFFF)
			heading &= 0xFFFF;

		return heading;
	}

	public int getMAtkSpd() {
		return _mAtkSpd;
	}

	public final int getMaxCp() {
		return _max_cp;
	}

	public int getMaxHp() {
		return _max_hp;
	}

	public int getMaxMp() {
		return _max_mp;
	}

	public int getMDef() {
		return _cur_mdef;
	}

	@Override
	public String getName() {
		return _name;
	}

	public int getPAtk() {
		return _cur_patk;
	}

	public int getPAtkSpd() {
		return _cur_patk;
	}

	public int getPDef() {
		return _cur_pdef;
	}

	public int getRunSpeed() {
		if (_cur_runspd > 0)
			return _cur_runspd;
		else
			return 50;
	}

	public L2Object getTarget() {
		return _target;
	}

	public boolean isDead() {
		return ((_currentHp <= 0) || _isDead);
	}

	public final void setCurrentHp(double newHp) {
		_currentHp = newHp;
	}

	public final void setCurrentMp(double newMp) {
		_currentMp = newMp;
	}

	public final void setCurrentCp(double newCp) {
		if (!isPlayer())
			return;
		_currentCp = newCp;
	}

	@Override
	public final int getHeading() {
		return _heading;
	}

	@Override
	public void setHeading(int heading) {
		_heading = heading;
	}

	public final void setIsTeleporting(boolean value) {
		setTeleporting(value);
	}

	public final void setName(String name) {
		_name = name;
	}

	public void setTarget(L2Object object) {
		_target = object;
	}

	public void setTitle(String title) {
		_title = title;
	}

	// implements
	public int getKarma() {
		return 0;
	}

	public int getNpcId() {
		return 0;
	}

	public int getTeam() {
		return 0;
	}

	public boolean isUndead() {
		return false;
	}

	public void sendPacket(L2GameClientPacket... mov) {
	}

	public void sitDown() {
	}

	public void standUp() {
	}

	public boolean hasMinions() {
		return false;
	}

	public boolean isCursedWeaponEquipped() {
		return false;
	}

	public boolean isHero() {
		return false;
	}

	public int getClanCrestId() {
		return 0;
	}

	public int getClanCrestLargeId() {
		return 0;
	}

	public int getAllyCrestId() {
		return 0;
	}

	public abstract byte getLevel();

	public void setmAtkSpd(int _mAtkSpd) {
		this._mAtkSpd = _mAtkSpd;
	}

	public void setMax_cp(int _max_cp) {
		this._max_cp = _max_cp;
	}

	public void setMax_hp(int _max_hp) {
		this._max_hp = _max_hp;
	}

	public void setMax_mp(int _max_mp) {
		this._max_mp = _max_mp;
	}

	public void setMdef(int _cur_mdef) {
		this._cur_mdef = _cur_mdef;
	}

	public void setPatk(int _cur_patk) {
		this._cur_patk = _cur_patk;
	}

	public void setPdef(int _cur_pdef) {
		this._cur_pdef = _cur_pdef;
	}

	public void setRunspd(int _cur_runspd) {
		this._cur_runspd = _cur_runspd;
	}

	public int getMatk() {
		return _cur_matk;
	}

	public void setMatk(int _cur_matk) {
		this._cur_matk = _cur_matk;
	}

	public double getMovementSpeedMultiplier() {
		return _MovementSpeedMultiplier;
	}

	public void setMovementSpeedMultiplier(double _spd) {
		_MovementSpeedMultiplier = _spd;
	}

	public boolean isHasCastle() {
		return HasCastle;
	}

	public void setHasCastle(boolean hasCastle) {
		HasCastle = hasCastle;
	}

	public boolean isFlags() {
		return flags;
	}

	public void setFlags(boolean flags) {
		this.flags = flags;
	}

	public boolean isSweepable() {
		return Sweepable;
	}

	public void setSweepable(boolean sweepable) {
		Sweepable = sweepable;
	}

	public boolean isHasHideout() {
		return HasHideout;
	}

	public void setHasHideout(boolean hasHideout) {
		HasHideout = hasHideout;
	}

	public boolean isAccess() {
		return Access;
	}

	public void setAccess(boolean access) {
		Access = access;
	}

	public boolean isTeleporting() {
		return _isTeleporting;
	}

	public void setTeleporting(boolean _isTeleporting) {
		this._isTeleporting = _isTeleporting;
	}

	public boolean isMove() {
		return _isMove;
	}

	public void setMove(boolean _isMove) {
		// _log.info("Move: "+_isMove);
		this._isMove = _isMove;
	}

	public float getMoveSpeed() {
		if (isRunning())
			return _cur_runspd;
		else
			return _cur_walkspd;
	}

	public void setRunning(boolean running) {
		this._running = running;
	}

	public boolean isRunning() {
		return this._running;
	}

	public void setInFight(boolean b) {
		this._infight = b;
	}

	public boolean isInFight() {
		return this._infight;
	}

	public void setAlikeDead(boolean isDead) {
		this._isDead = isDead;
	}

	public void setSummoned(boolean b) {
		this._isSummoned = b;
	}

	public boolean isSummoned() {
		return this._isSummoned;
	}

	public int getWalkspd() {
		return _cur_walkspd;
	}

	public void setWalkspd(int walkspd) {
		this._cur_walkspd = walkspd;
	}

}
