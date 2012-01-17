package model;

public class Bounds
{
    private Point tl;
    private Point br;

    public Bounds(Point topLeft, Point bottomRight)
    {
        this.tl = topLeft;
        this.br = bottomRight;
    }
    
    public Point getTopLeft()
    {
        return this.tl;
    }
    
    public Point getBottomRight()
    {
        return this.br;
    }
    
    public int getWidth()
    {
        return this.br.getX()-this.tl.getX();
    }
    
    public int getHeight()
    {
        return this.br.getY()-this.tl.getY();
    }
}
