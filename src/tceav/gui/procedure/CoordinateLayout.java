/*
 * @(#)CoordinateLayout.java	1.52 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package tceav.gui.procedure;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * A flow layout arranges components in a directional flow, much
 * like lines of text in a paragraph. The flow direction is
 * determined by the container's <code>componentOrientation</code>
 * property and may be one of two values: 
 * <ul>
 * <li><code>ComponentOrientation.LEFT_TO_RIGHT</code>
 * <li><code>ComponentOrientation.RIGHT_TO_LEFT</code>
 * </ul>
 * Flow layouts are typically used
 * to arrange buttons in a panel. It arranges buttons
 * horizontally until no more buttons fit on the same line.
 * The line alignment is determined by the <code>align</code>
 * property. The possible values are:
 * <ul>
 * <li>{@link #LEFT LEFT}
 * <li>{@link #RIGHT RIGHT}
 * <li>{@link #CENTER CENTER}
 * <li>{@link #LEADING LEADING}
 * <li>{@link #TRAILING TRAILING}
 * </ul>
 * <p>
 * For example, the following picture shows an applet using the flow
 * layout manager (its default layout manager) to position three buttons:
 * <p>
 * <img src="doc-files/CoordinateLayout-1.gif"
 * ALT="Graphic of Layout for Three Buttons"
 * ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * Here is the code for this applet:
 * <p>
 * <hr><blockquote><pre>
 * import java.awt.*;
 * import java.applet.Applet;
 *
 * public class myButtons extends Applet {
 *     Button button1, button2, button3;
 *     public void init() {
 *         button1 = new Button("Ok");
 *         button2 = new Button("Open");
 *         button3 = new Button("Close");
 *         add(button1);
 *         add(button2);
 *         add(button3);
 *     }
 * }
 * </pre></blockquote><hr>
 * <p>
 * A flow layout lets each component assume its natural (preferred) size.
 *
 * @version 	1.52, 12/19/03
 * @author 	Arthur van Hoff
 * @author 	Sami Shaio
 * @since       JDK1.0
 * @see ComponentOrientation
 */
public class CoordinateLayout implements LayoutManager, java.io.Serializable {

    int hgap;
    /**
     * The flow layout manager allows a seperation of
     * components with gaps.  The vertical gap will
     * specify the space between rows and between the
     * the rows and the borders of the <code>Container</code>.
     *
     * @serial
     * @see #getHgap()
     * @see #setHgap(int)
     */
    int vgap;

    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = -7262534875583282631L;

    /**
     * Constructs a new <code>CoordinateLayout</code> with a centered alignment and a
     * default 5-unit horizontal and vertical gap.
     */
    public CoordinateLayout() {
        this(5, 5);
    }

