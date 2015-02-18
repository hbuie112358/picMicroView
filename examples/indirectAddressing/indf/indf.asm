;;;;;;; indf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;Tests indirect addressing mechanism writes/reads to INDFx
;
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

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline
;This portion tests writing function of indf by loading an address in fsr and
;then writing to indf0, 1, and 2. Value in wreg is written to access memory 
;locations 0x105, 0x107, and 0x109.

	lfsr	0, 0x105
	movlw	241
	movwf	INDF0

	lfsr	1, 0x107
	movlw	163
	movwf	INDF1

	lfsr	2, 0x109
	movlw	201
	movwf	INDF2

;This tests to see if an indf register can be loaded with a value indirectly.
;An indirect write to an indf should have no effect.
;This attempts to write the value 10 into indf0 by loading indf0's address
;into fsr1 and telling indf1 to write into indf0.

	lfsr	1, 0x0ef	;Load address of INDF0 into FSR1
	movlw	10
	movwf	INDF1		;Try to load value of 10 into INDF0 register

;This section tests to see if it is possible to address memory locations that do 
;not exist in access memory, such as 0xddff. The write to FSR1H should mask 0xdd
;into 0x0d and the value 0xdd should be written into access memory address 0xdff.

	movlw	0xff		
	movwf	FSR1L
	movlw	0xdd
	movwf	FSR1H
	movwf	INDF1

;This section tests the indirect read function, then loads fsr1 with address
;of indf0, then attempts to indirectly read indf0 contents. This should always return 0.

	movlw	0		;Clear value in wreg
	movff	INDF1, WREG	;read location pointed to by fsr1, place in wreg
	lfsr	1, 0x0ef	;Load address of INDF0 into FSR1
	movlw	10		;Place 10 in wreg
	movff   INDF1, WREG     ;Try to indirectly read indf0, should place 0 in wreg


stop	goto		stop

	end





















