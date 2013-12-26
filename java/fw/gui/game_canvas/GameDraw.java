package fw.gui.game_canvas;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import fw.com.swtdesigner.SWTResourceManager;

public class GameDraw implements GameCanvasDrawInterface
{
	public Color bgColor = SWTResourceManager.getColor(0, 223, 223);
	public Color fgColor = SWTResourceManager.getColor(0, 0, 0);
	public Color fgColor2 = SWTResourceManager.getColor(0, 0, 0);
	public Font font1 = SWTResourceManager.getFont("Tahoma", 20, 1, false, false);
	GameCanvas gameCanvas;

	Rectangle bounds = null;
	int pos = 0;
	boolean isDisposed = false;

	public GameDraw(GameCanvas gameCanvas)
	{
		this.gameCanvas = gameCanvas;
		//new FrameControl(this).start();
	}

	public void dispose()
	{
		isDisposed = true;
	}

	public void setFrameBounds(Rectangle bounds)
	{
		this.bounds = bounds;
	}

	public boolean drawFrame(GC gc)
	{
		gc.setBackground(bgColor);
		gc.fillRectangle(0, 0, bounds.width, bounds.height);

		gc.setFont(font1);
		gc.setForeground(fgColor);
		for (int i = 0; i < 10; i++)
		{
			gc.drawText(".::Test::.", pos, 20 * i, true);
		}

		return true;
	}

	class FrameControl extends Thread
	{
		GameDraw gameDraw;

		public FrameControl(GameDraw gameDraw)
		{
			this.gameDraw = gameDraw;
		}

		@Override
		public void run()
		{
			long frameTime = 1000 / 30;

			while (!gameDraw.isDisposed)
			{
				try
				{
					long iniTime = System.currentTimeMillis();

					move();

					long endTime = System.currentTimeMillis();

					long sleepTime = frameTime - (endTime - iniTime);

					if (sleepTime < 0)
					{
						//System.out.println("#frame draw time is too long " + (endTime - iniTime));
					} else
					{
						sleep(sleepTime);
					}
				}
				catch (InterruptedException e)
				{
				}
			}
		}

		private void move()
		{
			gameDraw.pos++;
			if (gameDraw.pos >= gameDraw.bounds.width)
				pos = 0;
		}

	}

}
