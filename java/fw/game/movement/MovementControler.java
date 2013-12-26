package fw.game.movement;


import fw.game.L2Char;

import java.util.Iterator;

import javolution.util.FastMap;

public class MovementControler extends Thread
{
	FastMap<Integer, ObjMovement> mapCharsInMovement = new FastMap<Integer, ObjMovement>();
	FastMap<Integer, ObjMovement> mapCharsInMoveToPawn = new FastMap<Integer, ObjMovement>();

	public static final int ticksPerSecond = 10;
	public static final int millisBetweenTicks = 1000 / ticksPerSecond;
	private static MovementControler instance = null;

	public MovementControler()
	{
		instance = this;
	}

	public static MovementControler getInstance()
	{
		return instance;
	}

	public void clearAll()
	{
		mapCharsInMovement.clear();
	}

	@Override
	public void run()
	{

		long timeIni = 0, timeFim = 0;
		while (true)
		{
			timeIni = System.currentTimeMillis();
			try
			{
				moveObjects();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			timeFim = System.currentTimeMillis();

			if (timeFim - timeIni >= millisBetweenTicks)
			{
				continue;
			} else
			{
				try
				{
					sleep(millisBetweenTicks - (timeFim - timeIni));
				}
				catch (InterruptedException e)
				{
					// nada
				}
			}
		}
	}

	@SuppressWarnings("unused")
	public final void putMovement(final ObjMovement charMovement)
	{
		if (mapCharsInMovement.containsKey(charMovement.l2CharInfo.objId))
		{
			ObjMovement objMovementLocal = mapCharsInMovement.get(charMovement.l2CharInfo.objId);

			L2Char l2Char = objMovementLocal.l2CharInfo;
			l2Char.movementObject = null;

			if (l2Char.movementToPawn != null)
			{
				l2Char.movementToPawn.removeMoveToPawn(l2Char.objId);
				l2Char.movementToPawn = null;
			}

			mapCharsInMovement.remove(charMovement.l2CharInfo.objId);
		}

		charMovement.l2CharInfo.movementObject = charMovement;
		charMovement.x = charMovement.iX;
		charMovement.y = charMovement.iY;
		charMovement.z = charMovement.iZ;
		charMovement.l2CharInfo.x = charMovement.iX;
		charMovement.l2CharInfo.y = charMovement.iY;
		charMovement.l2CharInfo.z = charMovement.iZ;

		charMovement.distanciaX = charMovement.dX - charMovement.iX;
		charMovement.distanciaY = charMovement.dY - charMovement.iY;
		charMovement.distanciaZ = charMovement.dZ - charMovement.iZ;
		/* OBS: "z" nao e calculado na distancia pois o jogo seta automaticamente */
		charMovement.distanciaZ = 0;// charMovement.dZ-charMovement.iZ;
		charMovement.distancia = Math.sqrt((charMovement.distanciaX * charMovement.distanciaX) + (charMovement.distanciaY * charMovement.distanciaY) + (charMovement.distanciaZ * charMovement.distanciaZ));

		charMovement.ticksToMove = (int) (ticksPerSecond * (charMovement.distancia / charMovement.speed));

		charMovement.valorPorTickX = charMovement.distanciaX / charMovement.ticksToMove;
		charMovement.valorPorTickY = charMovement.distanciaY / charMovement.ticksToMove;
		/* OBS: "z" nao e calculado na distancia pois o jogo seta automaticamente */
		charMovement.valorPorTickZ = 0;
		// charMovement.ticksToMove;

		if (false)
		{
			final StringBuilder str = new StringBuilder();
			str.append("\n-------------------------\n");
			str.append("INIT MOVEMENT\n");
			if (charMovement.speed == 0)
				str.append(charMovement.l2CharInfo.realName + ": DEVEL HINT speed=0 is invalid\n");
			str.append(charMovement.l2CharInfo.realName + ": speed=" + charMovement.speed + " distancia=" + charMovement.distancia + "/" + charMovement.distanciaX + "," + charMovement.distanciaY + "," + charMovement.distanciaZ + " time=" + ((float) charMovement.ticksToMove / ticksPerSecond) + "s\n");
			str.append(charMovement.l2CharInfo.realName + ": ticksToMove=" + charMovement.ticksToMove + "\n");
			str.append(charMovement.l2CharInfo.realName + ": value per tick=" + charMovement.valorPorTickX + "," + charMovement.valorPorTickY + "," + charMovement.valorPorTickZ + "\n");
			str.append("\n-------------------------\n");

		}

		if (charMovement.ticksToMove == 0)
		{
			charMovement.l2CharInfo.x = charMovement.dX;
			charMovement.l2CharInfo.y = charMovement.dY;
			charMovement.l2CharInfo.z = charMovement.dZ;
		} else if (charMovement.speed == 0)
		{
			//MainForm.getInstance().logLn("DEVEL HINT: speed=0 , erro em " + charMovement.l2CharInfo.getClass().getName());
		} else
		{
			synchronized (charMovement.l2CharInfo)
			{
				mapCharsInMovement.put(charMovement.l2CharInfo.objId, charMovement);
			}
		}

	}

	public final void removeMovement(final Integer objId)
	{
		if (mapCharsInMovement.containsKey(objId))
		{
			ObjMovement objMovementLocal = mapCharsInMovement.get(objId);

/*			if (false)
			{
				StringBuilder str = new StringBuilder();
				str.append("\n-------------------------\n");
				str.append("REMOVE MOVEMENT\n");
				if (objMovementLocal.speed == 0)
					str.append(objMovementLocal.l2CharInfo.realName + ": DEVEL HINT speed=0 is invalid\n");
				str.append(objMovementLocal.l2CharInfo.realName + ": speed=" + objMovementLocal.speed + " distancia=" + objMovementLocal.distancia + "/" + objMovementLocal.distanciaX + "," + objMovementLocal.distanciaY + "," + objMovementLocal.distanciaZ + " time restant=" + ((float) objMovementLocal.ticksToMove / ticksPerSecond) + "s\n");
				str.append(objMovementLocal.l2CharInfo.realName + ": ticksToMove=" + objMovementLocal.ticksToMove + "\n");
				str.append(objMovementLocal.l2CharInfo.realName + ": value per tick=" + objMovementLocal.valorPorTickX + "," + objMovementLocal.valorPorTickY + "," + objMovementLocal.valorPorTickZ + "\n");
				str.append("\n-------------------------\n");
			}*/

			synchronized (objMovementLocal)
			{
				synchronized (objMovementLocal.l2CharInfo)
				{
					mapCharsInMovement.remove(objId);
					objMovementLocal.l2CharInfo.movementObject = null;
					objMovementLocal.l2CharInfo = null;
				}
			}
		}
	}

	public final ObjMovement getMovement(final String objId)
	{
		return mapCharsInMovement.get(objId);
	}

	private void moveObjects()
	{
		for (Iterator<ObjMovement> iter = mapCharsInMovement.values().iterator(); iter.hasNext();)
		{
			ObjMovement mv = (ObjMovement) iter.next();

			synchronized (mv)
			{// sync
				if (mv.l2CharInfo == null)
					continue;

				synchronized (mv.l2CharInfo)
				{// sync
					mv.ticksToMove--;

					mv.x = mv.x + mv.valorPorTickX;
					mv.y = mv.y + mv.valorPorTickY;
					mv.z = mv.z + mv.valorPorTickZ;

					mv.distanciaX = mv.dX - mv.x;
					mv.distanciaY = mv.dY - mv.y;
					mv.distanciaZ = mv.dZ - mv.z;
					/* OBS: "z" nao e calculado na distancia pois o jogo seta automaticamente */
					mv.distanciaZ = 0;
					mv.distancia = Math.sqrt((mv.distanciaX * mv.distanciaX) + (mv.distanciaY * mv.distanciaY) + (mv.distanciaZ * mv.distanciaZ));

					mv.l2CharInfo.x = (int) mv.x;
					mv.l2CharInfo.y = (int) mv.y;
					// mv.l2CharInfo.z = (int) mv.z;

					mv.l2CharInfo.updateMoveToPawnDestini();

					if (mv.ticksToMove == 0 || mv.distancia <= mv.distanciaLimite)
					{
						mv.l2CharInfo.z = mv.dZ;

						mapCharsInMovement.remove(mv.l2CharInfo.objId);
						mv.l2CharInfo.movementObject = null;
/*						if (false)
						{
							StringBuilder str = new StringBuilder();
							str.append("\n-------------------------\n");
							str.append(mv.l2CharInfo.realName + ": movimento terminado\n");
							str.append(mv.l2CharInfo.realName + ": distanciaLimite=" + mv.distanciaLimite + "\n");
							str.append(mv.l2CharInfo.realName + ": calculado=" + mv.l2CharInfo.x + "," + mv.l2CharInfo.y + "," + mv.l2CharInfo.z + "\n");
							str.append(mv.l2CharInfo.realName + ": ponto alvo=" + mv.dX + "," + mv.dY + "," + mv.dZ + "\n");
							str.append("\n-------------------------\n");
						}*/
					}

				}// sync

			}// sync

		}

	}

}
