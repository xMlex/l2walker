package fw.connection.game.server;

import java.nio.BufferUnderflowException;

import fw.game.GameEngine;

public abstract class GameServerPacket extends BaseServerPacket<GameEngine> {
	
	
	@Override
	public void run(){
		try{
			try{
				read();
			}catch(BufferUnderflowException e){e.printStackTrace();}
			xrun();
		}catch (Exception e) {			
			e.printStackTrace();
		}
	}
	public abstract void xrun();
}
