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
import bin.game.Updatable;
import bin.game.panels.ScreenControl;

import bin.game.panels.ScreenEnum;
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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import bin.game.dungeon.cristall.Touchable;
import bin.game.hero.HeroFigure;
import bin.game.util.Direction;
import bin.game.util.NPC_Sprite;
import bin.game.util.RPGM2K_NPC_Sprite;
import bin.game.util.Utils;
import bin.game.util.container.OverlappingDrawableContainer;
import bin.game.util.container.RandomContainer;
import bin.game.util.container.RandomSet;
import bin.game.util.container.RandomizerWrapper;
import bin.game.util.drawable.OverlappingDrawable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gbeljajew
 */
public class Village implements Room
{

    private final CoordinatesContainer<Tile> map = new CoordinatesSet<>();
    private final CoordinatesContainer<TextMessage> text = new CoordinatesSet<>();
    private final CoordinatesContainer<Touchable> doors = new CoordinatesSet<>();
    private final List<NpcVillager> villagers = new ArrayList<>();
    private final OverlappingDrawableContainer npcDrawer = new OverlappingDrawableContainer();

    private CameraField cameraField;
    private TextMessage textMessage;
    private Animation animation;
    private int heroStartX;
    private int heroStartY;
    private int innExitX;
    private int innExitY;

    public Village(InputStream stream)
    {
        this.fillVillageMap(stream);
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

        this.npcDrawer.draw(g, kameraOffsetX, kameraOffsetY);

        if (this.textMessage != null)
        {
            this.textMessage.draw(g, kameraOffsetX, kameraOffsetY);
        }

        if (this.animation != null)
        {
            this.animation.draw(g, kameraOffsetX, kameraOffsetY);
        }
    }

    @Override
    public void update()
    {
        if (this.animation != null)
        {
            if (this.animation.isDone())
            {
                this.animation = null;
            }
            else
            {
                return;
            }
        }

        Game.getHero().update();
        
        for (NpcVillager villager : this.villagers)
        {
            villager.update();
        }
        
    }

    public void heroToStart()
    {
        Game.getHero().setPosition(heroStartX, heroStartY);

        this.npcDrawer.add(Game.getHero());
    }

