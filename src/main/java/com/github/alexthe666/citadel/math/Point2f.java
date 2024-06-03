package com.github.alexthe666.citadel.math;

public class Point2f extends Tuple2f implements java.io.Serializable {

    // Compatible with 1.1
    static final long serialVersionUID = -4801347926528714435L;

    /**
     * Constructs and initializes a Point2f from the specified xy coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point2f(float x, float y)
    {
        super(x,y);
    }


    /**
     * Constructs and initializes a Point2f from the specified array.
     * @param p the array of length 2 containing xy in order
     */
    public Point2f(float[] p)
    {
        super(p);
    }


    /**
     * Constructs and initializes a Point2f from the specified Point2f.
     * @param p1 the Point2f containing the initialization x y data
     */
    public Point2f(Point2f p1)
    {
        super(p1);
    }

    /**
     * Constructs and initializes a Point2f from the specified Tuple2f.
     * @param t1 the Tuple2f containing the initialization x y data
     */
    public Point2f(Tuple2f t1)
    {
        super(t1);
    }


    /**
     * Constructs and initializes a Point2f to (0,0).
     */
    public Point2f()
    {
        super();
    }

    /**
     * Computes the square of the distance between this point and point p1.
     * @param p1 the other point
     */
    public final float distanceSquared(Point2f p1)
    {
        float dx, dy;

        dx = this.x-p1.x;
        dy = this.y-p1.y;
        return dx*dx+dy*dy;
    }

    /**
     * Computes the distance between this point and point p1.
     * @param p1 the other point
     */
    public final float distance(Point2f p1)
    {
        float  dx, dy;

        dx = this.x-p1.x;
        dy = this.y-p1.y;
        return (float) Math.sqrt(dx*dx+dy*dy);
    }


    /**
     * Computes the L-1 (Manhattan) distance between this point and
     * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
     * @param p1 the other point
     */
    public final float distanceL1(Point2f p1)
    {
        return( Math.abs(this.x-p1.x) + Math.abs(this.y-p1.y));
    }

    /**
     * Computes the L-infinite distance between this point and
     * point p1.  The L-infinite distance is equal to
     * MAX[abs(x1-x2), abs(y1-y2)].
     * @param p1 the other point
     */
    public final float distanceLinf(Point2f p1)
    {
        return(Math.max( Math.abs(this.x-p1.x), Math.abs(this.y-p1.y)));
    }

}
