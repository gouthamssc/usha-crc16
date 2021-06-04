
 import java.io.*;
 import java.util.*;
 
 public class CRC16 {
     public static void main(String[] args) throws FileNotFoundException {
             
             Scanner scIn = new Scanner(System.in);
             char input = scIn.next().charAt(0);
             String fileName = scIn.next();
         
     // Running the crc verification case
         if(input == 'v'){
             
             Scanner sc = new Scanner(new File(fileName));
 
             // The CRC-16 Code for x^16 + x^11 + x^8 + x^7 + x^3 +x^2 + 1 and converting it into a character array.
             String crcInit = "1000000000000101";
             char[] crcArr = crcInit.toCharArray();
             String hexIn= "";
         
             // Storing the hex input file into a string
             while (sc.hasNext() == true)
                 hexIn += sc.next();
         
             // Converting the hex into lower case letters and storing them into a character array
             char[] preHex = hexIn.trim().toLowerCase().toCharArray();
             int counterA = 0;
             System.out.print("The input file (hex): ");
         
             // Printing out the hex read in from file
             for(int i = 0; i < preHex.length; i++){
                 // Printing a new line after 80 characters
                 if(counterA%80 == 0 && i != 0){
                     System.out.println();
                 }
             
                 System.out.print(preHex[i]);
                 counterA++;
             }
             
             // Finding the observed hex value
             String observedHex = "";
             for(int i=0; i < preHex.length; i++){
                 if(i >= preHex.length-4){
                     observedHex = observedHex + preHex[i];
                 }
             }
             
             // Printing the observed hex value
             System.out.println();
             System.out.println("The polynomial that was used (bin): 1000 0000 0000 0101");
             System.out.print("The 16-bit CRC observed at the end of the file: ");
             System.out.print(observedHex.toUpperCase());
             System.out.print(" (hex) = ");
             
             String observedToBin = "";
             char[] obsHexNew = observedHex.toCharArray();
             
             // Converting the observed hex values to binary
             for(int i = 0; i < observedHex.length(); i++)
                 observedToBin += HexToBinary(obsHexNew[i]);
             
             System.out.print(observedToBin);
             System.out.println(" (bin)");
             
             String hexToBin = "";
             
             char[] convertedHex = preHex;
 
             // Converting the hex to binary and creating a string of binary
             for(int i = 0; i < convertedHex.length; i++)
                 hexToBin += HexToBinary(convertedHex[i]);
         
             System.out.println();
             char[] charArr = hexToBin.toCharArray();
         
             System.out.println("The input file (bin): ");
             System.out.println();
             
             // Printing out the hex to binary value and insuring there is only 64 bits per line with
             // a space after every 4 bits.
             for(int i = 0; i < charArr.length; i++){
                 if(i%4 == 0 && i != 0){
                     System.out.print(" ");
                 }
                 if(i%64 == 0 && i != 0){
                     System.out.println();
                 }
                 System.out.print(charArr[i]);
             }
 
             System.out.println();
             System.out.println();
         
             // Running the crc verifications
             crcCalculations(crcArr, charArr, input, observedHex);
             sc.close();
         }
         
     // Runing the crc calculation case
         else if(input == 'c'){
             Scanner sc = new Scanner(new File(fileName));
         
             // The CRC-16 Code for x^16 + x^11 + x^8 + x^7 + x^3 +x^2 + 1 and converting it into a character array.
             String crcInit = "1000000000000101";
             char[] crcArr = crcInit.toCharArray();
             String hexIn= "";
         
             // Storing the hex input file into a string
             while (sc.hasNext() == true)
                 hexIn += sc.next();
         
         
             // Converting the hex into lower case letters and storing them into a character array
             char[] convertedHex = hexIn.trim().toLowerCase().toCharArray();
             int counterA = 0;
             System.out.print("The input file (hex): ");
         
             // Printing out the hex read in from file
             for(int i = 0; i < convertedHex.length; i++){
                 // Printing a new line after 80 characters
                 if(counterA%80 == 0 && i != 0){
                     System.out.println();
                 }
             
                 System.out.print(convertedHex[i]);
                 counterA++;
             }
         
             String hexToBin = "";
         
             // Converting the hex to binary and creating a string of binary
             for(int i = 0; i < convertedHex.length; i++)
                 hexToBin += HexToBinary(convertedHex[i]);
         
             System.out.println();
             System.out.println("Padding binary value with 16 zeros.");
             hexToBin = hexToBin + "0000000000000000";
             char[] charArr = hexToBin.toCharArray();
         
             System.out.println("The input file (bin): ");
             System.out.println();
             
             // Printing out the hex to binary value and insuring there is only 64 bits per line with
             // a space after every 4 bits.
             for(int i = 0; i < charArr.length; i++){
                 if(i%4 == 0 && i != 0){
                     System.out.print(" ");
                 }
                 if(i%64 == 0 && i != 0){
                     System.out.println();
                 }
                 System.out.print(charArr[i]);
             }
         
             System.out.println();
             System.out.println();
             System.out.print("The polynomial that was used (binary bit string): 1000 0000 0000 0101");
             System.out.println();
             System.out.println();
         
             // Running the crc calculations
             crcCalculations(crcArr, charArr, input, null);
             sc.close();
         }
         
     // The input was not a v or c, prints error message and closes the program
         else
             System.out.println("Not a valid input. Closing program.");
         
             scIn.close();
             System.exit(0);
     }
     
     // Method for converting hex values into binary values
     static String HexToBinary(char h) {
 
         if(h == '0'){ return "0000"; }
         if(h == '1'){ return "0001"; }
         if(h == '2'){ return "0010"; }
         if(h == '3'){ return "0011"; }
         if(h == '4'){ return "0100"; }
         if(h == '5'){ return "0101"; }
         if(h == '6'){ return "0110"; }
         if(h == '7'){ return "0111"; }
         if(h == '8'){ return "1000"; }
         if(h == '9'){ return "1001"; }
         if(h == 'a'){ return "1010"; }
         if(h == 'b'){ return "1011"; }
         if(h == 'c'){ return "1100"; }
         if(h == 'd'){ return "1101"; }
         if(h == 'e'){ return "1110"; }
         if(h == 'f'){ return "1111"; }
         
         System.out.println("Not a valid hex input. Exiting.");
         System.exit(0);
         return null;
     }
     
     // Method for converting binary to hex
     static String BinaryToHex(String b){
         String retVal = Integer.toHexString(Integer.parseInt(b, 2));
         return retVal;
     }
     
     // Method for running the crc calculations/verfications
     static void crcCalculations(char[] crcArr, char[] charArr, char x, String beforeVal){
         
         int zeroCount = 0;
         boolean stopFlag = true;
         char[] resultArr = charArr;
         char[] crcFollow = new char[charArr.length];
         
         if(x == 'c'){
             System.out.println("The binary string answer at each XOR step of CRC calculation: ");
             System.out.println();
         }
         
         if(x == 'v'){
             System.out.println("The binary string answer at each XOR step of CRC verification: ");
             System.out.println();
         }
         
         String crcStringArr = new String(crcArr);
         char[] crcZeroIncrement = crcArr;
         
         // While loop for the CRC calculation logic
         while(stopFlag){
             
             int count = 0;
             for(int i = 0; i < crcZeroIncrement.length; i++){
 
                 // Will flag the while loop for termination when we reach the end of the CRC calculation
                 if(i >= charArr.length-1){
                     stopFlag = false;
                     break;
                 }
                 
                 // The following if/else statements are the XOR calculations.
                 if(resultArr[i] == crcZeroIncrement[i]){
                     resultArr[i] = '0';
                     count = (count+1)%17;
                 }
                 else{
                     resultArr[i] = '1';
                     count = (count+1)%17;
                 }
             }
             // Counting zeros to find how far to indent the next calculation
             for(int i = 0 + zeroCount; i<resultArr.length + zeroCount; i++){
                 if(resultArr[i] == '0'){
                     zeroCount += 1;
                 }
                 if(resultArr[i] == '1'){
                     break;
                 }
             }
             // Inserting a space every 4 bits
             for(int i = 0; i < resultArr.length; i++){
                 
                 if(i%4 == 0 && i != 0){
                     System.out.print(" ");
                 }
                 System.out.print(resultArr[i]);
             }
             
             crcStringArr = new String(crcArr);
             
             for(int i = 0; i<zeroCount; i++){
                 crcStringArr =  '0' + crcStringArr;
             }
             crcZeroIncrement = crcStringArr.toCharArray();
             
             System.out.println();
             System.out.println();
         }
     
         char[] resultBin = new char[16];
         int binCounter = 0;
         String binString = "";
         
         // Converting the last 16 digits of the results array into a binary string
         for(int i = resultArr.length-16; i < resultArr.length; i++){
             resultBin[binCounter] = resultArr[i];
             binString = binString + resultBin[binCounter];
             binCounter++;
         }
         System.out.println();
         
         // Printing out the CRC-16 values in binary and the converted hex values.
         String hexValue = BinaryToHex(binString);
         System.out.print("The computed CRC for this file is " + binString + " (bin) = " + hexValue.toUpperCase() + " (hex)");
         System.out.println();	
         
         if(x == 'v'){
             System.out.print("Did the CRC check pass? (Yes or No): ");
             if(hexValue == beforeVal){
                 System.out.println("Yes");
             }
             else
                 System.out.println("No");
         }
     }		
 }
 