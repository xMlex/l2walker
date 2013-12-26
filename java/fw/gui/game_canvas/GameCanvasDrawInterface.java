package fw.gui.game_canvas;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public interface GameCanvasDrawInterface
{
    public boolean drawFrame(GC gc);
    public void setFrameBounds(Rectangle bounds);
    public void dispose();
}
