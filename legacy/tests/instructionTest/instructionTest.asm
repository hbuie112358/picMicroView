;;;;;;; instructionTest.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	#include p18f452.inc
        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON

;;;;;;; Variables ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	cblock  0x000           ;Beginning of Access RAM
	temp
	endc

	cblock	0x100		;Beginning of 0x100 block, just above Access Ram
	tempBank1
	endc	

	cblock	0x2eb		;assign variables to same ls byte location as PLUSW0, PREINC0 
	testPW0
	testPI0
	endc

	cblock	0x6e2		;assign variable for example 2 and 3 test
	testAbit
	endc

;;;;;;; Macro definitions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Vectors ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


        org  0x0000             ;Reset vector
        nop
        goto  Mainline

        org  0x0008             ;High priority interrupt vector
        goto  $                 ;Trap

        org  0x0018             ;Low priority interrupt vector
        goto  $                 ;Trap

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;See manual chapter 31 for more detail on individual instructions
;Syntax: 	addwf	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;		lfsr	f, k		(f in {0, 1, 2})
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline

;;;;;;; addwf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;In-depth note;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;Found that compiler doesn't know the difference between plusw0 (or any other 
;INDF register, for that matter) and a register assigned to address 0x0eb (or 
;other INDF register address) in the cblock. This means that the chip doesn't 
;know the difference either. How could "27EB" be anything other than "27EB" to 
;the chip? See around line 105 below. Therefore, since writes to INDF registers 
;ignore the "a" bit, writes to all registers whose least significant byte is the 
;same as an INDF register must also ignore the "a" bit. How else could it work, 
;since all the chip knows is what the compiler tells it? This will cause a hole 
;in every bank where the least significant byte is the same as an INDF register. 
;Specifically, from 0xXeb to 0xXef, 0xXe3 to 0xXe7, and 0xXdb to 0xXdf. This 
;means that the assembly language programmer must know this and not plan to put
;any data in that range, because anything addressed in that byte range will 
;result in a read or write to an INDF register.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;See manual chapter 31 for more detail on individual instructions
;Syntax: 	addwf	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;		lfsr	f, k		(f in {0, 1, 2})
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


	movlw	5		;Place 5 in wreg
	movwf	temp		;Move 5 to temp
	movlw	6		;Place 6 in wreg
	addwf	temp, f		;Add contents of wreg and temp, place result in temp
	movlw	0xfe		;Place 0xfe in wreg (in order to produce carry with next add)
	addwf	temp, w		;Add wreg, temp, status reg bit 0 (C) goes high
	addwf	temp, w		;Add wreg, temp, status reg bit 0 (C) goes low

;Test with banked addressing, addwf disregards banked addressing in certain instances. Those are 
;tested later. This scenario should work as normal banked addressing.

	movlb 	0x01		;Place 1 in Bank Select Register (set for bank 1)
	movlw	0x37		;Place 0x37 in wreg
	movwf	tempBank1	;Using bank select, move wreg to tempBank1 in bank 1 at address 0x100
	movlw	0x10		;Place 0x10 in wreg
	addwf	tempBank1, 1	;Add contents of wreg and temp, place result in tempBank1 (at address 0x100)
	movlw	0xf7		;Place 0xf7 in wreg (in order to produce carry with next add)
	addwf	tempBank1, 0	;Add wreg, tempBank1, result in wreg, status reg bit 0 (C) goes high
	addwf	tempBank1, w	;Add wreg, temp, result in wreg, status reg bit 0 (C) goes low

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;This portion shows what "In-depth note" at top of this file tries to explain:
;This portion tests that instructions for addwf where the destination register is an INDF register, 
;which means INDFx, PLUSWx, PREINCx, POSTINCx, and POSTDECx (x in {0, 1, 2}), ignore the "a"bit. 
;Reads or writes to any register with least significant byte in the ranges 0xXeb to 0xXef, 0xXe3  
;to 0xXe7, and 0xXdb to 0xXdf will result in reading/writing to an INDF location. Effectively, 
;registers in these ranges, other than the ones in Access Memory (which is bank 0), do not exist.

	lfsr	0, 0x000
	movlb	2
	movlw	5
	movwf	INDF0
	movwf	PLUSW0
	movwf	PREINC0
	lfsr	0, 0x000
        addwf   testPW0, 1, 1   ;Assigned to same address as PLUSW0, see instruction == 0x27EB
				;Since the value is written to PLUSW0, the bsr value of 2 has 
				;been ignored

        addwf   testPI0, 1, 1   ;Assigned to same address as PREINC0, see instruction == 0x27EC
				;Since the value is written to PREINC0, the bsr value of 2 has 
				;been ignored

        addwf   POSTINC0, 1, 1  ;Assigned to same address as temp2, see instruction == 0x27EC
	lfsr	0, 0x000
        addwf   PLUSW0, 1, 1    ;Assigned to same address as temp1, see instruction == 0x27EB

        addwf   PREINC0, w, 1   ;Assigned to same address as temp2, see instruction == 0x27EC
				;This instruction only adds 1 to FSR since PREINC0 is only read once.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;Manual, example1, case 1:
