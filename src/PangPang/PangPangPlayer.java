package PangPang;

import Utility.Math_Vector;

/**
 * @author KJW finish at 2016/ 08/ 12
 * @version 2.0.0v
 * @description this class for the PangPang, this class manage the pangapng
 *              PangPangPlayer
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class PangPangPlayer {

	/**
	 * Unit left static number
	 */
	public static final int LEFT = 1;

	/**
	 * Unit right static number
	 */
	public static final int RIGHT = 0;

	/**
	 * Unit up static number
	 */
	public static final int UP = 2;

	private int life;
	private boolean is_Dead;
	private boolean is_Un_Dead;
	private int Un_Dead_Cult;
	private int nDirection;
	private Math_Vector velocity;
	private Math_Vector position;
	private String sName;

	/**
	 * PangPangPlayer constructor
	 * 
	 * @param playerId
	 *            - player unique string
	 * @param x
	 *            - start position
	 * @param y
	 *            - start position <- don modify
	 * @param life
	 *            - unit life number
	 */
	public PangPangPlayer(String playerId, double x, double y, int life) {

		this.velocity = new Math_Vector(0f, 0f);
		this.position = new Math_Vector(x, y);
		this.sName = playerId;
		this.life = life;
		this.nDirection = UP;
		reset();
	}

	/**
	 * reset unit information
	 */
	public void reset() {
		is_Dead = false;
		Un_Dead_Cult = 500;
		is_Un_Dead = true;

	}

	/**
	 * unit update
	 * 
	 * @param deltaTime
	 */
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

	/**
	 * get unit direction
	 * 
	 * @return
	 */
	public int getnDirection() {
		return nDirection;
	}

	/**
	 * get unit position
	 * 
	 * @return
	 */
	public Math_Vector getPosition() {
		return position;
	}

	/**
	 * set unit direction
	 * 
	 * @param nDirection
	 */
	public void setnDirection(int nDirection) {
		this.nDirection = nDirection;
	}

	/**
	 * get unit dead statues
	 * 
	 * @return
	 */
	public boolean get_Is_Dead() {
		return this.is_Dead;
	}

	/**
	 * get unit life
	 * 
	 * @return
	 */
	public int get_Int_Life() {
		return this.life;
	}

	/**
	 * get unit is_un_dead mode
	 * 
	 * @return
	 */
	public boolean get_Is_Un_Dead() {
		return this.is_Un_Dead;
	}

	/**
	 * set unit is_un_dead mode
	 * 
	 * @param input
	 */
	public void set_Is_Un_Dead(boolean input) {
		this.is_Un_Dead = input;
	}

	/**
	 * set un_dead_mode cultime
	 * 
	 * @param cul_Time
	 */
	public void set_Un_dead_CulTime(int cul_Time) {
		this.Un_Dead_Cult = cul_Time;
	}

	/**
	 * set unit init life number
	 * 
	 * @param a
	 */
	public void set_Int_Life(int a) {
		this.life = a;
	}

	/**
	 * set unit life increase
	 */
	public void set_Int_Life_Increase() {
		this.life += 1;
	}

	/**
	 * get unit unique Name
	 * 
	 * @return
	 */
	public String getsName() {
		return sName;
	}

	/**
	 * set unit unique Name
	 * 
	 * @param sName
	 */
	public void setsName(String sName) {
		this.sName = sName;
	}

}
