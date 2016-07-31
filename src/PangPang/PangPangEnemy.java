package PangPang;

import java.util.Random;

import ServerMainBody.Settings;
import Utility.Math_Vector;

public class PangPangEnemy {

	private Map_Controler map_controler;
	private Math_Vector velocity;
	private Math_Vector position;

	final static int ENTER = 1; // 캐릭터 입장
	final static int BEGINPOS = 2; // 전투 대형으로 가기 위해 좌표 계산
	final static int POSITION = 3; // 전투 대형으로 이동 중
	final static int SYNC = 4; // 전투 대형에서 대기중
	final static int ATTACK = 5; // 공격중
	final static int BEGINBACK = 6; // View를 벗어나서 다시 입장할 준비
	final static int BACKPOS = 7; // 다시 입장 중

	final int loaf = 50; // 탈영 max

	public static final float ENEMY_WIDTH = 20f; // 캐릭터 width
	public static final float ENEMY_HEIGHT = 20f; // 캐릭터 height

	private boolean isDead; // 전사자인가?
	private int shield; // 보호막
	private int status; // 상태

	private String sUnitName;

	private int sncX; // 싱크 위치로부터 떨어져 있는 거리
	private SinglePath sPath; // 캐럭터가 이동할 Path 1줄 (입장 및 공격 루트)
	private float sx, sy; // 캐릭터 이동 속도
	private int a_Kind; // 공격 루트 번호
	private int enemy_Img_Number; // 몹 이미지 번호
	private int sKind, sNum; // 캐릭터의 종류와 번호
	private int pNum, col; // Path 번호와 현재의 경로
	private int delay, dir, len; // 입장시 지연시간, 현재의 방향, 남은 거리
	private int posX, posY; // 이동해야 할 목적지 좌표
	private Random rnd = new Random();

	// --------------------------------
	// 생성자
	// --------------------------------
	public PangPangEnemy(Map_Controler paraMap_controler) {

		this.map_controler = paraMap_controler;
		this.velocity = new Math_Vector();
		this.position = new Math_Vector();
		// 내용 없음
	}

	// --------------------------------
	// Sprite 만들기
	// --------------------------------
	public void MakeEnemy(int kind, int num, int nName) {
		sKind = kind;
		sNum = num;
		sUnitName = "bubble" + nName;

		// 불필요한 캐릭터
		if (this.map_controler.get_Selection(kind, num) == -1) {
			isDead = true;
			return;
		}
		enemy_Img_Number = this.map_controler.get_Enemy_Num(sKind, num);// 몹
																		// 번호

		ResetEnemy();
	}

	// --------------------------------
	// Reset Sprite
	// --------------------------------
	public void ResetEnemy() {

		velocity.set(new Math_Vector(0, 0));
		pNum = this.map_controler.get_Selection(sKind, sNum); // Path
																// 번호
		delay = this.map_controler.get_Delay(sKind, sNum); // Delay 시간

		shield = this.map_controler.get_Enemy_Life(sKind, sNum); // 보호막
																	// 읽기

		posX = this.map_controler.get_Pos_X(sKind, sNum); // 전투대형 위치
		posY = this.map_controler.get_Pos_Y(sKind, sNum);

		GetPath(pNum); // pNum으로 구한 Path 읽기
		status = ENTER;
		isDead = false;

	}

	public void update(double deltaTime) {

		Move();

		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	}

	// --------------------------------
	// Path - Path 1줄 읽기
	// --------------------------------
	private void GetPath(int num) {
		sPath = this.map_controler.get_Path(num); // Path 읽기
		// Path의 시작 좌표
		if (sPath.get_Start_X() != -99)
			position.x = sPath.get_Start_X();
		if (sPath.get_Start_Y() != -99)
			position.y = sPath.get_Start_Y();

		col = 0;
		GetDir(col);
	}

	// --------------------------------
	// GetDir - 현위치의 방향과 거리
	// --------------------------------
	private void GetDir(int col) {
		dir = sPath.get_Directions()[col]; // 이동할 방향
		len = sPath.get_Lens()[col]; // 이동할 거리

		sx = this.map_controler.get_Direc_Dis_X(dir); // 이동 속도
		sy = this.map_controler.get_Direc_Dis_Y(dir);
	}

	// --------------------------------
	// Move
	// --------------------------------
	private void Move() {
		if (isDead && (sKind != 5 || sNum != 0))
			return;

		switch (status) {
		case ENTER: // 캐릭터 입장
			enter_Enemy();
			break;
		case BEGINPOS: // 전투 대형 위치 계산
			begin_Pos();
			break;
		case POSITION: // 전투 대형 위치로 이동중
			position();
			break;
		case SYNC: // 전투 대형 위치에서 대기 중
			make_Sync();
			break;
		case ATTACK: // 공격
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
			GetDir(col); // 다음 경로 찾기
		} else {
			status = BEGINPOS; // 경로의 끝이면 전투 대형으로 이동
		}
	}

