/*
 * RotatedTextIcon.java
 *
 * Created on 10 May 2008, 09:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import javax.swing.Icon;
import java.awt.geom.AffineTransform;
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
    
    
    public RotatedTextIcon(int rotate, ArrayList<Font> font, ArrayList<String> s) {
        this.rotate = rotate;
        this.height = 0;
        this.width = 0;
        glyphs = new ArrayList<GlyphVector>();
        
        for(int i=0; i<s.size(); i++)
            convertTextToGlyph(font.get(i), s.get(i));
        
    }
    
    public RotatedTextIcon(int rotate, Font font, String s) {
        this.height = 0;
        this.width = 0;
        this.rotate = rotate;
        glyphs = new ArrayList<GlyphVector>();
        
        convertTextToGlyph(font, s);
    }
    
    public void convertTextToGlyph(Font font, String s) {
        int strPad = 4;
        String[] text = s.split("\n");
        
        FontRenderContext fontRenderContext = new FontRenderContext(null,true,true);
        for(int i=0; i<text.length; i++) {
            
            ArrayList<String> strList = hyphenateString(text[i]);
            
            for(int k=0; k<strList.size(); k++) {
                glyphs.add(font.createGlyphVector(fontRenderContext,strList.get(k)));
                width = Math.max(width, (int)glyphs.get(glyphs.size()-1).getLogicalBounds().getWidth() + strPad);
                LineMetrics lineMetrics = font.getLineMetrics(strList.get(k), fontRenderContext);
                ascent = lineMetrics.getAscent();
                height = Math.max(height, (int)lineMetrics.getHeight());
            }
            
        }
        
        renderHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    private ArrayList<String> hyphenateString(String s) {
        int stringLimit = 45;
        int division;
        String indent;
        String pad = "   ";
        ArrayList<String> tmpList = new ArrayList<String>();
        
        if(s.length() > stringLimit) {
            division = s.length() / stringLimit;
            indent = getLeadingSpace(s);
            
            for(int k=0; k<=division; k++) {
                if (k == 0)
                    tmpList.add(s.substring(stringLimit*k,stringLimit*(k+1))+"...");
                else if(k == division)
                    tmpList.add(indent + pad + s.substring(stringLimit*k));
                else
                    tmpList.add(indent + pad + s.substring(stringLimit*k,stringLimit*(k+1))+"...");
            }
            
        } else
            tmpList.add(s);
        
        return tmpList;
        
    }
    
    private String getLeadingSpace(String s) {
        String tmp = s.trim();
        int len = s.length() - tmp.length();
        String indent = "";
        
        for(int i=0; i<len; i++)
            indent += " ";
        
        return indent;
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
    
}

