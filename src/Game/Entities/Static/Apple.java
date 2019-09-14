package Game.Entities.Static;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    public static boolean goodness = true;//**

    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }
    
    public static void setGood(boolean state) {//**
    	goodness = state;
    }
    
    public boolean isGood() {//**
    	return goodness;
    }

}
