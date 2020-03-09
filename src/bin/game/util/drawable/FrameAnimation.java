/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.game.util.drawable;

import bin.game.GameConstants;
import bin.game.resources.Animations;
import bin.game.resources.ResourcesContainer;
import bin.game.util.logger.MyLogger;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author gbeljajew
 */
public class FrameAnimation implements ResetableAnimation, RepeatableAnimation 
{
    private int x, y;
    
    private int count = 0, step = 0;
    
    private final Image[] images;
    private final Frame[] frames;
    private int repeats = 1;
    private RepeatingType.Repeater repeater;
    private int delay = 5;

    private FrameAnimation( Image[] images, Frame[] frames )
    {
        
        int minIndex = frames[0].index;
        int maxIndex = frames[0].index;

        for (Frame frame : frames)
        {
            if (frame.index < minIndex)
            {
                minIndex = frame.index;
            }

            if (frame.index > maxIndex)
            {
                maxIndex = frame.index;
            }
        }

        if (minIndex < 0 || maxIndex >= images.length)
        {
            throw new ArrayIndexOutOfBoundsException("indexses point outside image arrays: "
                    + "minIdex= " + minIndex + " maxIndex= " + maxIndex);
        }

        this.images = images;
        this.frames = frames;
        this.repeater = RepeatingType.NO_REPEATING.getRepeater( this.frames.length);
    }

    private FrameAnimation( Image[] images, Frame[] frames, RepeatingType.Repeater repeater, int repeats, int delay )
    {
        this(images, frames);
        
        this.repeats = repeats;
        this.repeater = repeater;
        this.delay = delay;
    }
    
    

    @Override
    public void reset()
    {
        count = 0;
        step = 0;
        repeater.reset();
    }

