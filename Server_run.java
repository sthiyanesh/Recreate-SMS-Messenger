import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.Random;
import java.net.*;
public class Server_run{
	public static String check(String s,String s1){
		String user;
		try{
		FileReader f1 = new FileReader("SERVER//prosecure.txt");
		BufferedReader br = new BufferedReader(f1);
		while((user = br.readLine()) != null) {
			String sp[] = user.split(" ");
			if((s.equals(sp[0]))&&(s1.equals(sp[1]))){
				return sp[2];
			}
		}
		f1.close();
		}catch(Exception e){
			e.getStackTrace();
		}
		return "-1";
	}
	public static boolean receive(String s, String s2,String msg){
		try{
			File f1 = new File("SERVER//"+s2);
			if(!(f1.isDirectory())){
				if(!(f1.mkdir())){
					System.out.print("Directory cannot be created for "+s2);
					return false;
				}
			}
			File f2 = new File("SERVER//"+s2+"//"+s+".txt");
			if(!(f2.exists())){
				if(!f2.createNewFile()){
					System.out.print("File cannot be created for "+s);
					return false;
				}
				else{
					FileWriter fw = new FileWriter("SERVER//"+s2+"//"+s+".txt");
					fw.write(msg);
					fw.close();
					return true;
				}
			}
			else{
				FileWriter fw = new FileWriter("SERVER//"+s2+"//"+s+".txt",true);
				fw.write("\n"+msg);
				fw.close();
				return true;
			}
		}catch(Exception e){
			System.out.print(e);
			return false;
		}
	}
	public static String register(String in,String in1,String in2,String in3){
		try{
			int ran;
			String user,val;
			Random rand = new Random();
			File f1 = new File("SERVER//prosecure.txt");
			FileReader rfr = new FileReader(f1);
			FileWriter rfw = new FileWriter(f1,true);
			BufferedReader br = new BufferedReader(rfr);
			while((user = br.readLine()) != null) {
				String sp[] = user.split(" ");
				if(sp[0].equals(in1)){
					return "-1";
				}
			}
			rfr.close();
			while(true){
				while(true){
					ran = rand.nextInt(10);
					if(ran!=0)
						break;
				}
				val = Integer.toString(ran);
				for(int i=1;i<10;i++){
					ran = rand.nextInt(10);
					val += Integer.toString(ran);
				}
				FileReader rfr1 = new FileReader(f1);
				BufferedReader br1 = new BufferedReader(rfr1);
				int flag = 0;
				while((user = br1.readLine()) != null) {
					String sp[] = user.split(" ");
					if(sp[2].equals(val)){
						flag=1;
						break;
					}
				}
				rfr1.close();
				if(flag == 0)
					break;
				flag=0;
			}
			rfw.write(in1+" "+in3+" "+val+" "+in+" "+in2+"\n");
			rfw.close();
			File f2 = new File("Users//"+val);
			if(f2.mkdir()){
				File f3 = new File("Users//"+val+"//newmsgnotify.txt");
				if(!(f3.createNewFile())){
					return "-3";
				}
			}else{
				return "-3";
			}
		}catch(Exception e){
			System.out.println(e);
			return "-3";
		}
		return "0";
	}
	public static String forgotpass(String in,String in1,String in2,String in3){
		try{
			String user="";
			int flag = 0;
			File f1 = new File("SERVER//prosecure.txt");
			FileReader rfr = new FileReader(f1);
			BufferedReader br = new BufferedReader(rfr);
			while((user = br.readLine()) != null) {
				String sp[] = user.split(" ");
				if(sp[0].equals(in1)){
					if(sp[3].equals(in)){
						if(sp[4].equals(in2)){
							flag = 1;
							break;
						}else{
							return "-5";
						}
					}else{
						return "-4";
					}
				}
			}
			if(flag==0){
				return "-1";
			}
			rfr.close();
			File f2 = new File("SERVER//prosecuretemp.txt");
			if(f2.createNewFile()){
				FileReader rfr1 = new FileReader(f1);
				BufferedReader br1 = new BufferedReader(rfr1);
				FileWriter rfw = new FileWriter(f2);
				while((user = br1.readLine()) != null) {
					String sp[] = user.split(" ");
					if(sp[0].equals(in1)){
						sp[1] = in3;
						user = String.join(" ", sp);
					}
					rfw.write(user+"\n");
				}
				rfr1.close();
				f1.delete();
				rfw.close();
				boolean bool = f2.renameTo(f1);
			}		
		}catch(Exception e){
			System.out.println(e);
			return "-3";
		}
		return "0";
	}
	public static void main(String args[]){
		while(true){
			try{  
				ServerSocket ss=new ServerSocket(3333);  
				Socket SSD=ss.accept();//establishes connection   
				DataInputStream dis=new DataInputStream(SSD.getInputStream());  
				DataOutputStream dout = new DataOutputStream(SSD.getOutputStream());
				Thread.sleep(1000);
				String str=(String)dis.readUTF();  
				if(str.equals("SENDER")){
					String s = (String)dis.readUTF();
					String s1 = (String)dis.readUTF();
					String msg = (String)dis.readUTF();
					if(receive(s,s1,msg)){
						dout.writeUTF("true");
					}
					else{
						dout.writeUTF("false");
					}
				}else if(str.equals("LOGIN")){
					String s = (String)dis.readUTF();
					String s1 = (String)dis.readUTF();
					dout.writeUTF(check(s,s1));
				}else if(str.equals("REGISTER")){
					String in = (String)dis.readUTF();
					String in1 = (String)dis.readUTF();
					String in2 = (String)dis.readUTF();
					String in3 = (String)dis.readUTF();
					String out = register(in,in1,in2,in3);
					dout.writeUTF(out);
				}else if(str.equals("FORGOT")){
					String in = (String)dis.readUTF();
					String in1 = (String)dis.readUTF();
					String in2 = (String)dis.readUTF();
					String in3 = (String)dis.readUTF();
					String out = forgotpass(in,in1,in2,in3);
					dout.writeUTF(out);
				}else if(str.equals("RECEIVE")){
					String s = (String)dis.readUTF();
					System.out.print(s);
					File f = new File("SERVER//"+s);
					if(f.isDirectory()){
						System.out.print("Files available");
						File[] listOfFiles = f.listFiles();
						for (File file : listOfFiles) {
							System.out.print(file.getName());
							dout.writeUTF(file.getName());
							FileReader fr3 = new FileReader(file);
							BufferedReader br = new BufferedReader(fr3);
							String temp;
							while((temp = br.readLine()) != null) {
								System.out.println(temp);
								dout.writeUTF(temp);
							}
							dout.writeUTF("#@#");
							br.close();
							fr3.close();
						}
						for(File file : f.listFiles()){
							file.delete();
						}
						f.delete();
					}
					dout.writeUTF("#@#");
				}
				dout.close();
				dis.close();
				ss.close();
				SSD.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}			
	}  
}