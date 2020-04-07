/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.dungeon;

import bin.game.Game;
import bin.game.GameConstants;
import static bin.game.GameConstants.MAP_SCREEN_HEIGHT;
import static bin.game.GameConstants.MAP_SCREEN_WIDTH;
import bin.game.GameLocale;
import bin.game.resources.GameResources;
import bin.game.resources.GraphicResources;
import bin.game.util.container.Coordinates2D;
import bin.game.util.container.CoordinatesContainer;
import bin.game.util.container.CoordinatesSet;
import bin.game.util.drawable.Animation;
import bin.game.util.drawable.CameraField;
import bin.game.util.drawable.Drawable;
import bin.game.util.logger.MyLogger;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Scanner;

/**
 *
 * @author gbeljajew
 */
public class Village implements Room
{

    CoordinatesContainer<Tile> map = new CoordinatesSet<>();
    CoordinatesContainer<TextMessage> text = new CoordinatesSet<>();
    private CameraField cameraField;
    private TextMessage textMessage;

    public Village()
    {
        this.fillVillageMap();
    }

    @Override
    public boolean isPassable(int x, int y)
    {
        Tile t = this.map.get(x, y);

        if (t == null)
        {
            return false;
        }

        return t.isPassable();
    }

    @Override
    public void draw(Graphics2D g, int kameraOffsetX, int kameraOffsetY)
    {
        for (Tile tile : map)
        {
            tile.draw(g, kameraOffsetX, kameraOffsetY);
        }

        Game.getHero().draw(g, kameraOffsetX, kameraOffsetY);
        
        if(this.textMessage != null)
            this.textMessage.draw(g, kameraOffsetX, kameraOffsetY);
    }

    @Override
    public void update()
    {

        Game.getHero().update();
    }

    private void fillVillageMap()
    {
//        FloorType floor = GameResources.getRandomFloorType();
//        
//        for (int x = 0; x < 20; x++)
//        {
//            for (int y = -0; y < 20; y++)
//            {
//                if(Math.random() > 0.1)
//                {
//                    this.map.add(new DefTile(floor.getFloorTile(), floor.getClutterImage(), true, x, y));
//                }
//                else
//                {
//                    this.map.add(new DefTile(floor.getWallTile(), floor.getClutterImage(), false, x, y));
//                }
//            }
//        }

        // TODO for now it is enough. will be a parameter later on, if there will be seasons for village map.
        String file = "/data/village.map";

        Scanner scanner = new Scanner(Village.class.getResourceAsStream(file));

        String tilesetKey = null;
        int mapY = 0, mapX = 0;

        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();

            // ignore comments and empty lines
            if (line.startsWith("//") || line.trim().isEmpty())
            {
                continue;
            }

            if (line.startsWith("TEXT"))
            {
                this.text.add(new TextMessage(line));
            }
            else if (line.startsWith("SHOP"))
            {
                // TODO add shop marker
            }
            else if (line.startsWith("#"))
            {
                tilesetKey = line.replace("#", "").trim();
            }
            else
            {
                if (tilesetKey == null
                    || tilesetKey.isEmpty()
                    || GraphicResources.getMiskTileSet(tilesetKey) == null)
                {
                    MyLogger.error("Error in map file: \"" + file
                                   + "\" unknown tileset key: " + tilesetKey);

                    throw new NullPointerException("Error in map file: \""
                                                   + file
                                                   + "\" unknown tileset key: " + tilesetKey);
                }
                
                // we allready checked for null so it sould be ok
                BufferedImage[] tileSet = GraphicResources.getMiskTileSet(tilesetKey);

                String[] tiles = line.split(";");
                
                if(mapX == 0)
                    mapX = tiles.length;
                else
                {
                    if(tiles.length != mapX)
                        MyLogger.warning("wrong number of tiles on line: " + mapY + 
                                         " in map file: " + file);
                }
                
                
                
                for(int x = 0; x < tiles.length; x++)
                {
                    try
                    {
                        String[] t = tiles[x].split(",");
                        Image tile = null, overlay = null;
                        boolean passable = true;
                        
                        if(t.length >= 1)
                        {
                            int tNum = Integer.parseInt(t[0].trim());
                            tile = tileSet[tNum];
                        }
                        
                        if(t.length >= 2)
                        {
                            passable = !t[1].trim().equals("0");
                        }
                        
                        if(t.length >= 3)
                        {
                            int tNum = Integer.parseInt(t[2].trim());
                            overlay = tileSet[tNum];
                        }
                        
                        this.map.add(new DefTile(tile, overlay, passable, x, mapY));
                    }
                    catch(NumberFormatException e)
                    {
                        MyLogger.error("Corrupted data in file: " + file + 
                                       " on line: " + mapY + 
                                       " position: " + mapX + 
                                       " text: \"" + tiles[x] + "\"");
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {
                        MyLogger.error("Unknown tile number in: " + file + 
                                       " on line: " + mapY + 
                                       " position: " + mapX + 
                                       " text: \"" + tiles[x] + "\"");
                    }
                    catch(IllegalArgumentException e)
                    {
                        MyLogger.error("no info about tile image in: " + file + 
                                       " on line: " + mapY + 
                                       " position: " + mapX + 
                                       " text: \"" + tiles[x] + "\"");
                    }
                            
                            
                }
                
                
                // should be the last line in this block
                mapY++;
            }

        }

    }

