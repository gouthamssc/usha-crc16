
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.ArrayList;


public class Crcheck{
	private static String fileName;
	private static final int POLYNOMIAL = /*0xA053;*/0x8005;
	private static final int PRESET_VALUE = 0xFFFF;
	private static byte[] data = new byte[512];
	private static int crc;
	private static byte[] receivedCrc = new byte[4];
	private static ArrayList<Integer> elements64 = new ArrayList<>();

	public Crcheck(){}

	
	public static byte[] XOR(byte[] a, byte[] b) {
	  byte[] result = new byte[Math.min(a.length, b.length)];

	  for (int i = 0; i < result.length; i++) {
	    result[i] = (byte) (((int) a[i]) ^ ((int) b[i]));
	  }

	  return result;
	}

	public static void displayTextFromFile()throws IOException{
		System.out.println("CRC16 Input text from file\n");
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String line;
		while((line = in.readLine()) != null){
			System.out.print(line+ "  "+ line.length()+ "  ");
		}
		in.close();

	}

	public static void printMessage(byte data[], String s){
		for (int i=0; i<data.length-4; i++ ) {
			System.out.print((char)data[i]);
			if ((i+1)%64==0) {
				System.out.print("\t-"+"  ");
				System.out.print(String.format("0000%x", elements64.get((i+1)/64-1))+"\n");
			}
		}
			System.out.print(s+Integer.toHexString(crc));
			System.out.print("\t-"+"  "+"0000"+Integer.toHexString(crc)+"\n");
			System.out.println();
	}

	public static void writeMessage(byte data[]){
		String data2String = new String(data);
		String hex = Integer.toHexString(crc);
		int length = data2String.length();

    	data2String = data2String.substring(0, length-8) + "0000"+hex;

    	//removing extension from the file...
    	int x;
		for(x=fileName.length()-1 ; x>=0 && fileName.charAt(x)!='.' ; x--);
		fileName = fileName.substring(0,x);
		//System.out.println(fileName);

    	File file = new File(fileName+".crc");
        FileOutputStream fos = null;
        try{
        	fos = new FileOutputStream(file);
        	fos.write(data2String.getBytes());
        }catch(IOException i){
        	System.out.println(i);
        }
	 	finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }

	}


	public static int crc16(byte[] data){
		System.out.println("\nCRC 16 calculation progress:\n");
		int current_crc_value = PRESET_VALUE;
		for (int i = 0; i < data.length-8; i++){
			current_crc_value ^= data[i] & 0xFF;
			for (int j = 0; j < 8; j++ ) {
				if ((current_crc_value & 1) != 0) {
					current_crc_value = (current_crc_value >>> 1) ^ POLYNOMIAL;
				}
				else{
					current_crc_value = current_crc_value >>> 1;
				}
			}
			if ((i+1)%64==0) {
				elements64.add((~current_crc_value & 0xFFFF));
				//System.out.println(String.format("\nValue: \t%x", ~current_crc_value & 0xFFFF));
			}
		}

		current_crc_value = ~current_crc_value;
		crc = current_crc_value & 0xFFFF;
		return current_crc_value & 0xFFFF;
	}

	public void setCRC(int crc){
		//this.crc = crc;
	}

	public int getCRC(){
		return this.crc;
	}

	public byte[] getData(){
		return this.data;
	}


	public static byte[] readDataFile(File file){
	try{
		//System.out.println("Trying to reads "+fileName);
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if(length > Integer.MAX_VALUE){
			throw new IOException("Could not completely read the file " + fileName + "as it is too long");	
		}		

		int offset =  0;
		int numRead = 0;
		
		while (offset < data.length && (numRead=is.read(data, offset, data.length-offset)) >= 0 ){
			offset += numRead;
		}

		for (int i=(int)length-1; i<504; i++ ) {
			data[i] = (int) '.';
		}

		if (offset < data.length){
			//throw new IOException("Could not completely read file " + fileName);
		}

		is.close();

		}catch(FileNotFoundException f){
			System.out.println("Please enter the correct file,,,,\nThere is no file named "+fileName);
		}
		catch(IOException e){
			System.out.println(e);
		}
		//System.out.println("Inside method.."+data.length);
      	return data;
	}

	public static byte[] readcrcFile(File file){
	try{
		//System.out.println("Trying to reads "+fileName);
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if(length > Integer.MAX_VALUE){
			throw new IOException("Could not completely read the file " + fileName + "as it is too long");	
		}		

		int offset =  0;
		int numRead = 0;
		
		while (offset < data.length && (numRead=is.read(data, offset, data.length-offset)) >= 0 ){
			offset += numRead;
		}

		for (int i=508; i<512; i++ ) {
			receivedCrc[i-508] = data[i];
			//System.out.println("Making receivedCrc part...");
			//System.out.print((char)data[i]);
		}

		if (offset < data.length){
			//throw new IOException("Could not completely read file " + fileName);
		}

		is.close();

		}catch(FileNotFoundException f){
			System.out.println("Please enter the correct file,,,,\nThere is no file named "+fileName);
		}
		catch(IOException e){
			System.out.println(e);
		}
		//System.out.println("Inside method.."+data.length);
      	return data;
	}


	//checksumcalculate method
	public void checksumCalculate()throws IOException{
	//System.out.println("inside checksumCalculate() method");
	byte[] ver = readDataFile(new File(fileName));
	displayTextFromFile();
	int ans = crc16(ver);
	printMessage(ver,"0000");
	writeMessage(ver);
	}

	//checksum verify method
	public void checksumVerify()throws IOException{
	//System.out.println("inside checksumVerify() method");
	byte[] ver = readcrcFile(new File(fileName));
	displayTextFromFile();
	int ans = crc16(ver);
	String r = new String(receivedCrc);
	int received = Integer.parseInt(r,16);

	printMessage(ver,"");
	System.out.println(ans);
	System.out.println("\nCRC16 result : 0000"+Integer.toHexString(ans));

	if (ans == received) {
		System.out.println("\nCRC 16 verification passed");
	}
	else{
		System.out.println("\nCRC 16 verification failed");
	}

	}

	//main method
	public static void main (String[] args)throws IOException{
	if (args[0].charAt(0)=='c') {
		Crcheck c = new Crcheck(); //create object if the command line arguments are correct
		c.fileName = args[1];
		c.checksumCalculate();
	}
	else if (args[0].charAt(0)=='v') {
		Crcheck c = new Crcheck(); // create object if the command line arguments are correct
		c.fileName = args[1];
		c.checksumVerify();
	}
	else{
		System.out.println("Please enter correct arguments                 ");
		System.out.println("---------------------------------------------  ");
		System.out.println("calculate checksum ==>  $ crcheck [filename] c ");
		System.out.println("verify checksum    ==>  $ crcheck [filename] v ");
		System.out.println("---------------------------------------------   ");

	}
	}


}
