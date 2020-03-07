package bin.game.util;

import bin.game.GameConstants;
import bin.game.util.logger.MyLogger;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.*;
import javax.swing.JOptionPane;

/**
 * this is an static class -> no instance needet.<br>
 * it contains some utility methods to load sprites from files.<br>
 *
 * constants are used for RPGM2k NPC Sprites
 *
 * @author gbeljajew
 */
public class Glob
{

    /** path to where the information about failed images is saved */
    private static final String LOG_FILE;
    /** paths of all images, that filed to load */
    private static final List<String> FAILED_IMAGES = new ArrayList<>();

    static
    {
        
        
        URI location;
        String workPath = ".";
        try
        {
            location = GameConstants.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            
            Path path = Paths.get( location).getParent();
            
            workPath = path.toAbsolutePath().toString();
        }
        catch( URISyntaxException ex )
        {
            JOptionPane.showMessageDialog(null, "Workpath creation failed" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
        LOG_FILE = workPath + "/log/failed images " + formater.format(new Date()) + ".log";
        
        //System.out.println(LOG_FILE);
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                try
                {
                    if(FAILED_IMAGES.size() > 0) //if there are no failed images, there is no need to write them out
                        Files.write(Paths.get(LOG_FILE), FAILED_IMAGES);
                } catch (IOException ex)
                {
                    // no need to react - it is allready to late
                }

            }
        }));
    }

    /**
     * this method should be called once after all images for your game are
     * loaded to check if everything is ok. list of all images, that failed to
     * load will be saved to fie '[WorkDirectory]/log/failed images [Date].log'
     *
     * @param debug if true it will show an message dialog if all images are
     * loaded correctly <br>if false only if there are some errors.
     */
    public static void checkImagesLoading(boolean debug)
    {
        if(!FAILED_IMAGES.isEmpty())
        {
            StringBuilder sb = new StringBuilder();
            
            for (String string : FAILED_IMAGES)
            {
                sb.append(string);
                sb.append("\n");
            }
            
            
            JOptionPane.showMessageDialog(null, 
                    "Some Images failed to load:\n" + sb, 
                    "Failed to load some images", 
                    JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            if(debug == true)
                JOptionPane.showMessageDialog(null, 
                        "All images are loaded successfuly.", 
                        "Load Successfull", 
                        JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     *
     * transforms BufferedImage to Image
     *
     * @param bufferedImage
     * @return
     */
    public static Image toImage(BufferedImage bufferedImage)
    {
        if (bufferedImage == null)
        {
            throw new NullPointerException();
        }
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }

    /**
     * loads a single Image from file
     *
     *
     * @param path make sure you use absolute path if your image is outside of
     * jar file.
     * @return null if there are some problems
     */
    public static Image getImage(String path)
    {
        BufferedImage bi;

        bi = getBufferedImage(path);

        if (bi == null)
        {
            return null;
        }

        return toImage(bi);

    }

    /**
     * loads sprites from a file and puts it in an array as Image one row after
     * another
     *
     *
     * @param path make sure you use absolute path if your image is outside of
     * jar file.
     * @param dx how wide is one sprite
     * @param dy how high is one sprite
     * @return an array of sprites
     */
    public static Image[] getTiles(String path, int dx, int dy)
    {
        BufferedImage bi;

        bi = getBufferedImage(path);

        return getTiles(bi, dx, dy);
    }

    /**
     * splits BufferedImage into Tiles.
     *
     * @param bi BufferedImage to be split
     * @param dx how wide is one sprite
     * @param dy how high is one sprite
     * @return an array of sprites
     */
    public static Image[] getTiles(BufferedImage bi, int dx, int dy)
    {
        Image[] erg;
        
        if (bi == null)
        {
            return null;
        }

        BufferedImage bis;
        int mx = bi.getWidth() / dx;
        int my = bi.getHeight() / dy;
        erg = new Image[mx * my];
        int ix, iy;
        for (iy = 0; iy < my; iy++)
        {
            for (ix = 0; ix < mx; ix++)
            {
                bis = bi.getSubimage(dx * ix, dy * iy, dx, dy);
                erg[ix + iy * mx] = toImage(bis);
            }
        }
        return erg;
    }
    
    /**
     * loads sprites from a file and puts it in an array as Image one row after
     * another. this one returns <code>BufferedImage</code> instead of
     * <code>Image</code> Useful if you need further splitting of resulting
     * images.
     *
     * @param path make sure you use absolute path if your image is outside of
     * jar file.
     * @param dx how wide is one sprite
     * @param dy how high is one sprite
     * @return an array of sprites
     */
    public static BufferedImage[] getImageParts(String path, int dx, int dy)
    {
        BufferedImage bi;
        
        bi = getBufferedImage(path);

        return getImageParts( bi, dx, dy );
    }

    /**
     * splits BufferedImage in an array of BufferedImages one row after
     * another. this one returns <code>BufferedImage</code> instead of
     * <code>Image</code> Useful if you need further splitting of resulting
     * images.
     *
     * jar file.
     * @param bi image to split
     * @param dx how wide is one sprite
     * @param dy how high is one sprite
     * @return an array of sprites
     */
    public static BufferedImage[] getImageParts( BufferedImage bi, int dx, int dy )
    {
        BufferedImage[] erg;
        if (bi == null)
        {
            return null;
        }
        BufferedImage bis;
        int mx = bi.getWidth() / dx;
        int my = bi.getHeight() / dy;
        erg = new BufferedImage[mx * my];
        int ix, iy;
        for (iy = 0; iy < my; iy++)
        {
            for (ix = 0; ix < mx; ix++)
            {
                bis = bi.getSubimage(dx * ix, dy * iy, dx, dy);
                erg[ix + iy * mx] = bis;
            }
        }
        return erg;
    }

    /**
     * load RPGM2k NPC (or something like that) Sprites<br>
     *
     * you can use it to load other sprite maps if you wish.<br>
     *
     *
     * @param path make sure you use absolute path if your image is outside of
     * jar file.
     * @param width width of sprite in pixel
     * @param high high of sprite in pixel
     * @return Image[step(0..2)][direction(0..3)] if you separated one NPC in
     * one file
     */
    public static Image[][] getHeroAnimation(String path, int width, int high)
    {

        if (width <= 0 || high <= 0)
        {
            throw new IllegalArgumentException("wrong size of tile: width = " + width + " high = " + high);
        }

        BufferedImage bi;
        bi = getBufferedImage(path);

        Image[][] erg = getHeroAnimation(bi, width, high);
        return erg;
    }

    /**
     * load RPGM2k NPC (or something like that) Sprites<br>
     *
     * you can use it to load other sprite maps if you wish.<br>
     *
     *
     * @param bi BufferedImage, that contains all tiles for this hero
     * @param width width of sprite in pixel
     * @param high high of sprite in pixel
     * @return Image[step(0..2)][direction(0..3)]
     */
    public static Image[][] getHeroAnimation(BufferedImage bi, int width, int high)
    {
        if (bi == null)
        {
            throw new NullPointerException("input image may be not null.");
        }

        if (width <= 0 || high <= 0)
        {
            throw new IllegalArgumentException("wrong size of tile: width = " + width + " high = " + high);
        }

        BufferedImage b;

        Image[][] erg = new Image[bi.getWidth() / width][bi.getHeight() / high];

        for (int ix = 0; ix < erg.length; ix++)
        {
            for (int iy = 0; iy < erg[ix].length; iy++)
            {
                b = bi.getSubimage(ix * width, iy * high, width, high);
                erg[ix][iy] = toImage(b);
            }
        }
        return erg;
    }

    /**
     *
     * loads a BufferedImage from file<br>
     * use getImage() instead
     *
     * @param path make sure you use absolute path if your image is outside of
     * jar file. Glob.getImage( "graphic/fly1.png" );
     * @return
     */
    public static BufferedImage getBufferedImage(String path)
    {
        BufferedImage bi;

        try
        {
            @SuppressWarnings("resource") // we definitely close it, if it is no null
            InputStream str = Glob.class.getClassLoader().getResourceAsStream(path);
            
            if (str != null) // null means outside
            {
                BufferedImage image = ImageIO.read(str);
                
                try
                {
                    str.close();
                }
                catch (Exception e)
                {
                    // ignored
                    
                    System.out.println(e);
                }
                
                return image;
            }
            
        } catch (IOException ex)
        {
            MyLogger.error("Error while loading image: " + path, ex);
        }

        File f = new File(path);

        try
        {
            bi = ImageIO.read(f);
        } catch (IOException ex)
        {

            FAILED_IMAGES.add(path);
            MyLogger.error(path + " not found");

            return null; // we do not expect this game to run well without this image, 
            // but we do not throw an exception here because there may be more than one image we fail to load.
            // so we give information about all images, that are missing.
        }
        
        return bi;
    }

}
