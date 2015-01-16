# picMicroView
picMicroView is an emulator for a PIC18F452 microprocessor. Its main purpose is to educate by allowing the user to step through instructions one at a time and inspect the corresponding discrete internal state of the microprocessor. There is also a "run" mode which, via MVC design, essentially works independent of the view (gui). The run mode does not access real-time capabilities, therefore its timing is scheduler-dependent. Currently, picMicroView is only partially functional, and the currently implemented instructions are listed at the bottom of this file. Future plans are to implement the remaining PIC18 instructions, add support for other microprocessors (PIC24 first), enhance gui with user configurability and greater visibility of microprocessor internals, add tooltips which contain links to deeper information and explanations, incorporate tutorials accessible from the help menu, improve perceived timing accuracy in run mode, and implement the "model" portion as a web service.

To try picMicroView, download the .jar file under the "Releases" tab. Make sure you have at least Java 1.8 installed. Then locate the "testFiles" directory on this site. Copy and paste the text from one of the .hex files into a text file and save the file locally on your machine. Then, from the command line, run "java -jar 'name-of-jar-file'". Click "File", "Load," then browse to the .hex file you saved and load it. 

The .hex file contains the machine language instructions corresponding to the assembly language .asm file with the same name prefix. After a file is loaded, you should see the left pane fill in with values. These are the hexadecimal values of the machine language instructions which are now loaded into the program memory. Select "Run" to run the program, or "Step" to step through the program one instruction at a time. If you load toggle1sec.hex and run it, you will see numbers above the right pane change about once per second. Currently, this location only monitors port A of the microprocessor. If you select "Step" and click it a few times, you will see values begin to change in the right pane as well. These are the memory locations being used by the PIC18 as it executes the machine language instructions. Other PIC18 register values, such as the W register, are visible if you scroll up in the right pane toward memory location 0x0e8 or 0xfe8. More test programs are coming soon. 

The center button "Input/Output" is just a placeholder for features that will be added between the left and right panes in the future.

Thank you for looking, and please keep in touch!

Currently implemented PIC18 instructions:

goto
movlw
movwf
decf
bz
rcall
bnz
return
btg
andlw
nop
iorwf
addwf
addwfc
movf
bsf
bcf
btfss
movff
bra