	// --------------------------------
	// BeginPos - 전투 대형으로 이동 준비
	// --------------------------------
	private void begin_Pos() {
		// 원래의 Path 읽기
		if (position.x < posX + this.map_controler.syncCnt) // 이동 방향
															// 결정
			dir = 2; // 북동(NW)쪽
		else
			dir = 14; // 북서(NW)쪽

		if (position.y < posY)
			dir = (dir == 2) ? 6 : 10;

		sx = this.map_controler.get_Direc_Dis_X(dir); // 이동 방향에 따른 속도
														// 계산
		sy = this.map_controler.get_Direc_Dis_Y(dir);
		status = POSITION; // 목적지로 이동 준비 끝
	}

	// --------------------------------
	// Position - 전투 대형으로 이동 중
	// --------------------------------
	private void position() {
		velocity.x = (int) (sx * 160);
		velocity.y = (int) (sy * 160);

		// 싱크 때문에 목적지가 멀어졌을 수 있으므로 방향 다시 계산
		if (position.x < posX + this.map_controler.syncCnt) // 이동 방향
															// 결정
			dir = 2; // 북동(NW)쪽
		else
			dir = 14; // 북서(NW)쪽

		if (position.y < posY)
			dir = (dir == 2) ? 2 : 14;// 6 : 10;

		// 수평 좌표 비교
		if (Math.abs(position.y - posY) <= 4) { // 수평 위치 도착
			position.y = posY;
			velocity.y = 0;
			if (position.x < posX + this.map_controler.syncCnt) // 좌우
																// 방향
																// 결정
				dir = 4; // 3시방향
			else
				dir = 12; // 9시 방향
		}

		// 수직 좌표 비교
		if (Math.abs(position.x - (posX + this.map_controler.syncCnt)) <= 4) {
			position.x = posX + this.map_controler.syncCnt;
			velocity.x = 0;
			dir = 0; // 12시 방향
		}

		if (position.y == posY && position.x == posX + this.map_controler.syncCnt) {
			dir = 0; // 전투대형 위치 도착
			sx = 1;
			status = SYNC; // 좌우로 이동하며 공격 명령 대기
			return; // 싱크 유지 중
		}

		sx = this.map_controler.get_Direc_Dis_X(dir); // 위에서 설정한 전투 대형
														// 위치로
		sy = this.map_controler.get_Direc_Dis_Y(dir); // 계속 이동
	}

	// --------------------------------
	// Sync & Move - 싱크를 유지하며 이동
	// --------------------------------
	private void make_Sync() {
		sncX = (int) this.map_controler.get_Direc_Dis_X(this.map_controler.dir); // 좌우
																					// 이동방향
																					// 계산
		position.x += sncX; // 좌 또는 우로 이동

		// Sync 설정
		if (sKind == 5 && sNum == 0) { // 이 캐릭터가 싱크를 설정한다
			this.map_controler.syncCnt += sncX; // 최초 도착자가 좌우로 이동한 거리
			this.map_controler.dirCnt++; // 현재 방향으로 이동한 거리
			if (this.map_controler.dirCnt >= this.map_controler.dirLen) {
				this.map_controler.dirCnt = 0; // 현재 방향의 끝에 도착
				this.map_controler.dirLen = 150; // 반대 방향으로 이동할 거리
				this.map_controler.dir = 16 - this.map_controler.dir; // 이동방향
																		// 반전
			}
		}
	}

	// --------------------------------
	// Begin Attack - 공격 루트 수령
	// --------------------------------
	public void begin_Attack(int aKind) {

		if (isDead || (sKind == 5 && sNum == 0))
			return; // 싱크 기준은
		a_Kind = aKind; // 공격에 참여하지 않음
		GetPath(a_Kind + 10);
		status = ATTACK;
	}

	// --------------------------------
	// Attack
	// --------------------------------
	private void attack() {

		velocity.x = (int) (sx * 150);
		velocity.y = (int) (sy * 150);

		// 비행 중에 전투 위치를 이탈함 - 탈영
		if (position.y < -loaf || position.y > Settings.nGameAsteroidSceneHeight + loaf || position.x < -loaf
				|| position.x > Settings.nGameAsteroidSceneWidth + loaf) {
			status = BEGINBACK;
			return;
		}

		len--;
		if (len >= 0)
			return; // 현재 방향으로 계속 이동 중

		col++; // 방향 전환
		if (col < sPath.get_Directions().length) {
			GetDir(col); // 방향 전환 후 공격 시작
			if (dir >= 6 && dir <= 10)
				shoot_Missile(dir);
		} else {
			status = BEGINPOS; // 공격을 끝내고 끝났으면 전투 대형으로 복귀
		}
	}

	private void begin_Back_Pos() {
		position.y = Settings.nGameAsteroidSceneHeight; // + 40;
		position.x = posX + this.map_controler.syncCnt;
		velocity.set(new Math_Vector(0, 0));

		status = BACKPOS;
	}

	private void back_Position() {
		// 전투 대향이 좌우 어느 쪽으로 이동중인가를 파ㄴ별

		sncX = (int) this.map_controler.get_Direc_Dis_X(this.map_controler.dir);
		velocity.y = -80;
		position.x += sncX;

		// 복귀 후 마지막 공격 루트 다시
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
