package fw.extensions.geodata;

import java.util.HashMap;
import java.util.logging.Logger;

import fw.extensions.util.GArray;
import fw.extensions.util.Location;
import fw.extensions.util.Rnd;
import fw.extensions.util.Util;
import fw.game.model.L2Character;
import fw.game.model.L2Object;

public class L2Zone
{
	private static Logger _log = Logger.getLogger(L2Zone.class.getName());

	public static enum ZoneType
	{
		Town,
		Castle,
		ClanHall,
		Fortress,
		OlympiadStadia,
		MonsterDerbyTrack,
		CastleDefenderSpawn,
		Fishing,
		Underground,
		Siege,
		water,
		battle_zone,
		damage,
		instant_skill,
		mother_tree,
		peace_zone,
		poison,
		ssq_zone,
		swamp,
		no_escape,
		no_landing,
		no_restart,
		no_spawn,
		siege_residense, // территория замка, ограничена забором
		dummy,
		offshore,
		unblock_actions,
		no_water,
		epic,
		monster_trap,
		no_summon,
		UnderGroundColiseum;
	}

	int _id;
	ZoneType _type;
	private String _name;
	private L2Territory _loc;
	private L2Territory _restartPoints;
	private L2Territory _PKrestartPoints;
	private int _entering_message_no;
	private int _leaving_message_no;
	private boolean _active;
	//L2Skill _skill;
	int _skillProb;
	long _unitTick;
	private long _initialDelay;
	private ZoneTarget _target;
	private int _index;
	private int _taxById;
	private long _restartTime;
	private String _blockedActions;
	public static final String BLOCKED_ACTION_PRIVATE_STORE = "private store";
	public static final String BLOCKED_ACTION_PRIVATE_WORKSHOP = "private workshop";

	private String _event;
	private int _reflectionId;

	//private ListenerEngine<L2Zone> listenerEngine;
	private DamageTask damageTask;
	private final GArray<L2Object> _objects = new GArray<L2Object>(0);
	private final HashMap<L2Character, ZoneSkillTimer> _skillTimers = new HashMap<L2Character, ZoneSkillTimer>(0);

	/**
	 * Ордер в зонах, с ним мы и добавляем/убираем статы.
	 * TODO: сравнить ордер с оффом, пока от фонаря
	 */
	public final static int ZONE_STATS_ORDER = 0x40;

	/**
	 * Сообщение которое шлется при уроне от зоны (не скилла)
	 * К примеру на осадах. Пока это только 686 (You have received $s1 damage from the fire of magic.)
	 */
	private int messageNumber;

	/**
	 * Урон от зоны по хп
	 */
	private int damageOnHP;

	/**
	 * Урон от зоны по мп
	 */
	private int damageOnMP;

	/**
	 * Бонус/штраф к скорости движения
	 */
	private int moveBonus;

	/**
	 * Бонус регенерации хп
	 */
	private int regenBonusHP;

	/**
	 * Бонус регенерации мп
	 */
	private int regenBonusMP;

	/**
	 * Раса на которую применим эффект
	 */
	private String affectRace;

	public L2Zone(int id)
	{
		_id = id;
	}

	public final int getId()
	{
		return _id;
	}

	@Override
	public final String toString()
	{
		return "zone '" + _id + "'";
	}

	public ZoneType getType()
	{
		return _type;
	}

	public void setType(ZoneType type)
	{
		_type = type;
	}

	public void setLoc(L2Territory t)
	{
		_loc = t;
	}

	public void setRestartPoints(L2Territory t)
	{
		_restartPoints = t;
	}

	public void setPKRestartPoints(L2Territory t)
	{
		_PKrestartPoints = t;
	}

	public L2Territory getLoc()
	{
		return _loc;
	}

	public L2Territory getRestartPoints()
	{
		return _restartPoints;
	}

	public L2Territory getPKRestartPoints()
	{
		return _PKrestartPoints;
	}

	public Location getSpawn()
	{
		// При ошибке портанет во флоран
		if(_restartPoints == null)
		{
			_log.warning("L2Zone.getSpawn(): restartPoint not found for " + toString());
			return new Location(17817, 170079, -3530);
		}
		GArray<int[]> coords = _restartPoints.getCoords();
		int[] coord = coords.get(Rnd.get(coords.size()));
		return new Location(coord);
	}

