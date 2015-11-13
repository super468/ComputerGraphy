package TurnHexagon;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TurnHexagonTest {
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				String s=JOptionPane.showInputDialog(null,"请输入特征多边形各点,用括号扩起并用逗号隔开！左下角为（0，0）");
				Pattern p=Pattern.compile("\\d+");
				Matcher m=p.matcher(s);
				List<String> an=new ArrayList<String>();
				while(m.find()){
					an.add(m.group());
				}	
				int count=0;
				for(int i=0;i<an.size();i+=2){
					MyComponent.Input(Double.parseDouble(an.get(i)),Double.parseDouble(an.get(i+1)),count++);
				}
				JFrame frame=new ImageFrame();
				frame.setTitle("旋转六边形");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

class ImageFrame extends JFrame{
	public ImageFrame(){
		add(new MyComponent());
		pack();
	}
}

class MyComponent extends JComponent{
	private static final int DEFUALT_WIDTH=400;
	private static final int DEFUALT_HEIGHT=400;
	private static Point2D.Double[] cp=new Point2D.Double[10];
	private static int index=0;
	
	public static void Input(double x,double y,int i){
		cp[i]=new Point2D.Double(x,y);
		index=i;
	}
	
	public static void line(double x,double y,int i)
	   {
		   double theta;//旋转角度
		   double mycos;
		   double mysin;
		   double XX,YY,X1,Y1;
		   //定点   
		   X1=1;
		   Y1=1;
		   theta=Math.PI/6;
		   mycos = Math.cos(theta);
		   mysin = Math.sin(theta);
		   XX=x-X1;
		   YY=y-Y1;
		   
		   x=XX*mycos-YY*mysin;
		   y=XX*mysin+YY*mycos;

		   x=x+X1;
		   y=y+Y1;
		   cp[i].x=x;cp[i].y=y;
		 }
	
	public void paintComponent(Graphics g){
		//g.drawLine(1, 400-1, 1, 400-1);
		for(int j=0;j<index;j++){
			g.drawLine((int)cp[j].x,400-(int)cp[j].y, (int)cp[j+1].x, 400-(int)cp[j+1].y);
		}
		g.drawLine((int)cp[index].x,400-(int)cp[index].y, (int)cp[0].x, 400-(int)cp[0].y);
		for(int j=0;j<=index;j++){
			g.drawLine((int)cp[j].x,400-(int)cp[j].y, 1, 399);
		}
		for(int i=0;i<=index;i++){
			line(cp[i].x,cp[i].y,i);
			
		}
		for(int j=0;j<index;j++){
			g.drawLine((int)cp[j].x, 400-(int)cp[j].y, (int)cp[j+1].x, 400-(int)cp[j+1].y);
		}
		g.drawLine((int)cp[index].x,400-(int)cp[index].y, (int)cp[0].x, 400-(int)cp[0].y);
		for(int j=0;j<=index;j++){
			g.drawLine((int)cp[j].x,400-(int)cp[j].y, 1, 399);
		}
	}
	public Dimension getPreferredSize(){
		return new Dimension(DEFUALT_WIDTH,DEFUALT_HEIGHT);
	}
}
