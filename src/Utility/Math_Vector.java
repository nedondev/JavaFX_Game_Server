package Utility;

/**
 * @author KJW finish at 2016/ 08/ 11
 * @version 2.0.0v
 * @description this class Meth_vector class for Mathematical calculation
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class Math_Vector {
	public static double TO_RADIANS = (1 / 180.0f) * Math.PI;
	public static double TO_DEGREES = (1 / Math.PI) * 180;
	public double x, y;

	/**
	 * Math_vector constructor
	 */
	public Math_Vector() {
	}

	/**
	 * Math_vector constructor
	 * 
	 * @param x
	 *            - double
	 * @param y
	 *            - double
	 */
	public Math_Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Math_vector constructor
	 * 
	 * @param other
	 *            - Math_Vector
	 */
	public Math_Vector(Math_Vector other) {
		this.x = other.x;
		this.y = other.y;
	}

	/**
	 * copy the Math_Vector
	 * 
	 * @return
	 */
	public Math_Vector cpy() {
		return new Math_Vector(x, y);
	}

	/**
	 * set Math_Vector X,Y
	 * 
	 * @param x
	 *            - double
	 * @param y
	 *            - double
	 * @return
	 */
	public Math_Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * set Math_Vector X,Y
	 * 
	 * @param other
	 *            - Math_Vector
	 * @return
	 */
	public Math_Vector set(Math_Vector other) {
		this.x = other.x;
		this.y = other.y;
		return this;
	}

	/**
	 * add the value to this Math_Vector
	 * 
	 * @param x
	 *            - double
	 * @param y
	 *            - double
	 * @return
	 */
	public Math_Vector add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * add the value to this Math_Vector
	 * 
	 * @param other
	 *            - Math_Vactor
	 * @return
	 */
	public Math_Vector add(Math_Vector other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	/**
	 * sub the value from this Math_Vector
	 * 
	 * @param x
	 *            - double
	 * @param y
	 *            - double
	 * @return
	 */
	public Math_Vector sub(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/**
	 * sub the value to this Math_Vector
	 * 
	 * @param other
	 *            - Math_Vactor
	 * @return
	 */
	public Math_Vector sub(Math_Vector other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	/**
	 * mul the Math_Vector using scalar
	 * 
	 * @param scalar
	 *            - double
	 * @return
	 */
	public Math_Vector mul(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	/**
	 * mul the Math_Vector using scalar
	 * 
	 * @param other
	 *            - Math_Vector
	 * @return
	 */
	public Math_Vector mul(Math_Vector other) {
		this.x *= other.x;
		this.y *= other.y;
		return this;
	}

	/**
	 * len of thie Math_Vector root(x^2+y^2)
	 * 
	 * @return
	 */
	public double len() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * normalization the vector
	 * 
	 * @return
	 */
	public Math_Vector nor() {
		double len = len();
		if (len != 0) {
			this.x /= len;
			this.y /= len;
		}
		return this;
	}

	/**
	 * compare each other vector, equals
	 * 
	 * @param other
	 *            - Math_Vector
	 * @return
	 */
	public boolean equals(Math_Vector other) {

		if (this.x == other.x && this.y == other.y)
			return true;

		return false;
	}

	/**
	 * compare each other Value, equals
	 * 
	 * @param other_x
	 *            - double
	 * @param other_y
	 *            - double
	 * @return
	 */
	public boolean equals(double other_x, double other_y) {

		if (this.x == other_x && this.y == other_y) {
			return true;
		}
		return false;
	}

	/**
	 * calculate the angle
	 * 
	 * @return
	 */
	public double angle() {
		double angle = Math.atan2(y, x) * TO_DEGREES;
		if (angle < 0)
			angle += 360;
		return angle;
	}

	/**
	 * rotate the vector using angle
	 * 
	 * @param angle
	 *            double
	 * @return
	 */
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

	/**
	 * calculate each vector's distance
	 * 
	 * @param other
	 *            - Math_vector
	 * @return
	 */
	public double dist(Math_Vector other) {
		double distX = this.x - other.x;
		double distY = this.y - other.y;
		return Math.sqrt(distX * distX + distY * distY);
	}

	/**
	 * calculate distance
	 * 
	 * @param x
	 *            - double
	 * @param y
	 *            - double
	 * @return
	 */
	public double dist(double x, double y) {
		double distX = this.x - x;
		double distY = this.y - y;
		return Math.sqrt(distX * distX + distY * distY);
	}

	/**
	 * calculate each vector's distance Square
	 * 
	 * @param other
	 *            - Math_vector
	 * @return
	 */
	public double distSquared(Math_Vector other) {
		double distX = this.x - other.x;
		double distY = this.y - other.y;
		return distX * distX + distY * distY;
	}

	/**
	 * calculate distance Square
	 * 
	 * @param x
	 *            - double
	 * @param y
	 *            - double
	 * @return
	 */
	public double distSquared(double x, double y) {
		double distX = this.x - x;
		double distY = this.y - y;
		return distX * distX + distY * distY;
	}
}