	public GArray<int[]> getSpawns()
	{
		return _restartPoints.getCoords();
	}

	public Location getPKSpawn()
	{
		if(_PKrestartPoints == null)
			return getSpawn();
		GArray<int[]> coords = _PKrestartPoints.getCoords();
		int rnd = Rnd.get(coords.size());
		int[] coord = coords.get(rnd);
		return new Location(coord);
	}

	/**
	 * Проверяет находятся ли даные координаты в зоне.
	 * _loc - стандартная территория для зоны
	 * @param x координата
	 * @param y координата
	 * @return находятся ли координаты в локации
	 */
	public boolean checkIfInZone(int x, int y)
	{
		return _loc != null && _loc.isInside(x, y);
	}

	public boolean checkIfInZone(int x, int y, int z)
	{
		return _loc != null && _loc.isInside(x, y, z);
	}

	public boolean checkIfInZone(L2Object obj)
	{
		return _loc != null && _loc.isInside(obj.getX(), obj.getY(), obj.getZ());
	}

	public final double findDistanceToZone(L2Object obj, boolean includeZAxis)
	{
		return findDistanceToZone(obj.getX(), obj.getY(), obj.getZ(), includeZAxis);
	}

	public final double findDistanceToZone(int x, int y, int z, boolean includeZAxis)
	{
		double closestDistance = 99999999;
		if(_loc == null)
			return closestDistance;
		double distance;
		for(int[] coord : _loc.getCoords())
		{
			distance = Util.calculateDistance(x, y, z, coord[0], coord[1], (coord[2] + coord[3]) / 2, includeZAxis);
			if(distance < closestDistance)
				closestDistance = distance;
		}
		return closestDistance;
	}

	public GArray<int[]> getCoords()
	{
		return _loc.getCoords();
	}

	/**
	 * Обработка входа в территорию
	 * Ojbect всегда добавляется в список вне зависимости от активности территории.
	 * Если зона акивная, то обработается вход в зону
	 * @param object кто входит
	 */
	public void doEnter(L2Object object)
	{
		synchronized (_objects)
		{
			if(!_objects.contains(object))
				_objects.add(object);
			else
				_log.severe("Attempt of double object add to zone " + _name + " id " + _id);
		}

		if(!isActive())
			return;

	}

	/**
	 * Обработка выхода из зоны
	 * Object всегда убирается со списка вне зависимости от зоны
	 * Если зона активная, то обработается выход из зоны
	 * @param object кто выходит
	 */
	public void doLeave(L2Object object, boolean notify)
	{
		synchronized (_objects)
		{
			boolean remove = _objects.remove(object);
			if(!remove)
				_log.severe("Attempt remove object from zone " + _name + " id " + _id + " where it's absent");
		}

		if(!isActive())
			return;
	}

	

	/**
	 * Установка активности зоны
	 * @param value активна ли зона
	 */
	public void setActive(boolean value)
	{
		if(_active == value)
			return;
		
		synchronized (_objects)
		{
			_active = value;

		}

		setDamageTaskActive(value);
	}

	/**
	 * Запускает или останавливает таск урона в зоне.<br>
	 * <b>Таск <font color="red">не</font> запустится если урон по хп и мп равны 0</b>
	 * @param value запустить или остановить таск урона
	 */
	public void setDamageTaskActive(boolean value)
	{
		if(value && (getDamageOnHP() > 0 || getDamageOnMP() > 0))
			new DamageTask().start();
		else
			damageTask = null;
	}

	public boolean isActive()
	{
		return _active;
	}

	public final String getName()
	{
		return _name;
	}

	public final void setName(String name)
	{
		_name = name;
	}

	public final int getEnteringMessageId()
	{
		return _entering_message_no;
	}

	public final void setEnteringMessageId(int id)
	{
		_entering_message_no = id;
	}

	public final int getLeavingMessageId()
	{
		return _leaving_message_no;
	}

	public final void setLeavingMessageId(int id)
	{
		_leaving_message_no = id;
	}

	public final void setIndex(int index)
	{
		_index = index;
	}

