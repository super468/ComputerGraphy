import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class DrawOvalTest {
	public static void main(String[] args){
		String s=JOptionPane.showInputDialog(null,"请输入长短半径a和b,用逗号隔开！");
		Pattern p=Pattern.compile("\\d+");
		Matcher m=p.matcher(s);
		List<String> an=new ArrayList<String>();
		while(m.find()){
			an.add(m.group());
		}
		int a=Integer.parseInt(an.get(0));
		int b=Integer.parseInt(an.get(1));
		num.seta(a);
		num.setb(b);
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				ImageFrame frame=new ImageFrame();
				
				frame.setTitle("绘制椭圆");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

class num{
	private static int a;
	private static int b;
	public static void seta(int wa){
		num.a=wa;
	}
	public static void setb(int wb){
		num.b=wb;
	}
	public static int geta(){
		return num.a;
	}
	
	public static int getb(){
		return num.b;
	}
}

class ImageFrame extends JFrame{
	public ImageFrame(){
		add(new MyComponent());
		MyComponent.Count(num.geta(),num.getb());
		pack();
	}
}

class MyComponent extends JComponent{
	private static final int DEFUALT_WIDTH=400;
	private static final int DEFUALT_HEIGHT=400;
	private static int[] xpoint=new int[2000];
	private static int[] ypoint=new int[2000];
	private static int index;
	
	public static void Count(int a,int b){

		index=0;
		int x,y;
		double d1,d2;
		x=0;y=b;
		d1=b*b+a*a*(-b+0.25);
		xpoint[index]=x;
		ypoint[index++]=y;
		//第一象限上半部
		while(b*b*(x+1)<a*a*(y-0.5)){
			if(d1<0){
				d1+=b*b*(2*x+3);
				x++;
				xpoint[index]=x;
				ypoint[index++]=y;
			}
			else{
				d1+=(b*b*(2*x+3)+a*a*(-2*y+2));
				x++;
				xpoint[index]=x;
				y--;
				ypoint[index++]=y;
			}
		}
		//第一象限下半部
		d2=(b*(x+0.5))*(b*(x+0.5))+(a*(y-1))*(a*(y-1))-(a*b)*(a*b);
		while(y>0){
			if(d2<0){
				d2+=b*b*(2*x+2)+a*a*(-2*y+3);
				x++;
				xpoint[index]=x;
				y--;
				ypoint[index++]=y;
			}
			else{
				d2+=a*a*(-2*y+3);
				xpoint[index]=x;
				y--;
				ypoint[index++]=y;
			}
		}
		//第四象限
		for(int i=index;i<2*index;i++){
			xpoint[i]=xpoint[2*index-i-1];
			ypoint[i]=-1*ypoint[2*index-i-1];
		}
		//第三象限
		for(int i=2*index;i<3*index;i++){
			xpoint[i]=xpoint[i-2*index]*-1;
			ypoint[i]=ypoint[i-2*index]*-1;
		}
		//第二象限
		for(int i=3*index;i<4*index;i++){
			xpoint[i]=xpoint[4*index-i-1]*-1;
			ypoint[i]=ypoint[4*index-i-1];
		}
		
	}
	
	public void paintComponent(Graphics g){
		for(int i=0;i<index*4;i++){
			xpoint[i]+=200;
			ypoint[i]=200-ypoint[i];
		}
		g.drawPolyline(xpoint, ypoint, 4*index);
		g.drawLine(50, 200, 350, 200);
		g.drawLine(200, 350, 200, 50);
		g.drawLine(200, 50, 195, 55);
		g.drawLine(200, 50, 205, 55);
		g.drawString("Y", 220, 55);
		g.drawLine(350, 200, 345, 195);
		g.drawLine(350, 200, 345, 205);
		g.drawString("X", 370, 200);
		
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(DEFUALT_WIDTH,DEFUALT_HEIGHT);
	}
}
