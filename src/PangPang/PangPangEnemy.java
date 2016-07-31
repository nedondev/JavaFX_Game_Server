package PangPang;

import java.util.Random;

import ServerMainBody.Settings;
import Utility.Math_Vector;

public class PangPangEnemy {

	private Map_Controler map_controler;
	private Math_Vector velocity;
	private Math_Vector position;

	final static int ENTER = 1; // ĳ���� ����
	final static int BEGINPOS = 2; // ���� �������� ���� ���� ��ǥ ���
	final static int POSITION = 3; // ���� �������� �̵� ��
	final static int SYNC = 4; // ���� �������� �����
	final static int ATTACK = 5; // ������
	final static int BEGINBACK = 6; // View�� ����� �ٽ� ������ �غ�
	final static int BACKPOS = 7; // �ٽ� ���� ��

	final int loaf = 50; // Ż�� max

	public static final float ENEMY_WIDTH = 20f; // ĳ���� width
	public static final float ENEMY_HEIGHT = 20f; // ĳ���� height

	private boolean isDead; // �������ΰ�?
	private int shield; // ��ȣ��
	private int status; // ����

	private String sUnitName;

	private int sncX; // ��ũ ��ġ�κ��� ������ �ִ� �Ÿ�
	private SinglePath sPath; // ĳ���Ͱ� �̵��� Path 1�� (���� �� ���� ��Ʈ)
	private float sx, sy; // ĳ���� �̵� �ӵ�
	private int a_Kind; // ���� ��Ʈ ��ȣ
	private int enemy_Img_Number; // �� �̹��� ��ȣ
	private int sKind, sNum; // ĳ������ ������ ��ȣ
	private int pNum, col; // Path ��ȣ�� ������ ���
	private int delay, dir, len; // ����� �����ð�, ������ ����, ���� �Ÿ�
	private int posX, posY; // �̵��ؾ� �� ������ ��ǥ
	private Random rnd = new Random();

	// --------------------------------
	// ������
	// --------------------------------
	public PangPangEnemy(Map_Controler paraMap_controler) {

		this.map_controler = paraMap_controler;
		this.velocity = new Math_Vector();
		this.position = new Math_Vector();
		// ���� ����
	}

	// --------------------------------
	// Sprite �����
	// --------------------------------
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

	// --------------------------------
	// Reset Sprite
	// --------------------------------
	public void ResetEnemy() {

		velocity.set(new Math_Vector(0, 0));
		pNum = this.map_controler.get_Selection(sKind, sNum); // Path
																// ��ȣ
		delay = this.map_controler.get_Delay(sKind, sNum); // Delay �ð�

		shield = this.map_controler.get_Enemy_Life(sKind, sNum); // ��ȣ��
																	// �б�

		posX = this.map_controler.get_Pos_X(sKind, sNum); // �������� ��ġ
		posY = this.map_controler.get_Pos_Y(sKind, sNum);

		GetPath(pNum); // pNum���� ���� Path �б�
		status = ENTER;
		isDead = false;

	}

	public void update(double deltaTime) {

		Move();

		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	}

	// --------------------------------
	// Path - Path 1�� �б�
	// --------------------------------
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

	// --------------------------------
	// GetDir - ����ġ�� ����� �Ÿ�
	// --------------------------------
	private void GetDir(int col) {
		dir = sPath.get_Directions()[col]; // �̵��� ����
		len = sPath.get_Lens()[col]; // �̵��� �Ÿ�

		sx = this.map_controler.get_Direc_Dis_X(dir); // �̵� �ӵ�
		sy = this.map_controler.get_Direc_Dis_Y(dir);
	}

	// --------------------------------
	// Move
	// --------------------------------
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

	// --------------------------------
	// Enter Sprite
	// --------------------------------
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

	// --------------------------------
	// BeginPos - ���� �������� �̵� �غ�
	// --------------------------------
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