    private void fillVillageMap(InputStream stream)
    {

        // TODO untill a normal entry is created
        //this.npcDrawer.add(Game.getHero());
        Scanner scanner = new Scanner(stream);

        String file = "XXX";

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

            if (line.startsWith("FILE"))
            {
                file = line.replace("FILE", "").trim();
            }
            else if (line.startsWith("START"))
            {
                String[] split = line.split(";");

                if (split.length < 2)
                {
                    MyLogger.debug("Corrupted map file: \"" + file
                                   + "\" in line: " + line + " expected: \"START;x,y\"");
                }
                else
                {
                    split = split[1].trim().split(",");
                    if (split.length < 2)
                    {
                        MyLogger.debug("Corrupted map file: \"" + file
                                       + "\" in line: " + line + " expected: \"START;x,y\"");
                    }
                    else
                    {
                        this.heroStartX = Integer.parseInt(split[0]);
                        this.heroStartY = Integer.parseInt(split[1]);
                    }
                }

            }
            else if (line.startsWith("INN"))
            {
                String[] split = line.split(";");

                if (split.length < 2)
                {
                    MyLogger.debug("Corrupted map file: \"" + file
                                   + "\" in line: " + line + " expected: \"START;x,y\"");
                }
                else
                {
                    split = split[1].trim().split(",");
                    if (split.length < 2)
                    {
                        MyLogger.debug("Corrupted map file: \"" + file
                                       + "\" in line: " + line + " expected: \"START;x,y\"");
                    }
                    else
                    {
                        this.innExitX = Integer.parseInt(split[0]);
                        this.innExitY = Integer.parseInt(split[1]);
                    }
                }

            }
            else if (line.startsWith("TEXT"))
            {
                this.text.add(TextMessage.createMapText(line, file));
            }
            else if (line.startsWith("SHOP"))
            {
                try
                {
                    this.doors.add(new DoorToShop(line));
                }
                catch (RuntimeException e)
                {
                    MyLogger.error("error while loading village map", e);
                }
            }
            else if (line.startsWith("DUNGEON"))
            {
                doors.add(new DoorToDungeon(line));
            }
            else if (line.startsWith("TILESET"))
            {
                tilesetKey = line.replace("TILESET", "").replace(";", "").trim();
            }
            else if (line.startsWith("NPC"))
            {
                this.add(NpcVillager.create(line, file));
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

                if (mapX == 0)
                {
                    mapX = tiles.length;
                }
                else
                {
                    if (tiles.length != mapX)
                    {
                        MyLogger.warning("wrong number of tiles on line: " + mapY
                                         + " in map file: " + file);
                    }
                }

                for (int x = 0; x < tiles.length; x++)
                {
                    try
                    {
                        String[] t = tiles[x].split(",");
                        Image tile = null, overlay = null;
                        boolean passable = true;

                        if (t.length >= 1)
                        {
                            int tNum = Integer.parseInt(t[0].trim());
                            tile = tileSet[tNum];
                        }

                        if (t.length >= 2)
                        {
                            passable = !t[1].trim().equals("0");
                        }

                        if (t.length >= 3)
                        {
                            int tNum = Integer.parseInt(t[2].trim());
                            overlay = tileSet[tNum];
                        }

                        this.map.add(new DefTile(tile, overlay, passable, x, mapY));
                    }
                    catch (NumberFormatException e)
                    {
                        MyLogger.error("Corrupted data in file: " + file
                                       + " on line: " + mapY
                                       + " position: " + mapX
                                       + " text: \"" + tiles[x] + "\"");
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        MyLogger.error("Unknown tile number in: " + file
                                       + " on line: " + mapY
                                       + " position: " + mapX
                                       + " text: \"" + tiles[x] + "\"");
                    }
                    catch (IllegalArgumentException e)
                    {
                        MyLogger.error("no info about tile image in: " + file
                                       + " on line: " + mapY
                                       + " position: " + mapX
                                       + " text: \"" + tiles[x] + "\"");
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
    public void touch(int x, int y)
    {

        //System.out.println("[" + x + "," + y + "]");
        
        this.textMessage = null; 
        
        
        for (NpcVillager villager : this.villagers)
        {
            if(villager.isClose(Game.getHero()))
            {
                this.textMessage = villager.getTextMessage();
                break;
            }
        }
        
        
        
        if(this.textMessage == null)
            this.textMessage = text.get(x, y);

        Touchable door = doors.get(x, y);

        if (door != null)
        {
            this.animation = door.touch();
        }

    }

    private void add(NpcVillager npc)
    {
        this.villagers.add(npc);
        this.npcDrawer.add(npc);
    }

    public void heroToInn()
    {
        Game.getHero().setPosition(this.innExitX, this.innExitY);

        this.npcDrawer.add(Game.getHero());
    }

    private class DoorToShop implements Touchable
    {

        private final int x, y;
        private final ScreenEnum shopScreen;
        private final int heroX, heroY;

        public DoorToShop(int x, int y, ScreenEnum shopScreen, int heroX, int heroY)
        {
            this.x = x;
            this.y = y;
            this.shopScreen = shopScreen;
            this.heroX = heroX;
            this.heroY = heroY;
        }

        private DoorToShop(String line)
        {

            if (!line.startsWith("SHOP"))
            {
                throw new IllegalArgumentException("unknown argument: " + line);
            }

            String[] parts = line.split(";");

            if (parts.length < 4)
            {
                throw new IllegalArgumentException("not enough parts: " + line);
            }

            this.shopScreen = ScreenEnum.valueOf(parts[1]);

            String[] doorPositions = parts[2].split(",");

            this.x = Integer.parseInt(doorPositions[0].trim());
            this.y = Integer.parseInt(doorPositions[1].trim());

            String[] exitPositions = parts[3].split(",");

            this.heroX = Integer.parseInt(exitPositions[0].trim());
            this.heroY = Integer.parseInt(exitPositions[1].trim());

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
        public Animation touch()
        {
            ScreenControl.switchScreen(shopScreen);
            Game.getHero().setPosition(heroX, heroY);

            return null;
        }

    }

    private class DoorToDungeon implements Touchable
    {

        private final int x;
        private final int y;

        private DoorToDungeon(String line)
        {
            String[] parts = line.split(";");

            String[] doorPositions = parts[1].split(",");

            this.x = Integer.parseInt(doorPositions[0].trim());
            this.y = Integer.parseInt(doorPositions[1].trim());
        }

        @Override
        public Animation touch()
        {
            // TODO a way to differ between new run from village and continue run from other preset map.
            Game.startNewRun(Game.getCurrentDungeonLevel() + 1);

            Village.this.npcDrawer.remove(Game.getHero());

            return null;
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

    }

}

class TextMessage implements Coordinates2D, Drawable
{

    private final static int LINE_PADDING = 5;
    private final static int TEXT_PADDING = 5;
    private static final Font FONT = new Font(Font.SERIF, Font.BOLD, 25);

    static TextMessage createMapText(String line, String file)
    {
        try
        {
            return new TextMessage(line, file);
        }
        catch (RuntimeException e)
        {

        }

        return null;
    }

    static TextMessage createNpcText(String key)
    {
        return new TextMessage(key);
    }

    private final int x, y;
    private final String key;

    /**
     * constructor, which parses line to create texts for precreated maps.
     *
     * @param line line to parse
     * @param file file name for logging.
     */
    private TextMessage(String line, String file)
    {
        String[] par;

        if (!line.startsWith("TEXT") || (par = line.split(";")).length < 3)
        {
            MyLogger.error("wrong String as parameter: " + line, new IllegalArgumentException());
            throw new IllegalArgumentException("wrong String as parameter: " + line + " in file: " + file);
        }

        this.key = par[1].trim();

        if (GameLocale.getString(key).equals(key))
        {
            MyLogger.debug("No text found for TextMessage. key: " + key + " line: " + line + " file: " + file);
            throw new NullPointerException();
        }

        String[] coords = par[2].split(",");

        if (coords.length < 2)
        {
            MyLogger.error("wrong String as parameter: " + line, new IllegalArgumentException());
            throw new IllegalArgumentException("wrong String as parameter: " + line + " in file: " + file);
        }

        try
        {
            this.x = Integer.parseInt(coords[0]);
            this.y = Integer.parseInt(coords[1]);
        }
        catch (NumberFormatException e)
        {
            MyLogger.error("wrong String as parameter: " + line, e);
            throw new IllegalArgumentException("wrong String as parameter: " + line + " in file: " + file);
        }
    }

    /**
     * constructor for NPC texts
     *
     * @param key
     */
    private TextMessage(String key)
    {
        if (key == null || GameLocale.getString(key).equals(key))
        {
            throw new NullPointerException();
        }

        this.key = key;
        this.x = 0;
        this.y = 0;
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

            if (w.getWidth() > textWidth)
            {
                textWidth = (int) w.getWidth();
            }
        }

        textWidth += 2 * TEXT_PADDING;

        Color standardColor = g.getColor();

        g.setColor(Color.LIGHT_GRAY);

        int posY = MAP_SCREEN_HEIGHT / 4 - textHeight / 2;

        if (posY < TEXT_PADDING)
        {
            posY = TEXT_PADDING;
        }

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

            if (width > MAP_SCREEN_WIDTH)
            {
                MyLogger.debug("text to wide: " + GameLocale.getString(key));
            }

            int posX = (int) (MAP_SCREEN_WIDTH / 2 - width / 2);

            g.drawString(line, posX, posY);

            posY += lineStep;
        }

        g.setFont(backFont);

    }

}

class NpcVillager implements Updatable, OverlappingDrawable
{

    private final NpcFigure figure;
    private final RandomContainer<RandomizerWrapper<TextMessage>> textRandomiser = new RandomSet<>();

    private final int startX;
    private final int startY;

    

    private int timeSinceLastGotText = 0;
    private TextMessage text;

    private final String figureKey;
    private int movingTicksLeft;

    public NpcVillager(String figureKey, int startX, int startY, int stepDelay, int stepLenght)
    {

        BufferedImage figur = GraphicResources.getFigur(figureKey);

        if (figur == null)
        {
            throw new NullPointerException();
        }

        NPC_Sprite sprite = new RPGM2K_NPC_Sprite(figur);

        this.figureKey = figureKey;

        this.figure = new NpcFigure(sprite, stepDelay, stepLenght);
        this.startX = startX;
        this.startY = startY;

        this.figure.setPosition(startX, startY);
    }

    @Override
    public void update()
    {
        this.timeSinceLastGotText++;
        this.movingTicksLeft --;
        
        if(this.movingTicksLeft <= 0)
            this.decideNextMovement();
        
        this.figure.update();
        
        if(!this.figure.isMooving())
            this.decideNextMovement();
        
    }

    @Override
    public void draw(Graphics2D g, int cameraOffsetX, int cameraOffsetY)
    {
        figure.draw(g, cameraOffsetX, cameraOffsetY);
    }

    public static NpcVillager create(String line, String file)
    {
        if (line == null || !line.startsWith("NPC"))
        {
            MyLogger.debug("Wrong usage of create NPC. line[" + line + "] in file: " + file);
            return null;        // NPC is just clutter. if we can't make one then we skip it. 
        }

        String[] tokens = line.split(";");

        if (tokens.length < 4)
        {
            MyLogger.debug("Wrong number of tokens. line[" + line + "] in file: " + file);
            return null;
        }

        //----- Figure ------------------------------------------------------------
        String figurKey = tokens[1].trim();

        int startX, startY, stepDelay, stepLength;
        String[] coords = tokens[2].split(",");

        if (coords.length < 2)
        {
            MyLogger.debug("Not enough coordinates. line[" + line + "] in file: " + file);
            return null;
        }

        try
        {
            startX = Integer.parseInt(coords[0].trim());
            startY = Integer.parseInt(coords[1].trim());
        }
        catch (NumberFormatException e)
        {
            MyLogger.debug("Coordinates are corrupted. line[" + line + "] in file: " + file);
            return null;
        }

        String[] stepData = tokens[3].split(",");

        if (stepData.length < 2)
        {
            MyLogger.debug("Not enough data about steps. line[" + line + "] in file: " + file);
            return null;
        }

        try
        {
            stepDelay = Integer.parseInt(stepData[0]);
            stepLength = Integer.parseInt(stepData[1]);
        }
        catch (NumberFormatException e)
        {
            MyLogger.debug("Step data corrupted. line[" + line + "] in file: " + file);
            return null;
        }

        NpcVillager npc;

        try
        {
            npc = new NpcVillager(figurKey, startX, startY, stepDelay, stepLength);
        }
        catch (NullPointerException e)
        {
            MyLogger.debug("Figur not found. line[" + line + "] in file: " + file);
            return null;
        }
        catch (Exception e)
        {
            MyLogger.debug("NPC Creation failed. line[" + line + "] in file: " + file + " Caused by " + e);
            return null;
        }
        
        if (coords.length >= 6)
        {
            npc.figure.minX = Integer.parseInt(coords[2].trim());
            npc.figure.minY = Integer.parseInt(coords[3].trim());
            npc.figure.maxX = Integer.parseInt(coords[4].trim());
            npc.figure.maxY = Integer.parseInt(coords[5].trim());
        }

        // TODO text add here
        if (tokens.length >= 5)
        {
            String[] textTokens = tokens[4].split(",");

            for (int i = 0; i < textTokens.length; i++)
            {
                String textToken = textTokens[i];

                String[] textParts = textToken.split(":");

                if (textParts.length < 2)
                {
                    MyLogger.debug("Text for NPC is corrupted. line: " + line + " file: " + file + " text number: " + i);
                    continue;
                }

                String key = textParts[0].trim();

                if (GameLocale.getString(key).equals(key))
                {
                    MyLogger.debug("Unknown key for NPC text.. line: " + line + " file: " + file + " text number: " + i);
                    continue;
                }

                double chance = 0;

                try
                {
                    chance = Double.parseDouble(textParts[1].trim());
                }
                catch (NumberFormatException e)
                {
                    MyLogger.debug("Chance data for NPC text is corrupted. line: " + line + " file: " + file + " text number: " + i);
                    continue;
                }

                npc.addText(key, chance);

            }
        }

        return npc;
    }

    @Override
    public String toString()
    {
        return "NPC " + figureKey + " (" + startX + "," + startY + ")";
    }

    @Override
    public int getPicksX()
    {
        return this.figure.getPicksX();
    }

    @Override
    public int getPicksY()
    {
        return this.figure.getPicksY();
    }

    private void addText(String key, double chance)
    {
        this.textRandomiser.add(new RandomizerWrapper<>(TextMessage.createNpcText(key), chance));
    }

    public TextMessage getTextMessage()
    {
        
        if (this.text == null || this.timeSinceLastGotText > GameConstants.TIME_TO_HOLD_ON_TEXT)
        {
            RandomizerWrapper<TextMessage> tr = this.textRandomiser.get();
            
            if(tr != null)
                this.text = tr.getItem();
        }

        this.timeSinceLastGotText = 0;

        return this.text;
    }

    public boolean isClose(OverlappingDrawable hero)
    {
        // square is good enough.
        return Math.abs(hero.getPicksX() - this.figure.getPicksX()) < GameConstants.TILE_WIDTH
               && Math.abs(hero.getPicksY() - this.figure.getPicksY()) < GameConstants.TILE_HEIGHT;

    }

    private void decideNextMovement()
    {
        
        if(!this.figure.isMovable())
        {
            this.figure.stop();
            this.movingTicksLeft = 10000;
        }
        
        Direction[] directions = Direction.values();
        
        int dirIndex = Utils.generateRandomInt(0, directions.length - 1);
        
        this.figure.going(directions[dirIndex]);
        
        this.movingTicksLeft = Utils.generateRandomInt(
                GameConstants.NPC_MOVEMENT_TICKS_MIN, 
                GameConstants.NPC_MOVEMENT_TICKS_MAX);
        
    }



    class NpcFigure extends HeroFigure
    {
        private int minX = -1;
        private int minY = -1;
        private int maxX = -1;
        private int maxY = -1;

        public NpcFigure(NPC_Sprite sprite, int stepDelay, int stepLength)
        {
            super(sprite, stepDelay, stepLength);
        }

        @Override
        protected Boolean checkPassable(int x, int y)
        {
            if(super.checkPassable(x, y) == false)
                return false;
            
            switch(this.direction)
            {
                case NORTH:
                    
                    if(this.minY >= 0 && y < this.minY)
                        return false;
                    
                    break;
                case EAST:
                    
                    if(this.maxX >= 0 && x > this.maxX)
                        return false;
                    
                    break;
                case SOUTH:
                    
                    if(this.maxY >= 0 && y > this.maxY)
                        return false;
                    
                    break;
                case WEST:
                    
                    if(this.minX >= 0 && x < this.minX)
                        return false;
                    
                    break;
                case NONE:
                    break;
                default:
                    throw new AssertionError(this.direction.name());
                
            }
            
            return true;
        }

        @Override
        protected void stop()
        {
            super.stop();
            this.sprite.step0().lookSouth();
        }

        @Override
        public void going(Direction direction)
        {
            super.going(direction);
            
            if(direction == Direction.NONE)
                this.stop();
        }
        
        
        

    }

}