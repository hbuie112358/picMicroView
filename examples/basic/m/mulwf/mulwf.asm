;;;;;;; mulwf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of mulwf instruction. Mulwf carries out 
;unsigned multiplication between contents of register f and contents of 
;wreg. 16 bit result is placed in PRODH:PRODL register pair. Wreg is  
;unchanged, no status bits affected.
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
	MYREG
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

	movlw		0xb5		;Place 0xb5 in wreg
	movwf		MYREG		;Copy wreg value to MYREG
	movlw		0xe2		;Place 0xe2 in wreg
	mulwf		MYREG		;Multiply wreg, MYREG, result in PRODH:PRODL (product is 0x9fca)

	mullw		0		;Clear PRODH:PRODL register pair
	movlb		1		;Place 1 in BSR (to use bank 0x100)
	movlw		0xb5		;Place 0xb5 in wreg
	movwf		MYREG, 1	;Copy wreg value to MYREG in bank 1
	movlw		0xe2		;Place 0xe2 in wreg
	mulwf		MYREG, 1	;Multiply wreg, MYREG, result in PRODH:PRODL (0x9fca)
	

stop	goto		stop

	end






