    /**
     * Creates a new flow layout manager with the indicated alignment
     * and the indicated horizontal and vertical gaps.
     * @param      hgap    the horizontal gap between components
     *                     and between the components and the 
     *                     borders of the <code>Container</code>
     * @param      vgap    the vertical gap between components
     *                     and between the components and the 
     *                     borders of the <code>Container</code>
     */
    public CoordinateLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    /**
     * Gets the horizontal gap between components
     * and between the components and the borders
     * of the <code>Container</code>
     *
     * @return     the horizontal gap between components
     *             and between the components and the borders
     *             of the <code>Container</code>
     * @see        java.awt.CoordinateLayout#setHgap
     * @since      JDK1.1
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * Sets the horizontal gap between components and
     * between the components and the borders of the
     * <code>Container</code>.
     *
     * @param hgap the horizontal gap between components
     *             and between the components and the borders
     *             of the <code>Container</code>
     * @see        java.awt.CoordinateLayout#getHgap
     * @since      JDK1.1
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * Gets the vertical gap between components and
     * between the components and the borders of the
     * <code>Container</code>.
     *
     * @return     the vertical gap between components
     *             and between the components and the borders
     *             of the <code>Container</code>
     * @see        java.awt.CoordinateLayout#setVgap
     * @since      JDK1.1
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * Sets the vertical gap between components and between
     * the components and the borders of the <code>Container</code>.
     *
     * @param vgap the vertical gap between components
     *             and between the components and the borders
     *             of the <code>Container</code>
     * @see        java.awt.CoordinateLayout#getVgap
     * @since      JDK1.1
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    /**
     * Adds the specified component to the layout.
     * Not used by this class.
     * @param name the name of the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout.
     * Not used by this class.
     * @param comp the component to remove
     * @see       java.awt.Container#removeAll
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Returns the preferred dimensions for this layout given the 
     * <i>visible</i> components in the specified target container.
     *
     * @param target the container that needs to be laid out
     * @return    the preferred dimensions to lay out the
     *            subcomponents of the specified container
     * @see Container
     * @see #minimumLayoutSize
     * @see       java.awt.Container#getPreferredSize
     */
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            int nmembers = target.getComponentCount();

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    dim.height = Math.max(dim.height, d.height + m.getLocation().y);
                    dim.width = Math.max(dim.width, d.width + m.getLocation().x);
                }
            }
            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right + hgap * 2;
            dim.height += insets.top + insets.bottom + vgap * 2;
            return dim;
        }
    }

    /**
     * Returns the minimum dimensions needed to layout the <i>visible</i>
     * components contained in the specified target container.
     * @param target the container that needs to be laid out
     * @return    the minimum dimensions to lay out the
     *            subcomponents of the specified container
     * @see #preferredLayoutSize
     * @see       java.awt.Container
     * @see       java.awt.Container#doLayout
     */
    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            int nmembers = target.getComponentCount();

            if (nmembers > 0) {
                Component m = target.getComponent(0);
                if (m.isVisible()) {
                    Dimension d = m.getMinimumSize();
                    dim.height = d.height + m.getLocation().y;
                    dim.width = d.width + m.getLocation().x;
                }
            }
            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right + hgap * 2;
            dim.height += insets.top + insets.bottom + vgap * 2;
            return dim;
        }
    }

    /**
     * Lays out the container. This method lets each 
     * <i>visible</i> component take
     * its preferred size by reshaping the components in the
     * target container in order to satisfy the alignment of
     * this <code>CoordinateLayout</code> object.
     *
     * @param target the specified component being laid out
     * @see Container
     * @see       java.awt.Container#doLayout
     */
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int nmembers = target.getComponentCount();
            int x = 0, y;

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    m.setSize(d.width, d.height);

                    x = m.getLocation().x;
                    y = m.getLocation().y;
                    if (x <= insets.left + hgap) {
                        x = insets.left + hgap + 1;
                    } 
                    if (y <=  insets.top + vgap) {
                        y =  insets.top + vgap + 1;
                    }
                    m.setLocation(x, y);
                }
            }
        }
    }

    //
    // the internal serial version which says which version was written
    // - 0 (default) for versions before the Java 2 platform, v1.2
    // - 1 for version >= Java 2 platform v1.2, which includes "newAlign" field
    //
    private static final int currentSerialVersion = 1;
    /**
     * This represent the <code>currentSerialVersion</code>
     * which is bein used.  It will be one of two values :
     * <code>0</code> versions before Java 2 platform v1.2..
     * <code>1</code> versions after  Java 2 platform v1.2..
     *
     * @serial
     * @since 1.2
     */
    private int serialVersionOnStream = currentSerialVersion;

    /**
     * Reads this object out of a serialization stream, handling
     * objects written by older versions of the class that didn't contain all
     * of the fields we use now..
     */
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();

        serialVersionOnStream = currentSerialVersion;
    }

    /**
     * Returns a string representation of this <code>CoordinateLayout</code>
     * object and its values.
     * @return     a string representation of this layout
     */
    @Override
    public String toString() {
        return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
    }
}
