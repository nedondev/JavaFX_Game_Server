package Utility;

public class Math_Vector {
	public static double TO_RADIANS = (1 / 180.0f) *  Math.PI;
	public static double TO_DEGREES = (1 / Math.PI) * 180;
	public double x, y;

	public Math_Vector() {
	}

	public Math_Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Math_Vector(Math_Vector other) {
		this.x = other.x;
		this.y = other.y;
	}

	public Math_Vector cpy() {
		return new Math_Vector(x, y);
	}

	public Math_Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Math_Vector set(Math_Vector other) {
		this.x = other.x;
		this.y = other.y;
		return this;
	}

	public Math_Vector add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Math_Vector add(Math_Vector other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public Math_Vector sub(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Math_Vector sub(Math_Vector other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public Math_Vector mul(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	public Math_Vector mul(Math_Vector other){
		this.x *= other.x;
		this.y *= other.y;
		return this;
	}

	public double len() {
		return Math.sqrt(x * x + y * y);
	}

	public Math_Vector nor() {
		double len = len();
		if (len != 0) {
			this.x /= len;
			this.y /= len;
		}
		return this;
	}
	
	public boolean equals(Math_Vector other){
		
		if(this.x == other.x && this.y == other.y)
			return true;
		
		return false;
	}
	
	public boolean equals(double other_x, double other_y){
		
		if(this.x == other_x && this.y == other_y)
		{
			return true;
		}
		return false;
	}

	public double angle() {
		double angle = Math.atan2(y, x) * TO_DEGREES;
		if (angle < 0)
			angle += 360;
		return angle;
	}

	public Math_Vector rotate(double angle) {
		double rad = angle * TO_RADIANS;
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		double newX = this.x * cos - this.y * sin;
		double newY = this.x * sin + this.y * cos;

		this.x = newX;
		this.y = newY;

		return this;
	}

	public double dist(Math_Vector other) {
		double distX = this.x - other.x;
		double distY = this.y - other.y;
		return Math.sqrt(distX * distX + distY * distY);
	}

	public double dist(double x, double y) {
		double distX = this.x - x;
		double distY = this.y - y;
		return Math.sqrt(distX * distX + distY * distY);
	}

	public double distSquared(Math_Vector other) {
		double distX = this.x - other.x;
		double distY = this.y - other.y;
		return distX * distX + distY * distY;
	}

	public double distSquared(double x, double y) {
		double distX = this.x - x;
		double distY = this.y - y;
		return distX * distX + distY * distY;
	}
}