	// --------------------------------
	// Position - ���� �������� �̵� ��
	// --------------------------------
	private void position() {
		velocity.x = (int) (sx * 160);
		velocity.y = (int) (sy * 160);

		// ��ũ ������ �������� �־����� �� �����Ƿ� ���� �ٽ� ���
		if (position.x < posX + this.map_controler.syncCnt) // �̵� ����
															// ����
			dir = 2; // �ϵ�(NW)��
		else
			dir = 14; // �ϼ�(NW)��

		if (position.y < posY)
			dir = (dir == 2) ? 2 : 14;// 6 : 10;

		// ���� ��ǥ ��
		if (Math.abs(position.y - posY) <= 4) { // ���� ��ġ ����
			position.y = posY;
			velocity.y = 0;
			if (position.x < posX + this.map_controler.syncCnt) // �¿�
																// ����
																// ����
				dir = 4; // 3�ù���
			else
				dir = 12; // 9�� ����
		}

		// ���� ��ǥ ��
		if (Math.abs(position.x - (posX + this.map_controler.syncCnt)) <= 4) {
			position.x = posX + this.map_controler.syncCnt;
			velocity.x = 0;
			dir = 0; // 12�� ����
		}

		if (position.y == posY && position.x == posX + this.map_controler.syncCnt) {
			dir = 0; // �������� ��ġ ����
			sx = 1;
			status = SYNC; // �¿�� �̵��ϸ� ���� ��� ���
			return; // ��ũ ���� ��
		}

		sx = this.map_controler.get_Direc_Dis_X(dir); // ������ ������ ���� ����
														// ��ġ��
		sy = this.map_controler.get_Direc_Dis_Y(dir); // ��� �̵�
	}

	// --------------------------------
	// Sync & Move - ��ũ�� �����ϸ� �̵�
	// --------------------------------
	private void make_Sync() {
		sncX = (int) this.map_controler.get_Direc_Dis_X(this.map_controler.dir); // �¿�
																					// �̵�����
																					// ���
		position.x += sncX; // �� �Ǵ� ��� �̵�

		// Sync ����
		if (sKind == 5 && sNum == 0) { // �� ĳ���Ͱ� ��ũ�� �����Ѵ�
			this.map_controler.syncCnt += sncX; // ���� �����ڰ� �¿�� �̵��� �Ÿ�
			this.map_controler.dirCnt++; // ���� �������� �̵��� �Ÿ�
			if (this.map_controler.dirCnt >= this.map_controler.dirLen) {
				this.map_controler.dirCnt = 0; // ���� ������ ���� ����
				this.map_controler.dirLen = 150; // �ݴ� �������� �̵��� �Ÿ�
				this.map_controler.dir = 16 - this.map_controler.dir; // �̵�����
																		// ����
			}
		}
	}

	// --------------------------------
	// Begin Attack - ���� ��Ʈ ����
	// --------------------------------
	public void begin_Attack(int aKind) {

		if (isDead || (sKind == 5 && sNum == 0))
			return; // ��ũ ������
		a_Kind = aKind; // ���ݿ� �������� ����
		GetPath(a_Kind + 10);
		status = ATTACK;
	}

	// --------------------------------
	// Attack
	// --------------------------------
	private void attack() {

		velocity.x = (int) (sx * 150);
		velocity.y = (int) (sy * 150);

		// ���� �߿� ���� ��ġ�� ��Ż�� - Ż��
		if (position.y < -loaf || position.y > Settings.nGameAsteroidSceneHeight + loaf || position.x < -loaf
				|| position.x > Settings.nGameAsteroidSceneWidth + loaf) {
			status = BEGINBACK;
			return;
		}

		len--;
		if (len >= 0)
			return; // ���� �������� ��� �̵� ��

		col++; // ���� ��ȯ
		if (col < sPath.get_Directions().length) {
			GetDir(col); // ���� ��ȯ �� ���� ����
			if (dir >= 6 && dir <= 10)
				shoot_Missile(dir);
		} else {
			status = BEGINPOS; // ������ ������ �������� ���� �������� ����
		}
	}

	private void begin_Back_Pos() {
		position.y = Settings.nGameAsteroidSceneHeight; // + 40;
		position.x = posX + this.map_controler.syncCnt;
		velocity.set(new Math_Vector(0, 0));

		status = BACKPOS;
	}

	private void back_Position() {
		// ���� ������ �¿� ��� ������ �̵����ΰ��� �Ĥ���

		sncX = (int) this.map_controler.get_Direc_Dis_X(this.map_controler.dir);
		velocity.y = -80;
		position.x += sncX;

		// ���� �� ������ ���� ��Ʈ �ٽ�
		if (Math.abs(position.y - posY) <= 4) {
			GetPath(a_Kind + 10);
			status = ATTACK;
		}

	}

	public int get_Chracter_Number() {
		return this.enemy_Img_Number;
	}

	public int get_Dir() {
		return this.dir;
	}

	public boolean get_Is_Dead() {
		return this.isDead;
	}

	public int get_Statue() {
		return this.status;
	}

	public int get_Shield() {
		return this.shield;
	}

	public Math_Vector getPosition() {
		return position;
	}

	public void setPosition(Math_Vector position) {
		this.position = position;
	}

	public String getsUnitName() {
		return sUnitName;
	}

	private void shoot_Missile(int dir) {
		// if (rnd.nextInt(10) > diff[df])
		// MainGame_Manager.mBubbleMis.add(new Bubble_Missile(this.position.x,
		// this.position.y, dir));
	}
}
