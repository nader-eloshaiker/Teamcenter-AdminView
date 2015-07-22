/*
 * ResourceLoader.java
 *
 * Created on 10 January 2008, 11:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.resources;

import java.net.URL;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author nzr4dl
 */
public class ResourceLoader {
    
    /**
     * Creates a new instance of ResourceLoader
     */
    public ResourceLoader() {
    }
    
    public static URL getImageURL(ImageEnum image) throws IOException {
        return ResourceLoader.class.getResource(image.value());
    }
    
    public static ImageIcon getImage(ImageEnum image) throws IOException {
        return new ImageIcon(getImageURL(image));
    }
    
}
