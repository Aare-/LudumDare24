package pl.subfty.sub.vision.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.NumberUtils;

public class BFonts extends BitmapFont{
	private Logger l = new Logger("SBitmapFont", Logger.DEBUG);
	
	//static private final int LOG2_PAGE_SIZE = 9;
	//static private final int PAGE_SIZE = 1 << LOG2_PAGE_SIZE;
	//static private final int PAGES = 0x10000 / PAGE_SIZE;

	static final char[] xChars = {'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z'};
	static final char[] capChars = {'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S',
		'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	TextureRegion region;
	private final TextBounds textBounds = new TextBounds();
	private float color = Color.WHITE.toFloatBits();
	private Color tempColor = new Color(1, 1, 1, 1),
				  actColor = Color.WHITE;
	private boolean flipped;
	private boolean integer = true;
	final BitmapFontData data;
	private boolean ownsTexture;

	/** Creates a BitmapFont using the default 15pt Arial font included in the libgdx JAR file. This is convenient to easily display
	 * text without bothering with generating a bitmap font. */
	public BFonts() {
		this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"),
			Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), false, true);
	}

	/** Creates a BitmapFont using the default 15pt Arial font included in the libgdx JAR file. This is convenient to easily display
	 * text without bothering with generating a bitmap font.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BFonts(boolean flip) {
		this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"),
			Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), flip, true);
	}

	/** Creates a BitmapFont with the glyphs relative to the specified region. If the region is null, the glyph textures are loaded
	 * from the image file given in the font file. The {@link #dispose()} method will not dispose the region's texture in this
	 * case!
	 * @param region The texture region containing the glyphs. The glyphs must be relative to the lower left corner (ie, the region
	 *           should not be flipped). If the region is null the glyph images are loaded from the image path in the font file.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BFonts(FileHandle fontFile, TextureRegion region, boolean flip) {
		this(new BitmapFontData(fontFile, flip), region, true);
		l.debug("SBitmapFont!");
	}

	/** Creates a BitmapFont from a BMFont file. The image file name is read from the BMFont file and the image is loaded from the
	 * same directory.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BFonts(FileHandle fontFile, boolean flip) {
		this(new BitmapFontData(fontFile, flip), null, true);
	}

	/** Creates a BitmapFont from a BMFont file, using the specified image for glyphs. Any image specified in the BMFont file is
	 * ignored.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public BFonts(FileHandle fontFile, FileHandle imageFile, boolean flip) {
		this(fontFile, imageFile, flip, true);
	}

	/** Creates a BitmapFont from a BMFont file, using the specified image for glyphs. Any image specified in the BMFont file is
	 * ignored.
	 * @param flip If true, the glyphs will be flipped for use with a perspective where 0,0 is the upper left corner.
	 * @param integer If true, rendering positions will be at integer values to avoid filtering artifacts.s */
	public BFonts(FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
		this(new BitmapFontData(fontFile, flip), new TextureRegion(new Texture(imageFile, false)), integer);
		ownsTexture = true;
	}

	/** Constructs a new BitmapFont from the given {@link BitmapFontData} and {@link TextureRegion}. If the TextureRegion is null,
	 * the image path is read from the BitmapFontData. The dispose() method will not dispose the texture of the region if the
	 * region is != null.
	 * @param data
	 * @param region
	 * @param integer */
	public BFonts(BitmapFontData data, TextureRegion region, boolean integer) {
		this.region = region == null ? new TextureRegion(new Texture(Gdx.files.internal(data.imagePath), false)) : region;
		this.flipped = data.flipped;
		this.integer = integer;
		this.data = data;
		load(data);
		ownsTexture = region != null;
	}

	private void load (BitmapFontData data) {
		float invTexWidth = 1.0f / region.getTexture().getWidth();
		float invTexHeight = 1.0f / region.getTexture().getHeight();
		float u = region.getU();
		float v = region.getV();

		for (Glyph[] page : data.glyphs) {
			if (page == null) continue;
			for (Glyph glyph : page) {
				if (glyph == null) continue;
				glyph.u = u + glyph.srcX * invTexWidth;
				glyph.u2 = u + (glyph.srcX + glyph.width) * invTexWidth;
				if (data.flipped) {
					glyph.v = v + glyph.srcY * invTexHeight;
					glyph.v2 = v + (glyph.srcY + glyph.height) * invTexHeight;
				} else {
					glyph.v2 = v + glyph.srcY * invTexHeight;
					glyph.v = v + (glyph.srcY + glyph.height) * invTexHeight;
				}
			}
		}
	}

	/** Draws a string at the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline). Note the same TextBounds
	 *         instance is used for all methods that return TextBounds. */
	public TextBounds draw (SpriteBatch spriteBatch, CharSequence str, float x, float y) {
		return draw(spriteBatch, str, x, y, 0, str.length());
	}

	/** Draws a substring at the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @param start The first character of the string to draw.
	 * @param end The last character of the string to draw (exclusive).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline). Note the same TextBounds
	 *         instance is used for all methods that return TextBounds. */
	public TextBounds draw (SpriteBatch spriteBatch, CharSequence str, float x, float y, int start, int end) {
		startS = start;
		float batchColor = spriteBatch.getColor().toFloatBits();
		spriteBatch.setColor(color);
		final Texture texture = region.getTexture();
		y += data.ascent;
		float startX = x;
		Glyph lastGlyph = null;
		if (data.scaleX == 1 && data.scaleY == 1) {
			if (integer) {
				y = (int)y;
				x = (int)x;
			}
			while (start < end) {
				lastGlyph = data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					spriteBatch.draw(texture, //
									 x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
									 lastGlyph.width, lastGlyph.height, //
									 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					x += lastGlyph.xadvance;
					break;
				}
			}
			while (start < end) {
				char ch = str.charAt(start++);
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch);
				if (integer) x = (int)x;
				lastGlyph = g;
				spriteBatch.draw(texture, //
								 x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
								 lastGlyph.width, lastGlyph.height, //
								 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				x += g.xadvance;
			}
		} else {
			float scaleX = this.data.scaleX, scaleY = this.data.scaleY;
			while (start < end) {
				lastGlyph = data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					if (!integer) {
						spriteBatch.draw(texture, //
										 x + lastGlyph.xoffset * scaleX, //
										 y + lastGlyph.yoffset * scaleY, //
										 lastGlyph.width * scaleX, //
										 lastGlyph.height * scaleY, //
										 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					} else {
						spriteBatch.draw(texture, //
										 (int)(x + lastGlyph.xoffset * scaleX), //
										 (int)(y + lastGlyph.yoffset * scaleY), //
										 (int)(lastGlyph.width * scaleX), //
										 (int)(lastGlyph.height * scaleY), //
										 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					}
					x += lastGlyph.xadvance * scaleX;
					break;
				}
			}
			while (start < end) {
				char ch = str.charAt(start++);
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch) * scaleX;
				lastGlyph = g;
				if (!integer) {
					spriteBatch.draw(texture, //
						x + lastGlyph.xoffset * scaleX, //
						y + lastGlyph.yoffset * scaleY, //
						lastGlyph.width * scaleX, //
						lastGlyph.height * scaleY, //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				} else {
					spriteBatch.draw(texture, //
						(int)(x + lastGlyph.xoffset * scaleX), //
						(int)(y + lastGlyph.yoffset * scaleY), //
						(int)(lastGlyph.width * scaleX), //
						(int)(lastGlyph.height * scaleY), //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				}
				x += g.xadvance * scaleX;
			}
		}
		spriteBatch.setColor(batchColor);
		textBounds.width = x - startX;
		textBounds.height = data.capHeight;
		
		return textBounds;
	}
	/** Draws a string, which may contain newlines (\n), at the specified position.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawMultiLine (SpriteBatch spriteBatch, CharSequence str, float x, float y) {
		return drawMultiLine(spriteBatch, str, x, y, 0, HAlignment.LEFT);
	}
  //THIS IS THE FUNCTION USED BY EVERY DRAWING METHOD
	/** Draws a string, which may contain newlines (\n), at the specified position and alignment. Each line is aligned horizontally
	 * within a rectangle of the specified width.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawMultiLine (SpriteBatch spriteBatch, CharSequence str, float x, float y, float alignmentWidth,
		HAlignment alignment) {
		float batchColor = spriteBatch.getColor().toFloatBits();
		float down = this.data.down;
		int start = 0;
		int numLines = 0;
		int length = str.length();
		float maxWidth = 0;
		while (start < length) {
			int lineEnd = indexOf(str, '\n', start);
			float xOffset = 0;
			if (alignment != HAlignment.LEFT) {
				float lineWidth = getBounds(str, start, lineEnd).width;
				xOffset = alignmentWidth - lineWidth;
				if (alignment == HAlignment.CENTER) xOffset = xOffset / 2;
			}
			float lineWidth = draw(spriteBatch, str, x + xOffset, y, start, lineEnd).width;
			maxWidth = Math.max(maxWidth, lineWidth);
			start = lineEnd + 1;
			y += down;
			numLines++;
		}
		spriteBatch.setColor(batchColor);

		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}
	
  //DRAWING WITH ALPHA
	public TextBounds drawMultiLine (SpriteBatch spriteBatch, CharSequence str, float x, float y, float alpha, boolean smooth) {
		return drawMultiLine(spriteBatch, str, x, y, 0, HAlignment.LEFT, alpha, smooth);
	}
	public TextBounds drawMultiLine (SpriteBatch spriteBatch, CharSequence str, float x, float y, float alignmentWidth,
			HAlignment alignment, float alpha, boolean smooth) {
		float batchColor = spriteBatch.getColor().toFloatBits();
		float down = this.data.down;
		int start = 0;
		int numLines = 0;
		int length = str.length();
		float maxWidth = 0;
		while (start < length) {
			int lineEnd = indexOf(str, '\n', start);
			float xOffset = 0;
			if (alignment != HAlignment.LEFT) {
				float lineWidth = getBounds(str, start, lineEnd).width;
				xOffset = alignmentWidth - lineWidth;
				if (alignment == HAlignment.CENTER) xOffset = xOffset / 2;
			}
			float lineWidth = draw(spriteBatch, str, x + xOffset, y, start, lineEnd, alpha, smooth).width;
			maxWidth = Math.max(maxWidth, lineWidth);
			start = lineEnd + 1;
			y += down;
			numLines++;
		}
		spriteBatch.setColor(batchColor);

		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}
	float startS,
		  initA;
	public TextBounds draw (SpriteBatch spriteBatch, CharSequence str, float x, float y, int start, int end, float alpha, boolean smooth) {
		initA = actColor.a;
		final float strLen = str.length();
		
		startS = start;
		float batchColor = spriteBatch.getColor().toFloatBits();
		spriteBatch.setColor(color);
		final Texture texture = region.getTexture();
		y += data.ascent;
		float startX = x;
		Glyph lastGlyph = null;
		if (data.scaleX == 1 && data.scaleY == 1) {
			if (integer) {
				y = (int)y;
				x = (int)x;
			}
			while (start < end) {
				lastGlyph = data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					
					actColor.a = Math.max(0, Math.min(1, (1-(start)/(strLen)+alpha)));
					if(!smooth && actColor.a < 1)
						actColor.a = 0;
					actColor.a *= initA;
					
					spriteBatch.setColor(actColor.toFloatBits());
					
					spriteBatch.draw(texture, //
									 x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
									 lastGlyph.width, lastGlyph.height, //
									 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					x += lastGlyph.xadvance;
					break;
				}
			}
			while (start < end) {
				
				actColor.a = Math.max(0, Math.min(1, (1-(start)/(strLen)+alpha)));
				if(!smooth && actColor.a < 1)
					actColor.a = 0;
				actColor.a *= initA;
				
				spriteBatch.setColor(actColor.toFloatBits());
				
				char ch = str.charAt(start++);
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch);
				if (integer) x = (int)x;
				lastGlyph = g;
				spriteBatch.draw(texture, //
								 x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
								 lastGlyph.width, lastGlyph.height, //
								 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				x += g.xadvance;
			}
		} else {
			float scaleX = this.data.scaleX, scaleY = this.data.scaleY;
			while (start < end) {
				
				actColor.a = Math.max(0, Math.min(1, (1-(start)/(strLen)+alpha)));
				if(!smooth && actColor.a < 1)
					actColor.a = 0;
				actColor.a *= initA;
				
				spriteBatch.setColor(actColor.toFloatBits());
				
				lastGlyph = data.getGlyph(str.charAt(start++));
				if (lastGlyph != null) {
					if (!integer) {
						spriteBatch.draw(texture, //
										 x + lastGlyph.xoffset * scaleX, //
										 y + lastGlyph.yoffset * scaleY, //
										 lastGlyph.width * scaleX, //
										 lastGlyph.height * scaleY, //
										 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					} else {
						spriteBatch.draw(texture, //
										 (int)(x + lastGlyph.xoffset * scaleX), //
										 (int)(y + lastGlyph.yoffset * scaleY), //
										 (int)(lastGlyph.width * scaleX), //
										 (int)(lastGlyph.height * scaleY), //
										 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					}
					x += lastGlyph.xadvance * scaleX;
					break;
				}
			}
			while (start < end) {
				
				actColor.a = Math.max(0, Math.min(1, (1-(start)/(strLen)+alpha)));
				if(!smooth && actColor.a < 1)
					actColor.a = 0;
				actColor.a *= initA;
				
				spriteBatch.setColor(actColor.toFloatBits());
				
				char ch = str.charAt(start++);
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch) * scaleX;
				lastGlyph = g;
				if (!integer) {
					spriteBatch.draw(texture, //
						x + lastGlyph.xoffset * scaleX, //
						y + lastGlyph.yoffset * scaleY, //
						lastGlyph.width * scaleX, //
						lastGlyph.height * scaleY, //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				} else {
					spriteBatch.draw(texture, //
						(int)(x + lastGlyph.xoffset * scaleX), //
						(int)(y + lastGlyph.yoffset * scaleY), //
						(int)(lastGlyph.width * scaleX), //
						(int)(lastGlyph.height * scaleY), //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				}
				x += g.xadvance * scaleX;
			}
		}
		spriteBatch.setColor(batchColor);
		textBounds.width = x - startX;
		textBounds.height = data.capHeight;
		
		actColor.a = initA;
		
		return textBounds;
	}

  //DRAWING CHAR ARRAYS

	public TextBounds drawMultiLine (SpriteBatch spriteBatch, char str[], float x, float y, float alignmentWidth, HAlignment alignment) {
			float batchColor = spriteBatch.getColor().toFloatBits();
			float down = this.data.down;
			int start = 0;
			int numLines = 0;
			int length = str.length;
			float maxWidth = 0;
			while (start < length) {
				int lineEnd = length;
				for(int i=start; i<length; i++){
					if(str[i] == '\n'){
						lineEnd = i;
						break;
					}
				}
				float xOffset = 0;
				if (alignment != HAlignment.LEFT) {
					float lineWidth = getBounds(str, start, lineEnd).width;
					xOffset = alignmentWidth - lineWidth;
					if (alignment == HAlignment.CENTER) xOffset = xOffset / 2;
				}
				float lineWidth = draw(spriteBatch, str, x + xOffset, y, start, lineEnd).width;
				maxWidth = Math.max(maxWidth, lineWidth);
				start = lineEnd + 1;
				y += down;
				numLines++;
			}
			spriteBatch.setColor(batchColor);

			textBounds.width = maxWidth;
			textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
			return textBounds;
		}
	
	/*
     * (non-Javadoc)
     * @see com.badlogic.gdx.graphics.g2d.BitmapFont#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch, java.lang.CharSequence, float, float, int, int)
     */
	public TextBounds draw (SpriteBatch spriteBatch, char str[], float x, float y, int start, int end) {
		float batchColor = spriteBatch.getColor().toFloatBits();
		spriteBatch.setColor(color);
		final Texture texture = region.getTexture();
		y += data.ascent;
		float startX = x;
		Glyph lastGlyph = null;
		if (data.scaleX == 1 && data.scaleY == 1) {
			if (integer) {
				y = (int)y;
				x = (int)x;
			}
			while (start < end) {
				lastGlyph = data.getGlyph(str[start++]);
				if (lastGlyph != null) {
					spriteBatch.draw(texture, //
									 x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
									 lastGlyph.width, lastGlyph.height, //
									 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					x += lastGlyph.xadvance;
					break;
				}
			}
			while (start < end) {
				char ch = str[start++];
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch);
				if (integer) x = (int)x;
				lastGlyph = g;
				spriteBatch.draw(texture, //
								 x + lastGlyph.xoffset, y + lastGlyph.yoffset, //
								 lastGlyph.width, lastGlyph.height, //
								 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				x += g.xadvance;
			}
		} else {
			float scaleX = this.data.scaleX, scaleY = this.data.scaleY;
			while (start < end) {
				lastGlyph = data.getGlyph(str[start++]);
				if (lastGlyph != null) {
					if (!integer) {
						spriteBatch.draw(texture, //
										 x + lastGlyph.xoffset * scaleX, //
										 y + lastGlyph.yoffset * scaleY, //
										 lastGlyph.width * scaleX, //
										 lastGlyph.height * scaleY, //
										 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					} else {
						spriteBatch.draw(texture, //
										 (int)(x + lastGlyph.xoffset * scaleX), //
										 (int)(y + lastGlyph.yoffset * scaleY), //
										 (int)(lastGlyph.width * scaleX), //
										 (int)(lastGlyph.height * scaleY), //
										 lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
					}
					x += lastGlyph.xadvance * scaleX;
					break;
				}
			}
			while (start < end) {
				char ch = str[start++];
				Glyph g = data.getGlyph(ch);
				if (g == null) continue;
				x += lastGlyph.getKerning(ch) * scaleX;
				lastGlyph = g;
				if (!integer) {
					spriteBatch.draw(texture, //
						x + lastGlyph.xoffset * scaleX, //
						y + lastGlyph.yoffset * scaleY, //
						lastGlyph.width * scaleX, //
						lastGlyph.height * scaleY, //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				} else {
					spriteBatch.draw(texture, //
						(int)(x + lastGlyph.xoffset * scaleX), //
						(int)(y + lastGlyph.yoffset * scaleY), //
						(int)(lastGlyph.width * scaleX), //
						(int)(lastGlyph.height * scaleY), //
						lastGlyph.u, lastGlyph.v, lastGlyph.u2, lastGlyph.v2);
				}
				x += g.xadvance * scaleX;
			}
		}
		spriteBatch.setColor(batchColor);
		textBounds.width = x - startX;
		textBounds.height = data.capHeight;
		return textBounds;
	}
	
	/** Draws a string, which may contain newlines (\n), with the specified position. Each line is automatically wrapped to keep it
	 * within a rectangle of the specified width.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawWrapped (SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth,
								   float alpha) {
		return drawWrapped(spriteBatch, str, x, y, wrapWidth, HAlignment.LEFT, alpha);
	}

	/** Draws a string, which may contain newlines (\n), with the specified position. Each line is automatically wrapped to keep it
	 * within a rectangle of the specified width, and aligned horizontally within that rectangle.
	 * @param x The x position for the left most character.
	 * @param y The y position for the top of most capital letters in the font (the {@link #getCapHeight() cap height}).
	 * @return The bounds of the rendered string (the height is the distance from y to the baseline of the last line). Note the
	 *         same TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds drawWrapped (SpriteBatch spriteBatch, CharSequence str, float x, float y, float wrapWidth,
								   HAlignment alignment, float alpha) {
		if (wrapWidth <= 0) wrapWidth = Integer.MAX_VALUE;
		float batchColor = spriteBatch.getColor().toFloatBits();
		float down = this.data.down;
		int start = 0;
		int numLines = 0;
		int length = str.length();
		float maxWidth = 0;
		while (start < length) {
			int newLine =BFonts.indexOf(str, '\n', start);
			// Eat whitespace at start of line.
			while (start < newLine) {
				if (!BFonts.isWhitespace(str.charAt(start))) break;
				start++;
			}
			int lineEnd = start + computeVisibleGlyphs(str, start, newLine, wrapWidth);
			int nextStart = lineEnd + 1;
			if (lineEnd < newLine) {
				// Find char to break on.
				while (lineEnd > start) {
					if (BFonts.isWhitespace(str.charAt(lineEnd))) break;
					lineEnd--;
				}
				if (lineEnd == start)
					lineEnd = nextStart; // If no characters to break, show all.
				else {
					nextStart = lineEnd;
					// Eat whitespace at end of line.
					while (lineEnd > start) {
						if (!BFonts.isWhitespace(str.charAt(lineEnd - 1))) break;
						lineEnd--;
					}
				}
			} else
				nextStart = lineEnd + 1;
			if (lineEnd > start) {
				float xOffset = 0;
				if (alignment != HAlignment.LEFT) {
					float lineWidth = getBounds(str, start, lineEnd).width;
					xOffset = wrapWidth - lineWidth;
					if (alignment == HAlignment.CENTER) xOffset /= 2;
				}
				float lineWidth = draw(spriteBatch, str, x + xOffset, y, start, lineEnd).width; 
				maxWidth = Math.max(maxWidth, lineWidth);
			}
			start = nextStart;
			y += down;
			numLines++;
		}
		spriteBatch.setColor(batchColor);
		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}

	/** Returns the size of the specified string. The height is the distance from the top of most capital letters in the font (the
	 * {@link #getCapHeight() cap height}) to the baseline. Note the same TextBounds instance is used for all methods that return
	 * TextBounds. */
	public TextBounds getBounds (CharSequence str) {
		return getBounds(str, 0, str.length());
	}

	/** Returns the size of the specified substring. The height is the distance from the top of most capital letters in the font
	 * (the {@link #getCapHeight() cap height}) to the baseline. Note the same TextBounds instance is used for all methods that
	 * return TextBounds.
	 * @param start The first character of the string.
	 * @param end The last character of the string (exclusive). */
	public TextBounds getBounds (CharSequence str, int start, int end) {
		int width = 0;
		Glyph lastGlyph = null;
		while (start < end) {
			lastGlyph = data.getGlyph(str.charAt(start++));
			if (lastGlyph != null) {
				width = lastGlyph.xadvance;
				break;
			}
		}
		while (start < end) {
			char ch = str.charAt(start++);
			Glyph g = data.getGlyph(ch);
			if (g != null) {
				width += lastGlyph.getKerning(ch);
				lastGlyph = g;
				width += g.xadvance;
			}
		}
		textBounds.width = width * data.scaleX;
		textBounds.height = data.capHeight;
		return textBounds;
	}	
	public TextBounds getBounds (char str[], int start, int end) {
		int width = 0;
		Glyph lastGlyph = null;
		while (start < end) {
			lastGlyph = data.getGlyph(str[start++]);
			if (lastGlyph != null) {
				width = lastGlyph.xadvance;
				break;
			}
		}
		while (start < end) {
			char ch = str[start++];
			Glyph g = data.getGlyph(ch);
			if (g != null) {
				width += lastGlyph.getKerning(ch);
				lastGlyph = g;
				width += g.xadvance;
			}
		}
		textBounds.width = width * data.scaleX;
		textBounds.height = data.capHeight;
		return textBounds;
	}	

	/** Returns the size of the specified string, which may contain newlines. The height is the distance from the top of most
	 * capital letters in the font (the {@link #getCapHeight() cap height}) to the baseline of the last line of text. Note the same
	 * TextBounds instance is used for all methods that return TextBounds. */
	public TextBounds getMultiLineBounds (CharSequence str) {
		int start = 0;
		float maxWidth = 0;
		int numLines = 0;
		int length = str.length();
		while (start < length) {
			int lineEnd = indexOf(str, '\n', start);
			float lineWidth = getBounds(str, start, lineEnd).width;
			maxWidth = Math.max(maxWidth, lineWidth);
			start = lineEnd + 1;
			numLines++;
		}
		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}

	/** Returns the size of the specified string, which may contain newlines and is wrapped to keep it within a rectangle of the
	 * specified width. The height is the distance from the top of most capital letters in the font (the {@link #getCapHeight() cap
	 * height}) to the baseline of the last line of text. Note the same TextBounds instance is used for all methods that return
	 * TextBounds. */
	public TextBounds getWrappedBounds (CharSequence str, float wrapWidth) {
		if (wrapWidth <= 0) wrapWidth = Integer.MAX_VALUE;
		//float down = this.data.down;
		int start = 0;
		int numLines = 0;
		int length = str.length();
		float maxWidth = 0;
		while (start < length) {
			int newLine = BFonts.indexOf(str, '\n', start);
			// Eat whitespace at start of line.
			while (start < newLine) {
				if (!BFonts.isWhitespace(str.charAt(start))) break;
				start++;
			}
			int lineEnd = start + computeVisibleGlyphs(str, start, newLine, wrapWidth);
			int nextStart = lineEnd + 1;
			if (lineEnd < newLine) {
				// Find char to break on.
				while (lineEnd > start) {
					if (BFonts.isWhitespace(str.charAt(lineEnd))) break;
					lineEnd--;
				}
				if (lineEnd == start)
					lineEnd = nextStart; // If no characters to break, show all.
				else {
					nextStart = lineEnd;
					// Eat whitespace at end of line.
					while (lineEnd > start) {
						if (!BFonts.isWhitespace(str.charAt(lineEnd - 1))) break;
						lineEnd--;
					}
				}
			}
			if (lineEnd > start) {
				float lineWidth = getBounds(str, start, lineEnd).width;
				maxWidth = Math.max(maxWidth, lineWidth);
			}
			start = nextStart;
			numLines++;
		}
		textBounds.width = maxWidth;
		textBounds.height = data.capHeight + (numLines - 1) * data.lineHeight;
		return textBounds;
	}

	/** Computes the glyph advances for the given character sequence and stores them in the provided {@link FloatArray}. The
	 * FloatArray is cleared. This will add an additional element at the end.
	 * @param str the character sequence
	 * @param glyphAdvances the glyph advances output array.
	 * @param glyphPositions the glyph positions output array. */
	public void computeGlyphAdvancesAndPositions (CharSequence str, FloatArray glyphAdvances, FloatArray glyphPositions) {
		glyphAdvances.clear();
		glyphPositions.clear();
		int index = 0;
		int end = str.length();
		int width = 0;
		Glyph lastGlyph = null;
		if (data.scaleX == 1) {
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch);
					lastGlyph = g;
					glyphAdvances.add(g.xadvance);
					glyphPositions.add(width);
					width += g.xadvance;
				}
			}
			glyphAdvances.add(0);
			glyphPositions.add(width);
		} else {
			float scaleX = this.data.scaleX;
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch) * scaleX;
					lastGlyph = g;
					glyphAdvances.add(g.xadvance * scaleX);
					glyphPositions.add(width);
					width += g.xadvance;
				}
			}
			glyphAdvances.add(0);
			glyphPositions.add(width);
		}
	}

	/** Returns the number of glyphs from the substring that can be rendered in the specified width.
	 * @param start The first character of the string.
	 * @param end The last character of the string (exclusive). */
	public int computeVisibleGlyphs (CharSequence str, int start, int end, float availableWidth) {
		int index = start;
		int width = 0;
		Glyph lastGlyph = null;
		if (data.scaleX == 1) {
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch);
					if (width + g.xadvance > availableWidth) break;
					width += g.xadvance;
					lastGlyph = g;
				}
			}
		} else {
			float scaleX = this.data.scaleX;
			for (; index < end; index++) {
				char ch = str.charAt(index);
				Glyph g = data.getGlyph(ch);
				if (g != null) {
					if (lastGlyph != null) width += lastGlyph.getKerning(ch) * scaleX;
					if (width + g.xadvance * scaleX > availableWidth) break;
					width += g.xadvance * scaleX;
					lastGlyph = g;
				}
			}
		}
		return index - start;
	}

	public void setColor (float color) {
		this.color = color;
	}

	public void setColor (Color tint) {
		actColor.set(tint);
		this.color = tint.toFloatBits();
	}

	public void setColor (float r, float g, float b, float a) {
		int intBits = (int)(255 * a) << 24 | (int)(255 * b) << 16 | (int)(255 * g) << 8 | (int)(255 * r);
		color = NumberUtils.intToFloatColor(intBits);
		actColor.set(r, g, b, a);
	}

	/** Returns the color of this font. Changing the returned color will have no affect, {@link #setColor(Color)} or
	 * {@link #setColor(float, float, float, float)} must be used. */
	public Color getColor () {
		int intBits = NumberUtils.floatToIntColor(color);
		Color color = this.tempColor;
		color.r = (intBits & 0xff) / 255f;
		color.g = ((intBits >>> 8) & 0xff) / 255f;
		color.b = ((intBits >>> 16) & 0xff) / 255f;
		color.a = ((intBits >>> 24) & 0xff) / 255f;
		return color;
	}

	public void setScale (float scaleX, float scaleY) {
		data.spaceWidth = data.spaceWidth / this.data.scaleX * scaleX;
		data.xHeight = data.xHeight / this.data.scaleY * scaleY;
		data.capHeight = data.capHeight / this.data.scaleY * scaleY;
		data.ascent = data.ascent / this.data.scaleY * scaleY;
		data.descent = data.descent / this.data.scaleY * scaleY;
		data.down = data.down / this.data.scaleY * scaleY;
		data.scaleX = scaleX;
		data.scaleY = scaleY;
	}

	/** Scales the font by the specified amount in both directions.<br>
	 * <br>
	 * Note that smoother scaling can be achieved if the texture backing the BitmapFont is using {@link TextureFilter#Linear}. The
	 * default is Nearest, so use a BitmapFont constructor that takes a {@link TextureRegion}. */
	public void setScale (float scaleXY) {
		setScale(scaleXY, scaleXY);
	}

	/** Sets the font's scale relative to the current scale. */
	public void scale (float amount) {
		setScale(data.scaleX + amount, data.scaleY + amount);
	}

	public float getScaleX () {
		return data.scaleX;
	}

	public float getScaleY () {
		return data.scaleY;
	}

	public TextureRegion getRegion () {
		return region;
	}

	/** Returns the line height, which is the distance from one line of text to the next. */
	public float getLineHeight () {
		return data.lineHeight;
	}

	/** Returns the width of the space character. */
	public float getSpaceWidth () {
		return data.spaceWidth;
	}

	/** Returns the x-height, which is the distance from the top of most lowercase characters to the baseline. */
	public float getXHeight () {
		return data.xHeight;
	}

	/** Returns the cap height, which is the distance from the top of most uppercase characters to the baseline. Since the drawing
	 * position is the cap height of the first line, the cap height can be used to get the location of the baseline. */
	public float getCapHeight () {
		return data.capHeight;
	}

	/** Returns the ascent, which is the distance from the cap height to the top of the tallest glyph. */
	public float getAscent () {
		return data.ascent;
	}

	/** Returns the descent, which is the distance from the bottom of the glyph that extends the lowest to the baseline. This number
	 * is negative. */
	public float getDescent () {
		return data.descent;
	}

	/** Returns true if this BitmapFont has been flipped for use with a y-down coordinate system. */
	public boolean isFlipped () {
		return flipped;
	}

	/** Disposes the texture used by this BitmapFont's region IF this BitmapFont created the texture. */
	public void dispose () {
		if (ownsTexture) region.getTexture().dispose();
	}

	/** Makes the specified glyphs fixed width. This can be useful to make the numbers in a font fixed width. Eg, when horizontally
	 * centering a score or loading percentage text, it will not jump around as different numbers are shown. */
	public void setFixedWidthGlyphs (CharSequence glyphs) {
		int maxAdvance = 0;
		for (int index = 0, end = glyphs.length(); index < end; index++) {
			Glyph g = data.getGlyph(glyphs.charAt(index));
			if (g != null && g.xadvance > maxAdvance) maxAdvance = g.xadvance;
		}
		for (int index = 0, end = glyphs.length(); index < end; index++) {
			Glyph g = data.getGlyph(glyphs.charAt(index));
			if (g == null) continue;
			g.xoffset += (maxAdvance - g.xadvance) / 2;
			g.xadvance = maxAdvance;
			g.kerning = null;
		}
	}

	/** @param character
	 * @return whether the given character is contained in this font. */
	public boolean containsCharacter (char character) {
		return data.getGlyph(character) != null;
	}

	/** Specifies whether to use integer positions or not. Default is to use them so filtering doesn't kick in as badly.
	 * @param use */
	public void setUseIntegerPositions (boolean use) {
		this.integer = use;
	}

	/** @return whether this font uses integer positions for drawing. */
	public boolean usesIntegerPositions () {
		return integer;
	}

	public BitmapFontData getData () {
		return data;
	}
	
	static int indexOf (CharSequence text, char ch, int start) {
		final int n = text.length();
		for (; start < n; start++)
			if (text.charAt(start) == ch) return start;
		return n;
	}
	
	static boolean isWhitespace (char c) {
		switch (c) {
		case '\n':
		case '\r':
		case '\t':
		case ' ':
			return true;
		default:
			return false;
		}
	}
}
