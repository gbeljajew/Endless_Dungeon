/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.util.drawable;

/**
 *
 * @author gbeljajew
 */
public interface RepeatableAnimation extends ResetableAnimation
{
    public void setRepeats(int repeats);
    
    public void setRepeater(RepeatingType repeatingType);
    
    public enum RepeatingType
    {
        /**
         * after counter hits last image it goes to first
         */
        CICLING {
            @Override
            public Repeater getRepeater( int numberImages )
            {
                return new Repeater(numberImages )
                {
                    int step = 0;
                    int repeats = 0;
                    
                    @Override
                    public int next()
                    {
                        step++;
                        
                        if(step >= this.numberImages)
                        {
                            step = 0;
                            repeats++;
                        }
                        
                        return step;
                    }
                    
                    @Override
                    public int getRepeats()
                    {
                        return repeats;
                    }

                    @Override
                    public void reset()
                    {
                        this.repeats = 0;
                        this.step = 0;
                    }
                };
            }
        }, 
        /**
         * after counter hits last image it will go backwards
         */
        FORT_AND_BACK {
            @Override
            public Repeater getRepeater( int numberImages )
            {
                return new Repeater(numberImages )
                {
                    int step = 0, repeats = 0, direction = 1;
                    
                    
                    @Override
                    public int next()
                    {
                        step += direction;
                        
                        if(step >= numberImages - 1)
                        {
                            direction = -1;
                        }
                        
                        if(step <= 0)
                        {
                            direction = 1;
                            repeats ++;
                        }
                        
                        return step;
                    }
                    
                    @Override
                    public int getRepeats()
                    {
                        return repeats;
                    }
                    
                    @Override
                    public void reset()
                    {
                        step = 0;
                        repeats = 0;
                        direction = 1;
                    }
                };
            }
        },
        
        NO_REPEATING
        {
             @Override
            public Repeater getRepeater( int numberImages )
            {
                return new Repeater(numberImages )
                {
                    int step = 0;
                    boolean done = false;
                    
                    @Override
                    public int next()
                    {
                        step ++;
                        
                        if(this.step > this.numberImages - 1)
                        {
                            this.step = this.numberImages - 1;
                            this.done = true;
                        }
                        
                        return step;
                    }
                    
                    @Override
                    public int getRepeats()
                    {
                        if(this.done == true)
                            return Integer.MAX_VALUE;
                        else
                            return 0;
                    }
                    
                    @Override
                    public void reset()
                    {
                        step = 0;
                        done = false;
                    }
                };
            }
        },
        
        ENDLESS_CICLING
        
        {
            @Override
            public Repeater getRepeater(int numberImages)
            {
                return new Repeater(numberImages)
                {
                    private int step = 0;

                    @Override
                    public void reset()
                    {
                        step  = 0;
                        
                    }
                    
                    @Override
                    public int next()
                    {
                        step++;
                        
                        if(step >= this.numberImages)
                        {
                            step = 0;
                        }
                        
                        return step;
                    }
                    
                    @Override
                    public int getRepeats()
                    {
                        return 0;
                    }
                };
            }
        },        
        
        ENDLESS_FORT_AND_BACK {
            @Override
            public Repeater getRepeater(int numberImages)
            {
                
                
                return new Repeater(numberImages)
                {
                    int step = 0, direction = 1;

                    @Override
                    public int next()
                    {
                        step += direction;
                        
                        if(step >= numberImages - 1)
                        {
                            direction = -1;
                        }
                        
                        if(step <= 0)
                        {
                            direction = 1;
                        }
                        
                        return step;
                    }

                    @Override
                    public int getRepeats()
                    {
                        return 0;
                    }

                    @Override
                    public void reset()
                    {
                        step = 0;
                        direction = 1;
                        
                    }
                    
                };
            }
        }
        
        
        
        ;
        public abstract Repeater getRepeater(int numberImages);
        
        
        public abstract class Repeater
        {
            protected final int numberImages;

            public Repeater( int numberImages )
            {
                this.numberImages = numberImages;
            }
            
            public abstract int next();
            public abstract int getRepeats();
            public abstract void reset();
        }
    }
}
