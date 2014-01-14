package fw.test;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import xmlex.config.ConfigSystem;

public class Window
{
    private JFrame frame;
    private Canvas glCanvas = new Canvas();
    private final JPanel panel1 = new JPanel();
    private final JPanel panel2 = new JPanel();
    private final JTextPane textPane = new JTextPane();

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
    	ConfigSystem.load();
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    Window window = new Window();
                    window.frame.setVisible(true);
                    Display.create();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                GL11.glClearColor(1f, 0f, 0f, 1f);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                Display.update();
                
                EventQueue.invokeLater(this);
            }
        });
    }
    
    /**
     * Create the application.
     */
    public Window()
    {
        initialize();
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frame = new JFrame();
        frame.addWindowListener(new FrameWindowListener());
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
        frame.getContentPane().add(panel1);
        panel1.setLayout(null);
        textPane.setBounds(10, 5, 124, 20);
        
        panel1.add(textPane);
        frame.getContentPane().add(panel2);
        panel2.setLayout(new BorderLayout(0, 0));
        
        glCanvas.setIgnoreRepaint(true);
        panel2.add(glCanvas);
        try { Display.setParent(glCanvas); }
        catch(LWJGLException e) { e.printStackTrace(); }
    }
    
    private class FrameWindowListener extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            Display.destroy();
        }
    }
}