	public final int getIndex()
	{
		return _index;
	}

	public final void setTaxById(int id)
	{
		_taxById = id;
	}

	public final Integer getTaxById()
	{
		return _taxById;
	}

	public void setSkill(String skill)
	{
		if(skill == null || skill.equalsIgnoreCase("null"))
			return;
		String[] sk = skill.split(";");
	}

	public void setSkillProb(String chance)
	{
		if(chance == null)
		{
			_skillProb = -1;
			return;
		}
		_skillProb = Integer.parseInt(chance);
	}

	public void setUnitTick(String delay)
	{
		if(delay == null)
		{
			_unitTick = 1000;
			return;
		}

		_unitTick = Integer.parseInt(delay) * 1000L;
	}

	public void setInitialDelay(String delay)
	{
		if(delay == null)
		{
			_initialDelay = 0;
			return;
		}

		_initialDelay = Integer.parseInt(delay) * 1000L;
	}

	private static class ZoneSkillTimer implements Runnable
	{
		private boolean canceled = false;
		private L2Character _target;
		private int _skillProb;
		private long _unitTick;

		public ZoneSkillTimer(L2Character target, int skillProb, long unitTick)
		{
			_target = target;

			_skillProb = skillProb;
			_unitTick = unitTick;
		}

		public void run()
		{

		}

		public void setCanceled(boolean val)
		{
			canceled = val;
		}
	}

	public void setTarget(String target)
	{
		if(target == null)
			return;

		_target = ZoneTarget.valueOf(target);
	}

	public enum ZoneTarget
	{
		pc,
		npc,
		only_pc
	}

	public long getRestartTime()
	{
		return _restartTime;
	}

	public ZoneTarget getZoneTarget()
	{
		return _target;
	}

	public void setRestartTime(long restartTime)
	{
		_restartTime = restartTime;
	}

	public void setBlockedActions(String blockedActions)
	{
		_blockedActions = blockedActions;
	}

	public boolean isActionBlocked(String action)
	{
		return _blockedActions != null && _blockedActions.contains(action);
	}

	/**
	 * Номер системного вообщения которое будет отослано игроку при нанесении урона зоной
	 * @return SystemMessage ID
	 */
	public int getMessageNumber()
	{
		return messageNumber;
	}

	/**
	 * Устанавлевает номер системного сообщение которое будет отослано игроку при уроне зоной
	 * @param messageNumber SystemMessage ID
	 */
	public void setMessageNumber(int messageNumber)
	{
		this.messageNumber = messageNumber;
	}

	/**
	 * Устанавлевает номер системного сообщение которое будет отослано игроку при уроне зоной
	 * @param number SystemMessage ID - Системное сообщение сторой
	 */
	public void setMessageNumber(String number)
	{
		setMessageNumber(number == null ? 0 : Integer.parseInt(number));
	}

	/**
	 * Сколько урона зона нанесет по хп
	 * @return количество урона
	 */
	public int getDamageOnHP()
	{
		return damageOnHP;
	}

	/**
	 * Установка урона по хп зоной
	 * @param damageOnHP сколько зона нанесет урона по хп
	 */
	public void setDamageOnHP(int damageOnHP)
	{
		this.damageOnHP = damageOnHP;
	}

	/**
	 * Установка урона по хп зоной. Парсит строку.
	 * @param damage урон в строке
	 */
	public void setDamageOnHP(String damage)
	{
		setDamageOnHP(damage == null ? 0 : Integer.parseInt(damage));
	}

	/**
	 * Сколько урона зона нанесет по мп
	 * @return количество урона
	 */
	public int getDamageOnMP()
	{
		return damageOnMP;
	}

	/**
	 * Установка урона по мп зоной
	 * @param damageOnMP сколько зона нанесет урона по мп
	 */
	public void setDamageOnMP(int damageOnMP)
	{
		this.damageOnMP = damageOnMP;
	}

	/**
	 * Установка урона по мп зоной. Парсит строку.
	 * @param damage урон в строке
	 */
	public void setDamageOnМP(String damage)
	{
		setDamageOnMP(damage == null ? 0 : Integer.parseInt(damage));
	}

