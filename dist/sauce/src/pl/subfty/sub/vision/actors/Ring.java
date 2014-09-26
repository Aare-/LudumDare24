package pl.subfty.sub.vision.actors;

import pl.subfty.sub.Sub;
import pl.subfty.sub.interfaces.Native;
import pl.subfty.sub.vision.stage.actors.SActor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ring extends SActor{
	public float vertices[],
				 outterRadius,
				 innerRadius,
				 time;
	private float fcolor;
	private Mesh mesh;
	private int precission,
				vert;
	
	public Ring(int precission, float outterRadius, float innerRadius){
		this.precission = precission;
		this.vert = (precission+2)*2;
		this.outterRadius = outterRadius;
		this.innerRadius = innerRadius;
		
		
		vertices = new float[(vert)*5];
		mesh = new Mesh(false, vert, vert,
						new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
						new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
						new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		short indices[] = new short[vert];
		for(int i=0; i<vert; i++)
			indices[i] = (short)i;
		mesh.setIndices(indices);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
	  //LOGIC
		fcolor = color.toFloatBits();
		for(int i=0; i<vert; i+=2){
			float p = 2*Native.PI*(((float)(i/2)/((float)precission+1)*time));
			vertices[i*5]=x+Sub.nat.sin(p+rotation)*outterRadius;
			vertices[i*5+1]=y+Sub.nat.cos(p+rotation)*outterRadius;
		}
		for(int i=1; i<vert; i+=2){
			float p = 2*Native.PI*(((float)(i/2)/((float)precission+1)*time));
			vertices[i*5]=x+Sub.nat.sin(p+rotation)*innerRadius;
			vertices[i*5+1]=y+Sub.nat.cos(p+rotation)*innerRadius;
		}
		for(int i=0; i<vert; i++){
			vertices[i*5+2] = fcolor;
			vertices[i*5+3] = 0;
			vertices[i*5+4] = 0;
		}
		
		mesh.setVertices(vertices);
		
	  //DRAWING
		batch.end();
		Gdx.gl.glEnable(GL10.GL_BLEND);
		//if(useTexture)
			//Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);	
		mesh.render(GL10.GL_TRIANGLE_STRIP);
		batch.begin();
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}
}
