# picMicroView
picMicroView is an emulator for a PIC18F452 microprocessor. Its main purpose is to educate by allowing the user to step through instructions one at a time and inspect the corresponding discrete internal state of the microprocessor. There is also a "run" mode which, via MVC design, essentially works independent of the view (gui). The run mode does not access real-time capabilities, therefore its timing is not incredibly accurate. Currently, picMicroView is only partially functional. Future plans are to implement the remaining instructions, enhance gui with user configurability, add tooltips which contain links to deeper information and explanations, incorporate tutorials accessible from the help menu, improve perceived timing accuracy in run mode, and to implement the "model" portion as a web service.

To try picMicroView, download the .jar file under "Releases." Locate the "testFiles" directory on this site. Copy and paste the text from one of the .hex files into a text file and save file locally on your machine. Make sure you have at least Java 1.8, then run java -jar jar-file-name. Click "Load," then browse to the .hex file you saved and load it. You should see the left pane fill in with some values. These are the hexadecimal values of the program instructions which are now loaded into the program memory. Select "Run" and you should see a value above the right pane change about once per second (if you loaded toggle1sec.hex). If you select "Step" and click it a few times, you will see values begin to change in the right pane as well. These are the memory locations being used by the program as its instructions are executed.

Thank you for looking, and please keep in touch!
