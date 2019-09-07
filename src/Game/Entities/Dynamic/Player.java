package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
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
	public int currScore = 0;
	// public static double score=0;

	public String direction;// is your first name one?

	public Player(Handler handler) {
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction = "Right";
		justAte = false;
		lenght = 2;
		// score = Math.sqrt(2*myScore+1);
	}

	public void tick() {
		moveCounter++;
		if (moveCounter >= 6) { // this is where I change the SPEED
			checkCollisionAndMove();
			moveCounter = 0; // it was 0 i change it to 1
		}

		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)) {
			direction = "Up";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)) {
			direction = "Down";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)) {
			direction = "Left";
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)) {
			direction = "Right";
		}
		// Add piece to tail with N
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
			lenght++;
			handler.getWorld().body.addLast(new Tail(xCoord, yCoord, handler));
		}
		// Subtract 1
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
			moveCheck--;
		}
		// Adds 1 to moveCheck
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			moveCheck++;
		}

	}

	public void checkCollisionAndMove() {
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
			currScore++;
		}

		if (!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body
					.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y, handler));
		}

	}

	public void render(Graphics g, Boolean[][] playeLocation) {
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(Color.GREEN);

				if (playeLocation[i][j] || handler.getWorld().appleLocation[i][j]) {
					g.fillRect((i * handler.getWorld().GridPixelsize), (j * handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize, handler.getWorld().GridPixelsize);
				}

			}
		}
		// draw the score
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.CENTER_BASELINE, 20));
		g.drawString("Score: " + Math.sqrt(2 * currScore + 1), 60, 30);

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
						// moveCounter--;

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
	}

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
