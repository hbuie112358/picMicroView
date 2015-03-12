;;;;;;; xorwf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of xorwf, exclusive OR the WREG with 
;contents of register f using the examples starting on page 790 of 
;the PIC18 user manual.
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
	REG1
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
;Syntax: 	xorwf	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline

;Example 1:

	movlb	1		;Select bank 1 of data memory
	movlw	0xaf		;Place 0xaf in wreg
	movwf	REG1, 1		;Copy 0xaf to REG1:		1010 1111
	movlw	0xb5		;Place 0xb5 in wreg:		1011 0101
	xorwf	REG1, 1, 1	;Bit wise XOR wreg, REG1:	0001 1010
				;result in REG1
			
;Example 2, Case 1:
	
	movlw	0xaf		;Place 0xaf in wreg
	movwf	REG1, 1		;Copy 0xaf to REG1:		1010 1111
	movlw	0xb5		;Place 0xb5 in wreg:		1011 0101
	xorwf	REG1, 0, 1	;Bit wise XOR wreg, REG1:	0001 1010
				;result in wreg
	
;Example 3:

	lfsr	0, 0x7c2	;Place 0x7c2 in FSR0
	movlw	0xaf		;Place 0xaf in wreg		
	movwf	INDF0, 1	;Copy to INDF0			1010 1111
	movlw	0xb5		;Place 0xb5 in wreg		1011 0101
	xorwf	INDF0, 1, 1	;Bit wise XOR wreg, INDF0:	0001 1010

;Example 4:

	movlw	0xaa		;Place 0xaa in wreg		
	movwf	REG1		;Copy to REG1			1010 1010
	movlw	0xf8		;Place 0xf8 in wreg		1111 1000
	xorwf	REG1, 1, 0	;Bit wise XOR wreg, REG1:	0101 0010	

stop		goto		stop

		end
