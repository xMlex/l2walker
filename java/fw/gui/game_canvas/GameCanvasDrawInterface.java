package fw.gui.game_canvas;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public interface GameCanvasDrawInterface
{
    public boolean drawFrame(GC gc);
    public void setFrameBounds(Rectangle bounds);
    public void onMouseMove(MouseEvent evt);
    public void onMouseUp(MouseEvent evt);
    public void onMouseDown(MouseEvent evt);
    public void onMouseDoubleClick(MouseEvent evt);
    public void dispose();
}
