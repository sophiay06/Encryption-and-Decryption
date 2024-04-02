import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.Integer.toHexString;

public class Main{

    public static void main(String[] args) throws Exception {
        runTests();

        StringBuilder lines = new StringBuilder(); //create a stringbuilder that stores the output for the user's input

        Scanner user = new Scanner(System.in);
        System.out.println("Do you want to encrypt or decrypt (E/D):");
        String userInput = user.nextLine();
        System.out.println("FileName:");
        String fileName = user.nextLine();
        List<StringBuilder> userData = new ReadFile(fileName).getFileContent();
        System.out.println("Secret Key:");
        String secretKey = user.nextLine();
        System.out.println("Output File:");
        String outputFile = user.nextLine();

        List<String> dataToBinary = new ArrayList<>(); //this list stores the binary

        if (userInput.equals("E")) {
            //convert the data (Strings) from the file the user want to encrypt to binary
            for (int i = 0; i < userData.size(); i++) {
                dataToBinary.add(String2Binary(userData.get(i).toString()));
            }
            //if the user choose to encrypt the data from the file they input, the for loop runs through every element in the dataToBinary list
            // (which contains the binary of the data from the file) and calls the encryption method with the secret key
            for (int i = 0; i < dataToBinary.size(); i++) {
                lines.append(encryption(dataToBinary.get(i), secretKey)).append("\n");
            }
        } else {
            //for loop for decryption; runs through the data from data1.txt (or file that returns the encryption output) and call the
            //decryption method with the secret key
            for (int i = 0; i < userData.size(); i++) {
                lines.append(decryption(userData.get(i).toString(), secretKey)).append("\n");
            }
        }

        ReadFile.writeToFile(outputFile, lines.toString());
    }

    public static void runTests() throws Exception {
        List<StringBuilder> fileContentList = new ReadFile("runTests.txt").getFileContent(); //fileContentList stores data from runTests.txt
        List<StringBuilder> keyList = new ReadFile("key.txt").getFileContent(); //keyList stores data from key.txt

        StringBuilder lines = new StringBuilder();

        for(int i = 0; i < 4; i++) {
            lines.append("Output for: encryption(" + fileContentList.get(i).toString() + ", " + keyList.get(i).toString() + ")").append("\n");
            lines.append((encryption(fileContentList.get(i).toString(), keyList.get(i).toString()))).append("\n");
        }
        for(int i = 4; i < 9; i++) {
            lines.append("Output for: decryption(" + fileContentList.get(i).toString() + ", " + keyList.get(i).toString() + ")").append("\n");
            lines.append(decryption(fileContentList.get(i).toString(), keyList.get(i).toString())).append("\n");
        }

        System.out.println(lines);
    }

    //the following 2 methods (String2Binary and charToBinary) convert string to binary
    public static String String2Binary(String text) {
        StringBuilder binaryResult = new StringBuilder();
        String paddingResult = "";

        for(int i = 0; i < text.length(); i++) {
            binaryResult.append(charToBinary(((text.charAt(i))))); //convert each character in the "text" string to binary by calling the charToBinary method
        }

        int i = binaryResult.length();
        while (i % 64 != 0) { //padded the string so that the total bits in the binary is a multiple of 64
            paddingResult += "0";
            i++;
        }

        return paddingResult + binaryResult;
    }

    //convert characters to binary
    public static String charToBinary(char s) {
        String binaryString = Integer.toBinaryString(s);
        String paddedBinaryString = String.format("%8s", binaryString).replace(' ', '0'); // Pad with leading zeros if necessary
        return paddedBinaryString;
    }

    //performs the bitwise XOR operation between two binary inputs
    public static String xorIt(String binary1, String binary2) throws Exception {

        if (binary1.length() != binary2.length()) {
            throw new IllegalArgumentException();
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < binary1.length(); i++) {
            int bit1 = Character.getNumericValue(binary1.charAt(i));
            int bit2 = Character.getNumericValue(binary2.charAt(i));

            if (bit1 == bit2) {
                result.append("0");
            } else {
                result.append("1");
            }
        }

        return result.toString();
    }


    //shifts bits to the left by 1
    public static String shiftIt(String binaryInput) throws Exception{

        if (binaryInput.length() != 28) {
            throw new IllegalArgumentException();
        }

        String shiftBits = "";

        for (int i = 1; i < binaryInput.length(); i++) {
            shiftBits += binaryInput.charAt(i);
        }
        shiftBits += binaryInput.charAt(0);

        return shiftBits;
    }

