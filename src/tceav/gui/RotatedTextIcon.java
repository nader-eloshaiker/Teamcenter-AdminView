/*
 * RotatedTextIcon.java
 *
 * Created on 10 May 2008, 09:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui;

import javax.swing.Icon;
import java.awt.Font;
import java.awt.font.*;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Component;
import java.util.ArrayList;
/**
 *
 * @author NZR4DL
 */

public class RotatedTextIcon implements Icon {
    public static final int NONE = 0;
    public static final int RIGHT = 1;
    public static final int LEFT = 2;
    
    private int rotate;
    private ArrayList<GlyphVector> glyphs;
    private float width;
    private float height;
    private float ascent;
    private RenderingHints renderHints;
    
    
    public RotatedTextIcon(int rotate, Font[] font, String[] s) {
        this.rotate = rotate;
        this.height = 0;
        this.width = 0;
        glyphs = new ArrayList<GlyphVector>();
        
        for(int i=0; i<s.length; i++)
            convertTextToGlyph(font[i], s[i]);
        
    }
    
    public RotatedTextIcon(int rotate, Font font, String s) {
        this.height = 0;
        this.width = 0;
        this.rotate = rotate;
        glyphs = new ArrayList<GlyphVector>();

        convertTextToGlyph(font, s);
    }
    
    public void convertTextToGlyph(Font font, String s) {
        String[] text = s.split("\n");
        
        FontRenderContext fontRenderContext
                = new FontRenderContext(null,true,true);
        for(int i=0; i<text.length; i++) {
            glyphs.add(font.createGlyphVector(fontRenderContext,text[i]));
            width = Math.max(width, (int)glyphs.get(i).getLogicalBounds().getWidth() + 4);
            
            LineMetrics lineMetrics = font.getLineMetrics(text[i], fontRenderContext);
            ascent = lineMetrics.getAscent();
            height = Math.max(height, (int)lineMetrics.getHeight());
        }
        
        renderHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    public int getIconWidth() {
        return (int)(rotate == RotatedTextIcon.RIGHT
                || rotate == RotatedTextIcon.LEFT
                ? height*glyphs.size() : width);
    }
    
    
    public int getIconHeight() {
        return (int)(rotate == RotatedTextIcon.RIGHT
                || rotate == RotatedTextIcon.LEFT
                ? width : height*glyphs.size());
    }
    
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D)g;
        //g2d.setFont(font);
        AffineTransform oldTransform = g2d.getTransform();
        RenderingHints oldHints = g2d.getRenderingHints();
        
        g2d.setRenderingHints(renderHints);
        g2d.setColor(c.getForeground());
        
        
        if(rotate == RotatedTextIcon.NONE) {
            for(int i=0; i<glyphs.size(); i++) {
                g2d.drawGlyphVector(glyphs.get(i), x + 2, y + (height*i) + ascent);
            }
        } else if(rotate == RotatedTextIcon.RIGHT) {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x,y + 2);
            trans.rotate(Math.PI / 2,
                    height / 2, width / 2);
            g2d.setTransform(trans);
            for(int i=0; i<glyphs.size(); i++) {
                g2d.drawGlyphVector(glyphs.get(i),(height - width) / 2,
                        (width - height) / 2 + (height*i)
                        + ascent);
            }
        } else if(rotate == RotatedTextIcon.LEFT) {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x - 2, y - 2);
            trans.rotate(Math.PI * 3 / 2,
                    height / 2, width / 2);
            g2d.setTransform(trans);
            for(int i=0; i<glyphs.size(); i++) {
                g2d.drawGlyphVector(glyphs.get(i),(height - width) / 2,
                        (width - height) / 2 + (height*i)
                        + ascent);
            }
        }
        
        g2d.setTransform(oldTransform);
        g2d.setRenderingHints(oldHints);
    }
    /*
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(font);
        AffineTransform oldTransform = g2d.getTransform();
        RenderingHints oldHints = g2d.getRenderingHints();
     
        g2d.setRenderingHints(renderHints);
        g2d.setColor(c.getForeground());
     
     
        if(rotate == RotatedTextIcon.NONE) {
            g2d.drawGlyphVector(glyphs,x + 2,y + ascent);
        } else if(rotate == RotatedTextIcon.RIGHT) {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x,y + 2);
            trans.rotate(Math.PI / 2,
                    height / 2, width / 2);
            g2d.setTransform(trans);
            g2d.drawGlyphVector(glyphs,(height - width) / 2,
                    (width - height) / 2
                    + ascent);
        } else if(rotate == RotatedTextIcon.LEFT) {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(oldTransform);
            trans.translate(x,y - 2);
            trans.rotate(Math.PI * 3 / 2,
                    height / 2, width / 2);
            g2d.setTransform(trans);
            g2d.drawGlyphVector(glyphs,(height - width) / 2,
                    (width - height) / 2
                    + ascent);
        }
     
        g2d.setTransform(oldTransform);
        g2d.setRenderingHints(oldHints);
    }
     */
    
}

