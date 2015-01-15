package net.sourceforge.picmicroview.model;


public class DataMemory {


	private Pic18F452 pic18;
	
	Register[] gpMem;
	Register wreg;
	Register status;
	Register pcL;
	Register pclatH;
	Register pclatU;
	Register stkptr;
	Register intcon;
	Register porta;
	
	public DataMemory(Pic18F452 pic18) {
		this.pic18 = pic18;
		gpMem = new Register[4096];
		initialize();
		wreg = gpMem[0x0e8];
		status = gpMem[0x0d8];	
		pcL = gpMem[0x0f9];
		pclatH = gpMem[0x0fa];
		pclatU = gpMem[0x0fb];
		stkptr = gpMem[0x0fc];
		intcon = gpMem[0x0f2];
		porta = gpMem[0x080];
	}
	
	int read(int address){
		return gpMem[address].read();
	}
	
	void write(int address, int value){
		gpMem[address].write(value);;
	}
	
	void decrement(int address){
		gpMem[address].decrement();
	}
	
	void increment(int address){
		gpMem[address].increment();
	}
	
	private void initialize(){
		int i = 0;
		while(i < 0x080){
			gpMem[i] = new Register(pic18, i, "gpr");
			i++;
		}
		gpMem[0x080] = gpMem[0xf80] = new PortA(pic18, 0xf80, "portA");
//		gpMem[0x080].printInfo();
//		System.out.println("gmMem[0x080] is object of type: " + gpMem[0x080].getClass());
		gpMem[0x081] = gpMem[0xf81] = new Register(pic18, 0xf81, "portB");
		gpMem[0x082] = gpMem[0xf82] = new Register(pic18, 0xf82, "portC");
		gpMem[0x083] = gpMem[0xf83] = new Register(pic18, 0xf83, "portD");
		gpMem[0x084] = gpMem[0xf84] = new Register(pic18, 0xf84, "portE");
		gpMem[0x085] = gpMem[0xf85] = new Register(pic18, 0xf85, "unused");
		gpMem[0x086] = gpMem[0xf86] = new Register(pic18, 0xf86, "unused");
		gpMem[0x087] = gpMem[0xf87] = new Register(pic18, 0xf87, "unused");
		gpMem[0x088] = gpMem[0xf88] = new Register(pic18, 0xf88, "unused");
		gpMem[0x089] = gpMem[0xf89] = new Register(pic18, 0xf89, "latA");
		gpMem[0x08a] = gpMem[0xf8a] = new Register(pic18, 0xf8a, "latB");
		gpMem[0x08b] = gpMem[0xf8b] = new Register(pic18, 0xf8b, "latC");
		gpMem[0x08c] = gpMem[0xf8c] = new Register(pic18, 0xf8c, "latD");
		gpMem[0x08d] = gpMem[0xf8d] = new Register(pic18, 0xf8d, "latE");
		gpMem[0x08e] = gpMem[0xf8e] = new Register(pic18, 0xf8e, "unused");
		gpMem[0x08f] = gpMem[0xf8f] = new Register(pic18, 0xf80, "unused");
		gpMem[0x090] = gpMem[0xf90] = new Register(pic18, 0xf90, "unused");
		gpMem[0x091] = gpMem[0xf91] = new Register(pic18, 0xf91, "unused");
		gpMem[0x092] = gpMem[0xf92] = new Register(pic18, 0xf92, "trisA");
		gpMem[0x093] = gpMem[0xf93] = new Register(pic18, 0xf93, "trisB");
		gpMem[0x094] = gpMem[0xf94] = new Register(pic18, 0xf94, "trisC");
		gpMem[0x095] = gpMem[0xf95] = new Register(pic18, 0xf95, "tricD");
		gpMem[0x096] = gpMem[0xf96] = new Register(pic18, 0xf96, "trisE");
		gpMem[0x097] = gpMem[0xf97] = new Register(pic18, 0xf97, "unused");
		gpMem[0x098] = gpMem[0xf98] = new Register(pic18, 0xf98, "unused");
		gpMem[0x099] = gpMem[0xf99] = new Register(pic18, 0xf99, "unused");
		gpMem[0x09a] = gpMem[0xf9a] = new Register(pic18, 0xf9a, "unused");
		gpMem[0x09b] = gpMem[0xf9b] = new Register(pic18, 0xf9b, "unused");
		gpMem[0x09c] = gpMem[0xf9c] = new Register(pic18, 0xf9c, "unused");
		gpMem[0x09d] = gpMem[0xf9d] = new Register(pic18, 0xf9d, "pie1");
		gpMem[0x09e] = gpMem[0xf9e] = new Register(pic18, 0xf9e, "pir1");
		gpMem[0x09f] = gpMem[0xf9f] = new Register(pic18, 0xf9f, "ipr1");
		gpMem[0x0a0] = gpMem[0xfa0] = new Register(pic18, 0xfa0, "pie2");
		gpMem[0x0a1] = gpMem[0xfa1] = new Register(pic18, 0xfa1, "pir2");
		gpMem[0x0a2] = gpMem[0xfa2] = new Register(pic18, 0xfa2, "ipr2");
		gpMem[0x0a3] = gpMem[0xfa3] = new Register(pic18, 0xfa3, "unused");
		gpMem[0x0a4] = gpMem[0xfa4] = new Register(pic18, 0xfa4, "unused");
		gpMem[0x0a5] = gpMem[0xfa5] = new Register(pic18, 0xfa5, "unused");
		gpMem[0x0a6] = gpMem[0xfa6] = new Register(pic18, 0xfa6, "unused");
		gpMem[0x0a7] = gpMem[0xfa7] = new Register(pic18, 0xfa7, "unused");
		gpMem[0x0a8] = gpMem[0xfa8] = new Register(pic18, 0xfa8, "unused");
		gpMem[0x0a9] = gpMem[0xfa9] = new Register(pic18, 0xfa9, "unused");
		gpMem[0x0aa] = gpMem[0xfaa] = new Register(pic18, 0xfaa, "unused");
		gpMem[0x0ab] = gpMem[0xfab] = new Register(pic18, 0xfab, "rcsta");
		gpMem[0x0ac] = gpMem[0xfac] = new Register(pic18, 0xfac, "txsta");
		gpMem[0x0ad] = gpMem[0xfad] = new Register(pic18, 0xfad, "txreg");
		gpMem[0x0ae] = gpMem[0xfae] = new Register(pic18, 0xfae, "rcreg");
		gpMem[0x0af] = gpMem[0xfaf] = new Register(pic18, 0xfaf, "spbrg");
		gpMem[0x0b0] = gpMem[0xfb0] = new Register(pic18, 0xfb0, "unused");
		gpMem[0x0b1] = gpMem[0xfb1] = new Register(pic18, 0xfb1, "t3con");
		gpMem[0x0b2] = gpMem[0xfb2] = new Register(pic18, 0xfb2, "tmr3l");
		gpMem[0x0b3] = gpMem[0xfb3] = new Register(pic18, 0xfb3, "tmr3h");
		gpMem[0x0b4] = gpMem[0xfb4] = new Register(pic18, 0xfb4, "unused");
		gpMem[0x0b5] = gpMem[0xfb5] = new Register(pic18, 0xfb5, "unused");
		gpMem[0x0b6] = gpMem[0xfb6] = new Register(pic18, 0xfb6, "unused");
		gpMem[0x0b7] = gpMem[0xfb7] = new Register(pic18, 0xfb7, "unused");
		gpMem[0x0b8] = gpMem[0xfb8] = new Register(pic18, 0xfb8, "unused");
		gpMem[0x0b9] = gpMem[0xfb9] = new Register(pic18, 0xfb9, "unused");
		gpMem[0x0ba] = gpMem[0xfba] = new Register(pic18, 0xfba, "ccp2con");
		gpMem[0x0bb] = gpMem[0xfbb] = new Register(pic18, 0xfbb, "ccpr2L");
		gpMem[0x0bc] = gpMem[0xfbc] = new Register(pic18, 0xfbc, "ccpr2H");
		gpMem[0x0bd] = gpMem[0xfbd] = new Register(pic18, 0xfbd, "ccp1con");
		gpMem[0x0be] = gpMem[0xfbe] = new Register(pic18, 0xfbe, "ccpr1L");
		gpMem[0x0bf] = gpMem[0xfbf] = new Register(pic18, 0xfbf, "ccpr1H");
		gpMem[0x0c0] = gpMem[0xfc0] = new Register(pic18, 0xfc0, "unused");
		gpMem[0x0c1] = gpMem[0xfc1] = new Register(pic18, 0xfc1, "unused");
		gpMem[0x0c2] = gpMem[0xfc2] = new Register(pic18, 0xfc2, "unused");
		gpMem[0x0c3] = gpMem[0xfc3] = new Register(pic18, 0xfc3, "unused");
		gpMem[0x0c4] = gpMem[0xfc4] = new Register(pic18, 0xfc4, "unused");
		gpMem[0x0c5] = gpMem[0xfc5] = new Register(pic18, 0xfc5, "unused");
		gpMem[0x0c6] = gpMem[0xfc6] = new Register(pic18, 0xfc6, "unused");
		gpMem[0x0c7] = gpMem[0xfc7] = new Register(pic18, 0xfc7, "unused");
		gpMem[0x0c8] = gpMem[0xfc8] = new Register(pic18, 0xfc8, "unused");
		gpMem[0x0c9] = gpMem[0xfc9] = new Register(pic18, 0xfc9, "unused");
		gpMem[0x0ca] = gpMem[0xfca] = new Register(pic18, 0xfca, "unused");
		gpMem[0x0cb] = gpMem[0xfcb] = new Register(pic18, 0xfcb, "unused");
		gpMem[0x0cc] = gpMem[0xfcc] = new Register(pic18, 0xfcc, "unused");
		gpMem[0x0cd] = gpMem[0xfcd] = new Register(pic18, 0xfcd, "unused");
		gpMem[0x0ce] = gpMem[0xfce] = new Register(pic18, 0xfce, "unused");
		gpMem[0x0cf] = gpMem[0xfcf] = new Register(pic18, 0xfcf, "unused");
		gpMem[0x0d0] = gpMem[0xfd0] = new Register(pic18, 0xfd0, "unused");
		gpMem[0x0d1] = gpMem[0xfd1] = new Register(pic18, 0xfd1, "unused");
		gpMem[0x0d2] = gpMem[0xfd2] = new Register(pic18, 0xfd2, "unused");
		gpMem[0x0d3] = gpMem[0xfd3] = new Register(pic18, 0xfd3, "unused");
		gpMem[0x0d4] = gpMem[0xfd4] = new Register(pic18, 0xfd4, "unused");
		gpMem[0x0d5] = gpMem[0xfd5] = new Register(pic18, 0xfd5, "unused");
		gpMem[0x0d6] = gpMem[0xfd6] = new Register(pic18, 0xfd6, "unused");
		gpMem[0x0d7] = gpMem[0xfd7] = new Register(pic18, 0xfd7, "unused");
		gpMem[0x0d8] = gpMem[0xfd8] = new Status(pic18, 0xfd8, "status");
		gpMem[0x0d9] = gpMem[0xfd9] = new Register(pic18, 0xfd9, "unused");
		gpMem[0x0da] = gpMem[0xfda] = new Register(pic18, 0xfda, "unused");
		gpMem[0x0db] = gpMem[0xfdb] = new Register(pic18, 0xfdb, "unused");
		gpMem[0x0dc] = gpMem[0xfdc] = new Register(pic18, 0xfdc, "unused");
		gpMem[0x0dd] = gpMem[0xfdd] = new Register(pic18, 0xfdd, "unused");
		gpMem[0x0de] = gpMem[0xfde] = new Register(pic18, 0xfde, "unused");
		gpMem[0x0df] = gpMem[0xfdf] = new Register(pic18, 0xfdf, "unused");
		gpMem[0x0e0] = gpMem[0xfe0] = new Register(pic18, 0xfe0, "unused");
		gpMem[0x0e1] = gpMem[0xfe1] = new Register(pic18, 0xfe1, "unused");
		gpMem[0x0e2] = gpMem[0xfe2] = new Register(pic18, 0xfe2, "unused");
		gpMem[0x0e3] = gpMem[0xfe3] = new Register(pic18, 0xfe3, "unused");
		gpMem[0x0e4] = gpMem[0xfe4] = new Register(pic18, 0xfe4, "unused");
		gpMem[0x0e5] = gpMem[0xfe5] = new Register(pic18, 0xfe5, "unused");
		gpMem[0x0e6] = gpMem[0xfe6] = new Register(pic18, 0xfe6, "unused");
		gpMem[0x0e7] = gpMem[0xfe7] = new Register(pic18, 0xfe7, "unused");
		gpMem[0x0e8] = gpMem[0xfe8] = new Wreg(pic18, 0xfe8, "wreg");
		gpMem[0x0e9] = gpMem[0xfe9] = new Register(pic18, 0xfe9, "unused");
		gpMem[0x0ea] = gpMem[0xfea] = new Register(pic18, 0xfea, "unused");
		gpMem[0x0eb] = gpMem[0xfeb] = new Register(pic18, 0xfeb, "unused");
		gpMem[0x0ec] = gpMem[0xfec] = new Register(pic18, 0xfec, "unused");
		gpMem[0x0ed] = gpMem[0xfed] = new Register(pic18, 0xfed, "unused");
		gpMem[0x0ee] = gpMem[0xfee] = new Register(pic18, 0xfee, "unused");
		gpMem[0x0ef] = gpMem[0xfef] = new Register(pic18, 0xfef, "unused");
		gpMem[0x0f0] = gpMem[0xff0] = new Register(pic18, 0xff0, "unused");
		gpMem[0x0f1] = gpMem[0xff1] = new Register(pic18, 0xff1, "unused");
		gpMem[0x0f2] = gpMem[0xff2] = new Intcon(pic18, 0xff2, "unused");
		gpMem[0x0f3] = gpMem[0xff3] = new Register(pic18, 0xff3, "unused");
		gpMem[0x0f4] = gpMem[0xff4] = new Register(pic18, 0xff4, "unused");
		gpMem[0x0f5] = gpMem[0xff5] = new Register(pic18, 0xff5, "unused");
		gpMem[0x0f6] = gpMem[0xff6] = new Register(pic18, 0xff6, "unused");
		gpMem[0x0f7] = gpMem[0xff7] = new Register(pic18, 0xff7, "unused");
		gpMem[0x0f8] = gpMem[0xff8] = new Register(pic18, 0xff8, "unused");
		gpMem[0x0f9] = gpMem[0xff9] = new PCL(pic18, 0xff9, "pcL");
		gpMem[0x0fa] = gpMem[0xffa] = new PclatH(pic18, 0xffa, "pclatH");
		gpMem[0x0fb] = gpMem[0xffb] = new PclatU(pic18, 0xffb, "pclatU");
		gpMem[0x0fc] = gpMem[0xffc] = new Stkptr(pic18, 0xffc, "stkptr");
		gpMem[0x0fd] = gpMem[0xffd] = new Register(pic18, 0xffd, "unused");
		gpMem[0x0fe] = gpMem[0xffe] = new Register(pic18, 0xffe, "unused");
		gpMem[0x0ff] = gpMem[0xfff] = new Register(pic18, 0xfff, "unused");

		
		i = 0x100;
		while(i < 0xf80){
			gpMem[i] = new Register(pic18, i, "gpr");
			i++;
		}
	}
}