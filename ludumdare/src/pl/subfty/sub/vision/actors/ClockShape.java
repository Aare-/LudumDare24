package pl.subfty.sub.vision.actors;

import pl.subfty.sub.Sub;
import pl.subfty.sub.interfaces.Native;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ClockShape extends SActor{
	public float radius,
				 vertices[],
				 time,
				 alpha,
				 borderMixColorA;
	protected float oldRadius=-1;
	public Color c,
				 internalC;
	protected int precission;
	
	
	protected Mesh mesh;
	
	
	protected static AtlasRegion text;
	
	public boolean useTexture=true,
				   useGradient=false;
	
	public ClockShape(float x, float y,
					  float radius, float time,
					  int precission, Color c){
		this.x = x;
		this.y = y;
		this.radius = radius;
		setTime(time);
		this.c = new Color();
		internalC = this.c;
		this.c.set(c);
		alpha=1;
		
		borderMixColorA=0;
		
		this.precission = precission;
		vertices = new float[(precission+3)*5];
		mesh = new Mesh(false, precission+3, precission+3,
						new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
						new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
						new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		
		short indices[] = new short[precission+3];
		for(int i=0; i<precission+3; i++)
			indices[i] = (short)i;
		mesh.setIndices(indices);
		
		useTexture=false;
	}
	
	public void setTexture(AtlasRegion texture){
		useTexture = true;
		text = texture;
	}
	
	@Override
	public void draw(SpriteBatch sbatch, float parentAlpha){
	  //UPDATING VERTICES
		vertices[0] = x;
		vertices[1] = y;
		
		float newColor =  Color.toFloatBits(c.r, c.g, c.b, c.a*alpha*parentAlpha);
	  //WYWALENIE GRADIENTOW
		if(useGradient)
			Color.toFloatBits(internalC.r, 
							  internalC.g, 
							  internalC.b,
							  internalC.a*alpha*parentAlpha);
		else
			vertices[2] = newColor;
		
	  //UPDATING ADDITIONAL VERTICLES
		vertices[5]=x+Sub.nat.sin(rotation)*radius;
		vertices[5+1]=y+Sub.nat.cos(rotation)*radius;
		vertices[5+2] = newColor;	
		
		vertices[(precission+2)*5]=x+Sub.nat.sin((2*Native.PI*time)+rotation)*radius;
		vertices[(precission+2)*5+1]=y+Sub.nat.cos((2*Native.PI*time)+rotation)*radius;
		vertices[(precission+2)*5+2] = newColor;	
		
		final int size = precission+2;
		for(int i=2; i<size; i++){
			float p =  2*Native.PI*(((i-2)/((float)precission-1)*time));
			vertices[i*5]=x+Sub.nat.sin(p+rotation)*radius;
			vertices[i*5+1]=y+Sub.nat.cos(p+rotation)*radius;

			vertices[i*5+2] =newColor;	
		}
		
	  //RELOADING TEXTURE COORDINATES IN CASE THE RADIUS CHANGED
		if(useTexture && 
		   oldRadius != radius){
			oldRadius = radius;
			vertices[3] = text.getU();
			vertices[4] = text.getV2();
			for(int i=1; i<=size; i++){
				vertices[i*5+3] = (i%2==0)?text.getU2() : text.getU();
				vertices[i*5+4] = (i%2==0)?text.getV() : text.getV();
			}
		}
		
		mesh.setVertices(vertices);	
		Gdx.gl.glEnable(GL10.GL_BLEND);
		if(useTexture)
			Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);	
		mesh.render(GL10.GL_TRIANGLE_FAN);
	}

	public void setTime(float time){
		this.time = Math.max(0, time);
		if(this.time > 1)
			this.time %= 1;
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}
}
