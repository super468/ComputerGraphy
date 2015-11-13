package HexagonTest;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//参考数据：(150,200),(175,250),(225,250),(250,200),(225,150),(175,150)
public class HexagonTest {
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				//用正则表达式读取字符串，并转化成double型的点
				String s=JOptionPane.showInputDialog(null,"请输入特征多边形各点,用括号扩起并用逗号隔开！左下角为（0，0）");
				Pattern p=Pattern.compile("\\d+");
				Matcher m=p.matcher(s);
				List<String> an=new ArrayList<String>();
				while(m.find()){
					an.add(m.group());
				}
				int count=0;
				//输入顶点
				MyComponent.Initcp();
				for(int i=0;i<an.size();i+=2){
					MyComponent.setcp(Double.parseDouble(an.get(i)),Double.parseDouble(an.get(i+1)),count++);
				}
				
				JFrame frame=new ImageFrame();
				frame.setTitle("填充六边形");
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
	private static int CP_LENGTH=10;
	private static Point2D.Double[] cp=new Point2D.Double[CP_LENGTH];
	private static int index=0;
	private ArrayList<NET> Table;
	
	//给输入顶点分配内存空间
	public static void Initcp(){
		for(int i=0;i<CP_LENGTH;i++){
			cp[i]=new Point2D.Double();
		}
	}
	
	//接受输入来的顶点
	public static void setcp(double x,double y,int i){
		MyComponent.cp[i].x=x;
		MyComponent.cp[i].y=y;
		index=i;
	}
	
	
	
	
	//初始化新边表
	public void InitTable(){
		double min=10000,max=-1000;
		for(int i=0;i<=index;i++){
			if(cp[i].y>max){
				max=cp[i].y;
			}
			if(cp[i].y<min){
				min=cp[i].y;
			}
		}
		Table=new ArrayList();
		for(int j=(int)min;j<=(int)max;j++){
			Table.add(new NET(j));
		}
		
		for(int m=0;m<index;m++){
			int maxyi=cp[m].y>cp[m+1].y?m:m+1;
			int minyi=cp[m].y>cp[m+1].y?m+1:m;
			double x1,x2,y1,y2,dx;
			x1=cp[minyi].x;
			x2=cp[maxyi].x;
			y1=cp[minyi].y;
			y2=cp[maxyi].y;
			if(y2==y1)dx=0;else
			dx=(x2-x1)/(y2-y1);
			for(int i=0;i<Table.size();i++){
				if(Table.get(i).getScanner()==(int)cp[minyi].y){
					Table.get(i).getAET().addAE(x1, dx, y2);
					break;
				}
			}		
		}
		
		int maxyi=cp[0].y>cp[index].y?0:index;
		int minyi=cp[0].y>cp[index].y?index:0;
		double x1,x2,y1,y2,dx;
		x1=cp[minyi].x;
		x2=cp[maxyi].x;
		y1=cp[minyi].y;
		y2=cp[maxyi].y;
		if(y2==y1)dx=0;else
		dx=(x2-x1)/(y2-y1);
		for(int i=0;i<Table.size();i++){
			if(Table.get(i).getScanner()==(int)cp[minyi].y){
				Table.get(i).getAET().addAE(x1, dx, y2);
				break;
			}
		}		
		
		for(int i=0;i<Table.size();i++){
			sort(i);
		}
			
	}
	
	//活性边中的各交点按照x的升序排序
	public void sort(int i){
		if(Table.get(i).getAET()!=null){
			for(int j=0;j<Table.get(i).getAET().AETLength()-1;j++){
				for(int m=0;m<Table.get(i).getAET().AETLength()-j;m++){
					if(Table.get(i).getAET().getAE(j).returnx()>Table.get(i).getAET().getAE(j+1).returnx()){
						AE t;
						t=Table.get(i).getAET().getAE(j);
						Table.get(i).getAET().setAE(j,Table.get(i).getAET().getAE(j+1));
						Table.get(i).getAET().setAE(j+1,t);
					}
				}
			}
		}
	}	
	
	//填充
	public void paintComponent(Graphics g){
		
		InitTable();//初始化新边表
		for(int i=0;i<Table.size()-1;i++){	//从下到上依次扫描线
			sort(i);//现将各点按x值升序排序
			for(int j=0;j<Table.get(i).getAET().AETLength()-1;j+=2){	//两两匹配
				
				double x1=Table.get(i).getAET().getAE(j).returnx();
				double x2=Table.get(i).getAET().getAE(j+1).returnx();
				double y=Table.get(i).getScanner();
				
				g.drawLine((int)x1, 400-(int)y, (int)x2, 400-(int)y);	//填充［x1,x2]之间的点
				if(x1==x2)j--;	//如果两个点为同一点，则保留一个点与下一个点匹配
			}
			
			for(int m=0;m<Table.get(i).getAET().AETLength();m++){		//将各条边加上增量并判断是否加入上一条扫描线
				if(Table.get(i).getAET().getAE(m).returnymax()>Table.get(i).getScanner()){	//未到达顶点
					double maxy=Table.get(i).getAET().getAE(m).returnymax();
					double dx=Table.get(i).getAET().getAE(m).returndx();
					double x=Table.get(i).getAET().getAE(m).returnx();
					Table.get(i+1).getAET().addAE(x+dx, dx, maxy);	//加入下一条扫描线
				}
			}
		}
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(DEFUALT_WIDTH,DEFUALT_HEIGHT);
	}
}

//活性边
class AE{
	private double x;	//交点X坐标
	private double dx;	//增量
	private double ymax;	//顶点
	
	public AE(double wx,double wdx,double wymax){
		x=wx;
		dx=wdx;
		ymax=wymax;
	}
	
	public double returnx(){
		return x;
	}
	
	public double returndx(){
		return dx;
	}
	
	public double returnymax(){
		return ymax;
	}
}

//活性边表
class AET{
	private LinkedList<AE> list;
	AET(){
		list=new LinkedList();
	}
	
	public void addAE(double x,double dx,double maxy){
		list.add(new AE(x,dx,maxy));
	}
	
	public int AETLength(){
		return list.size();
	}
	
	public AE getAE(int i){
		return list.get(i);
	}
	
	public void setAE(int i,AE t){
		list.set(i,t);
	}
}

//新边表
class NET{
	private int Scanner;	//扫描线
	private AET list;	//活性边表
	NET(int i){
		list=new AET();
		//list=null;
		Scanner=i;
	}
	
	public int getScanner(){
		return Scanner;
	}
	
	public AET getAET(){
		return list;
	}

}
