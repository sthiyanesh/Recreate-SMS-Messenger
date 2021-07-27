import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class trying{
	public static String s,s1,s2,a;
	public static String t = "";
	public static File f;
	public static JTextArea jta4 = new JTextArea();
	public static String logger1(String s,String s1){
		String str = "";
		int flag1 = 0;
		for(int i=0;i<3;i++){
			try{
				Socket SD = new Socket("localhost",3333);
				DataOutputStream dout = new DataOutputStream(SD.getOutputStream());
				DataInputStream din = new DataInputStream(SD.getInputStream());
				dout.writeUTF("LOGIN");
				dout.writeUTF(s);
				dout.writeUTF(s1);
				str = (String)din.readUTF();
				flag1=1;
				din.close();
				dout.flush();
				dout.close();
				SD.close();
				break;
			}catch(Exception e){
				System.out.println(e);
			}
		}
		if(flag1==0){
			return "-2";
		}
		flag1=0;
		return str;
	}
	public static void opener(){
		String temp;
		String Newfinder = "###----!!!UNREAD NEW MESSAGES!!!---###";
		try{
			t = "";
			f = new File("Users//"+a+"//"+s2+".txt");
			File f1  = new File("Users//"+a+"//"+s2+"temp.txt");
			boolean h = f1.createNewFile();
			if(f.createNewFile()){
				
			}else{
				FileReader fr3 = new FileReader("Users//"+a+"//"+s2+".txt");
				FileWriter fw3 = new FileWriter(f1);
				BufferedReader br = new BufferedReader(fr3);
				while((temp = br.readLine()) != null) {
					t += temp;
					t += "\n";
					if(!(temp.equals(Newfinder))){
						fw3.write(temp+"\n");
					}
				}
				fr3.close();
				fw3.close();
			}
			f.delete();
			boolean bool = f1.renameTo(f);
		}catch(Exception e){
			e.getStackTrace();
			System.out.println(e);
		}
	}
	public static boolean receive(){
		String str,str1;
		int flag1 = 0;
		FileWriter f1;
		for(int i=0;i<3;i++){
			try{
				Socket SD = new Socket("localhost",3333);
				DataOutputStream dout = new DataOutputStream(SD.getOutputStream());
				DataInputStream din = new DataInputStream(SD.getInputStream());
				flag1=1;
				dout.writeUTF("RECEIVE");
				dout.writeUTF(s);
				while(true){
					Thread.sleep(100);
					str = (String)din.readUTF();
					if(str.equals("#@#")){
						break;
					}
					File f = new File("Users//"+a+"//"+str);
					if(!(f.exists())){
						f.createNewFile();
						f1 = new FileWriter(f);
					}else{
						f1 = new FileWriter(f,true);
					}
					f1.write("###----!!!UNREAD NEW MESSAGES!!!---###\n");
					while(true){
						Thread.sleep(100);
						str1 = (String)din.readUTF();
						if(str1.equals("#@#")){
							break;
						}
						f1.write(str.substring(0,4)+":"+str1+"\n");
						notify(str.substring(0,4));
					}
					f1.close();
				}
				din.close();
				dout.flush();
				dout.close();
				SD.close();
				break;
			}catch(Exception e){
				System.out.println(e);
			}
		}
		if(flag1==0){
			return false;
		}
		flag1=0;
		return true;
	}
	public static void store(String msg){
		try{
			FileWriter f1 = new FileWriter("Users//"+a+"//"+s2+".txt",true);
			f1.write("You:"+msg+"\n");
			f1.close();
		}catch(Exception e){
			System.out.print(e);
		}
	}
	public static boolean send(String msg){
		int i,flag=0,flag1=0;
		for(i=0;i<3;i++){
			try{
				Socket SD = new Socket("localhost",Integer.parseInt(s2));
				DataOutputStream dout = new DataOutputStream(SD.getOutputStream());
				dout.writeUTF(s);
				dout.writeUTF(msg);
				flag=1;
				dout.flush();
				dout.close();
				SD.close();
				store(msg);
				break;
			}catch(Exception e){
				System.out.println(e);
			}
		}
		if(flag==0){
			for(i=0;i<3;i++){
				try{
					Socket SD = new Socket("localhost",3333);
					DataOutputStream dout = new DataOutputStream(SD.getOutputStream());
					DataInputStream din = new DataInputStream(SD.getInputStream());
					dout.writeUTF("SENDER");
					dout.writeUTF(s);
					dout.writeUTF(s2);
					dout.writeUTF(msg);
					String str = (String)din.readUTF();
					if(str.equals("true")){
						store(msg);
					}
					flag1=1;
					din.close();
					dout.flush();
					dout.close();
					SD.close();
					break;
				}catch(Exception e){
					System.out.print(e);
				}
			}
			if(flag1==0){
				JFrame jf = new JFrame("Alert");
				JOptionPane.showMessageDialog(jf,"SEVER TIMED OUT.Try Later.","Alert",JOptionPane.WARNING_MESSAGE);
				return false;
			}
			flag1=0;
			return true;
		}
		flag=0;
		return true;
	}
	public static void backrun(){
		while(true){
			try{
				ServerSocket ssD = new ServerSocket(Integer.parseInt(s));
				Socket sD = ssD.accept();
				DataInputStream din = new DataInputStream(sD.getInputStream());
				DataOutputStream dout = new DataOutputStream(sD.getOutputStream());
				Thread.sleep(1000);
				String bstr = (String)din.readUTF();
				if(bstr.equals("-1")){
					dout.close();
					din.close();
					sD.close();
					ssD.close();
					return;
				}
				File bf = new File("Users//"+a+"//"+bstr+".txt");
				FileWriter bfw;
				if(bf.exists()){
					bfw = new FileWriter(bf,true);
				}else{
					bf.createNewFile();
					bfw  = new FileWriter(bf);
				}
				String bmsg = (String)din.readUTF();
				if(!(s2.equals(bstr))){
					int flag = 0;
					String temp;
					FileReader bfr = new FileReader(bf);
					BufferedReader br = new BufferedReader(bfr);
					while((temp = br.readLine()) != null) {
						if(temp.equals("###----!!!UNREAD NEW MESSAGES!!!---###")){
							flag=1;
							break;
						}
					}
					if(flag == 0){
						bfw.write("###----!!!UNREAD NEW MESSAGES!!!---###\n");
					}
					flag=0;
				}
				bfw.write(bstr+":"+bmsg+"\n");
				notify(bstr);
				bfw.close();
				dout.close();
				din.close();
				sD.close();
				ssD.close();
				opener();
				jta4.setText(t);
			}catch(Exception e){
				System.out.println(e);
			}
		}
	}
	public static void notify(String str){
		try{
			int flag = 0;
			String temp;
			f = new File("Users//"+a+"//newmsgnotify.txt");
			File f1 = new File("Users//"+a+"//newmsgnotifytemp.txt");
			boolean bool = f1.createNewFile();
			String[] word = new String[2];
			FileReader nfr = new FileReader(f);
			FileWriter nfw = new FileWriter(f1);
			BufferedReader br = new BufferedReader(nfr);
			while((temp = br.readLine()) != null) {
				word = temp.split(" ");
				if(str.equals(word[0])){
					word[1] = Integer.toString((Integer.parseInt(word[1]))+1);
					nfw.write(word[0]+" "+word[1]+"\n");
					flag = 1;
				}
				else{
					nfw.write(temp);
				}
			}
			if(flag == 0){
				nfw.write(str+" 1\n");
			}
			nfr.close();
			nfw.close();
			f.delete();
			boolean val = f1.renameTo(f);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public static void unnotify(String str){
		try{
			int flag = 0;
			String temp;
			f = new File("Users//"+a+"//newmsgnotify.txt");
			File f1 = new File("Users//"+a+"//newmsgnotifytemp.txt");
			boolean bool = f1.createNewFile();
			String[] word = new String[2];
			FileReader nfr = new FileReader(f);
			FileWriter nfw = new FileWriter(f1);
			BufferedReader br = new BufferedReader(nfr);
			while((temp = br.readLine()) != null) {
				word = temp.split(" ");
				if(!(str.equals(word[0]))){
					nfw.write(temp);
				}
			}
			nfr.close();
			nfw.close();
			f.delete();
			boolean val = f1.renameTo(f);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public static String notifytext(){
		String temp,t="";
		String[] word = new String[2];
		try{
			f = new File("Users//"+a+"//newmsgnotify.txt");
			if(f.length()==0)
				return "No New Notifications";
			FileReader nfr = new FileReader(f);
			BufferedReader br = new BufferedReader(nfr);
			while((temp = br.readLine()) != null) {
				word = temp.split(" ");
				t += word[1]+" new message from "+word[0]+"\n";
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return t;
	}
	public static void closer(){
		int i;
		for(i=0;i<3;i++){
			try{
				Socket SD = new Socket("localhost",Integer.parseInt(s));
				DataOutputStream dout = new DataOutputStream(SD.getOutputStream());
				dout.writeUTF("-1");
				dout.flush();
				dout.close();
				SD.close();
				break;
			}catch(Exception e){
				System.out.println(e);
			}
		}
	}
	public static String register(String in,String in1,String in2,String in3){
		int i,flag =0;
		String str = "";
		for(i=0;i<3;i++){
			try{
				Socket SD = new Socket("localhost",3333);
				DataOutputStream dout = new DataOutputStream(SD.getOutputStream());
				DataInputStream din = new DataInputStream(SD.getInputStream());
				dout.writeUTF("REGISTER");
				dout.writeUTF(in);
				dout.writeUTF(in1);
				dout.writeUTF(in2);
				dout.writeUTF(in3);
				Thread.sleep(1000);
				str = (String)din.readUTF();
				dout.flush();
				dout.close();
				SD.close();
				flag = 1;
				break;
			}catch(Exception e){
				System.out.println(e);
			}
		}
		if(flag==0){
			return "-2";
		}
		flag = 0;
		return str;
	}
	public static String forgotpass(String in,String in1,String in2,String in3){
		int i,flag =0;
		String str = "";
		for(i=0;i<3;i++){
			try{
				Socket SD = new Socket("localhost",3333);
				DataOutputStream dout = new DataOutputStream(SD.getOutputStream());
				DataInputStream din = new DataInputStream(SD.getInputStream());
				dout.writeUTF("FORGOT");
				dout.writeUTF(in);
				dout.writeUTF(in1);
				dout.writeUTF(in2);
				dout.writeUTF(in3);
				Thread.sleep(1000);
				str = (String)din.readUTF();
				dout.flush();
				dout.close();
				SD.close();
				flag = 1;
				break;
			}catch(Exception e){
				System.out.println(e);
			}
		}
		if(flag==0){
			return "-2";
		}
		flag = 0;
		return str;
	}
	
	// Main() Method .....
	
	
	public static void main(String args[]){
		//Frames
		JFrame jf0 = new JFrame("Messages_Welcome");
		JFrame jf1 = new JFrame("Messages_Login");
		JFrame jf2 = new JFrame("Messages_Home");
		JFrame jf3 = new JFrame("Messages_Open");
		JFrame jf4 = new JFrame("Messages_Conversion");
		JFrame jf5 = new JFrame("Messages_Register");
		JFrame jf6 = new JFrame("Messages_Forget_Password");
		JTextArea jta2 = new JTextArea();
		
		//Frame 0
		JLabel jl0 = new JLabel("WELCOME TO MESSAGE APP");
		Image img0 = Toolkit.getDefaultToolkit().getImage("Images//register.jpg");
		Image newimg0 = img0.getScaledInstance(300, 70, java.awt.Image.SCALE_SMOOTH);
		JButton jb0 = new JButton(new ImageIcon(newimg0));
		Image img01 = Toolkit.getDefaultToolkit().getImage("Images//signin.jpg");
		Image newimg01 = img01.getScaledInstance(300, 70, java.awt.Image.SCALE_SMOOTH);
		JButton jb01 = new JButton(new ImageIcon(newimg01));
		jb01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				jf0.setVisible(false);
				jf1.setVisible(true);
			}
		});
		jb0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				jf0.setVisible(false);
				jf5.setVisible(true);
			}
		});
		Font f0 = new Font("Times New Roman",Font.BOLD+Font.ITALIC,30);
		Font f1 = new Font("Times New Roman",Font.BOLD+Font.ITALIC,25);
		jl0.setBounds(75,100,450,55);
		jb0.setBounds(150,200,300,70);
		jb01.setBounds(150,300,300,70);
		jl0.setHorizontalAlignment(JTextField.CENTER);
		jl0.setFont(f0);
		jf0.add(jb0);
		jf0.add(jb01);
		jf0.add(jl0);
		Image icon = Toolkit.getDefaultToolkit().getImage("Images//message.jpg");    
		jf0.setIconImage(icon);
		jf0.setLayout(null);
		jf0.setSize(650,650);
		jf0.setVisible(true);
		
		// Frame 1
		JLabel jl1 = new JLabel("Enter Your Unique ID:");
		JTextField jtf1 = new JTextField();
		JLabel jl01 = new JLabel("Enter Your Password:");
		JPasswordField jpf1 = new JPasswordField();
		Image img11 = Toolkit.getDefaultToolkit().getImage("Images//forgotpassword.png");
		Image newimg11 = img11.getScaledInstance(150, 35, java.awt.Image.SCALE_SMOOTH);
		JButton jb11 = new JButton(new ImageIcon(newimg11));
		jb11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				jf1.setVisible(false);
				jf6.setVisible(true);
			}
		});
		Image img = Toolkit.getDefaultToolkit().getImage("Images//enter.jpg");
		Image newimg = img.getScaledInstance(150, 35, java.awt.Image.SCALE_SMOOTH);
		JButton jb1 = new JButton(new ImageIcon(newimg));
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				s = jtf1.getText();
				s1 = String.valueOf(jpf1.getPassword());
				System.out.println(s+" "+s1+"5");
				a = logger1(s,s1);
				System.out.println(a);
				if(a.equals("-2")){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Server Timed out Try Later","Alert",JOptionPane.WARNING_MESSAGE);
				}
				else if(a.equals("-1")){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Invalid UserID or Password","Alert",JOptionPane.WARNING_MESSAGE);
				}else{
					JFrame jfload = new JFrame("Success");
					JOptionPane.showMessageDialog(jfload,"Login Succesfull.\nLoading Your New Messages Please Wait");
					jf1.setVisible(false);
					jf2.setVisible(true);
					if(!(receive())){
						JFrame jf = new JFrame("Alert");
						JOptionPane.showMessageDialog(jf,"Server Timed out Try Later","Alert",JOptionPane.WARNING_MESSAGE);
					}
					jta2.setText(notifytext());
					new Thread(){
						public void run(){
							backrun();
						}
					}.start();
				}
			}
		});
		jl1.setBounds(100,100,300,50);
		jtf1.setBounds(175,150,150,35);
		jl01.setBounds(100,200,300,50);
		jpf1.setBounds(175,250,150,35);
		jb11.setBounds(175,300,150,35);
		jb1.setBounds(175,350,150,35);
		jtf1.setHorizontalAlignment(JTextField.CENTER);
		jl1.setHorizontalAlignment(JTextField.CENTER);
		jpf1.setHorizontalAlignment(JTextField.CENTER);
		jl01.setHorizontalAlignment(JTextField.CENTER);
		jtf1.setFont(f1);
		jl1.setFont(f1);
		jpf1.setFont(f1);
		jl01.setFont(f1);
		jf1.add(jb1);
		jf1.add(jb11);
		jf1.add(jtf1);
		jf1.add(jl1);
		jf1.add(jpf1);
		jf1.add(jl01);    
		jf1.setIconImage(icon);
		jf1.setLayout(null);
		jf1.setSize(650,650);
		
		//Frame 2
		JLabel jl2 = new JLabel("<html><h1 style=\"text-align:center;\">WELCOME TO MESSAGE APP<br>Click Start to<br>start a new/continue a old <br>Conversation</h1></html>");
		Image img2 = Toolkit.getDefaultToolkit().getImage("Images//start.jpg");
		Image newimg2 = img2.getScaledInstance(150, 35, java.awt.Image.SCALE_SMOOTH);
		JButton jb2 = new JButton(new ImageIcon(newimg2));
		JLabel jl21 = new JLabel("Notification Area:");
		Image logout = Toolkit.getDefaultToolkit().getImage("Images//logout.jpg");
		Image logout1 = logout.getScaledInstance(150, 35, java.awt.Image.SCALE_SMOOTH);
		JButton jb21 = new JButton(new ImageIcon(logout1));
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				jf2.setVisible(false);
				jf3.setVisible(true);
			}
		});
		jb21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				closer();
				s = "";
				s1 = "";
				a = "";
				jtf1.setText("");
				jpf1.setText("");
				jf2.setVisible(false);
				jf0.setVisible(true);
			}
		});
		jl2.setBounds(100,50,400,200);
		jb2.setBounds(225,250,150,35);
		jb21.setBounds(475,0,150,35);
		jl21.setBounds(50,300,500,50);
		jta2.setBounds(50,350,500,200);
		jl2.setHorizontalAlignment(JTextField.CENTER);
		jta2.setEditable(false);
		jl2.setFont(f1);
		jta2.setFont(f1);
		jl21.setFont(f1);
		jf2.add(jl21);
		jf2.add(jta2);
		jf2.add(jb21);
		jf2.add(jb2);
		jf2.add(jl2);
		jf2.setIconImage(icon);
		jf2.setLayout(null);
		jf2.setSize(650,650);
		
		//Frame 3
		JLabel jl3 = new JLabel("Enter Your Receiver ID:");
		JTextField jtf3 = new JTextField();
		JButton jb3 = new JButton(new ImageIcon(newimg));
		jb3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				s2 = jtf3.getText();
				try{
					int ch = Integer.parseInt(s2);
					if(s.equals(s2)){
						JFrame jf = new JFrame("Alert");
						JOptionPane.showMessageDialog(jf,"Receiver ID is Same as Your ID","Alert",JOptionPane.WARNING_MESSAGE);
					}
					else{
						jf3.setVisible(false);
						unnotify(s2);
						opener();
						jta4.setText(t);
						jf4.setVisible(true);
					}
				}catch(Exception e){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Invalid Number","Alert",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		jl3.setBounds(100,100,300,50);
		jtf3.setBounds(175,150,150,35);
		jb3.setBounds(175,200,150,35);
		jtf3.setHorizontalAlignment(JTextField.CENTER);
		jl3.setHorizontalAlignment(JTextField.CENTER);
		jtf3.setFont(f1);
		jl3.setFont(f1);
		jf3.add(jb3);
		jf3.add(jtf3);
		jf3.add(jl3);		
		jf3.setIconImage(icon);
		jf3.setLayout(null);
		jf3.setSize(650,650);
		
		//Frame 4
		JTextField jtf4 = new JTextField("");
		Image back = Toolkit.getDefaultToolkit().getImage("Images//back.png");
		Image back1 = back.getScaledInstance(150, 35, java.awt.Image.SCALE_SMOOTH);
		JButton jb41 = new JButton(new ImageIcon(back1));
		JButton jb4 = new JButton("SEND >>");
		JScrollPane jsp4 = new JScrollPane(jta4,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jb4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				String str = jtf4.getText();
				if(str.trim().isEmpty()){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Empty Message","Alert",JOptionPane.WARNING_MESSAGE);
				}else{
					if(send(str)){
						jtf4.setText("");
						opener();
						jta4.setText(t);
					}
				}
			}
		});
		jb41.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				s2 = "";
				jtf3.setText("");
				jtf4.setText("");
				jta2.setText(notifytext());
				jf4.setVisible(false);
				jf2.setVisible(true);
			}
		});
		jsp4.setBounds(5,50,621,550);
		jta4.setEditable(false);
		jb4.setBounds(521,575,100,25);
		jtf4.setEditable(true);
		jtf4.setBounds(5,575,521,25);
		jb41.setBounds(0,0,150,35);
		jf4.add(jtf4);
		jf4.add(jb4);
		jf4.add(jsp4);
		jf4.add(jb41);
		jf4.setIconImage(icon);
		jf4.setLayout(null);
		jf4.setSize(650,650);
		
		//Frame 5
		JLabel jl5 = new JLabel("Enter Your Name:");
		JTextField jtf5 = new JTextField();
		JLabel jl51 = new JLabel("Enter Your Unique ID");
		JTextField jtf51 = new JTextField();
		JLabel jl52 = new JLabel("Enter a Security Key");
		JTextField jtf52 = new JTextField();
		JLabel jl53 = new JLabel("Enter a New Password:");
		JPasswordField jtf53 = new JPasswordField();
		JLabel jl54 = new JLabel("Confirm Password:");
		JPasswordField jtf54 = new JPasswordField();
		Image img5 = Toolkit.getDefaultToolkit().getImage("Images//submit.jpg");
		Image newimg5 = img5.getScaledInstance(150, 35, java.awt.Image.SCALE_SMOOTH);
		JButton jb5 = new JButton(new ImageIcon(newimg5));
		jb5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				String in = jtf5.getText();
				String in1 = jtf51.getText();
				String in2 = jtf52.getText();
				String in3 = String.valueOf(jtf53.getPassword());
				String in4 = String.valueOf(jtf54.getPassword());
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(in);
				if((in.trim().isEmpty())||(in1.trim().isEmpty())||(in2.trim().isEmpty())||(in3.trim().isEmpty())||(in4.trim().isEmpty())){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Empty Fields. Give Input","Alert",JOptionPane.WARNING_MESSAGE);
				}else if(!(in3.equals(in4))){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"The Values of Password and Confirm Password Field are not Same","Alert",JOptionPane.WARNING_MESSAGE);
				}
				else if(matcher.find()){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Username cannot contain spaces instead you can use undescore","Alert",JOptionPane.WARNING_MESSAGE);
				}else{
					try{
						int a = Integer.parseInt(in1);
						if(a<0 || a>10000 || a==3333){
							JFrame jf = new JFrame("Alert");
							JOptionPane.showMessageDialog(jf,"Your Unique ID must be Integer of range 1 to 10000 not 3333","Alert",JOptionPane.WARNING_MESSAGE);
						}
						else{
							String str = register(in,in1,in2,in3);
							if(str.equals("-1")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"This Unique ID doesn't found","Alert",JOptionPane.WARNING_MESSAGE);
							}else if(str.equals("-2")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"Server not available.Try later","Alert",JOptionPane.WARNING_MESSAGE);
							}else if(str.equals("-3")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"SomeThing went wrong in Server.Try later","Alert",JOptionPane.WARNING_MESSAGE);
							}
							else if(str.equals("0")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"Password changed Succesfully.","Alert",JOptionPane.WARNING_MESSAGE);
								jtf5.setText("");
								jtf51.setText("");
								jtf52.setText("");
								jtf53.setText("");
								jtf54.setText("");
								jf5.setVisible(false);
								jf0.setVisible(true);
							}
						}
					}catch(Exception e){
						System.out.println(e);
						JFrame jf = new JFrame("Alert");
						JOptionPane.showMessageDialog(jf,"Your Unique ID must be Integer of range 1 to 1000 not 3333","Alert",JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		jl5.setBounds(100,25,300,50);
		jtf5.setBounds(125,75,250,50);
		jl51.setBounds(100,125,300,50);
		jtf51.setBounds(175,175,150,50);
		jl52.setBounds(100,225,300,50);
		jtf52.setBounds(125,275,250,50);
		jl53.setBounds(100,325,300,50);
		jtf53.setBounds(125,375,250,50);
		jl54.setBounds(100,425,300,50);
		jtf54.setBounds(125,475,250,50);
		jb5.setBounds(175,550,150,35);
		jl5.setHorizontalAlignment(JTextField.CENTER);
		jtf5.setHorizontalAlignment(JTextField.CENTER);
		jl51.setHorizontalAlignment(JTextField.CENTER);
		jtf51.setHorizontalAlignment(JTextField.CENTER);
		jl52.setHorizontalAlignment(JTextField.CENTER);
		jtf52.setHorizontalAlignment(JTextField.CENTER);
		jl53.setHorizontalAlignment(JTextField.CENTER);
		jtf53.setHorizontalAlignment(JTextField.CENTER);
		jl54.setHorizontalAlignment(JTextField.CENTER);
		jtf54.setHorizontalAlignment(JTextField.CENTER);
		jl5.setFont(f1);
		jtf5.setFont(f1);
		jl51.setFont(f1);
		jtf51.setFont(f1);
		jl52.setFont(f1);
		jtf52.setFont(f1);
		jl53.setFont(f1);
		jtf53.setFont(f1);
		jl54.setFont(f1);
		jtf54.setFont(f1);
		jf5.add(jl5);
		jf5.add(jtf5);
		jf5.add(jl51);
		jf5.add(jtf51);
		jf5.add(jl52);
		jf5.add(jtf52);
		jf5.add(jl53);
		jf5.add(jtf53);
		jf5.add(jl54);
		jf5.add(jtf54);
		jf5.add(jb5);
		jf5.setIconImage(icon);
		jf5.setLayout(null);
		jf5.setSize(650,650);
		
		//Frame 6
		JLabel jl6 = new JLabel("Enter Your Name:");
		JTextField jtf6 = new JTextField();
		JLabel jl61 = new JLabel("Enter Your Unique ID");
		JTextField jtf61 = new JTextField();
		JLabel jl62 = new JLabel("Enter a Security Key");
		JTextField jtf62 = new JTextField();
		JLabel jl63 = new JLabel("Enter a New Password:");
		JPasswordField jtf63 = new JPasswordField();
		JLabel jl64 = new JLabel("Confirm Password:");
		JPasswordField jtf64 = new JPasswordField();
		JButton jb6 = new JButton(new ImageIcon(newimg5));
		jb6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev){
				String in = jtf6.getText();
				String in1 = jtf61.getText();
				String in2 = jtf62.getText();
				String in3 = String.valueOf(jtf63.getPassword());
				String in4 = String.valueOf(jtf64.getPassword());
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher(in);
				if((in.trim().isEmpty())||(in1.trim().isEmpty())||(in2.trim().isEmpty())||(in3.trim().isEmpty())||(in4.trim().isEmpty())){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Empty Fields. Give Input","Alert",JOptionPane.WARNING_MESSAGE);
				}else if(!(in3.equals(in4))){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"The Values of Password and Confirm Password Field are not Same","Alert",JOptionPane.WARNING_MESSAGE);
				}
				else if(matcher.find()){
					JFrame jf = new JFrame("Alert");
					JOptionPane.showMessageDialog(jf,"Username cannot contain spaces instead you can use undescore","Alert",JOptionPane.WARNING_MESSAGE);
				}else{
					try{
						int a = Integer.parseInt(in1);
						if(a<0 || a>10000 || a==3333){
							JFrame jf = new JFrame("Alert");
							JOptionPane.showMessageDialog(jf,"Your Unique ID must be Integer of range 1 to 10000 not 3333","Alert",JOptionPane.WARNING_MESSAGE);
						}
						else{
							String str = forgotpass(in,in1,in2,in3);
							if(str.equals("-1")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"This Unique ID already Exist","Alert",JOptionPane.WARNING_MESSAGE);
							}else if(str.equals("-2")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"Server not available.Try later","Alert",JOptionPane.WARNING_MESSAGE);
							}else if(str.equals("-3")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"SomeThing went wrong in Server.Try later","Alert",JOptionPane.WARNING_MESSAGE);
							}else if(str.equals("-4")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"This Unique ID exists. But Wrong Username.","Alert",JOptionPane.WARNING_MESSAGE);
							}else if(str.equals("-5")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"This Unique ID exists. But Wrong Security key","Alert",JOptionPane.WARNING_MESSAGE);
							}else if(str.equals("0")){
								JFrame jf = new JFrame("Alert");
								JOptionPane.showMessageDialog(jf,"Succesfully created","Alert",JOptionPane.WARNING_MESSAGE);
								jtf6.setText("");
								jtf61.setText("");
								jtf62.setText("");
								jtf63.setText("");
								jtf64.setText("");
								jf6.setVisible(false);
								jf1.setVisible(true);
							}
						}
					}catch(Exception e){
						System.out.println(e);
						JFrame jf = new JFrame("Alert");
						JOptionPane.showMessageDialog(jf,"Your Unique ID must be Integer of range 1 to 1000 not 3333","Alert",JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		jl6.setBounds(100,25,300,50);
		jtf6.setBounds(125,75,250,50);
		jl61.setBounds(100,125,300,50);
		jtf61.setBounds(175,175,150,50);
		jl62.setBounds(100,225,300,50);
		jtf62.setBounds(125,275,250,50);
		jl63.setBounds(100,325,300,50);
		jtf63.setBounds(125,375,250,50);
		jl64.setBounds(100,425,300,50);
		jtf64.setBounds(125,475,250,50);
		jb6.setBounds(175,550,150,35);
		jl6.setHorizontalAlignment(JTextField.CENTER);
		jtf6.setHorizontalAlignment(JTextField.CENTER);
		jl61.setHorizontalAlignment(JTextField.CENTER);
		jtf61.setHorizontalAlignment(JTextField.CENTER);
		jl62.setHorizontalAlignment(JTextField.CENTER);
		jtf62.setHorizontalAlignment(JTextField.CENTER);
		jl63.setHorizontalAlignment(JTextField.CENTER);
		jtf63.setHorizontalAlignment(JTextField.CENTER);
		jl64.setHorizontalAlignment(JTextField.CENTER);
		jtf64.setHorizontalAlignment(JTextField.CENTER);
		jl6.setFont(f1);
		jtf6.setFont(f1);
		jl61.setFont(f1);
		jtf61.setFont(f1);
		jl62.setFont(f1);
		jtf62.setFont(f1);
		jl63.setFont(f1);
		jtf63.setFont(f1);
		jl64.setFont(f1);
		jtf64.setFont(f1);
		jf6.add(jl6);
		jf6.add(jtf6);
		jf6.add(jl61);
		jf6.add(jtf61);
		jf6.add(jl62);
		jf6.add(jtf62);
		jf6.add(jl63);
		jf6.add(jtf63);
		jf6.add(jl64);
		jf6.add(jtf64);
		jf6.add(jb6);
		jf6.setIconImage(icon);
		jf6.setLayout(null);
		jf6.setSize(650,650);
	}
}