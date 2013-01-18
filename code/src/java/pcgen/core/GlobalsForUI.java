package pcgen.core;

import javax.swing.JFrame;

public class GlobalsForUI {
    private static JFrame rootFrame;
    private static JFrame currentFrame;

    /**
     * Sets the root frame The root frame is the container in which all other panels, frame etc are placed.
     * 
     * @param frame
     *            the <code>PCGen_Frame1</code> which is to be root
     */
    public static void setRootFrame(final JFrame frame)
    {
        rootFrame = frame;
    }

    /**
     * Returns the current root frame.
     * 
     * @return the <code>rootFrame</code> property
     */
    public static JFrame getRootFrame()
    {
        return rootFrame;
    }

    /**
     * Set the current frame
     * 
     * @param frame
     */
    public static void setCurrentFrame(final JFrame frame)
    {
        currentFrame = frame;
    }

    /**
     * Get the current frame
     * 
     * @return current frame
     */
    public static JFrame getCurrentFrame()
    {
        if (currentFrame == null)
        {
            return rootFrame;
        }
        return currentFrame;
    }
}
