package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;
//import java.lang.Math; 

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	private Handler handler;
	public int moveCheck;
	public int xCoord;
	public int yCoord;
    public int moveCounter;
 //   public int currScore;
    public double score; 
    
    public String direction;//is your first name one?
    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght = 1;
        score = 0;
    }

	public void tick() {
		moveCounter++;
		if (moveCounter >= 8) { // this is where I change the SPEED
			checkCollisionAndMove();
			moveCounter = 0; // it was 0 i change it to ID and then I change it to -- 
		}
		//Backtracking
		if (direction != "Down" && handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)) {
			direction = "Up";
		}
		if (direction != "Up" && handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)) {
			direction = "Down";
		}
		if (direction != "Right" && handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)) {
			direction = "Left";
		}
		if (direction != "Left" && handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)) {
			direction = "Right";
		}
		// Add piece to tail with N
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
			lenght++;
			handler.getWorld().body.addLast(new Tail(xCoord, yCoord, handler));
		}
		// Subtract 1 increase the speed
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
			moveCheck--;
		}
		// Adds 1 to moveCheck sum decrease the speed // bregue aqui y arriba
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			moveCheck++;
		}
		//Press the ESC key for the PAUSE menu
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
            State.setState(handler.getGame().pauseState);
		}
	}

	public void checkCollisionAndMove() { //CheckColision and Move,
		// para cada case del movement tienes que incluir 
		// return si el block de alfrente es true pues poner el game over
		handler.getWorld().playerLocation[xCoord][yCoord] = false;
		int x = xCoord;
		int y = yCoord;
		switch (direction) {
		case "Left":
			if (xCoord == 0) { // bregando en esto xCoord==0
				kill();
			} else {
				xCoord--;
			}
			break;
		case "Right":
			if (xCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
				kill();
			} else {
				xCoord++;
			}
			break;
		case "Up":
			if (yCoord == 0) { // bregando en esto yCoord==0
				kill();
			} else {
				yCoord--;
			}
			break;
		case "Down":
			if (yCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
				kill();
			} else {
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord] = true;
		
		if (handler.getWorld().appleLocation[xCoord][yCoord]) {
			Eat();
		//Added the score formula
			this.score += Math.sqrt(2 * score + 1);
		}
		//Collision with itself 
		for(int i=0; i < lenght-1; i++) {
			if (handler.getWorld().body.get(i).x == xCoord
			&& handler.getWorld().body.get(i).y == yCoord) {
				State.setState(handler.getGame().gameoverState);
			}
		}		

		if (!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body
					.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y, handler));
			
			
		}
		
	}

	public void render(Graphics g, Boolean[][] playerLocation) {
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(Color.GREEN);
				if(playerLocation[i][j]||handler.getWorld().appleLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                   
                    //DRAW THE SCORE
                    
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("arial", Font.CENTER_BASELINE, 20));
                    g.drawString("Score: "+score, 60, 30);
                }

			}
		}
	}

	public void Eat() {
		lenght++;
		Tail tail = null;
		handler.getWorld().appleLocation[xCoord][yCoord] = false;
		handler.getWorld().appleOnBoard = false;
		switch (direction) {
		case "Left":
			if (handler.getWorld().body.isEmpty()) {
				if (this.xCoord != handler.getWorld().GridWidthHeightPixelCount--) {
					tail = new Tail(this.xCoord + 1, this.yCoord, handler);

				} else {
					if (this.yCoord != 0) {
						tail = new Tail(this.xCoord, this.yCoord - 1, handler);
						// moveCounter--;
					} else {
						tail = new Tail(this.xCoord, this.yCoord + 1, handler);
						// moveCounter--;
					}
				}
			} else {
				if (handler.getWorld().body.getLast().x != handler.getWorld().GridWidthHeightPixelCount - 1) {
					tail = new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler);
					// moveCounter--;
				} else {
					if (handler.getWorld().body.getLast().y != 0) {
						tail = new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler);
						// moveCounter--;
					} else {
						tail = new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler);
					

					}
				}

			}
			break;
		case "Right":
			if (handler.getWorld().body.isEmpty()) {
				if (this.xCoord != 0) {
					tail = new Tail(this.xCoord - 1, this.yCoord, handler);
					// moveCounter--;
				} else {
					if (this.yCoord != 0) {
						tail = new Tail(this.xCoord, this.yCoord - 1, handler);
						// moveCounter--;
					} else {
						tail = new Tail(this.xCoord, this.yCoord + 1, handler);
						// moveCounter--;
					}
				}
			} else {
				if (handler.getWorld().body.getLast().x != 0) {
					tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
					// moveCounter--;
				} else {
					if (handler.getWorld().body.getLast().y != 0) {
						tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler));
						// moveCounter--;
					} else {
						tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler));
						// moveCounter--;
					}
				}

			}
			break;
		case "Up":
			if (handler.getWorld().body.isEmpty()) {
				if (this.yCoord != handler.getWorld().GridWidthHeightPixelCount - 1) {
					tail = (new Tail(this.xCoord, this.yCoord + 1, handler));
					// moveCounter--;
				} else {
					if (this.xCoord != 0) {
						tail = (new Tail(this.xCoord - 1, this.yCoord, handler));
						// moveCounter--;
					} else {
						tail = (new Tail(this.xCoord + 1, this.yCoord, handler));
						// moveCounter--;
					}
				}
			} else {
				if (handler.getWorld().body.getLast().y != handler.getWorld().GridWidthHeightPixelCount - 1) {
					tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler));
					// moveCounter--;
				} else {
					if (handler.getWorld().body.getLast().x != 0) {
						tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
						// moveCounter--;
					} else {
						tail = (new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler));
						// moveCounter--;
					}
				}

			}
			break;
		case "Down":
			if (handler.getWorld().body.isEmpty()) {
				if (this.yCoord != 0) {
					tail = (new Tail(this.xCoord, this.yCoord - 1, handler));
					// moveCounter--;
				} else {
					if (this.xCoord != 0) {
						tail = (new Tail(this.xCoord - 1, this.yCoord, handler));
						// moveCounter--;
					} else {
						tail = (new Tail(this.xCoord + 1, this.yCoord, handler));
						// moveCounter--;
					} // System.out.println("Tu biscochito");
				}
			} else {
				if (handler.getWorld().body.getLast().y != 0) {
					tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler));
					// moveCounter--;
				} else {
					if (handler.getWorld().body.getLast().x != 0) {
						tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
						// moveCounter--;
					} else {
						tail = (new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler));
						// moveCounter--;
					}
				}

			}
			break;
		}
		handler.getWorld().body.addLast(tail);
		moveCounter--;
		handler.getWorld().playerLocation[tail.x][tail.y] = true;
	}

	public void kill() {
		lenght = 0;
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j] = false;

			}
		}
		State.setState(handler.getGame().gameoverState);
	}

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