	/**
	 * @return Бонус к скорости движения в зоне
	 */
	public int getMoveBonus()
	{
		return moveBonus;
	}

	/**
	 * Устанавливает бонус на движение в зоне
	 * @param moveBonus бонус
	 */
	public void setMoveBonus(int moveBonus)
	{
		this.moveBonus = moveBonus;
	}

	/**
	 * Устанавливает бонус на движение в зоне
	 * @param moveBonus бонус
	 */
	public void setMoveBonus(String moveBonus)
	{
		setMoveBonus(moveBonus == null ? 0 : Integer.parseInt(moveBonus));
	}

	/**
	 * Возвращает бонус регенерации хп в этой зоне
	 * @return Бонус регенарации хп в этой зоне
	 */
	public int getRegenBonusHP()
	{
		return regenBonusHP;
	}

	/**
	 * Устанавливает бонус регена хп в этой зоне. Парсит строку
	 * @param bonus бонус регена
	 */
	public void setRegenBonusHP(String bonus)
	{
		setRegenBonusHP(bonus == null ? 0 : Integer.parseInt(bonus));
	}

	/**
	 * Устанавливает бонус регенерации хп в этой зоне
	 * @param regenBonusHP бонус регена ХП
	 */
	public void setRegenBonusHP(int regenBonusHP)
	{
		this.regenBonusHP = regenBonusHP;
	}

	/**
	 * Возвращает бонус регенерации мп в этой зоне
	 * @return Бонус регенарации мп в этой зоне
	 */
	public int getRegenBonusMP()
	{
		return regenBonusMP;
	}

	/**
	 * Устанавливает бонус регенерации хп в этой зоне
	 * @param regenBonusMP бонус регена МП
	 */
	public void setRegenBonusMP(int regenBonusMP)
	{
		this.regenBonusMP = regenBonusMP;
	}

	/**
	 * Устанавливает бонус регена мп в этой зоне. Парсит строку
	 * @param bonus бонус регена
	 */
	public void setRegenBonusMP(String bonus)
	{
		setRegenBonusMP(bonus == null ? 0 : Integer.parseInt(bonus));
	}

	/**
	 * Возвращает расу на которую примени эффект. Раса может быть:
	 * <br><b>all</b> - любая раса
	 * <br><b>null</b> - проверка не применяется
	 * ну или любая из энумы Race
	 * @see l2p.gameserver.model.base.Race
	 * @return раса
	 */
	public String getAffectRace()
	{
		return affectRace;
	}

	/**
	 * Устанавливает расу зоне на которую будут применятся эффекты. Раса может быть:
	 * <br><b>all</b> - любая раса
	 * <br><b>null</b> - проверка не применяется
	 * ну или любая из энумы Race
	 * @see l2p.gameserver.model.base.Race
	 * @param affectRace строка с расой
	 */
	public void setAffectRace(String affectRace)
	{
		if(affectRace != null)
			affectRace = affectRace.toLowerCase().replace(" ", "");

		this.affectRace = affectRace;
	}

	/**
	 * Класс используется для нанесения урона всем кто в зоне.
	 */
	private class DamageTask implements Runnable
	{
		public void run()
		{
			if(damageTask != this || !isActive())
				return;

			synchronized (_objects)
			{
				for(L2Object object : _objects)
				{
				

					if(_target == null && !object.isPlayable()) // Если цель не указана, то валим только игроков
						continue;

					L2Character target = (L2Character) object;

					int hp = getDamageOnHP();
					int mp = getDamageOnMP();
					int message = getMessageNumber();

					if(hp > 0)
					{
						
					}

					if(mp > 0)
					{
						
					}
				}
			}
		}

		/**
		 * Запускает таймер нанесения урона зоной
		 */
		public void start()
		{
			damageTask = this;

			if(_initialDelay == 0)
			{
				run();
				return;
			}
		}
	}

	public String getEvent()
	{
		return _event;
	}

	public void setEvent(String event)
	{
		_event = event;
	}

	public void setReflectionId(int val)
	{
		_reflectionId = val;
	}

	public int getReflectionId()
	{
		return _reflectionId;
	}

	public void setParam(String name, String value)
	{
	// TODO: implement
	}
}