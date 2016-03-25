package sandog;

import processing.core.PApplet;
import processing.core.PImage;

@SuppressWarnings("serial")
public class ResizingWebImages extends PApplet {

	String url = "https://s3.amazonaws.com/eb-blog-blog/wp-content/uploads/ada-lovelace.jpg";
	PImage ada;
	
	public void setup() {
		size(800	, 800, OPENGL);
		background(0, 0, 0);
		ada = loadImage(url, "jpg");
	}
	
	public void draw() {
		ada.resize(width, height);
		image(ada, 0, 0);
	}

}