;Shows that adding FSR ignores "a" bit, and that addwf gives a correct answer

	lfsr	0, 0x2c2
	movlw	0x17
	addwf	FSR0L, 1, 1

;Manual, example1, case 2:
;Shows that adding FSR ignores "a" bit when adding to FSR, and that when adding to FSR0L creates a carry, FSR0H is incremented
	lfsr	0, 0x2ff
	addwf	FSR0L, 1, 1

;Manual, example 2:
;Shows that addwf ignores "a" bit when adding to an INDF register

	movlb	2		;Set BSR value to 2
	lfsr	0, 0x6c2	;Load FSR with value
	movlw	0x20		;Place 0x20 in wreg
	movwf	INDF0, 1	;Place value in wreg in location pointed to by INDF0
	movlw	0x17		;Place 0x17 in wreg
	addwf	INDF0, 1, 1	;Add wreg with value in location pointed to by INDF0,
				;place result in location pointed to by INDF0.
				;Since "a" bit is set (the second 1), banked addressing 
				;is called for, but since the register argument is an
				;INDF register, banked addressing is ignored

;Manual, example 3:
	
	movlw	 0x20		;Place 0x20 in wreg
	movwf	INDF0, 1	;Place value in wreg in location pointed to by INDF0
	movlw	0x17		;Place 0x17 in wreg
	addwf	INDF0, 1, 0	;Add wreg with value in location pointed to by INDF0,
				;place result in location pointe to by INDF0.
				;Shows that "a" bit being 1 or 0 doesn't matter when 
				;register argument is an INDF register.
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;addwfc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of the addwfc instruction. It adds the 
;contents of the WREG and the carry bit to the contents of register f.
;

;Test no carry, use bsr, write wreg:

	movlw	0x17
	movlb	1
	movwf	tempBank1	;Store 0x17 at address 0x100
	movlw	0xc2
	addwfc	tempBank1, 0, 1	;Add WREG, C bit, and temp, 
				;place result (0xd9) in WREG (no carry)

;Test no carry, use bsr, write freg:
	movlw	0x18
	movlb	1
	movwf	tempBank1	;Store 0x18 at address 0x100
	movlw	0xc2
	addwfc	tempBank1, 1, 1	;Add WREG, C bit, and temp, 
				;place result (0xd9) in tempBank1 (no carry)

;Test no carry, no bsr, write wreg:

	movlw	0x17
	movlb	1
	movwf	temp		;Store 0x17 at address 0x100
	movlw	0xc2
	addwfc	temp, 0		;Add WREG, C bit, and temp, 
				;place result (0xd9) in WREG (no carry)

;Test no carry, no bsr, write freg:
	movlw	0x18
	movlb	1
	movwf	temp		;Store 0x18 at address 0x000
	movlw	0xc2
	addwfc	temp, 1		;Add WREG, C bit, and temp, 
				;place result (0xd9) in temp (no carry)

;Cause carry bit to be set:

	movlw	0xfe
	movwf	temp
	movlw	0xad
	addwfc	temp, 0		;Set carry bit

;Test add with carry:

	movwf	temp		
	movlw	0x02
	addwfc	temp, 0		;Add WREG, C bit, and temp (0x02 + 1 + 0xab), 
				;place result (0xae) in WREG (clears carry bit)



















	nop
	nop
	nop
	nop


stop	goto		stop

	end





