    //applies a substitution operation on the input binary using a predefined S-box table
    public static String SubstitutionS(String binaryInput) throws Exception{

        //S-box table
        String[][] S = new String[][] {
                {"01100011", "01111100", "01110111", "01111011", "11110010", "01101011", "01101111", "11000101", "00110000", "00000001", "01100111", "00101011", "11111110", "11010111", "10101011", "01110110"},
                {"11001010", "10000010", "11001001", "01111101", "11111010", "01011001", "01000111", "11110000", "10101101", "11010100", "10100010", "10101111", "10011100", "10100100", "01110010", "11000000"},
                {"10110111", "11111101", "10010011", "00100110", "00110110", "00111111", "11110111", "11001100", "00110100", "10100101", "11100101", "11110001", "01110001", "11011000", "00110001", "00010101"},
                {"00000100", "11000111", "00100011", "11000011", "00011000", "10010110", "00000101", "10011010", "00000111", "00010010", "10000000", "11100010", "11101011", "00100111", "10110010", "01110101"},
                {"00001001", "10000011", "00101100", "00011010", "00011011", "01101110", "01011010", "10100000", "01010010", "00111011", "11010110", "10110011", "00101001", "11100011", "00101111", "10000100"},
                {"01010011", "11010001", "00000000", "11101101", "00100000", "11111100", "10110001", "01011011", "01101010", "11001011", "10111110", "00111001", "01001010", "01001100", "01011000", "11001111"},
                {"11010000", "11101111", "10101010", "11111011", "01000011", "01001101", "00110011", "10000101", "01000101", "11111001", "00000010", "01111111", "01010000", "00111100", "10011111", "10101000"},
                {"01010001", "10100011", "01000000", "10001111", "10010010", "10011101", "00111000", "11110101", "10111100", "10110110", "11011010", "00100001", "00010000", "11111111", "11110011", "11010010"},
                {"11001101", "00001100", "00010011", "11101100", "01011111", "10010111", "01000100", "00010111", "11000100", "10100111", "01111110", "00111101", "01100100", "01011101", "00011001", "01110011"},
                {"01100000", "10000001", "01001111", "11011100", "00100010", "00101010", "10010000", "10001000", "01000110", "11101110", "10111000", "00010100", "11011110", "01011110", "00001011", "11011011"},
                {"11100000", "00110010", "00111010", "00001010", "01001001", "00000110", "00100100", "01011100", "11000010", "11010011", "10101100", "01100010", "10010001", "10010101", "11100100", "01111001"},
                {"11100111", "11001000", "00110111", "01101101", "10001101", "11010101", "01001110", "10101001", "01101100", "01010110", "11110100", "11101010", "01100101", "01111010", "10101110", "00001000"},
                {"10111010", "01111000", "00100101", "00101110", "00011100", "10100110", "10110100", "11000110", "11101000", "11011101", "01110100", "00011111", "01001011", "10111101", "10001011", "10001010"},
                {"01110000", "00111110", "10110101", "01100110", "01001000", "00000011", "11110110", "00001110", "01100001", "00110101", "01010111", "10111001", "10000110", "11000001", "00011101", "10011110"},
                {"11100001", "11111000", "10011000", "00010001", "01101001", "11011001", "10001110", "10010100", "10011011", "00011110", "10000111", "11101001", "11001110", "01010101", "00101000", "11011111"},
                {"10001100", "10100001", "10001001", "00001101", "10111111", "11100110", "01000010", "01101000", "01000001", "10011001", "00101101", "00001111", "10110000", "01010100", "10111011", "00010110"}
        };

        if (binaryInput.length() != 8) {
            throw new IllegalArgumentException();
        }

        String subResult = "";
        int decimal = binaryToDecimal(binaryInput);

        String hexDec = toHexString(decimal); //convert the binary to hexadecimal

        int row = 0, column = 0;

        String[] hex = {"a", "b", "c", "d", "e", "f"};
        int[] hexD = {10, 11, 12, 13, 14, 15};

        if (hexDec.length() == 1) {
            for (int i = 0; i < 10; i++) {
                if (hexDec.substring(0, 1).equals(String.valueOf(i + 1))) {
                    column = i + 1;
                }
            }
            for (int j = 0; j < 6; j++) {
                if (hexDec.substring(0, 1).equals(hex[j])) {
                    column = hexD[j];
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                if (hexDec.substring(0, 1).equals(String.valueOf(i + 1))) {
                    row = i + 1;
                }
                if (hexDec.substring(1, 2).equals(String.valueOf(i + 1))) {
                    column = i + 1;
                }
            }
            for (int j = 0; j < 6; j++) {
                if (hexDec.substring(0, 1).equals(hex[j])) {
                    row = hexD[j];
                }

                if (hexDec.substring(1, 2).equals(hex[j])) {
                    column = hexD[j];
                }
            }
        }

        String substitution = S[row][column];

        subResult += substitution;

        return subResult;
    }

    //convert binary to decimal
    public static int binaryToDecimal(String binaryInput) {
        long binary = Long.parseLong(binaryInput);
        int decimalNum = 0;
        long remainder = 0;

        int i = 0;
        while (binary != 0) {
            remainder = binary % 10;
            binary /= 10;
            decimalNum += (int)(remainder * Math.pow(2, i));
            i++;
        }

        return decimalNum;
    }

    //applies a permutation operation on the input binary based on a predefined permutation table
    public static String permuteIt(String binaryInput) throws Exception{

        int[] permutationTable = {16, 7, 20, 21, 29, 12, 28, 17,
                                    1, 15, 23, 26, 5, 18, 31, 10,
                                    2, 8, 24, 14, 32, 27, 3, 9,
                                    19, 13, 30, 6, 22, 11, 4, 25};

        if (binaryInput.length() != 32) {
            throw new IllegalArgumentException();
        }

        String permuteBinary = "";

        for(int i = 0; i < 32; i++) {
            permuteBinary += binaryInput.charAt(permutationTable[i] - 1);
        }

        return permuteBinary;
    }


    //generates the round keys (subkeys) used in the encryption and decryption processes based on the input key
    public static String[] keyScheduleTransform(String inputKey) throws Exception{

        if (inputKey.length() != 56) {
            throw new IllegalArgumentException();
        }

        String left = inputKey.substring(0, 28);
        String right = inputKey.substring(28);

        String[] shiftResult = new String[10];
        String combinedKey = "";

        for (int i = 0; i < 10; i++) {
            left = shiftIt(left);
            right = shiftIt(right);
            combinedKey = left + right;
            shiftResult[i] = combinedKey.substring(0, 32);
        }
        return shiftResult;
    }

    //implements the round function used in the encryption algorithm
    //takes the right half of the input data and a subKey as input and produces the output of the round function
    public static String functionF(String rightHalf, String subKey) throws Exception {

        if (rightHalf.length() != subKey.length()) {
            throw new IllegalArgumentException();
        }

        String permuteString = "";

        String subKeyXorRight = xorIt(rightHalf, subKey);

        String[] eightBits = new String[4];

        for(int i = 0; i < 4; i++) {
            eightBits[i] = subKeyXorRight.substring(i * 8, (i + 1) * 8);
            permuteString += SubstitutionS(eightBits[i]);
        }

        return permuteIt(permuteString);
    }

    //encrypt the entire data
    public static String encryption(String binaryInput, String inputKey) throws Exception {

       if (binaryInput.length() % 64 != 0 || inputKey.length() != 56) {
            throw new IllegalArgumentException("Invalid input length.");
        }

        String encryptedInput = "";

       //divide the entire binary to blocks, in which each block has 64 bits
        for (int i = 0; i < binaryInput.length(); i += 64) {
            String block = binaryInput.substring(i, i + 64);
            encryptedInput += encryptBlock(block, inputKey);
        }

        return encryptedInput;
    }

    //"encryptBlock" method encrypts a single block of data (a block with 64 bits) using a given encryption key
    public static String encryptBlock(String block, String inputKey) throws Exception {

        if (block.length() != 64) {
            throw new IllegalArgumentException("Block size must be 64 bits.");
        }

        String[] subKey = keyScheduleTransform(inputKey); //calling keyScheduleTransform method and stores all the subkeys to the string array

        String leftHalf = block.substring(0, 32);
        String rightHalf = block.substring(32);

        // Use subkeys (k1 to k10)
        for (int i = 0; i < subKey.length; i++) {
            String functionResult = functionF(rightHalf, subKey[i]);
            String newRightHalf = xorIt(leftHalf, functionResult);

            leftHalf = rightHalf;
            rightHalf = newRightHalf;
        }

        return leftHalf + rightHalf; // Concatenate for the final decrypted block
    }

    //decrypt the entire data
    public static String decryption(String binaryInput, String inputKey) throws Exception {

        if (binaryInput.length() % 64 != 0 || inputKey.length() != 56) {
            throw new IllegalArgumentException("Invalid input length.");
        }

        String decryptedInput = "";

        //divide the entire binary to blocks, in which each block has 64 bits
        for (int i = 0; i < binaryInput.length(); i += 64) {
            String block = binaryInput.substring(i, i + 64);
            decryptedInput += decryptBlock(block, inputKey);
        }

        return decryptedInput;
    }

    //"decryptBlock" method decrypts a single block of data (a block with 64 bits) using a given encryption key
    public static String decryptBlock(String block, String inputKey) throws Exception {

        if (block.length() != 64) {
            throw new IllegalArgumentException("Block size must be 64 bits.");
        }

        String[] subKey = keyScheduleTransform(inputKey); // Generate subkeys for decryption

        String rightHalf = block.substring(0, 32);
        String leftHalf = block.substring(32);

        // Use subkeys in reverse order for decryption, from k10 to k1
        for (int i = subKey.length - 1; i >= 0; i--) {
            String temp = rightHalf; // Store rightHalf temporarily
            rightHalf = xorIt(leftHalf, functionF(rightHalf, subKey[i])); // Apply functionF on rightHalf, then XOR with leftHalf
            leftHalf = temp; // Swap leftHalf and rightHalf
        }

        return rightHalf + leftHalf; // Concatenate for the final decrypted block
    }
}