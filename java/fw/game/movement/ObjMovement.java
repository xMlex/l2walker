package fw.game.movement;

import fw.game.L2Char;


public class ObjMovement
{ 
	public L2Char l2CharInfo = null;
  
	/*INICIO*/
	public int iX = 0, iY = 0, iZ = 0;

	/*DESTINO*/
	public int dX = 0, dY = 0, dZ = 0; 
	
	public int speed = -1;

	//-----------//
	 
	public int ticksToMove = 0;
	
	public double distancia = 0;
	
	public double distanciaLimite = 0;
	
	public double distanciaX = 0;

	public double distanciaY = 0;

	public double distanciaZ = 0;
	
	public double valorPorTickX = 0;

	public double valorPorTickY = 0;

	public double valorPorTickZ = 0;

	public double x = 0;

	public double y = 0;

	public double z = 0; 

}
