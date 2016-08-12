package PangPang;

import java.text.DecimalFormat;
import java.util.Random;

import ServerMainBody.MainProtocolProcesser.GameRoom;
import ServerMainBody.Settings;
import Utility.Math_Vector;

/**
 * @author KJW finish at 2016/ 08/ 12
 * @version 2.0.0v
 * @description this class for the PangPang, this class manage the pangapng
 *              enemy information
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class PangPangEnemy {

	private Map_Controler map_controler;
	private Math_Vector velocity;
	private Math_Vector position;

	final static int ENTER = 1; // enemy enter state
	final static int BEGINPOS = 2; // enemy return position state
	final static int POSITION = 3; // enemy position state
	final static int SYNC = 4; // enemy sync state
	final static int ATTACK = 5; // enemy attack state
	final static int BEGINBACK = 6; // enemy begin attack state
	final static int BACKPOS = 7; // enemy back postion state

	final int loaf = 50; // max gap

	public static int Enemy_Missile_ID = 0;

	private boolean isDead;
	private int shield;
	private int status;

	private String sUnitName;

	private int sncX;
	private SinglePath sPath;
	private float sx, sy;
	private int a_Kind;
	private int enemy_Img_Number;
	private int sKind, sNum;
	private int pNum, col;
	private int delay, dir, len;
	private int posX, posY; //
	private Random rnd = new Random();
	private GameRoom mnt;

	/**
	 * pangapng enemy constructor
	 * 
	 * @param mnt
	 *            - map controller
	 * @param paraMap_controler
	 */
	public PangPangEnemy(GameRoom mnt, Map_Controler paraMap_controler) {

		this.mnt = mnt;
		this.map_controler = paraMap_controler;
		this.velocity = new Math_Vector();
		this.position = new Math_Vector();
		// ���� ����
	}

	/**
	 * enemy init
	 * 
	 * @param kind
	 * @param num
	 * @param nName
	 */
	public void MakeEnemy(int kind, int num, int nName) {
		sKind = kind;
		sNum = num;
		sUnitName = "bubble" + nName;

		// ���ʿ��� ĳ����
		if (this.map_controler.get_Selection(kind, num) == -1) {
			isDead = true;
			return;
		}
		enemy_Img_Number = this.map_controler.get_Enemy_Num(sKind, num);// ��
																		// ��ȣ

		ResetEnemy();
	}

	/**
	 * reset enemy state
	 */
	public void ResetEnemy() {

		velocity.set(new Math_Vector(0, 0));
		pNum = this.map_controler.get_Selection(sKind, sNum);

		delay = this.map_controler.get_Delay(sKind, sNum);
		shield = this.map_controler.get_Enemy_Life(sKind, sNum);

		posX = this.map_controler.get_Pos_X(sKind, sNum);
		posY = this.map_controler.get_Pos_Y(sKind, sNum);

		GetPath(pNum);
		status = ENTER;
		isDead = false;

	}

	/**
	 * update enemy
	 * 
	 * @param deltaTime
	 */
	public void update(double deltaTime) {

		Move();

		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	}

	/**
	 * get enemy path
	 * 
	 * @param num
	 */
	private void GetPath(int num) {
		sPath = this.map_controler.get_Path(num); // Path �б�
		// Path�� ���� ��ǥ
		if (sPath.get_Start_X() != -99)
			position.x = sPath.get_Start_X();
		if (sPath.get_Start_Y() != -99)
			position.y = sPath.get_Start_Y();

		col = 0;
		GetDir(col);
	}

	/**
	 * get enemy direction
	 * 
	 * @param col
	 */
	private void GetDir(int col) {
		dir = sPath.get_Directions()[col]; // �̵��� ����
		len = sPath.get_Lens()[col]; // �̵��� �Ÿ�

		sx = this.map_controler.get_Direc_Dis_X(dir); // �̵� �ӵ�
		sy = this.map_controler.get_Direc_Dis_Y(dir);
	}

	/**
	 * move enemy event
	 */
	private void Move() {
		if (isDead && (sKind != 5 || sNum != 0))
			return;

		switch (status) {
		case ENTER: // ĳ���� ����
			enter_Enemy();
			break;
		case BEGINPOS: // ���� ���� ��ġ ���
			begin_Pos();
			break;
		case POSITION: // ���� ���� ��ġ�� �̵���
			position();
			break;
		case SYNC: // ���� ���� ��ġ���� ��� ��
			make_Sync();
			break;
		case ATTACK: // ����
			attack();
			break;
		case BEGINBACK:
			begin_Back_Pos();
			break;
		case BACKPOS:
			back_Position();
		}
	}

	/**
	 * enemy enter the main scene
	 */
	private void enter_Enemy() {
		if (--delay >= 0)
			return;

		velocity.x = (int) (sx * 180);
		velocity.y = (int) (sy * 180);

		// when unit enter the view that attack main Charecter
		int dr = rnd.nextInt(5) + 6; // 6~11 direction;
		if (len % 500 == 0)
			shoot_Missile(dr);

		len--;
		if (len >= 0)
			return;

		col++;
		if (col < sPath.get_Directions().length) {
			GetDir(col); // ���� ��� ã��
		} else {
			status = BEGINPOS; // ����� ���̸� ���� �������� �̵�
		}
	}

	/**
	 * enemy moving attack position
	 */
	private void begin_Pos() {
		// ������ Path �б�
		if (position.x < posX + this.map_controler.syncCnt) // �̵� ����
															// ����
			dir = 2; // �ϵ�(NW)��
		else
			dir = 14; // �ϼ�(NW)��

		if (position.y < posY)
			dir = (dir == 2) ? 6 : 10;

		sx = this.map_controler.get_Direc_Dis_X(dir); // �̵� ���⿡ ���� �ӵ�
														// ���
		sy = this.map_controler.get_Direc_Dis_Y(dir);
		status = POSITION; // �������� �̵� �غ� ��
	}

	/**
	 * enemy position event
	 */
	private void position() {
		velocity.x = (int) (sx * 160);
		velocity.y = (int) (sy * 160);

		if (position.x < posX + this.map_controler.syncCnt)
			dir = 2;
		else
			dir = 14;

		if (position.y < posY)
			dir = (dir == 2) ? 2 : 14;

		if (Math.abs(position.y - posY) <= 4) {
			position.y = posY;
			velocity.y = 0;
			if (position.x < posX + this.map_controler.syncCnt)
				dir = 4;
			else
				dir = 12;
		}

		if (Math.abs(position.x - (posX + this.map_controler.syncCnt)) <= 4) {
			position.x = posX + this.map_controler.syncCnt;
			velocity.x = 0;
			dir = 0;
		}

		if (position.y == posY && position.x == posX + this.map_controler.syncCnt) {
			dir = 0;
			sx = 1;
			status = SYNC;
			return;
		}

		sx = this.map_controler.get_Direc_Dis_X(dir);
		sy = this.map_controler.get_Direc_Dis_Y(dir);
	}

	/**
	 * synk moving
	 */
	private void make_Sync() {
		sncX = (int) this.map_controler.get_Direc_Dis_X(this.map_controler.dir);
		position.x += sncX;

		if (sKind == 5 && sNum == 0) {
			this.map_controler.syncCnt += sncX;
			this.map_controler.dirCnt++;
			if (this.map_controler.dirCnt >= this.map_controler.dirLen) {
				this.map_controler.dirCnt = 0;
				this.map_controler.dirLen = 150;
				this.map_controler.dir = 16 - this.map_controler.dir;
			}
		}
	}

	/**
	 * begin attack enemy - re attack
	 * 
	 * @param aKind
	 */
	public void begin_Attack(int aKind) {

		if (isDead || (sKind == 5 && sNum == 0))
			return;
		a_Kind = aKind;
		GetPath(a_Kind + 10);
		status = ATTACK;
	}

	/**
	 * attack enemy
	 */
	private void attack() {

		velocity.x = (int) (sx * 150);
		velocity.y = (int) (sy * 150);

		if (position.y < -loaf || position.y > Settings.nGameAsteroidSceneHeight + loaf || position.x < -loaf
				|| position.x > Settings.nGameAsteroidSceneWidth + loaf) {
			status = BEGINBACK;
			return;
		}

		len--;
		if (len >= 0)
			return;

		col++;
		if (col < sPath.get_Directions().length) {
			GetDir(col);
			if (dir >= 6 && dir <= 10)
				shoot_Missile(dir);
		} else {
			status = BEGINPOS;
		}
	}

	/**
	 * begin back position
	 */
	private void begin_Back_Pos() {
		position.y = Settings.nGameAsteroidSceneHeight;
		position.x = posX + this.map_controler.syncCnt;
		velocity.set(new Math_Vector(0, 0));

		status = BACKPOS;
	}

	/**
	 * back position
	 */
	private void back_Position() {

		sncX = (int) this.map_controler.get_Direc_Dis_X(this.map_controler.dir);
		velocity.y = -80;
		position.x += sncX;

		if (Math.abs(position.y - posY) <= 4) {
			GetPath(a_Kind + 10);
			status = ATTACK;
		}

	}

	/**
	 * get image character number
	 * 
	 * @return
	 */
	public int get_Chracter_Number() {
		return this.enemy_Img_Number;
	}

	/**
	 * get unit direction
	 * 
	 * @return
	 */
	public int get_Dir() {
		return this.dir;
	}

	/**
	 * get unit dead statues
	 * 
	 * @return
	 */
	public boolean get_Is_Dead() {
		return this.isDead;
	}

	/**
	 * get statues
	 * 
	 * @return
	 */
	public int get_Statue() {
		return this.status;
	}

	/**
	 * get shield statues
	 * 
	 * @return
	 */
	public int get_Shield() {
		return this.shield;
	}

	/**
	 * decrease shield if the shield value down then 0, this unit will be dead
	 */
	public void decreaseShield() {
		this.shield--;

		if (shield <= 0)
			this.isDead = true;
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
	 * set unit position
	 * 
	 * @param position
	 */
	public void setPosition(Math_Vector position) {
		this.position = position;
	}

	/**
	 * get unit unique name
	 * 
	 * @return
	 */
	public String getsUnitName() {
		return sUnitName;
	}

	/**
	 * make pangpang enemy missile
	 * 
	 * @param dir
	 */
	private void shoot_Missile(int dir) {
		DecimalFormat df = new DecimalFormat("#.##");

		// 20 % attack enemy event occurred
		if (rnd.nextInt(10) > 8) {
			mnt.sendMessageInTheRoomPeople(Settings._ANSWER_PANGAPNG_ENEMY_ATTACK + "", df.format(this.position.x) + "",
					df.format(this.position.y) + "", dir + "", Settings.sPangPangEnemyName + Enemy_Missile_ID);
			Enemy_Missile_ID++;
		}

	}
}
