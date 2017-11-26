public enum SurfaceType {
    GRASS (5.0),
    GLASS (1.2),
    ICE   (0.01);
    
    private final double friction;
    
    SurfaceType(double friction)
    {
    	this.friction = friction;
    }
    
    public double getFriction()
    {
    	return friction;
    }
}
	

