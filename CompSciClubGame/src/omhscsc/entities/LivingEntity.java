package omhscsc.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import omhscsc.Game;
import omhscsc.state.GameStateState;
import omhscsc.util.Hitbox;
import omhscsc.util.Location;
import omhscsc.util.Velocity;
import omhscsc.world.World;
import omhscsc.world.WorldObject;

public abstract class LivingEntity extends Entity {

	protected double health,maxHealth,jumpHeight,speed,damage;
	protected boolean canJump;
	protected Velocity velocity;
	
	/*
	 * Haven't finished, feel free to work on it
	 */
	
	public LivingEntity(Hitbox h) {
		super(h);
		canJump = true;
	}
	
	public LivingEntity(World wr, int x, int y, int w, int h)
	{
		super(wr,x,y,w,h);
		canJump = true;
	}
	
	public boolean canJump()
	{
		return canJump;
	}
	
	public void setCanJump(boolean jump) {
		this.canJump=jump;
	}

	
	@Override
	public void tick(GameStateState s)
	{
		super.tick(s);
		//Always super tick (Exceptions may happen)
		defaultMovement();
		fixCollisions(s);
	}
	


	protected void fixCollisions(GameStateState gs)
	{
		for(WorldObject wo: gs.getCurrentWorld().getWorldObjects()){
			if(wo.getHitbox().getBounds().intersects(getLeftBound())){
				velocity.setX(0);
				hitbox.setX(wo.getHitbox().getLocation().getX()+wo.getHitbox().getBounds().getWidth());
			}
			if(wo.getHitbox().getBounds().intersects(getRightBound())){
				velocity.setX(0);
				hitbox.setX(wo.getHitbox().getLocation().getX()-hitbox.getBounds().getWidth());
			}
			if(wo.getHitbox().getBounds().intersects(getTopBound())){
				velocity.setY(0);
				hitbox.setY(wo.getHitbox().getLocation().getY()+wo.getHitbox().getBounds().getHeight());
			}
			if(wo.getHitbox().getBounds().intersects(getBottomBound())){
				velocity.setY(0);
				hitbox.setY(wo.getHitbox().getLocation().getY()-hitbox.getBounds().getHeight());
				setCanJump(true);
			}
		}
	}
	
	protected void defaultMovement()
	{
		velocity.addY((double)Game.GRAVITY/(double)Game.TPS);
		hitbox.addY(velocity.getY()/(double)Game.TPS);
		hitbox.addX(velocity.getX()/(double)Game.TPS);
		velocity.addX((velocity.getX()*-.9)/(double)Game.TPS);
		if(velocity.getX() < 50)
			velocity.setX(0);
	}
	
	@Override
	public Location getLocation() {
		return hitbox.getLocation();
	}
	
	public Rectangle getLeftBound() {
		return new Rectangle((int)hitbox.getLocation().getX(), (int)hitbox.getLocation().getY()+5, 10, (int)this.hitbox.getBounds().getHeight()-10);
	}
	
	public Rectangle getRightBound() {
		return new Rectangle((int)hitbox.getLocation().getX() + (int)(hitbox.getBounds().getWidth()-10), (int)hitbox.getLocation().getY()+5, 10, (int)this.hitbox.getBounds().getHeight()-10);
	}
	
	public Rectangle getTopBound() {
		return new Rectangle((int)hitbox.getLocation().getX()+5, (int)hitbox.getLocation().getY(), (int)(this.hitbox.getBounds().getWidth()-10), 10);
	}
	
	public Rectangle getBottomBound() {
		return new Rectangle((int)hitbox.getLocation().getX()+5, (int)hitbox.getLocation().getY()+(int)(hitbox.getBounds().getHeight()-10), (int)(this.hitbox.getBounds().getWidth()-10), 10);
	}
	
	public void drawHitBoxes(Graphics g, int x, int y)
	{
		g.setColor(Color.ORANGE);
		Graphics2D g2 = (Graphics2D)g;
		g2.draw(getLeftBound());
		g2.setColor(Color.RED);
		g2.draw(getRightBound());
		g2.setColor(Color.PINK);
		g2.draw(getTopBound());
		g2.setColor(Color.BLUE);
		g2.draw(getBottomBound());
	}
	

	public void jump()
	{
		if(!canJump)
			return;
		velocity.setY(-jumpHeight*400);
		hitbox.addY(-5);
		canJump = false;
	}
}