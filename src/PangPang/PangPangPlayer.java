package PangPang;

import Utility.Math_Vector;

public class PangPangPlayer {

	public static final int LEFT = 1;

	public static final int RIGHT = 0;

	public static final int UP = 2;

	public static final float GIRL_MOVE_VELOCITY = 40;
	public static final float GIRL_WIDTH = 30f;
	public static final float GIRL_HEIGHT = 30f;

	private int life;
	private boolean is_Dead;
	private boolean is_Un_Dead;
	private int Un_Dead_Cult;

	private int nDirection;

	private Math_Vector velocity;
	private Math_Vector position;

	private String sName;

	public PangPangPlayer(String playerId, double x, double y, int life) {

		this.velocity = new Math_Vector(0f, 0f);
		this.position = new Math_Vector(x, y);
		this.sName = playerId;
		this.life = life;
		this.nDirection = UP;
		reset();
	}

	public void reset() {
		is_Dead = false;
		Un_Dead_Cult = 500;
		is_Un_Dead = true;

	}

	public void update(double deltaTime) {

		if (is_Un_Dead) {
			Un_Dead_Cult--;

			if (Un_Dead_Cult < 0) {
				is_Un_Dead = false;
			}
		}

		if (getnDirection() == RIGHT)
			velocity.set(8, 0);
		else if (getnDirection() == LEFT)
			velocity.set(-8, 0);
		else if (getnDirection() == UP)
			velocity.set(0, 0);

		position.add(velocity.x * deltaTime, velocity.y * deltaTime);

	}

	public int getnDirection() {
		return nDirection;
	}

	public Math_Vector getPosition() {
		return position;
	}

	public void setnDirection(int nDirection) {
		this.nDirection = nDirection;
	}

	public boolean get_Is_Dead() {
		return this.is_Dead;
	}

	public int get_Int_Life() {
		return this.life;
	}

	public boolean get_Is_Un_Dead() {
		return this.is_Un_Dead;
	}

	public void set_Is_Un_Dead(boolean input) {
		this.is_Un_Dead = input;
	}

	public void set_Un_dead_CulTime(int cul_Time) {
		this.Un_Dead_Cult = cul_Time;
	}

	public void set_Int_Life(int a) {
		this.life = a;
	}

	public void set_Int_Life_Increase() {
		this.life += 1;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

}