    @Override
    public void setPosition( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public ResetableAnimation cloneYourself()
    {
        return new FrameAnimation(images, frames, repeater, repeats, delay );
    }

    @Override
    public void draw( Graphics2D g, int kameraX, int kameraY )
    {
        this.count++;
        
        if(this.count % this.delay == 0)
            this.step = this.repeater.next();
        
        Frame frame = this.frames[this.step];
        
        Image image = this.images[frame.index];
        
        int imgDX = image.getWidth( null) / 2 * -1;
        int imgDY = image.getHeight( null) / 2 * -1;
        
        int px = imgDX + frame.dx + kameraX + this.x;
        int py = imgDY + frame.dy + kameraY + this.y;
        
        g.drawImage( image, px, py, null);
    }

    @Override
    public boolean isDone()
    {
        return this.repeater.getRepeats() >= this.repeats;
    }

    @Override
    public void setRepeats( int repeats )
    {
        this.repeats = repeats;
    }

    @Override
    public void setRepeater( RepeatingType repeatingType )
    {
        this.repeater = repeatingType.getRepeater( this.frames.length);
    }
    
    /**
     * takes a Node and creates an FrameAnimation with it
     * @param node
     * @return
     * @throws Exception if something is not O.K
     */
    @SuppressWarnings("ManualArrayToCollectionCopy")
    public static FrameAnimation loadAnimation(Node node) throws Exception
    {
        
        if(!node.getNodeName().equals("animation"))
            throw new IllegalArgumentException("wrong node: " + node.getNodeName());
        
        Element el = (Element)node;
        
        String key = el.getAttribute("key");
        
        String delayS = el.getAttribute("delay");
        String repeatsS = el.getAttribute("repeats");
        
        int delay = GameConstants.STANDARD_ANIMATION_DELAY;
        
        if(!delayS.isEmpty())
            delay = Integer.parseInt(delayS);
        
        int repeats = 1;
        
        if(!repeatsS.isEmpty())
            repeats = Integer.parseInt(repeatsS);
        
        RepeatingType repeatingType = RepeatingType.NO_REPEATING;
        
        String repeatingtype = el.getAttribute("repeattype");
        
        if(!repeatingtype.isEmpty())
            repeatingType = RepeatingType.valueOf(repeatingtype);
        
        
        List<Image> images = new ArrayList<>();
        NodeList imageNodes = el.getElementsByTagName("image");
        for(int i = 0; i < imageNodes.getLength(); i++)
        {
            Node item = imageNodes.item(i);
            Element e = (Element)item;
            
            String imgKey = e.getAttribute("key");
            String imgType = e.getAttribute("type");
            
            switch(imgType)
            {
                case "ANIMATION":
                    for (Image ma : ResourcesContainer.getMiskAnimationSprites(imgKey))
                    {
                        images.add(ma);
                    }
                    break;
                    
                case "TILESET":
                    for (Image ma : ResourcesContainer.getMiskTileSet(imgKey))
                    {
                        images.add(ma);
                    }
                    break;
                    
                case "IMAGE":
                    images.add(ResourcesContainer.getMiskImage(imgKey));
                    break;
                default:
                    MyLogger.error("FrameAnimation.loadAnimation: unknown image type: " + imgType);
            }
            
            
        }
        
        List<Frame> frames = new ArrayList<>();
        
        NodeList frameNodes = el.getElementsByTagName("frame");
        for(int i = 0; i < frameNodes.getLength(); i++)
        {
            Node item = frameNodes.item(i);
            Element e = (Element)item;
            
            int index = Integer.parseInt(e.getAttribute("index"));
            int dx = Integer.parseInt(e.getAttribute("dx"));
            int dy = Integer.parseInt(e.getAttribute("dy"));
            
            frames.add(new Frame(index, dx, dy));
            
        }
        
        FrameAnimation res = new FrameAnimation(
                images.toArray(new Image[0]), 
                frames.toArray(new Frame[0]), 
                repeatingType.getRepeater(frames.size()), 
                repeats, 
                delay);
        
        Animations.putAnimation(key, res);
        
        return res;
        
    }
    
    private static class Frame
    {
        public final int index, dx, dy;

        public Frame( int index, int dx, int dy )
        {
            this.index = index;
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public String toString()
        {
            return "Frame{" + "index=" + index + ", dx=" + dx + ", dy=" + dy + '}';
        }
        
        
    }
    
    public interface FrameAnimationBuilder
    {
        public FrameAnimationBuilder addImage(Image image);
        public default FrameAnimationBuilder addImage(Image[] images)
        {
            for( Image image : images )
            {
                this.addImage( image );
            }
            return this;
        }
        public default FrameAnimationBuilder addImage(Iterable<? extends Image> images)
        {
            for( Image image : images )
            {
                this.addImage( image );
            }
            return this;
        }
        
        public FrameAnimationBuilder addFrame(int index, int dx, int dy);
        
        public FrameAnimationBuilder setRepeater( RepeatingType repeatingType );
        
        public FrameAnimationBuilder setNumberOfRepeats(int repeats);
        
        public FrameAnimationBuilder setDelay(int delay);
        
        /**
         * 
         * @return
         * @throws ArrayStoreException if there are no images or frames
         * @throws ArrayIndexOutOfBoundsException if any index points outside of images array
         */
        public FrameAnimation create() throws ArrayStoreException, ArrayIndexOutOfBoundsException;
    }
    
    public static FrameAnimationBuilder getBuilder()
    {
        return new FrameAnimationBuilder()
        {
            List<Image> imageList = new ArrayList<>();
            List<Frame> frameList = new ArrayList<>();
            private RepeatingType repeatingType = RepeatingType.NO_REPEATING;
            private int repeats = 1;
            private int delay = 5;
            
            @Override
            public FrameAnimationBuilder addImage( Image image )
            {
                this.imageList.add( image );
                return this;
            }

            @Override
            public FrameAnimationBuilder addFrame( int index, int dx, int dy )
            {
                this.frameList.add( new Frame(index, dx, dy));
                return this;
                
            }

            @Override
            public FrameAnimationBuilder setRepeater( RepeatingType repeatingType )
            {
                this.repeatingType = repeatingType;
                return this;
            }

            @Override
            public FrameAnimationBuilder setNumberOfRepeats( int repeats )
            {
                this.repeats = repeats;
                return this;
            }

            @Override
            public FrameAnimationBuilder setDelay( int delay )
            {
                this.delay = delay;
                return this;
            }

            @Override
            public FrameAnimation create() throws ArrayStoreException, ArrayIndexOutOfBoundsException
            {
                if(this.frameList.isEmpty() || this.imageList.isEmpty())
                    throw new ArrayStoreException("new created animation is empty");
                
                Frame f = this.frameList.get( 0);
                
                int minIndex = f.index;
                int maxIndex = f.index;
                
                for( Frame frame : this.frameList)
                {
                    if(frame.index < minIndex)
                        minIndex = frame.index;
                    
                    if(frame.index > maxIndex)
                        maxIndex = frame.index;
                }
                
                if(minIndex < 0 || maxIndex >= imageList.size())
                    throw new ArrayIndexOutOfBoundsException("indexses point outside image arrays: "
                            + "minIdex= " + minIndex + " maxIndex= " + maxIndex);
                
                
                FrameAnimation frameAnimation = new FrameAnimation(imageList.toArray( new Image[0]), 
                        frameList.toArray( new Frame[0]),
                        repeatingType.getRepeater( frameList.size()),
                        repeats, 
                        delay);
                
                return frameAnimation;
            }
        };
    }
}