    @Override
    public CameraField getCameraField()
    {
        if (this.cameraField == null)
        {
            int minX = this.map.getMinX() * GameConstants.TILE_WIDTH;
            int maxX = (this.map.getMaxX() + 1) * GameConstants.TILE_WIDTH;
            int minY = this.map.getMinY() * GameConstants.TILE_HEIGHT;
            int maxY = (this.map.getMaxY() + 1) * GameConstants.TILE_HEIGHT;

            this.cameraField = new CameraField(minX, minY, maxX, maxY,
                    GameConstants.MAP_SCREEN_WIDTH,
                    GameConstants.MAP_SCREEN_HEIGHT);
        }

        return this.cameraField;
    }

    @Override
    public void tap(int x, int y)
    {
        // TODO enter shops is here
        
        this.textMessage = text.get(x, y);
    }

}

class TextMessage implements Coordinates2D, Drawable
{
    private final static int LINE_PADDING = 5;
    private final static int TEXT_PADDING = 5;
    private static final Font FONT = new Font(Font.SERIF, Font.BOLD, 25);
    
    private final int x, y;
    private final String key;
    
    

    public TextMessage(String line)
    {
        String[] par;
        
        if(!line.startsWith("TEXT") || (par = line.split(";")).length < 3)
        {
            MyLogger.error("wrong String as parameter: " + line, new IllegalArgumentException());
            throw new IllegalArgumentException("wrong String as parameter: " + line);
        }
        
        this.key = par[1].trim();
        
        String[] coords = par[2].split(",");
        
        if(coords.length < 2)
        {
            MyLogger.error("wrong String as parameter: " + line, new IllegalArgumentException());
            throw new IllegalArgumentException("wrong String as parameter: " + line);
        }
        
        try
        {
        this.x = Integer.parseInt(coords[0]);
        this.y = Integer.parseInt(coords[1]);
        }
        catch(NumberFormatException e)
        {
            MyLogger.error("wrong String as parameter: " + line, e);
            throw new IllegalArgumentException("wrong String as parameter: " + line);
        }
    }
    
    

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    @Override
    public void draw(Graphics2D g, int cameraOffsetX, int cameraOffsetY)
    {
        Font backFont = g.getFont();
        g.setFont(FONT);
        
        
        FontMetrics metrics = g.getFontMetrics();
        
        String[] lines = GameLocale.getString(key).split("\n");
        
        int lineHeight = (int) metrics.getStringBounds(lines[0], g).getHeight();
        
        
        int textHeight = (int) (lineHeight * lines.length 
                                + LINE_PADDING * (lines.length - 1)
                                + TEXT_PADDING * 2);
        
        int textWidth = 0;
        
        for (String line : lines)
        {
            Rectangle2D w = metrics.getStringBounds(line, g);
            
            if(w.getWidth() > textWidth)
                textWidth = (int) w.getWidth();
        }
        
        textWidth += 2 * TEXT_PADDING;
        
        Color standardColor = g.getColor();
        
        g.setColor(Color.LIGHT_GRAY);
        
        int posY = MAP_SCREEN_HEIGHT / 4 - textHeight / 2;
        
        if(posY < TEXT_PADDING)
            posY = TEXT_PADDING;
        
        g.fillRect((MAP_SCREEN_WIDTH - textWidth) / 2,
                    posY, 
                    textWidth, 
                    textHeight);
        
        g.setColor(standardColor);
        
        posY += lineHeight;
        
        
        int lineStep = lineHeight + LINE_PADDING;
        
        for (String line : lines)
        {
            double width = metrics.getStringBounds(line, g).getWidth();
            
            if(width > MAP_SCREEN_WIDTH)
                MyLogger.debug("text to wide: " + GameLocale.getString(key) );
            
            int posX = (int) (MAP_SCREEN_WIDTH / 2 - width / 2);
            
            g.drawString(line, posX, posY);
            
            posY += lineStep;
        }
        
        g.setFont(backFont);
        
    }

    
    
}
