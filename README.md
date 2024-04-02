## Encryption/Decryption Project
Andhiya Madamala and Sophia Yang

We have carried out the implementation of encryption/decryption of data in text files by utilizing a secret key provided by the user.

Features of our Project
- Encryption converts data from text file to encrypted text using a secret key.
- Decryption carries out the reverse process and converts the text back into plain text using the same key.
- We have utilized 4 files for this project, data.txt, data1.txt, key.txt, runTests.txt from which we read input and write output into.
- We have shown the testing of certain predefined tests to show the encryption and decryption.
- Key schedule is a major part of this code, which is what provides the symmetry of this encryption. By reversing the order of the keys,
  the decryption can be easily carried out.
- We use the required permutation to encrypt and decrypt.
- We also include the implementation of the provided S-box.
- There are also methods for XOR, convert strings to binary, convert binary to decimal, and shift bits to help the encryption and decryption
  functions to work properly.
- Moreover, we have a separate class for read file, which read and write data to files. Specifically, there are methods that read and write
  data from a file.

Instructions
- The project can be tested by running the Main class. The user will have to provide their choice of encryption or decryption, along with the file
  they wish to read from and the file they want to write into. They also have to provide the secret key according to which the data is encoded.
- As mentioned, there are a few test cases that we carry out. The output of these cases is printed out and the output of the user's
  instructions can be found in the data1.txt file.

Work split up
- We worked together by reading through the project together and bouncing ideas off each other about how to go about each part. We corrected
  each other's issues with the code. Most of the code was written and then rewritten multiple times by both of us.
- To put it generally, Sophia worked on the encryption while Andhiya worked on decryption, and we both worked on the overlapping functions
  for both processes together.