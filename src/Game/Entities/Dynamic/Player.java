package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.Entities.Static.Apple;
import Game.GameStates.State;
//import java.lang.Math; 

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	private Handler handler;
	public int speed;
	public int xCoord;
	public int yCoord;
	public int moveCounter;
	public double score;
	public int steps;

	public String direction;// is your first name one?

	public Player(Handler handler) {
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction = "Right";
		justAte = false;
		lenght = 1;
		score = 0;
	}

	public void tick() {
		moveCounter++;
		// Speed

		if (moveCounter > 10) { // this is where I change the SPEED
			checkCollisionAndMove();
			moveCounter = 0; // it was 0 i change it to ID and then I change it to --
		}
		if (score > 1) {
			moveCounter += 843165700 + 1; // LAST 4 ID DIGITS 5700
		}
		// Backtracking
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
			moveCounter--;
		}
		// Adds 1 to moveCheck sum decrease the speed // bregue aqui y arriba
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			moveCounter++;
		}
		// Press the ESC key for the PAUSE menu
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			State.setState(handler.getGame().pauseState);
		}
	}

	public void checkCollisionAndMove() {
		handler.getWorld().playerLocation[xCoord][yCoord] = false;
		int x = xCoord;
		int y = yCoord;
		switch (direction) {
		// Teleport
		case "Left":
			if (xCoord == 0) {
				xCoord = handler.getWorld().GridWidthHeightPixelCount - 1;
			} else {
				xCoord--;
			}
			steps++;

			break;
		case "Right":
			if (xCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
				xCoord = 0;
			} else {
				xCoord++;
			}
			steps++;

			break;
		case "Up":
			if (yCoord == 0) {
				yCoord = handler.getWorld().GridWidthHeightPixelCount - 1;
			} else {
				yCoord--;
			}
			steps++;

			break;
		case "Down":
			if (yCoord == handler.getWorld().GridWidthHeightPixelCount - 1) {
				yCoord = 0;
			} else {
				yCoord++;
			}
			steps++;

			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord] = true;

		if (handler.getWorld().appleLocation[xCoord][yCoord]) {
			Eat();
			// Added the score formula
			this.score += Math.sqrt(2 * score + 1);
		}
		// Collision with itself
		for (int i = 0; i < lenght - 1; i++) {
			if (handler.getWorld().body.get(i).x == xCoord && handler.getWorld().body.get(i).y == yCoord) {
				kill();
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

				if (playerLocation[i][j]) {
					g.setColor(Color.GREEN);
					g.fillRect((i * handler.getWorld().GridPixelsize), (j * handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);
				}

				if (steps == 300) {
					Apple.setGood(false);
				}
				if (Apple.isGood()) {
					g.setColor(Color.red);

				} else {
					g.setColor(Color.BLACK);
				}

				if (handler.getWorld().appleLocation[i][j]) {
					g.fillRect((i * handler.getWorld().GridPixelsize), (j * handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);

					// Draw the score
					g.setColor(Color.WHITE);
					g.setFont(new Font("arial", Font.CENTER_BASELINE, 20));
					g.drawString("Score: " + score, 60, 60);
				}

			}
		}
	}

	public void Eat() {
		Apple.setGood(true);//**
		steps = 0;//**
		
		this.handler.getWorld().getApple();
		if (Apple.isGood()) {

			this.lenght++;

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
						} else {
							tail = new Tail(this.xCoord, this.yCoord + 1, handler);
						}
					}
				} else {
					if (handler.getWorld().body.getLast().x != handler.getWorld().GridWidthHeightPixelCount - 1) {
						tail = new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler);
					} else {
						if (handler.getWorld().body.getLast().y != 0) {
							tail = new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler);
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
					} else {
						if (this.yCoord != 0) {
							tail = new Tail(this.xCoord, this.yCoord - 1, handler);
						} else {
							tail = new Tail(this.xCoord, this.yCoord + 1, handler);
						}
					}
				} else {
					if (handler.getWorld().body.getLast().x != 0) {
						tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
					} else {
						if (handler.getWorld().body.getLast().y != 0) {
							tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler));
						} else {
							tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler));
						}
					}
				}
				break;
			case "Up":
				if (handler.getWorld().body.isEmpty()) {
					if (this.yCoord != handler.getWorld().GridWidthHeightPixelCount - 1) {
						tail = (new Tail(this.xCoord, this.yCoord + 1, handler));
					} else {
						if (this.xCoord != 0) {
							tail = (new Tail(this.xCoord - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(this.xCoord + 1, this.yCoord, handler));
						}
					}
				} else {
					if (handler.getWorld().body.getLast().y != handler.getWorld().GridWidthHeightPixelCount - 1) {
						tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord + 1, handler));
					} else {
						if (handler.getWorld().body.getLast().x != 0) {
							tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler));
						}
					}

				}
				break;
			case "Down":
				if (handler.getWorld().body.isEmpty()) {
					if (this.yCoord != 0) {
						tail = (new Tail(this.xCoord, this.yCoord - 1, handler));
					} else {
						if (this.xCoord != 0) {
							tail = (new Tail(this.xCoord - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(this.xCoord + 1, this.yCoord, handler));
						} // System.out.println("Baby quien tu eres? Tu biscochito");
					}
				} else {
					if (handler.getWorld().body.getLast().y != 0) {
						tail = (new Tail(handler.getWorld().body.getLast().x, this.yCoord - 1, handler));
					} else {
						if (handler.getWorld().body.getLast().x != 0) {
							tail = (new Tail(handler.getWorld().body.getLast().x - 1, this.yCoord, handler));
						} else {
							tail = (new Tail(handler.getWorld().body.getLast().x + 1, this.yCoord, handler));
						}
					}
				}
				break;
			}
			handler.getWorld().body.addLast(tail);
			handler.getWorld().playerLocation[tail.x][tail.y] = true;
			if (score > 0) {
				this.moveCounter = moveCounter + 1;
			}
		} // ***
		else {// ***
			
			if (lenght > 1) {
				lenght--; // **
				score = Math.sqrt((2 * score) + 1);
				handler.getWorld().body.removeLast();// **
				handler.getWorld().appleLocation[xCoord][yCoord] = false; // ***
				handler.getWorld().appleOnBoard = false; // **
				if(score < 0) {
					score = 0;
				
				}

			} else
				this.kill();// **

		}

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

