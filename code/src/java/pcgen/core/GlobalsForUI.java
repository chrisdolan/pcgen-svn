/*
 * Copyright (c) 2013 Chris Dolan <chris@chrisdolan.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package pcgen.core;

import javax.swing.JFrame;

/**
 * This class exists to separate the Swing code from the Globals class.
 */
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
