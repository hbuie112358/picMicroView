;;;;;;; lfsr.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;Shows function of lfsr instruction, writes a value to indf register
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

	lfsr	0, 0x105	;Loads FSR0 with 0x105
	movlw	241		;Places 241 in wreg
	movwf	INDF0		;Places wreg value into mem location pointed to by FSR0 (0x105)

	lfsr	1, 0x107	;Loads FSR1 with 0x107
	movlw	163		;Places 163 in wreg
	movwf	INDF1		;Places wreg value into mem location pointed to by FSR0 (0x107)

	lfsr	2, 0x109	;Loads FSR2 with 0x109
	movlw	201		;Places 201 in wreg
	movwf	INDF2		;Places wreg value into mem location pointed to by FSR0 (0x109)


stop	goto		stop

	end





















