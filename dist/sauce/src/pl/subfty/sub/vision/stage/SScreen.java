package pl.subfty.sub.vision.stage;

import java.io.IOException;

import pl.subfty.sub.Sub;
import pl.subfty.sub.interfaces.Callback;
import pl.subfty.sub.vision.actors.SpriteActor;
import pl.subfty.sub.vision.sprites.SSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public abstract class SScreen implements Screen{
	
	public Group cntnt = new Group();	
	
  //PIPELINES
	public Array<Callback> restartPipeline;
	
	private XmlReader xReader = new XmlReader();
	private float val[] = new float[6];
	
  //RWECTANGLE
	private final static String RECT_TEXTURE="GME";
	private final static int RECT_ID=2;
	
	public SScreen(){
		restartPipeline = new Array<Callback>();
		
		Sub.stage.addActor(cntnt);
		cntnt.visible=false;
	}
	
  //XML PROCESSING
	protected void loadScreenLayout(String inputFile, Group target, String root){
		Element xRoot;
		try {
			xRoot = xReader.parse(Gdx.files.internal(inputFile));
			Array<Element> arr = xRoot.getChildrenByNameRecursively("g");
			for(Element e : arr){
				if(e.getAttribute("id").equals(root)){
				  //FILLING NODES ARRAY
					processNodes(e, target);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void processNodes(Element root, Group group){		
		final int size = root.getChildCount();
		for(int i=0; i<size; i++){
			Element e = root.getChild(i);
			if(e.getName().equals("image")){
				String href = e.getAttribute("xlink:href");
				SpriteActor sprite = new SpriteActor(e.getAttribute("id"), new SSprite());
				sprite.sprite.loadSprite(href.substring(href.lastIndexOf("/")+1,
														href.lastIndexOf("_")), 
										 Integer.parseInt(
												 href.substring(href.lastIndexOf("_")+1, 
												 				href.lastIndexOf("."))));
				//sprite.sprite.setOrigin(0, 0);
				sprite.sprite.setScale(Float.parseFloat(e.getAttribute("width"))/sprite.sprite.getWidth(),
									   Float.parseFloat(e.getAttribute("height"))/sprite.sprite.getHeight());
				sprite.sprite.setPosition(Float.parseFloat(e.getAttribute("x","0")),
										  Sub.STAGE_H-Float.parseFloat(e.getAttribute("y","0"))
										  -Float.parseFloat(e.getAttribute("height")));
				
				if(e.getAttribute("transform", "").length() > 0){
					String transform = e.getAttribute("transform", ""),
						   token = transform.substring(0, transform.indexOf("(")),
						   values = transform.substring(transform.indexOf("(")+1, transform.indexOf(")"))+",";
					if(token.equals("matrix")){
						for(int j=0; j<6; j++){
							val[j] = Float.parseFloat(values.substring(0, values.indexOf(",")));
							values = values.substring(values.indexOf(",")+1);
						}
						sprite.sprite.setScale(sprite.sprite.getScaleX()*val[0], sprite.sprite.getScaleY()*val[3]);
						sprite.sprite.setPosition(val[4],
												  Sub.STAGE_H-val[5]-sprite.sprite.getWidth()*sprite.sprite.getScaleX());
					}else if(token.equals("translate")){
						for(int j=0; j<2; j++){
							val[j] = Float.parseFloat(values.substring(0, values.indexOf(",")));
							values = values.substring(values.indexOf(",")+1);
						}
						sprite.sprite.setPosition(sprite.sprite.getX()+val[0], 
												  sprite.sprite.getY()+(-val[1]));
					}
				}
				group.addActor(sprite);
			}else if(e.getName().equals("g")){
			  //CREATE NEW GROUP
				Group newG = new Group(e.getAttribute("id",""));
			  //TRANSFORMING NEW GROUP
				String transform = e.getAttribute("transform", "");
				while(transform.length()>0){
					String token = transform.substring(0, transform.indexOf("(")),
						   values = transform.substring(transform.indexOf("(")+1, transform.indexOf(")"))+",";
					transform = transform.substring(transform.indexOf(")")+1);
					if(token.equals("matrix")){
						for(int j=0; j<6; j++){
							val[j] = Float.parseFloat(values.substring(0, values.indexOf(",")));
							values = values.substring(values.indexOf(",")+1);
						}
						newG.originX = newG.originY = 0;
						newG.x = val[4]/val[0];
						newG.y = (val[5])/val[3]+Sub.STAGE_H;
						newG.scaleX = val[0];
						newG.scaleY = val[3];
					}else if(token.equals("scale")){
						for(int j=0; j<2; j++){
							val[j] = Float.parseFloat(values.substring(0, values.indexOf(",")));
							values = values.substring(values.indexOf(",")+1);
						}
						newG.scaleX = val[0];
						newG.scaleY = val[1];
					}else if(token.equals("translate")){
						for(int j=0; j<2; j++){
							val[j] = Float.parseFloat(values.substring(0, values.indexOf(",")));
							values = values.substring(values.indexOf(",")+1);
						}
						newG.x = val[0];
						newG.y = -(val[1]);
					}
				}
				
				group.addActor(newG);
				processNodes(e,newG);
			}else if(e.getName().equals("rect")){
			  //TODO: dodaj wczytywanie kwadratow
			}
		}
	}
}
